package chat.handler

import akka.actor.{ Actor, ActorRef, Props }
import akka.io.{ IO, Tcp }
import akka.util.ByteString
import java.net.InetSocketAddress

/**
  * Created by Rachel on 2017. 5. 12..
  */
class SimplisticHandler extends Actor {

  import Tcp._

  def receive = {
    case Received(data) =>
      sender() ! Write(data)

    case PeerClosed =>
      context stop self
  }
}
