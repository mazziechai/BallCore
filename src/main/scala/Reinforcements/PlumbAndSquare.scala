package BallCore.Reinforcements

import scala.collection.JavaConverters._
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup
import org.bukkit.NamespacedKey
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack
import org.bukkit.Material
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType
import org.bukkit.inventory.ItemStack
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import org.bukkit.ChatColor
import io.github.thebusybiscuit.slimefun4.core.attributes.NotConfigurable

object PlumbAndSquare:
    val group = ItemGroup(NamespacedKey("ballcore", "reinforcements"), CustomItemStack(Material.DIAMOND_PICKAXE, "BallCore Reinforcements"))
    val itemStack = SlimefunItemStack("PLUMB_AND_SQUARE", Material.STICK, "&rPlumb-and-Square", defaultLore())
    val persistenceKeyCount = NamespacedKey("ballcore", "plumb_and_square_item_count")
    val persistenceKeyType = NamespacedKey("ballcore", "plumb_and_square_item_type")

    def defaultLore(): String =
        "&7Can be loaded with reinforcement materials to protect blocks"

    def itemCountLore(kind: ReinforcementTypes, count: Int): String =
        kind match
            case ReinforcementTypes.Stone => s"&fLoaded with: &7&lStone &7Reinforcement&f × ${count}"
            case ReinforcementTypes.Deepslate => s"&fLoaded with: &8&lDeepslate &8Reinforcement&f × ${count}"
            case ReinforcementTypes.CopperLike => s"&fLoaded with: &c&lRed &cReinforcement&f × ${count}"
            case ReinforcementTypes.IronLike => s"&fLoaded with: &f&lWhite &fReinforcement&f × ${count}"

class PlumbAndSquare()
    extends SlimefunItem(PlumbAndSquare.group, PlumbAndSquare.itemStack, RecipeType.NULL, null), NotConfigurable:

    setUseableInWorkbench(true)

    def colourise(str: String): String =
        ChatColor.translateAlternateColorCodes('&', str)

    def getMaterials(item: ItemStack): Option[(ReinforcementTypes, Int)] =
        val keyCount = PlumbAndSquare.persistenceKeyCount
        val keyType = PlumbAndSquare.persistenceKeyType        
        val meta = item.getItemMeta()
        val pdc = meta.getPersistentDataContainer()
        val kind = ReinforcementTypes.from(pdc.getOrDefault(keyType, PersistentDataType.STRING, ""))
        val loaded = pdc.getOrDefault(keyCount, PersistentDataType.INTEGER, 0)

        kind.map((_, loaded))

    def updateLore(item: ItemStack): Unit =
        val keyCount = PlumbAndSquare.persistenceKeyCount
        val keyType = PlumbAndSquare.persistenceKeyType
        val meta = item.getItemMeta()
        val pdc = meta.getPersistentDataContainer()
        val kind = ReinforcementTypes.from(pdc.getOrDefault(keyType, PersistentDataType.STRING, ""))
        val loaded = pdc.getOrDefault(keyCount, PersistentDataType.INTEGER, 0)

        if kind.isDefined && loaded > 0 then
            val lore = List(
                PlumbAndSquare.defaultLore(),
                "",
                PlumbAndSquare.itemCountLore(kind.get, loaded)
            ).map(colourise).asJava
            meta.setLore(lore)
        else
            meta.setLore(List(PlumbAndSquare.defaultLore()).map(colourise).asJava)

        item.setItemMeta(meta)

    def loadReinforcementMaterials(p: Player, item: ItemStack, count: Int, kind: ReinforcementTypes): Unit =
        if item.getAmount() > 1 then
            item.setAmount(item.getAmount() - 1)

            val separateItem = item.clone()
            separateItem.setAmount(1)
            loadReinforcementMaterials(p, separateItem, count, kind)

            if !p.getInventory().addItem(separateItem).isEmpty() then
                p.getWorld().dropItemNaturally(p.getLocation(), separateItem);

            return

        val meta = item.getItemMeta()
        val keyCount = PlumbAndSquare.persistenceKeyCount
        val keyType = PlumbAndSquare.persistenceKeyType
        val pdc = meta.getPersistentDataContainer()
        val existingKind = ReinforcementTypes.from(pdc.getOrDefault(keyType, PersistentDataType.STRING, ""))

        if existingKind.isEmpty then
            pdc.set(keyType, PersistentDataType.STRING, kind.into())
        else if existingKind.get != kind then
            return

        val countLoaded = pdc.getOrDefault(keyCount, PersistentDataType.INTEGER, 0) + count
        if countLoaded == 0 then
            pdc.remove(keyType)
            pdc.remove(keyCount)
        else
            pdc.set(keyCount, PersistentDataType.INTEGER, countLoaded)
        item.setItemMeta(meta)
        updateLore(item)
