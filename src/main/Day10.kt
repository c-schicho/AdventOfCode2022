class Day10 : Puzzle {
    override val day = 10

    override fun task1(): String {
        val introductions = parseIntroductions()
        val cyclesOfInterest = listOf(20, 60, 100, 140, 180, 220)
        var x = 1
        var previous = 0
        var signalStrength = 0L
        var cycle = 0

        fun calculateSignalStrength() {
            if (cycle in cyclesOfInterest) {
                signalStrength += x * cycle
            }
        }

        for (introduction in introductions) {
            calculateSignalStrength()
            x += previous
            cycle++

            if (introduction.size == 1) {
                previous = 0
            } else {
                previous = introduction.last().toInt()
                calculateSignalStrength()
                cycle++
            }
        }

        return signalStrength.toString()
    }

    override fun task2(): String {
        val introductions = parseIntroductions()
        val crt = List(6) { MutableList(40) { '.' } }
        var spitePosition = 1
        var previous = 0
        var cycle = 0

        fun getCrtCol() = cycle % 40
        fun getCrtRow() = cycle / 40

        fun drawCrtPixel() {
            if (getCrtCol() in IntRange(spitePosition - 1, spitePosition + 1)) {
                crt[getCrtRow()][getCrtCol()] = '#'
            }
        }


        for (introduction in introductions) {
            spitePosition += previous
            drawCrtPixel()
            cycle++

            if (introduction.first() == "noop") {
                previous = 0
            } else {
                previous = introduction.last().toInt()
                drawCrtPixel()
                cycle++
            }
        }

        return crt.formatToString()
    }

    private fun parseIntroductions(): List<List<String>> {
        return readDataFile()
            .readLines()
            .map { it.split(' ') }
    }

    private fun List<MutableList<Char>>.formatToString(): String {
        return this.map { row ->
            row.map { element ->
                when (element) {
                    '#' -> "$COLOR_YELLOW#$COLOR_RESET"
                    else -> "$COLOR_BLACK.$COLOR_RESET"
                }
            }
        }.map {
            it.reduce { acc, s -> acc + s }
        }.reduce { acc, s -> acc + "\n" + s }
    }

    companion object {
        private const val COLOR_RESET = "\u001B[0m"
        private const val COLOR_BLACK = "\u001B[30m"
        private const val COLOR_YELLOW = "\u001B[33m"
    }
}

fun main() {
    Day10().printResults()
}