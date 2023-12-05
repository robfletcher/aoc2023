class Day05 : Puzzle {
  override fun test() {
    val input = """
      seeds: 79 14 55 13

      seed-to-soil map:
      50 98 2
      52 50 48

      soil-to-fertilizer map:
      0 15 37
      37 52 2
      39 0 15

      fertilizer-to-water map:
      49 53 8
      0 11 42
      42 0 7
      57 7 4

      water-to-light map:
      88 18 7
      18 25 70

      light-to-temperature map:
      45 77 23
      81 45 19
      68 64 13

      temperature-to-humidity map:
      0 69 1
      1 0 69

      humidity-to-location map:
      60 56 37
      56 93 4""".trimIndent()

    assert(part1(input.lineSequence()) == 35L)
  }

  override fun part1(input: Sequence<String>): Long {
    val lines = input.toList()
    val seeds = Regex("""\d+""").findAll(lines.first()).map(MatchResult::value).map(String::toLong)
    val transforms = mutableMapOf<String, Transform>()
    var lastSrc = "seed"
    lines.drop(1).forEach { line ->
      when {
        line.matches(Regex("""(\w+)-to-(\w+) map:""")) -> {
          lastSrc = line.substringBefore("-to-")
          transforms[lastSrc] = Transform(src = lastSrc, dest = line.substringAfter("-to-").substringBefore(' '))
          println("found $lastSrc to ${transforms[lastSrc]!!.dest}")
        }

        line.isNotBlank() -> Regex("""(\d+) (\d+) (\d+)""").find(line)!!.groupValues.drop(1).map(String::toLong).let { (destStart, srcStart, length) ->
          transforms[lastSrc] = transforms[lastSrc]!!.run {
            copy(mappings = mappings + Mapping(LongRange(srcStart, srcStart + length), destStart)).also {
              println(it)
            }
          }
        }
      }
    }

    return seeds.minOf { seed ->
      transforms.resolve("seed", seed)
    }
  }

  fun Map<String, Transform>.resolve(type: String, value: Long): Long {
    val transform = this[type]
    return if (transform == null) value else resolve(transform.dest, transform.translate(value)).also {
      println("$type $value = ${transform.dest} $it")
    }
  }

  data class Transform(
    val src: String,
    val dest: String,
    val mappings: Set<Mapping> = emptySet()
  ) {
    fun translate(srcValue: Long): Long {
      val mapping = mappings.find { srcValue in it.srcRange }
      return if (mapping == null) srcValue
      else mapping.srcRange.indexOf(srcValue) + mapping.destStart
    }
  }

  data class Mapping(
    val srcRange: LongRange,
    val destStart: Long
  )
}