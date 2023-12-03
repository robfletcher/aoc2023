class Day02 : Puzzle<Int> {
  override fun test() {
    val testInput = """
    Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
    Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
    Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
    Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
    Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green""".trimIndent()

    assert(part1(testInput.lineSequence()) == 8)
    assert(part2(testInput.lineSequence()) == 2286)
  }

  fun parseHand(group: String) =
    Regex("""(\d+) (\w+)""")
      .findAll(group)
      .map(MatchResult::groupValues)
      .associate { (_, n, color) -> color to n.toInt() }

  override fun part1(input: Sequence<String>): Int {
    val bag = mapOf("red" to 12, "green" to 13, "blue" to 14)
    return input.sumOf { line ->
      val id = checkNotNull(Regex("""Game (\d+):""").find(line)).groupValues[1].toInt()
      val possible = line.substringAfter(':').split(';').all { group ->
        parseHand(group).all { it.value <= bag.getValue(it.key) }
      }
      if (possible) id else 0
    }
  }

  override fun part2(input: Sequence<String>): Int =
    input.fold(0) { acc, line ->
      line
        .substringAfter(':')
        .split(';')
        .fold(mapOf("red" to 0, "green" to 0, "blue" to 0)) { bag, group ->
          parseHand(group).let { hand ->
            bag.mapValues { (color, n) -> maxOf(n, hand[color] ?: 0) }
          }
        }
        .run { acc + values.reduce(Int::times) }
    }
}