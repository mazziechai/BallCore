// SPDX-FileCopyrightText: 2023 Janet Blackquill <uhhadd@gmail.com>
//
// SPDX-License-Identifier: AGPL-3.0-or-later

package BallCore.CustomItems

import org.bukkit.inventory.ItemStack
import org.bukkit.NamespacedKey
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.Recipe
import org.bukkit.Material
import net.kyori.adventure.text.Component

object Listeners:
    trait BlockPlaced:
        def onBlockPlace(event: BlockPlaceEvent): Unit
    trait BlockRemoved:
        def onBlockRemoved(event: BlockBreakEvent): Unit
    trait BlockClicked:
        def onBlockClicked(event: PlayerInteractEvent): Unit
    trait ItemUsedOnBlock:
        def onItemUsedOnBlock(event: PlayerInteractEvent): Unit
    trait ItemUsed:
        def onItemUsed(event: PlayerInteractEvent): Unit

trait CustomItem:
    def group: ItemGroup
    def template: CustomItemStack
    def id = template.id

class PlainCustomItem(ig: ItemGroup, is: CustomItemStack) extends CustomItem:
    def group = ig
    def template = is

trait ItemRegistry:
    def register(item: CustomItem): Unit
    def lookup(from: NamespacedKey): Option[CustomItem]
    def lookup(from: ItemStack): Option[CustomItem]
    def addRecipe(it: Recipe): Unit
    def recipes(): List[NamespacedKey]

enum CustomMaterial:
    case custom(named: String)
    case vanilla(named: Material)

    def displayName()(using r: ItemRegistry): Component =
        this match
            case custom(named) =>
                r.lookup(NamespacedKey.fromString(named)).map(_.template.displayName()).getOrElse(Component.text(s"Invalid item ${named}"))
            case vanilla(named) =>
                ItemStack(named).displayName()
        
