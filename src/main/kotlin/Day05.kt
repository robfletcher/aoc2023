import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlin.math.max
import kotlin.math.min

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
    assert(part2(input.lineSequence()) == 46L)
  }

  override fun part1(input: Sequence<String>): Long {
    val lines = input.toList()
    val seeds = Regex("""\d+""").findAll(lines.first()).map(MatchResult::value).map(String::toLong)
    val transforms = parseInput(lines.drop(1))

    return seeds.minOf { transforms.resolve("seed", it) }
  }

  override fun part2(input: Sequence<String>): Long {
    val lines = input.toList()
    val seeds = Regex("""(\d+) (\d+)""").findAll(lines.first()).map(MatchResult::groupValues).map { (_, start, length) ->
      start.toLong()until(start.toLong() + length.toLong())
    }
    val transforms = parseInput(lines.drop(1))

//    seeds.fold(emptyList<LongRange>()) { list, r2 ->
//      when {
//        list.isEmpty() -> listOf(r2)
//        list.any { it.first <= r2.first && it.last >= r2.last } -> list
//        else -> list + listOf(r2)
//      }
//    }

    return seeds.toList().run {
      runBlocking {
        map { seedRange ->
          async(Dispatchers.Default) {
            println("Searching ${seedRange.first}..${seedRange.last}")
            seedRange.minOf { seed ->
              transforms.resolve("seed", seed)
            }
          }
        }
          .minOf { it.await() }
      }
    }
  }

  private fun LongRange.merge(other: LongRange): List<LongRange> {
    return if (first <= other.first && last >= other.last) {
      // this contains other
      listOf(this)
    } else if (first >= other.first && last <= other.last) {
      // other contains this
      listOf(other)
    } else if (last < other.first || first > other.last) {
      // separate ranges
      listOf(this, other)
    } else {
      listOf(min(first, other.first)..max(last, other.last))
    }
  }

//  private suspend fun Map<String, Transform>.search(seeds: Sequence<Long>) =
//    coroutineScope {
//      seeds.toList()
//        .map { seed ->
//          async {
//            resolve("seed", seed)
//          }
//        }
//        .map { it.await() }
//    }

  private fun parseInput(lines: List<String>): Map<String, Transform> {
    val transforms = mutableMapOf<String, Transform>()
    var lastSrc = "seed"
    lines.forEach { line ->
      when {
        line.matches(Regex("""(\w+)-to-(\w+) map:""")) -> {
          lastSrc = line.substringBefore("-to-")
          transforms[lastSrc] = Transform(src = lastSrc, dest = line.substringAfter("-to-").substringBefore(' '))
        }

        line.isNotBlank() -> Regex("""(\d+) (\d+) (\d+)""").find(line)!!.groupValues.drop(1).map(String::toLong).let { (destStart, srcStart, length) ->
          transforms[lastSrc] = transforms[lastSrc]!!.run {
            copy(mappings = mappings + Mapping(srcStart, srcStart + length, destStart))
          }
        }
      }
    }
    return transforms
  }

  fun Map<String, Transform>.resolve(type: String, value: Long): Long {
    val transform = this[type]
    return if (transform == null) value else resolve(transform.dest, transform.translate(value))
  }

  data class Transform(
    val src: String,
    val dest: String,
    val mappings: Set<Mapping> = emptySet()
  ) {
    fun translate(srcValue: Long): Long {
      val mapping = mappings.find { srcValue >= it.srcStart && srcValue < it.srcEnd }
      return if (mapping == null) srcValue
      else srcValue - mapping.srcStart + mapping.destStart
    }
  }

  data class Mapping(
    val srcStart: Long,
    val srcEnd: Long,
    val destStart: Long
  )
}