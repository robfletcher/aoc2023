class Day06 : Puzzle {
  override fun test() {
    val input = """
      Time:      7  15   30
      Distance:  9  40  200""".trimIndent()

    assert(part1(input.lineSequence()) == 288)
    assert(part2(input.lineSequence()) == 71503)
  }

  override fun part1(input: Sequence<String>) =
    parseInput1(input)
      .map { race ->
        (0..race.time).count { hold ->
          ((race.time - hold) * hold) > race.distance
        }
      }
      .reduce(Int::times)

  override fun part2(input: Sequence<String>) =
    parseInput2(input).let {race ->
      (0..race.time).count { hold ->
        ((race.time - hold) * hold) > race.distance
      }
    }

  private fun parseInput1(input: Sequence<String>) =
    input.toList().let {
      Regex("""\d+""").run {
        findAll(it.first()).map { it.value.toInt() }
          .zip(findAll(it.last()).map { it.value.toInt() })
          .map { (time, distance) -> Race(time, distance) }
      }
    }

  private fun parseInput2(input: Sequence<String>) =
    input.toList().let {
      Race(
        it.first().filter(Char::isDigit).toLong(),
        it.last().filter(Char::isDigit).toLong()
      )
    }

  data class Race<N : Number>(val time: N, val distance: N)
}