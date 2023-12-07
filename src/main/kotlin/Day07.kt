import Day07.HandType.*

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

  override fun part1(input: Sequence<String>) =
    input
      .associate { it.split(' ').let { (cards, bid) -> Hand(cards) to bid.toInt() } }
      .toSortedMap()
      .values
      .foldIndexed(0) { i, sum, bid -> sum + (bid * (i + 1)) }

  companion object {
    const val CARDS = "23456789TJQKA"
  }

  data class Hand(
    val cards: String
  ) : Comparable<Hand> {
    val type: HandType = cards.run { associate { c -> c to count { it == c } } }.run {
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

    override fun compareTo(other: Hand): Int {
      var result = type.compareTo(other.type)
      if (result != 0) return result
      var i = 0
      while (i < cards.length) {
        result = cards[i].let { CARDS.indexOf(it) }.compareTo(other.cards[i].let { CARDS.indexOf(it) })
        if (result != 0) return result
        i++
      }
      return 0
    }
  }

  enum class HandType {
    HighCard, OnePair, TwoPair, ThreeOfAKind, FullHouse, FourOfAKind, FiveOfAKind
  }
}