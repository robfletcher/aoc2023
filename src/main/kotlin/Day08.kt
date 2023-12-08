class Day08 : Puzzle {
  override fun test() {
    val input = """
      RL

      AAA = (BBB, CCC)
      BBB = (DDD, EEE)
      CCC = (ZZZ, GGG)
      DDD = (DDD, DDD)
      EEE = (EEE, EEE)
      GGG = (GGG, GGG)
      ZZZ = (ZZZ, ZZZ)""".trimIndent()

    assert(part1(input.lineSequence()) == 2)
  }

  override fun part1(input: Sequence<String>): Int {
    val iterator = input.iterator()
    val instructions = iterator.next().asSequence().repeatForever().iterator()
    val terrain = iterator.asSequence().filterNot(String::isBlank).associate { line ->
      checkNotNull(Regex("""(\w+) = \((\w+), (\w+)\)""").matchEntire(line)).groupValues.let { (_, key, l, r) ->
        key to Either(l, r)
      }
    }

    var pos = "AAA"
    var steps = 0
    while (pos != "ZZZ") {
      when (instructions.next()) {
        'L' -> pos = terrain.getValue(pos).l
        'R' -> pos = terrain.getValue(pos).r
      }
      steps++
    }

    return steps
  }

  data class Either(val l: String, val r: String)
}

fun <T> Sequence<T>.repeatForever() =
  generateSequence(this) { it }.flatten()