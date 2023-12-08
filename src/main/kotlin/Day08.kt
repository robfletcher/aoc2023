import kotlin.math.max
import kotlin.math.min

class Day08 : Puzzle {
  override fun test() {
    val input1 = """
      RL

      AAA = (BBB, CCC)
      BBB = (DDD, EEE)
      CCC = (ZZZ, GGG)
      DDD = (DDD, DDD)
      EEE = (EEE, EEE)
      GGG = (GGG, GGG)
      ZZZ = (ZZZ, ZZZ)""".trimIndent()

    assert(part1(input1.lineSequence()) == 2)

    val input2 = """
      LR

      11A = (11B, XXX)
      11B = (XXX, 11Z)
      11Z = (11B, XXX)
      22A = (22B, XXX)
      22B = (22C, 22C)
      22C = (22Z, 22Z)
      22Z = (22B, 22B)
      XXX = (XXX, XXX)""".trimIndent()

    assert(part2(input2.lineSequence()) == 6L)
  }

  override fun part1(input: Sequence<String>): Int {
    val (instructions, terrain) = parseInput(input)

    return pathLength(instructions, terrain, "AAA") { it == "ZZZ" }
  }

  override fun part2(input: Sequence<String>): Long {
    val (instructions, terrain) = parseInput(input)

    return terrain.keys
      .filter { it.endsWith('A') }
      .map { start ->
        pathLength(instructions, terrain, start) { pos -> pos.endsWith('Z') }
      }
      .map(Int::toLong)
      .reduce(::lcm)
  }

  private fun pathLength(instructions: Iterator<Char>, terrain: Map<String, Either>, start: String, done: (String) -> Boolean): Int {
    var pos = start
    var steps = 0
    while (!done(pos)) {
      when (instructions.next()) {
        'L' -> pos = terrain.getValue(pos).l
        'R' -> pos = terrain.getValue(pos).r
      }
      steps++
    }
    return steps
  }

  private fun parseInput(input: Sequence<String>): Pair<Iterator<Char>, Map<String, Either>> {
    val iterator = input.iterator()
    val instructions = iterator.next().asSequence().repeatForever().iterator()
    val terrain = iterator.asSequence().filterNot(String::isBlank).associate { line ->
      checkNotNull(Regex("""(\w+) = \((\w+), (\w+)\)""").matchEntire(line)).groupValues.let { (_, key, l, r) ->
        key to Either(l, r)
      }
    }
    return instructions to terrain
  }

  // stolen from https://www.baeldung.com/java-least-common-multiple
  fun lcm(a: Long, b: Long): Long {
    if (a == 0L || b == 0L) {
      return 0
    }
    val high = max(a, b)
    val low = min(a, b)
    var lcm = high
    while (lcm % low != 0L) {
      lcm += high
    }
    return lcm
  }

  data class Either(val l: String, val r: String)

  fun <T> Sequence<T>.repeatForever() =
    generateSequence(this) { it }.flatten()
}
