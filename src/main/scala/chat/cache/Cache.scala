package chat.cache

import scala.collection.mutable
import scala.collection.Set

/**
  * Created by Rachel on 2017. 5. 17..
  */

object Cache {

  var alphabetValueMap = new mutable.HashMap[Char, mutable.LinkedHashSet[String]]
  val alphabetCountMap = new mutable.HashMap[Char, mutable.HashMap[String, Int]]
  val alphabetOrderMap = new mutable.HashMap[Char, mutable.HashMap[Int, mutable.LinkedHashSet[String]]]

  var capacity = 5
  var min = -1

  def get(alphabet: Char, key: String): Set[String] = {
    val valueSet = getCollectionByAlphabet(alphabet)._1

    return valueSet
  }

  def put(alphabet: Char, value: String): Unit = {
    val (valueSet, countMap, orderMap) = getCollectionByAlphabet(alphabet)

    if (valueSet.contains(value)) {
      arrange(alphabet, value)
      return
    }

    if (valueSet.size >= capacity) {
      val removedKey = orderMap.get(min).get.head
      orderMap.get(min).get -= (removedKey)
      valueSet -= (removedKey)
    }

    valueSet += (value)
    countMap += (value -> 1)
    min = 1
    orderMap.get(min).get += (value)
  }

  def arrange(alphabet: Char, key: String) = {
    val collections = getCollectionByAlphabet(alphabet)
    val countMap = collections._2
    val orderMap = collections._3

    val count = countMap.get(key).getOrElse(0)
    countMap += (key -> (count + 1))

    orderMap.get(count).get -= (key)

    if (count == min && orderMap.get(count).get.size == 0)
      min = min + 1;

    if (!orderMap.contains(count + 1))
      orderMap += ((count + 1) -> new mutable.LinkedHashSet[String])

    orderMap.get(count + 1).get += (key)
  }

  def getCollectionByAlphabet(alphabet: Char): (
      mutable.LinkedHashSet[String],
      mutable.HashMap[String, Int],
      mutable.HashMap[Int, mutable.LinkedHashSet[String]]) = {

    val valueSet = alphabetValueMap.get(alphabet).getOrElse(new mutable.LinkedHashSet[String])
    val countMap = alphabetCountMap.get(alphabet).getOrElse(new mutable.HashMap[String, Int])
    val orderMap = alphabetOrderMap.get(alphabet).getOrElse(new mutable.HashMap[Int, mutable.LinkedHashSet[String]])

    return (valueSet, countMap, orderMap)
  }

}
