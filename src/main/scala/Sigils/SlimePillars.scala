// SPDX-FileCopyrightText: 2023 Janet Blackquill <uhhadd@gmail.com>
//
// SPDX-License-Identifier: AGPL-3.0-or-later

package BallCore.Sigils

import BallCore.CustomItems.CustomItemStack
import org.bukkit.inventory.ItemStack
import org.bukkit.Material
import org.bukkit.NamespacedKey
import BallCore.TextComponents._
import scala.util.chaining._
import BallCore.Storage
import BallCore.Beacons.CivBeaconManager
import BallCore.CustomItems.CustomItem
import skunk.implicits._
import org.bukkit.event.player.PlayerInteractEvent
import BallCore.CustomItems.Listeners
import org.bukkit.entity.Interaction
import BallCore.Beacons.BeaconID
import skunk.Session
import cats.effect.IO

object SlimePillar:
	val slimePillarModel = ItemStack(Material.STICK)
	slimePillarModel.setItemMeta(slimePillarModel.getItemMeta().tap(_.setCustomModelData(7)))

	val debugSpawnItemStack = CustomItemStack.make(NamespacedKey("ballcore", "slime_pillar_debug"), Material.PAPER, txt"Slime Pillar Debug")
	debugSpawnItemStack.setItemMeta(debugSpawnItemStack.getItemMeta().tap(_.setCustomModelData(3)))

	val slimeScale = 4.5
	val heightBlocks = (8.0 * slimeScale) / 16.0

	val entityKind = NamespacedKey("ballcore", "slime_pillar")

class SlimePillarManager(using sql: Storage.SQLManager, cbm: CivBeaconManager, cem: CustomEntityManager):
	sql.applyMigration(
		Storage.Migration(
			"Initial Slime Pillar Manager",
			List(
				sql"""
				CREATE TABLE SlimePillars (
					BeaconID UUID NOT NULL,
					InteractionEntityID UUID NOT NULL,
					Health INTEGER NOT NULL,
					UNIQUE(InteractionEntityID),
					FOREIGN KEY (InteractionEntityID) REFERENCES CustomEntities(InteractionEntityID) ON DELETE CASCADE
				);
				""".command
			),
			List(
				sql"""
				DROP TABLE SlimePillars;
				""".command
			)
		),
	)
	val _ = cbm
	val _ = cem

	def addPillar(interaction: Interaction, beacon: BeaconID)(using Session[IO]): IO[Unit] =
		???

class SlimePillarDebugSpawnItemStack(using cem: CustomEntityManager, spm: SlimePillarManager, cbm: CivBeaconManager, sql: Storage.SQLManager) extends CustomItem, Listeners.ItemUsedOnBlock:
	def group = Sigil.group
	def template = SlimePillar.debugSpawnItemStack

	val _ = sql
	val _ = spm
	val _ = cbm
	val _ = cem

	override def onItemUsedOnBlock(event: PlayerInteractEvent): Unit =
		???
