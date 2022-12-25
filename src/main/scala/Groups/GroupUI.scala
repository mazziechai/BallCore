// SPDX-FileCopyrightText: 2022 Janet Blackquill <uhhadd@gmail.com>
//
// SPDX-License-Identifier: AGPL-3.0-or-later

package BallCore.Groups

import BallCore.UI.UI
import BallCore.UI.callback
import BallCore.UI.Elements._
import scala.xml.Elem
import org.bukkit.entity.HumanEntity
import com.github.stefvanschie.inventoryframework.pane.Pane.Priority
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import BallCore.UI.Prompts
import org.bukkit.entity.Player
import scala.concurrent.ExecutionContext

class GroupUI(target: HumanEntity)(using prompts: Prompts) extends UI:
    showingTo = target
    override def view(): Elem =
        Root("Groups", 6) {
            OutlinePane(0, 0, 1, 6) {
                Item("name_tag", displayName = Some("§aCreate Group"), onClick = callback(createGroup))()
            }
            OutlinePane(1, 0, 1, 6, priority = Priority.LOWEST, repeat = true) {
                Item("black_stained_glass_pane", displayName = Some(" "))()
            }
        }
    def createGroup(event: InventoryClickEvent): Unit =
        event.setCancelled(true)
        prompts.prompt(event.getWhoClicked().asInstanceOf[Player], "What do you want to call the group?")
            .map { result =>
                event.getWhoClicked().sendMessage(s"you said ${result}")
            }