// SPDX-FileCopyrightText: 2023 Janet Blackquill <uhhadd@gmail.com>
//
// SPDX-License-Identifier: AGPL-3.0-or-later

package BallCore.Ores

import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.inventory.FurnaceSmeltEvent
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.block.Block
import BallCore.CustomItems.ItemGroup
import BallCore.CustomItems.ItemRegistry
import BallCore.CustomItems.CustomItem
import BallCore.CustomItems.CustomItemStack
import org.bukkit.Server
import org.bukkit.plugin.java.JavaPlugin
import BallCore.CustomItems.BlockManager
import scala.util.chaining._
import BallCore.UI.Elements._
import org.bukkit.block.{Furnace => BFurnace}

enum FurnaceTier:
    // tier 0 (vanilla furnace)
    // raw => 1 ingot + 1 depleted
    case Zero

    // raw => 2 ingot + 1 scraps
    // depleted => 1 ingot + 1 scraps
    case One

    // raw => 3 ingot + 1 dust
    // depleted => 2 ingot + 1 dust
    // scraps => 1 ingot + 1 dust
    case Two

    // raw => 4 ingots
    // depleted => 3 ingots
    // scraps => 2 ingots
    // dust => 1 ingot
    case Three

class FurnaceListener(using bm: BlockManager, registry: ItemRegistry) extends Listener:
    def oreSmeltingResult(furnaceTier: FurnaceTier): (Int, OreTier) =
        furnaceTier match
            case FurnaceTier.Zero =>
                (4, OreTier.Nugget)
            case FurnaceTier.One =>
                (6, OreTier.Nugget)
            case FurnaceTier.Two =>
                (1, OreTier.Ingot)
            case FurnaceTier.Three =>
                (12, OreTier.Nugget)

    def tier(of: Block): FurnaceTier =
        val furnaceItem = bm.getCustomItem(of)
        if !furnaceItem.isDefined || !furnaceItem.get.isInstanceOf[Furnace] then
            FurnaceTier.Zero
        else
            furnaceItem.asInstanceOf[Furnace].tier

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    def onItemSmelt(event: FurnaceSmeltEvent): Unit =
        val smeltingItem = registry.lookup(event.getSource())
        if !smeltingItem.isDefined || !smeltingItem.get.isInstanceOf[Ore] then
            return
        val ore = smeltingItem.get.asInstanceOf[Ore]
        val (num, kind) = oreSmeltingResult(tier(event.getBlock()))

        // WORKAROUND: bukkit code doesn't seem to like custom items all that much
        event.setCancelled(true)
        val result = ore.variants.ore(kind).clone().tap(_.setAmount(num))
        val furnaceState = event.getBlock().getState(false).asInstanceOf[BFurnace]
        val resultSlot = furnaceState.getInventory().getResult()
        val newResultSlot =
            if resultSlot == null then
                result
            else if result.isSimilar(resultSlot) then
                resultSlot.setAmount(resultSlot.getAmount() + result.getAmount())
                resultSlot
            else
                null

        if newResultSlot == null then
            return

        furnaceState.getInventory().setResult(newResultSlot)

object Furnaces:
    val group = ItemGroup(NamespacedKey("ballcore", "furnaces"), ItemStack(Material.WHITE_CONCRETE))

object Furnace:
    val tierOneLore = txt"Capable of smelting ores with increased efficiency"

    val ironFurnace = CustomItemStack.make(NamespacedKey("ballcore", "iron_furnace"), Material.FURNACE, txt"Iron Furnace", tierOneLore)
    val tinFurnace = CustomItemStack.make(NamespacedKey("ballcore", "tin_furnace"), Material.FURNACE, txt"Tin Furnace", tierOneLore)
    val aluminumFurnace = CustomItemStack.make(NamespacedKey("ballcore", "aluminum_furnace"), Material.FURNACE, txt"Aluminum Furnace", tierOneLore)
    val zincFurnace = CustomItemStack.make(NamespacedKey("ballcore", "zinc_furnace"), Material.FURNACE, txt"Zinc Furnace", tierOneLore)

    val tierOne = List(ironFurnace, tinFurnace, aluminumFurnace, zincFurnace)

    val tierTwoLore = txt"Capable of smelting ores with astounding efficiency"

    val entschloseniteFurnace = CustomItemStack.make(NamespacedKey("ballcore", "entschlossenite_furnace"), Material.FURNACE, txt"Entschlossenite Furnace", tierTwoLore)

    val tierTwo = List(entschloseniteFurnace)

    val tierThreeLore = txt"Capable of smelting ores with supernatural efficiency"

    val praecantatioFurnace = CustomItemStack.make(NamespacedKey("ballcore", "praecantatio_furnace"), Material.BLAST_FURNACE, txt"Praecantatio Furnace", tierThreeLore)
    val auramFurnace = CustomItemStack.make(NamespacedKey("ballcore", "auram_furnace"), Material.BLAST_FURNACE, txt"Auram Furnace", tierThreeLore)
    val alkimiaFurnace = CustomItemStack.make(NamespacedKey("ballcore", "alkimia_furnace"), Material.BLAST_FURNACE, txt"Alkimia Furnace", tierThreeLore)

    val tierThree = List(praecantatioFurnace, auramFurnace, alkimiaFurnace)

    def registerItems()(using bm: BlockManager, registry: ItemRegistry, server: Server, plugin: JavaPlugin): Unit =
        server.getPluginManager().registerEvents(FurnaceListener(), plugin)
        List((FurnaceTier.One, tierOne), (FurnaceTier.Two, tierTwo), (FurnaceTier.Three, tierThree))
            .foreach { (tier, items) => items.foreach { item => registry.register(Furnace(tier, item)) } }

class Furnace(furnaceTier: FurnaceTier, items: CustomItemStack) extends CustomItem:
    def group = Furnaces.group
    def template = items
    val tier = furnaceTier
