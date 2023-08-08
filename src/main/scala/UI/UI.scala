// SPDX-FileCopyrightText: 2022 Janet Blackquill <uhhadd@gmail.com>
//
// SPDX-License-Identifier: AGPL-3.0-or-later

package BallCore.UI

import com.github.stefvanschie.inventoryframework.gui.`type`.ChestGui
import com.github.stefvanschie.inventoryframework.gui.`type`.util.Gui
import com.github.stefvanschie.inventoryframework.pane.OutlinePane
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.entity.HumanEntity
import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import org.bukkit.entity.Player
import BallCore.Folia.EntityExecutionContext
import org.bukkit.plugin.Plugin
import BallCore.Folia.FireAndForget

trait UITransferrer:
    def transferTo(p: UIProgram, f: p.Flags): Unit

trait UIPrompts:
    def prompt(prompt: String): Future[String]
    def notify(what: String): Unit

trait UIServices extends UITransferrer, UIPrompts, ExecutionContext

trait UIProgram:
    given Conversion[Model, Future[Model]] = Future.successful(_)
    type Flags
    type Model
    type Message
    type Callback = Object => InventoryClickEvent => Unit

    def init(flags: Flags): Model
    def view(model: Model): Callback ?=> Gui
    def update(msg: Message, model: Model)(using services: UIServices): Future[Model]

class UIProgramRunner(program: UIProgram, flags: program.Flags, showingTo: Player)(using prompts: Prompts, plugin: Plugin) extends UIServices:
    private var model = program.init(flags)
    private var transferred = false
    private val ec = EntityExecutionContext(showingTo)
    private given ctx: ExecutionContext = ec

    def render(): Unit =
        val mod = model
        FireAndForget {
            given cb: program.Callback = this.dispatch
            val res = program.view(mod)
            res.show(showingTo)
        }
    def block(event: InventoryClickEvent): Unit =
        event.setCancelled(true)
    def dispatch(obj: Object)(event: InventoryClickEvent): Unit =
        implicit val s: UIServices = this
        event.setCancelled(true)
        program.update(obj.asInstanceOf[program.Message], model).map { x =>
            model = x
            if !transferred then
                render()
        }
    def transferTo(newProgram: UIProgram, newFlags: newProgram.Flags): Unit =
        transferred = true
        val newUI = UIProgramRunner(newProgram, newFlags, showingTo)
        newUI.render()
    def prompt(prompt: String): Future[String] =
        prompts.prompt(showingTo, prompt)
    def notify(what: String): Unit =
        FireAndForget { showingTo.sendMessage(what) }
    def execute(runnable: Runnable): Unit =
        ec.execute(runnable)
    def reportFailure(cause: Throwable): Unit =
        ec.reportFailure(cause)
