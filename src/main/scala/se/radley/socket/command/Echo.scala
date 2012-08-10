package se.radley.socket.command

import akka.actor.IO
import se.radley.socket.SocketConstants._
import akka.util.ByteString

object Echo extends Command {

  val command = ByteString("ECHO")

  def read(implicit socket: IO.SocketHandle) = {
    for {
      all <- IO takeUntil EOL
    } yield {
      println("Echo: " + all)
      socket.write(all ++ EOL)
    }
  }

}