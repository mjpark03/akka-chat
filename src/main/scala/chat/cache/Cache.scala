package chat.cache

import scala.collection.mutable
import scala.collection.Set

/**
  * Created by Rachel on 2017. 5. 17..
  */

object Cache {

  var alphabetValueMap = new mutable.HashMap[Char, mutable.HashMap[String, String]]
  val alphabetCountMap = new mutable.HashMap[Char, mutable.HashMap[String, Int]]
  val alphabetOrderMap = new mutable.HashMap[Char, mutable.HashMap[Int, mutable.LinkedHashSet[String]]]

  var capacity = 5
  var min = -1

  def get(alphabet: Char, key: String): Set[String] = {
    val valueMap = getCollectionByAlphabet(alphabet)._1

    return valueMap.keySet
  }

  def put(alphabet: Char, key: String, value: String): Unit = {
    val (valueMap, countMap, orderMap) = getCollectionByAlphabet(alphabet)

    if (valueMap.contains(key)) {
      valueMap += (key -> value)
      arrange(alphabet, key)

      return
    }

    if (valueMap.size >= capacity) {
      val removedKey = orderMap.get(min).get.head
      orderMap.get(min).get -= (removedKey)
      valueMap -= (removedKey)
    }

    valueMap += (key -> value)
    countMap += (key -> 1)
    min = 1
    orderMap.get(min).get += (key)
  }

  def arrange(alphabet: Char, key: String): String = {
    val (valueMap, countMap, orderMap) = getCollectionByAlphabet(alphabet)

    val count = countMap.get(key).getOrElse(0)
    countMap += (key -> (count + 1))

    orderMap.get(count).get -= (key)

    if (count == min && orderMap.get(count).get.size == 0)
      min = min + 1;

    if (!orderMap.contains(count + 1))
      orderMap += ((count + 1) -> new mutable.LinkedHashSet[String])

    orderMap.get(count + 1).get += (key)

    return valueMap.get(key).get
  }

  def getCollectionByAlphabet(alphabet: Char): (
      mutable.HashMap[String, String],
      mutable.HashMap[String, Int],
      mutable.HashMap[Int, mutable.LinkedHashSet[String]]) = {

    val valueMap = alphabetValueMap.get(alphabet).getOrElse(new mutable.HashMap[String, String])
    val countMap = alphabetCountMap.get(alphabet).getOrElse(new mutable.HashMap[String, Int])
    val orderMap = alphabetOrderMap.get(alphabet).getOrElse(new mutable.HashMap[Int, mutable.LinkedHashSet[String]])

    return (valueMap, countMap, orderMap)
  }

}
