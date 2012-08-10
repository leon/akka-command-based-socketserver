package se.radley.socket.command

import akka.actor.IO
import akka.util.ByteString

trait Command {

  def command: ByteString

  def read(implicit socket: IO.SocketHandle): IO.Iteratee[Unit]
}