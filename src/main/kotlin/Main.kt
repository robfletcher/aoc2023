import kotlin.time.TimedValue
import kotlin.time.measureTimedValue

object Main

const val ANSI_GREEN = "\u001B[32m"
const val ANSI_BRIGHT_BLACK = "\u001B[90m"
const val ANSI_RESET = "\u001B[0m"

private val Int.padded
  get() = toString().padStart(2, '0')

private fun execute(n: Int) {
  puzzle(n).apply {
    println("Day $n")
    test()
    listOf(Puzzle::part1, Puzzle::part2).forEach { function ->
      execute(function, n).apply { println("Part ${function.name.takeLast(1)}: $output") }
    }
    println()
  }
}

@Suppress("UNCHECKED_CAST")
private fun puzzle(n: Int): Puzzle =
  (Class.forName("Day${n.padded}") as Class<Puzzle>).getDeclaredConstructor().newInstance()

private fun Puzzle.execute(function: Puzzle.(Sequence<String>) -> Any?, day: Int) =
  readInput("day${day.padded}").useLines { measureTimedValue { function.invoke(this, it) } }

private fun readInput(name: String) =
  checkNotNull(Main.javaClass.getResourceAsStream(name)) { "input file $name not found" }
    .reader()

private val TimedValue<*>.output
  get() = "${ANSI_GREEN}$value$ANSI_RESET ${ANSI_BRIGHT_BLACK}in $duration$ANSI_RESET"

fun main() {
  try {
    (1..25).forEach(::execute)
  } catch (_: ClassNotFoundException) {
  }
}
