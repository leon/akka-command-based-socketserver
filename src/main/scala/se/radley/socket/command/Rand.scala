package se.radley.socket.command

import akka.actor.IO.SocketHandle
import akka.actor.IO
import se.radley.socket.SocketConstants._
import se.radley.socket.SocketIteratees._
import akka.util.ByteString
import scala.util.Random

object Rand extends Command {

  val command = ByteString("RAND")

  def read(implicit socket: SocketHandle) = {
    for {
      _ <- IO drop 1
      cols <- IO takeUntil ByteString(" of ") map ascii map(_.toInt)
      rows <- IO takeUntil EOL map ascii map(_.toInt)
    } yield {
      val vals = Seq.fill(cols * rows)(Random.nextInt(10))
      val grouped = vals.grouped(cols)
      grouped.foreach { values =>
        if (values.length == cols && grouped.hasNext) {
          socket.write(ByteString(values.mkString("|") + "\n\r"))
        } else {
          socket.write(ByteString(values.mkString("|")))
        }
      }
      socket.write(EOL)
    }
  }

}
