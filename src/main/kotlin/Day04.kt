class Day04 : Puzzle {
  override fun test() {
    val input = """
      Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
      Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
      Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
      Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
      Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
      Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11""".trimIndent()

    assert(part1(input.lineSequence()) == 13)
    assert(part2(input.lineSequence()) == 30)
  }

  override fun part1(input: Sequence<String>) =
    input.sumOf { line ->
      when (val n = line.parse().winCount) {
        0 -> 0
        else -> (2..n).fold(1) { sum, _ -> sum * 2 } as Int
      }
    }

  override fun part2(input: Sequence<String>): Any {
    val cards = input.map { it.parse() }.toList()
    val counts = cards.associate { it.number to 1 }.toMutableMap()
    cards.forEach { card ->
      card.won.forEach { n ->
        counts[n] = counts.getValue(n) + counts.getValue(card.number)
      }
    }
    return counts.values.sum()
  }

  data class Card(
    val number: Int,
    val winCount: Int
  ) {
    val won: List<Int>
      get() = ((number + 1)..(number + winCount)).toList()
  }

  fun String.parse() =
    Card(
      checkNotNull(Regex("""Card\s+(\d+):""").find(this)).groupValues.last().toInt(),
      Regex("""\d+""").run {
        val winners = findAll(substringAfter(':').substringBefore('|')).map { it.value.toInt() }
        findAll(substringAfter('|')).map { it.value.toInt() }.filter { it in winners }.toList()
      }.size
    )
}
