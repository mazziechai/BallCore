package BallCore.CustomItems

import BallCore.Storage.KeyVal

import io.circe._
import org.bukkit.block.Block

class KeyValBlockManager(using kv: KeyVal) extends BlockManager:
    private def keyof(b: Block, key: String): String =
        s"${b.getWorld().getUID()}|${b.getX()}|${b.getY()}|${b.getZ()}|${key}"
    def store[A](block: Block, key: String, what: A)(using Encoder[A]): Unit =
        kv.set("blockdb", keyof(block, key), what)
    def retrieve[A](block: Block, key: String)(using Decoder[A]): Option[A] =
        kv.get("blockdb", keyof(block, key))
    def remove(block: Block, key: String): Unit =
        kv.remove("blockdb", keyof(block, key))
