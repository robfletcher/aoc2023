import java.io.Reader

fun main() {
  fun value(line: String) = "${line.first(Char::isDigit)}${line.last(Char::isDigit)}".toInt()

  fun part1(input: Reader) =
    input.useLines { lines ->
      lines.sumOf(::value)
    }

  val digits = (1..9).toList().map(Int::toString)
  val words = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")

  val parse = { digit: String ->
    when {
      digit in words -> "${words.indexOf(digit) + 1}"
      else -> digit
    }
  }

  fun firstDigit(line: String) = checkNotNull(line.findAnyOf(digits + words)).second
  fun lastDigit(line: String) = checkNotNull(line.findLastAnyOf(digits + words)).second

  fun part2(input: Reader) =
    input.useLines { lines ->
      lines.sumOf { line ->
        "${firstDigit(line).let(parse)}${lastDigit(line).let(parse)}".toInt()
      }
    }

  val testInput1 = """
    1abc2
    pqr3stu8vwx
    a1b2c3d4e5f
    treb7uchet""".trimIndent()

  assert(part1(testInput1.reader()) == 142)

  val testInput2 = """
    two1nine
    eightwothree
    abcone2threexyz
    xtwone3four
    4nineeightseven2
    zoneight234
    7pqrstsixteen""".trimIndent()

  assert(part2(testInput2.reader()) == 281)

  execute("day01", "Part 1", ::part1)
  execute("day01", "Part 2", ::part2)
}
