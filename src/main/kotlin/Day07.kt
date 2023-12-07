import Day07.HandType.*
import java.util.Comparator

class Day07 : Puzzle {
  override fun test() {
    val input = """
      32T3K 765
      T55J5 684
      KK677 28
      KTJJT 220
      QQQJA 483""".trimIndent()

    assert(part1(input.lineSequence()) == 6440)
    assert(part2(input.lineSequence()) == 5905)
  }

  fun handComparator(cardRank: String) = object : Comparator<Hand> {
    override fun compare(a: Hand, b: Hand): Int {
      var result = a.type.compareTo(b.type)
      if (result != 0) return result
      var i = 0
      while (i < a.cards.length) {
        result = a.cards[i].let { cardRank.indexOf(it) }.compareTo(b.cards[i].let { cardRank.indexOf(it) })
        if (result != 0) return result
        i++
      }
      return 0
    }
  }

  override fun part1(input: Sequence<String>) =
    input
      .associate { it.split(' ').let { (cards, bid) -> Hand(cards) to bid.toInt() } }
      .toSortedMap(handComparator(CARDS_1))
      .values
      .foldIndexed(0) { i, sum, bid -> sum + (bid * (i + 1)) }

  override fun part2(input: Sequence<String>) =
    input
      .associate { it.split(' ').let { (cards, bid) -> Hand(cards, true) to bid.toInt() } }
      .toSortedMap(handComparator(CARDS_2))
      .values
      .foldIndexed(0) { i, sum, bid -> sum + (bid * (i + 1)) }

  companion object {
    const val CARDS_1 = "23456789TJQKA"
    const val CARDS_2 = "J23456789TQKA"
  }

  data class Hand(
    val cards: String,
    val wildcards: Boolean = false
  ) {
    val type: HandType =
      cards
        .run {
          if (wildcards) {
            val mostFrequent = filter { it != 'J' }.groupBy { it }.mapValues { it.value.size }.ifEmpty { mapOf('A' to 5) }.maxBy { it.value }.key
            replace('J', mostFrequent)
          } else {
            this
          }
        }
        .run { associate { c -> c to count { it == c } } }.run {
          when {
            size == 1 -> FiveOfAKind
            size == 2 && values.max() == 4 -> FourOfAKind
            size == 2 -> FullHouse
            values.max() == 3 -> ThreeOfAKind
            values.count { it == 2 } == 2 -> TwoPair
            values.max() == 2 -> OnePair
            else -> HighCard
          }
        }

    override fun toString(): String {
      return "Hand(cards=$cards, type=$type)"
    }
  }

  enum class HandType {
    HighCard, OnePair, TwoPair, ThreeOfAKind, FullHouse, FourOfAKind, FiveOfAKind
  }
}