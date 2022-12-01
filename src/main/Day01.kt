class Day01 : Puzzle {
    override val day = 1

    override fun task1(): String {
        return getCarriedCalories()
            .max()
            .toString()
    }

    override fun task2(): String {
        return getCarriedCalories()
            .sortedDescending()
            .take(3)
            .sum()
            .toString()
    }

    private fun getCarriedCalories() = readDataFile()
        .readText()
        .split("\n\n")
        .map { elfString ->
            elfString.split("\n")
                .sumOf { it.toInt() }
        }
}

fun main() {
    Day01().printResults()
}