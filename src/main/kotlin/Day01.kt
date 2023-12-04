class Day01 : Puzzle {
  override fun test() {
    val testInput1 = """
    1abc2
    pqr3stu8vwx
    a1b2c3d4e5f
    treb7uchet""".trimIndent()

    assert(part1(testInput1.lineSequence()) == 142)

    val testInput2 = """
    two1nine
    eightwothree
    abcone2threexyz
    xtwone3four
    4nineeightseven2
    zoneight234
    7pqrstsixteen""".trimIndent()

    assert(part2(testInput2.lineSequence()) == 281)
  }

  fun value(line: String) = "${line.first(Char::isDigit)}${line.last(Char::isDigit)}".toInt()

  override fun part1(input: Sequence<String>): Int =
    input.sumOf(::value)

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

  override fun part2(input: Sequence<String>) =
    input.sumOf { line ->
      "${firstDigit(line).let(parse)}${lastDigit(line).let(parse)}".toInt()
    }
}