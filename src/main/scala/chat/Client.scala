package chat

import akka.actor.{ Actor, ActorRef, Props }
import akka.io.{ IO, Tcp }
import akka.util.ByteString
import java.net.InetSocketAddress

/**
  * Created by Rachel on 2017. 5. 12..
  */

object Client {

}

class Client(remote: InetSocketAddress, listener: ActorRef) extends Actor {

  import Tcp._
  import context.system

  IO(Tcp) ! Connect(remote)

  def receive = {

    case CommandFailed(_: Connect) =>
      listener ! "connect failed"
      context stop self

    case c @ Connected(remote, local) =>
      listener ! c
      val connection = sender()
      connection ! Register(self)

      context become {
        case data: ByteString =>
          connection ! Write(data)

        case CommandFailed(w: Write) =>
          listener ! "write failed"

        case Received(data) =>
          listener ! data

        case "close" =>
          connection ! Close

        case _: ConnectionClosed =>
          listener ! "connection closed"
          context stop self
      }
  }

}
