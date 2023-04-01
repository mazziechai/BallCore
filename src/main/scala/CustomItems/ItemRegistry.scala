package BallCore.CustomItems

import org.bukkit.inventory.ItemStack
import org.bukkit.NamespacedKey
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.block.BlockBreakEvent

object Listeners:
    trait BlockPlaced:
        def onBlockPlace(event: BlockPlaceEvent): Unit
    trait BlockRemoved:
        def onBlockRemoved(event: BlockBreakEvent): Unit

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
