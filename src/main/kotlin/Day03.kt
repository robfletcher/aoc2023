import kotlin.math.max
import kotlin.math.min

class Day03 : Puzzle {
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
    assert(part2(input.lineSequence()) == 467835)
  }

  fun List<String>.isPartNumber(x: IntRange, y: Int): Boolean {
    val xRange = max(0, x.first - 1)..min(get(y).lastIndex, x.last + 1)
    val yRange = max(0, y - 1)..min(lastIndex, y + 1)
    return yRange.any { y ->
      xRange.any { x ->
        this[y][x].let { !it.isDigit() && it != '.' }
      }
    }
  }

  override fun part1(input: Sequence<String>) =
    input.toList()
      .run {
        mapIndexed { y, line ->
          Regex("""\d+""").findAll(line)
            .filter { isPartNumber(it.range, y) }
            .map { it.value.toInt() }
            .sum()
        }
      }
      .reduce(Int::plus)

  fun List<String>.adjacentNumbers(x: Int, y: Int): Collection<Int> {
    val xRange = max(0, x - 1)..min(get(y).lastIndex, x + 1)
    val yRange = max(0, y - 1)..min(lastIndex, y + 1)
    return slice(yRange)
      .flatMap { line ->
        Regex("""\d+""")
          .findAll(line)
          .filter { match -> match.range.any { it in xRange } }
          .map { it.value.toInt() }
      }
  }

  fun List<String>.gearRatio(x: Int, y: Int) =
    when (this[y][x]) {
      '*' -> adjacentNumbers(x, y).run { if (size < 2) 0 else reduce(Int::times) }
      else -> 0
    }

  override fun part2(input: Sequence<String>) =
    input.toList()
      .run {
        mapIndexed { y, line ->
          line.indices.sumOf { x -> gearRatio(x, y) }
        }
      }
      .reduce(Int::plus)
}