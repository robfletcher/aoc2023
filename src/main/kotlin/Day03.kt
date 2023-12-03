import kotlin.math.max
import kotlin.math.min

class Day03 : Puzzle<Int> {
  override fun test() {
    val input = """
      467..114..
      ...*......
      ..35..633.
      ......#...
      617*......
      .....+.58.
      ..592.....
      ......755.
      ...\$.*....
      .664.598..""".trimIndent()

    assert(part1(input.lineSequence()) == 4361)
  }

  fun List<String>.isPartNumber(x: IntRange, y: Int): Boolean {
    val xRange = max(0, x.first - 1) ..min(get(y).lastIndex, x.last + 1)
    val yRange = max(0, y - 1)..min(get(y).lastIndex, y + 1)
    return yRange.any { y ->
      xRange.any { x ->
        get(y).get(x).let { !it.isDigit() && it != '.' }
      }
    }
  }

  override fun part1(input: Sequence<String>): Int {
    var sum = 0
    val schematic = input.toList()
    schematic.forEachIndexed { y, line ->
      Regex("""\d+""").findAll(line)
        .filter { schematic.isPartNumber(it.range, y) }
        .map { it.value.toInt() }
        .let { sum += it.sum() }
    }
    return sum
  }
}