package se.radley.socket

import java.net._
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import akka.actor._
import akka.util.{Timeout, ByteString}
import akka.dispatch.Promise
import command._

object SocketConstants {
  val EOL = ByteString("\r\n")
  val SPACE = ByteString(" ")
  val COMMA = ByteString(",")
  val COLON = ByteString(":")
  val HASH = ByteString("#")

  // Return Commands
  val OK = ByteString("OK\r\n")
  val YES = ByteString("YES\r\n")
  val NO = ByteString("NO\r\n")
}

object SocketIteratees {
  import SocketConstants._

  def ascii(bytes: ByteString): String = bytes.decodeString("US-ASCII").trim
  def dateTime(bytes: ByteString): DateTime = DateTime.parse(ascii(bytes), DateTimeFormat.forPattern("yy-MM-dd"))
}

class SocketServer(address: InetSocketAddress = new InetSocketAddress("localhost", 0), addressPromise: Promise[SocketAddress]) extends Actor {

  val state = IO.IterateeRef.Map.async[IO.Handle]()(context.dispatcher)
  val server = IOManager(context.system) listen (address)

  override def postStop() {
    server.close()
    state.keySet foreach (_.close())
  }

  def receive = {
    case Timeout =>
      postStop()

    case IO.Listening(server, address) =>
      addressPromise.success(address)

    case IO.NewClient(server) =>
      import SocketConstants._
      val socket = server.accept()
      socket.write(ByteString("Welcome!") ++ EOL ++ ByteString("for help, type HELP and press Enter.") ++ EOL)
      state(socket) flatMap (_ => SocketServer.processRequest(socket))

    case IO.Read(socket, bytes) => state(socket)(IO.Chunk(bytes))

    case IO.Closed(socket, cause) =>
      state(socket)(IO.EOF(None))
      state -= socket
  }
}

object SocketServer {

  import SocketConstants._

  def processRequest(implicit socket: IO.SocketHandle): IO.Iteratee[Unit] = {
    IO.repeat {
      IO.take(4).flatMap {
        case Exit.command => Exit.read
        case Help.command => Help.read
        case Echo.command => Echo.read
        case Date.command => Date.read
        case Rand.command => Rand.read
        case _ => Unknown.read
      }
    }
  }
}