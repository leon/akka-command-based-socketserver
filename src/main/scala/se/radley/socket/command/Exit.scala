package se.radley.socket.command

import akka.util.ByteString
import akka.actor.IO
import se.radley.socket.SocketConstants._

object Exit extends Command {

  val command = ByteString("EXIT")

  def read(implicit socket: IO.SocketHandle) = {
    for {
      _ <- IO takeUntil EOL
    } yield {
      println("Exit")
      socket.close()
    }
  }

}
