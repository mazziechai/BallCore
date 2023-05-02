// SPDX-FileCopyrightText: 2022 Janet Blackquill <uhhadd@gmail.com>
//
// SPDX-License-Identifier: AGPL-3.0-or-later

package BallCore.UI

import scala.collection.JavaConverters._
import scala.xml.Elem
import scala.xml.Node
import com.github.stefvanschie.inventoryframework.pane.Pane.Priority
import io.circe._, io.circe.parser._, io.circe.syntax._
import com.github.stefvanschie.inventoryframework.gui.`type`.util.Gui
import scala.reflect.ClassTag
import net.kyori.adventure.text.Component
import com.github.stefvanschie.inventoryframework.gui.`type`.ChestGui
import com.github.stefvanschie.inventoryframework.adventuresupport.TextHolder
import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder
import com.github.stefvanschie.inventoryframework.pane.Pane
import com.github.stefvanschie.inventoryframework.pane.{OutlinePane => IFOutlinePane, StaticPane => IFStaticPane}
import com.github.stefvanschie.inventoryframework.gui.GuiItem
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.format.Style
import org.bukkit.event.inventory.InventoryClickEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration.State
import org.bukkit.inventory.meta.SkullMeta

// trait AccumulatorFn:

object Accumulator:
    def run[T](inner: (Accumulator[T, Unit]) ?=> Unit): List[T] =
        run(inner, ())
    def run[T, E](inner: (Accumulator[T, E]) ?=> Unit, item: E): List[T] =
        given akku: Accumulator[T, E] = Accumulator(item)
        inner
        akku.items.toList

class Box[T](val initial: T):
    var it = initial

class Accumulator[T, E](val extra: E):
    var items = scala.collection.mutable.ArrayBuffer[T]()
    def ctx = extra
    def add(item: T): Unit =
        items.append(item)

type PaneAccumulator = Accumulator[Pane, Object => InventoryClickEvent => Unit]
type ItemAccumulator = Accumulator[GuiItem, Object => InventoryClickEvent => Unit]
type LoreAccumulator = Accumulator[Component, Box[Option[String]]]

object Elements:
    private val registeredProperties = scala.collection.mutable.Set[String]()
    def nil[T, E]: (Accumulator[T, E]) ?=> Unit = {

    }

    extension (sc: StringContext)
        def txt(args: Any*): Component =
            val strings = sc.parts.iterator
            var it = Component.text(strings.next())
            val expressions = args.iterator
            while strings.hasNext do
                expressions.next() match
                    case c: ComponentLike => it = it.append(c)
                    case obj => it = it.append(Component.text(obj.toString()))
                it = it.append(Component.text(strings.next()))
            it

    extension (c: Component)
        def style(color: TextColor, decorations: TextDecoration*): Component =
            c.style(Style.style(color, decorations: _*))
        def not(decoration: TextDecoration): Component =
            c.decoration(decoration, false)

    def Root(title: Component, rows: Int)(inner: (PaneAccumulator) ?=> Unit = nil)(using cb: Object => InventoryClickEvent => Unit): ChestGui =
        val chest = ChestGui(rows, ComponentHolder.of(title))
        Accumulator.run(inner, cb).foreach(x => chest.addPane(x))
        chest

    def OutlinePane(x: Int, y: Int, length: Int, height: Int, priority: Priority = Priority.NORMAL, repeat: Boolean = false)(inner: (ItemAccumulator) ?=> Unit = nil)(using an: PaneAccumulator): Unit =
        val pane = IFOutlinePane(x, y, length, height, priority)
        pane.setRepeat(repeat)
        Accumulator.run(inner, an.extra).foreach(x => pane.addItem(x))
        an add pane

    def Item(id: Material, amount: Int = 1, displayName: Option[Component] = None)(inner: (LoreAccumulator) ?=> Unit = nil)(using an: ItemAccumulator): Unit =
        val is = ItemStack(id, amount)
        val im = is.getItemMeta()

        displayName match
            case Some(x) => im.displayName(x.style(x => x.decorationIfAbsent(TextDecoration.ITALIC, State.FALSE)))
            case None =>
        val poki = Box[Option[String]](None)
        im.lore(Accumulator.run(inner, poki).map(line => line.style(x => x.decorationIfAbsent(TextDecoration.ITALIC, State.FALSE))).asJava)

        poki.it match
            case Some(x) if im.isInstanceOf[SkullMeta] => im.asInstanceOf[SkullMeta].setOwner(x)
            case _ =>

        is.setItemMeta(im)
        an add GuiItem(is, ev => ev.setCancelled(true))

    def Button[Msg](id: Material, displayName: Component, onClick: Msg, amount: Int = 1)(inner: (LoreAccumulator) ?=> Unit = nil)(using an: ItemAccumulator): Unit =
        val is = ItemStack(id, amount)
        val im = is.getItemMeta()
        val baked = an.extra(onClick.asInstanceOf[Object])

        im.displayName(displayName.style(x => x.decorationIfAbsent(TextDecoration.ITALIC, State.FALSE)))
        val poki = Box[Option[String]](None)
        im.lore(Accumulator.run(inner, poki).map(line => line.style(x => x.decorationIfAbsent(TextDecoration.ITALIC, State.FALSE))).asJava)

        poki.it match
            case Some(x) if im.isInstanceOf[SkullMeta] => im.asInstanceOf[SkullMeta].setOwner(x)
            case _ =>

        is.setItemMeta(im)
        an add GuiItem(is, ev => baked(ev))

    def Lore(line: Component)(using an: LoreAccumulator): Unit =
        an add line

    def SkullUsername(username: String)(using an: LoreAccumulator): Unit =
        an.extra.it = Some(username)
