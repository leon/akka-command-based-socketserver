package se.radley.socket.command

import akka.util.ByteString
import akka.actor.IO
import se.radley.socket.SocketConstants._

object Help extends Command {

  val command = ByteString("HELP")

  val helpText =
    """
      | Socket Server Help!
      |
      | Available commands:
      |
      | * EXIT
      |   Closes connection to socket server
      |
      | * ECHO Hello World
      |   Echoes everything after ECHO back to the socket
      |
      | * DATE 2012-01-01
      |   Parses date input YYYY-mm-dd to a jodatime and outputs to socket in YY/MM/DD format
      |
      | * RAND 5 of 10
      |   Generates a 5 column, 10 row grid with numbers between 0 and 9
      |
    """.stripMargin

  def read(implicit socket: IO.SocketHandle) = {
    for {
      _ <- IO takeUntil EOL
    } yield {
      println("Help")
      socket.write(ByteString(helpText))
    }
  }

}
