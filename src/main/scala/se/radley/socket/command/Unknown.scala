package se.radley.socket.command

import akka.actor.IO
import akka.util.ByteString
import se.radley.socket.SocketConstants._

object Unknown extends Command {

  val command = null

  def read(implicit socket: IO.SocketHandle) = {
    for {
      all <- IO.takeUntil(EOL)
    } yield {
      socket.write(ByteString("Unknown") ++ EOL)
    }
  }

}
