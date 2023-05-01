package BallCore.Contemplations

import BallCore.CraftingStations
import BallCore.CraftingStations.DyeVat
import BallCore.CraftingStations.GlazingKiln
import BallCore.CraftingStations.Kiln

case class ContemplationTag(
	val name: String,
	val description: String,
)

case class ContemplationScene(
	val name: String,
	val body: ContemplationSceneBody,
	val tags: List[ContemplationTag],
)

enum ContemplationSceneBody:
	case craftingStation(val recipes: List[CraftingStations.Recipe])

object Contemplations:
	object Tags:
		val hearts = ContemplationTag("Civilization Beacons", "Items that provide benefits to their civilizations")
		val craftingStations = ContemplationTag("Crafting Stations", "Items that allow for mass production with increased efficiency")

	ContemplationScene("Dye Vat", ContemplationSceneBody.craftingStation(DyeVat.recipes), List(Tags.craftingStations))
	ContemplationScene("Glazing Kiln", ContemplationSceneBody.craftingStation(GlazingKiln.recipes), List(Tags.craftingStations))
	ContemplationScene("Kiln", ContemplationSceneBody.craftingStation(Kiln.recipes), List(Tags.craftingStations))
