class Day06 : Puzzle {
    override val day = 6

    override fun task1(): String {
        return findNumberOfProcessedChars(4)
    }

    override fun task2(): String {
        return findNumberOfProcessedChars(14)
    }

    private fun findNumberOfProcessedChars(markerSize: Int): String {
        return readDataFile()
            .readText()
            .windowed(markerSize)
            .map { window -> window.toCharArray().toSet() }
            .indexOfFirst { it.size == markerSize }
            .plus(markerSize)
            .toString()
    }
}

fun main() {
    Day06().printResults()
}