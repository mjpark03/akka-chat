package chat

import scala.collection.mutable

import cache.Cache

/**
  * Created by Rachel on 2017. 5. 12..
  */

object ChatApp extends App {

  val username = args(0)
  println("Welcome " + username)

  while(true) {
    val input = Console.in.read.toChar

    input match {
      case '\n' =>
      case ' ' =>
        val word = CacheHandler.inputChars.mkString
        println("[" + username + "] " + word)
        CacheHandler.insertCache(word)
      case char: Char => CacheHandler.inputChars += char
    }
  }

}

object CacheHandler {

  var inputChars = mutable.MutableList[Char]()

  def insertCache(word: String) = {
    val firstChar = word.head
    Cache.put(firstChar, word)
  }

}

