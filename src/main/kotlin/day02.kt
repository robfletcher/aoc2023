import java.io.Reader

fun main() {
  fun parseHand(group: String) =
    Regex("""(\d+) (\w+)""").findAll(group).map(MatchResult::groupValues).associate { (_, n, color) ->
      color to n.toInt()
    }

  fun part1(input: Reader) =
    input.useLines { lines ->
      val bag = mapOf("red" to 12, "green" to 13, "blue" to 14)
      lines.sumOf { line ->
        val id = checkNotNull(Regex("""Game (\d+):""").find(line)).groupValues[1]
        val possible = line.substringAfter(':').split(';').all { group ->
          val hand = parseHand(group)
          hand.all { it.value <= bag.getValue(it.key) }
        }
        if (possible) {
          id.toInt()
        } else {
          0
        }
      }
    }

  fun part2(input: Reader) =
    input.useLines { lines ->
      lines.fold(0) { acc, line ->
        line
          .substringAfter(':')
          .split(';')
          .fold(mapOf("red" to 0, "green" to 0, "blue" to 0)) { bag, group ->
            parseHand(group).let { hand ->
              bag.mapValues { (color, n) -> maxOf(n, hand[color] ?: 0) }
            }
          }
          .let { acc + it.values.fold(1, Int::times) }
      }
    }

  val testInput = """
    Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
    Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
    Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
    Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
    Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green""".trimIndent()

  assert(part1(testInput.reader()) == 8)
  assert(part2(testInput.reader()) == 2286)

  execute("day02", "Part 1", ::part1)
  execute("day02", "Part 2", ::part2)
}