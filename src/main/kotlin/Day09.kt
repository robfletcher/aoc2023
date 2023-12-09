class Day09 : Puzzle {
  override fun test() {
    val input = """
      0 3 6 9 12 15
      1 3 6 10 15 21
      10 13 16 21 30 45""".trimIndent()

    assert(part1(input.lineSequence()) == 114)
    assert(part2(input.lineSequence()) == 2)
  }

  override fun part1(input: Sequence<String>) =
    input.sumOf { line ->
      line.split(' ').map(String::toInt).findNext()
    }

  override fun part2(input: Sequence<String>) =
    input.sumOf { line ->
      line.split(' ').map(String::toInt).findPrevious()
    }

  private fun Iterable<Int>.findNext(): Int =
    if (all { it == 0 }) {
      0
    } else {
      last() + windowed(2).map { (a, b) -> b - a }.findNext()
    }

  private fun Iterable<Int>.findPrevious(): Int =
    if (all { it == 0 }) {
      0
    } else {
      first() - windowed(2).map { (a, b) -> b - a }.findPrevious()
    }
}
