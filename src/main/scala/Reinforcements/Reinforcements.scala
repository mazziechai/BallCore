// SPDX-FileCopyrightText: 2023 Janet Blackquill <uhhadd@gmail.com>
//
// SPDX-License-Identifier: AGPL-3.0-or-later

package BallCore.Reinforcements

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon
import BallCore.Groups.GroupManager
import BallCore.UI.Prompts
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ShapelessRecipe
import org.bukkit.inventory.RecipeChoice.ExactChoice
import org.bukkit.Material
import org.bukkit.inventory.RecipeChoice.MaterialChoice
import org.bukkit.inventory.ItemStack

object Recipes:
    val kinds = List(
        (NamespacedKey("ballcore", "plumb_and_square_stone"), Material.STONE),
        (NamespacedKey("ballcore", "plumb_and_square_deepslate"), Material.DEEPSLATE),
        (NamespacedKey("ballcore", "plumb_and_square_iron"), Material.IRON_INGOT),
        (NamespacedKey("ballcore", "plumb_and_square_copper"), Material.COPPER_INGOT),
    )

object Reinforcements:
    def register()(using sf: SlimefunAddon, rm: ReinforcementManager, gm: GroupManager, holos: HologramManager, prompts: Prompts): Unit =
        val plugin = sf.getJavaPlugin()
        plugin.getServer().getPluginManager().registerEvents(Listener(), plugin)
        (new PlumbAndSquare()).register(sf)

        // plumb-and-square crafting registration

        val serv = sf.getJavaPlugin().getServer()

        Recipes.kinds.foreach { it =>
            val (key, mat) = it

            val doot = PlumbAndSquare.itemStack.clone()
            val recp = ShapelessRecipe(key, doot)
            recp.addIngredient(1, PlumbAndSquare.itemStack.getType())
            recp.addIngredient(1, mat)
            serv.addRecipe(recp)
        }

