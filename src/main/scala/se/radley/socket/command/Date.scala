package se.radley.socket.command

import org.joda.time.DateTime
import akka.actor.IO
import se.radley.socket.SocketConstants._
import se.radley.socket.SocketIteratees._
import akka.util.ByteString

object Date extends Command {

  val command = ByteString("DATE")

  def read(implicit socket: IO.SocketHandle) = {
    for {
      _ <- IO drop 1
      date <- IO takeUntil EOL map dateTime
    } yield {
      println(date)
      socket.write(ByteString(date.toString("yy/MM/dd")) ++ EOL)
    }
  }

}
