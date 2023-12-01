import java.io.Reader

fun main() {
  fun part1(input: Reader) =
      input.useLines { lines ->
        lines.sumOf { line ->
          "${line.first(Char::isDigit)}${line.last(Char::isDigit)}".toInt()
        }
      }

  val testInput = """
    1abc2
    pqr3stu8vwx
    a1b2c3d4e5f
    treb7uchet""".trimIndent()

  assert(part1(testInput.reader()) == 142)

  execute("day01", "Part 1", ::part1)
}