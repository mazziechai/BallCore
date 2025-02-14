// SPDX-FileCopyrightText: 2023 Janet Blackquill <uhhadd@gmail.com>
//
// SPDX-License-Identifier: AGPL-3.0-or-later

package BallCore.Gear

import BallCore.CustomItems.ItemRegistry
import org.bukkit.enchantments.Enchantment

object QuadrantGear:

    import BallCore.Ores.QuadrantOres
    import BallCore.Ores.QuadrantOres.ItemStacks.*
    import Gear.*
    import ToolSet.*

    def registerItems()(using registry: ItemRegistry): Unit =
        tools(
            Iron,
            iron.ingot,
            iron.name,
            iron.id,
            IronToolSetCustomModelDatas.iron,
            (Enchantment.DURABILITY, 1),
        )
        sword(
            Iron,
            iron.ingot,
            iron.name,
            iron.id,
            IronToolSetCustomModelDatas.iron,
            (Enchantment.DURABILITY, 1),
            (Enchantment.DAMAGE_ALL, 1),
        )
        armor(
            Iron,
            iron.ingot,
            iron.name,
            iron.id,
            IronToolSetCustomModelDatas.iron,
            (Enchantment.DURABILITY, 1),
        )

        tools(
            Gold,
            gold.ingot,
            gold.name,
            gold.id,
            GoldToolSetCustomModelDatas.gold,
            (Enchantment.DURABILITY, 7),
        )
        sword(
            Gold,
            gold.ingot,
            gold.name,
            gold.id,
            GoldToolSetCustomModelDatas.gold,
            (Enchantment.DURABILITY, 4),
            (Enchantment.DAMAGE_ALL, 1),
        )
        armor(
            Gold,
            gold.ingot,
            gold.name,
            gold.id,
            GoldToolSetCustomModelDatas.gold,
            (Enchantment.DURABILITY, 4),
            (Enchantment.PROTECTION_ENVIRONMENTAL, 1),
        )

        tools(
            Iron,
            copper.ingot,
            copper.name,
            copper.id,
            IronToolSetCustomModelDatas.copper,
            (Enchantment.DURABILITY, 2),
        )
        sword(
            Iron,
            copper.ingot,
            copper.name,
            copper.id,
            IronToolSetCustomModelDatas.copper,
            (Enchantment.DURABILITY, 2),
            (Enchantment.SWEEPING_EDGE, 1),
        )
        armor(
            Iron,
            copper.ingot,
            copper.name,
            copper.id,
            IronToolSetCustomModelDatas.copper,
            (Enchantment.DURABILITY, 2),
        )

        tools(
            Iron,
            tin.ingot,
            tin.name,
            tin.id,
            IronToolSetCustomModelDatas.tin,
            (Enchantment.DIG_SPEED, 1),
            (Enchantment.DURABILITY, 1),
        )
        sword(
            Iron,
            tin.ingot,
            tin.name,
            tin.id,
            IronToolSetCustomModelDatas.tin,
            (Enchantment.KNOCKBACK, 1),
            (Enchantment.DURABILITY, 1),
        )
        armor(
            Iron,
            tin.ingot,
            tin.name,
            tin.id,
            IronToolSetCustomModelDatas.tin,
            (Enchantment.THORNS, 1),
        )

        tools(
            Gold,
            silver.ingot,
            silver.name,
            silver.id,
            GoldToolSetCustomModelDatas.silver,
            (Enchantment.DIG_SPEED, 2),
            (Enchantment.DURABILITY, 4),
        )
        sword(
            Gold,
            silver.ingot,
            silver.name,
            silver.id,
            GoldToolSetCustomModelDatas.silver,
            (Enchantment.KNOCKBACK, 2),
            (Enchantment.DURABILITY, 4),
        )
        armor(
            Gold,
            silver.ingot,
            silver.name,
            silver.id,
            GoldToolSetCustomModelDatas.silver,
            (Enchantment.THORNS, 2),
            (Enchantment.DURABILITY, 4),
        )

        tools(
            Iron,
            orichalcum.ingot,
            orichalcum.name,
            orichalcum.id,
            IronToolSetCustomModelDatas.orichalcum,
            (Enchantment.SILK_TOUCH, 1),
        )
        sword(
            Iron,
            orichalcum.ingot,
            orichalcum.name,
            orichalcum.id,
            IronToolSetCustomModelDatas.orichalcum,
            (Enchantment.LOOT_BONUS_MOBS, 2),
        )
        armor(
            Iron,
            orichalcum.ingot,
            orichalcum.name,
            orichalcum.id,
            IronToolSetCustomModelDatas.orichalcum,
            (Enchantment.PROTECTION_FALL, 2),
        )

        tools(
            Iron,
            aluminum.ingot,
            aluminum.name,
            aluminum.id,
            IronToolSetCustomModelDatas.aluminum,
            (Enchantment.DURABILITY, 3),
        )
        sword(
            Iron,
            aluminum.ingot,
            aluminum.name,
            aluminum.id,
            IronToolSetCustomModelDatas.aluminum,
            (Enchantment.DURABILITY, 5),
        )
        armor(
            Iron,
            aluminum.ingot,
            aluminum.name,
            aluminum.id,
            IronToolSetCustomModelDatas.aluminum,
            (Enchantment.DURABILITY, 5),
        )

        tools(
            Gold,
            palladium.ingot,
            palladium.name,
            palladium.id,
            GoldToolSetCustomModelDatas.palladium,
            (Enchantment.DURABILITY, 4),
            (Enchantment.SILK_TOUCH, 1),
        )
        sword(
            Gold,
            palladium.ingot,
            palladium.name,
            palladium.id,
            GoldToolSetCustomModelDatas.palladium,
            (Enchantment.DURABILITY, 4),
            (Enchantment.LOOT_BONUS_MOBS, 3),
        )
        armor(
            Gold,
            palladium.ingot,
            palladium.name,
            palladium.id,
            GoldToolSetCustomModelDatas.palladium,
            (Enchantment.DURABILITY, 4),
            (Enchantment.PROTECTION_PROJECTILE, 2),
        )

        tools(
            Iron,
            hihiirogane.ingot,
            hihiirogane.name,
            hihiirogane.id,
            IronToolSetCustomModelDatas.hihiirogane,
            (Enchantment.DURABILITY, 1),
            (Enchantment.LOOT_BONUS_BLOCKS, 2),
        )
        sword(
            Iron,
            hihiirogane.ingot,
            hihiirogane.name,
            hihiirogane.id,
            IronToolSetCustomModelDatas.hihiirogane,
            (Enchantment.DURABILITY, 1),
            (Enchantment.DAMAGE_ALL, 1),
        )
        armor(
            Iron,
            hihiirogane.ingot,
            hihiirogane.name,
            hihiirogane.id,
            IronToolSetCustomModelDatas.hihiirogane,
            (Enchantment.DURABILITY, 1),
            (Enchantment.PROTECTION_FIRE, 3),
        )

        tools(
            Iron,
            zinc.ingot,
            zinc.name,
            zinc.id,
            IronToolSetCustomModelDatas.zinc,
            (Enchantment.DIG_SPEED, 2),
        )
        sword(
            Iron,
            zinc.ingot,
            zinc.name,
            zinc.id,
            IronToolSetCustomModelDatas.zinc,
            (Enchantment.DIG_SPEED, 2),
        )
        armor(
            Iron,
            zinc.ingot,
            zinc.name,
            zinc.id,
            IronToolSetCustomModelDatas.zinc,
            (Enchantment.THORNS, 4),
        )

        tools(
            Gold,
            magnesium.ingot,
            magnesium.name,
            magnesium.id,
            GoldToolSetCustomModelDatas.magnesium,
            (Enchantment.DIG_SPEED, 10),
            (Enchantment.DURABILITY, 2),
        )
        sword(
            Gold,
            magnesium.ingot,
            magnesium.name,
            magnesium.id,
            GoldToolSetCustomModelDatas.magnesium,
            (Enchantment.LOOT_BONUS_MOBS, 1),
            (Enchantment.DURABILITY, 2),
        )
        armor(
            Gold,
            magnesium.ingot,
            magnesium.name,
            magnesium.id,
            GoldToolSetCustomModelDatas.magnesium,
            (Enchantment.OXYGEN, 1),
            (Enchantment.DURABILITY, 2),
        )

        tools(
            Iron,
            meteorite.ingot,
            meteorite.name,
            meteorite.id,
            IronToolSetCustomModelDatas.meteorite,
            (Enchantment.DIG_SPEED, 1),
        )
        sword(
            Iron,
            meteorite.ingot,
            meteorite.name,
            meteorite.id,
            IronToolSetCustomModelDatas.meteorite,
            (Enchantment.KNOCKBACK, 4),
        )
        armor(
            Iron,
            meteorite.ingot,
            meteorite.name,
            meteorite.id,
            IronToolSetCustomModelDatas.meteorite,
            (Enchantment.OXYGEN, 1),
            (Enchantment.WATER_WORKER, 1),
        )
