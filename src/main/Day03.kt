class Day03 : Puzzle {
    override val day = 3

    override fun task1(): String {
        return readDataFile()
            .readLines()
            .sumOf { contents ->
                val firstCompartment = contents.substring(0, contents.length / 2)
                var secondCompartment = contents.substring(contents.length / 2)

                firstCompartment.sumOf { itemType ->
                    if (secondCompartment.contains(itemType)) {
                        secondCompartment = secondCompartment.replace(itemType, ' ')
                        getPriority(itemType)
                    } else {
                        0
                    }
                }
            }.toString()
    }

    override fun task2(): String {
        return readDataFile()
            .readLines()
            .windowed(3, 3)
            .sumOf { backpacks ->
                val commonType = backpacks.flatMap { backpack ->
                    backpack.toCharArray().asIterable()
                }.first {
                    backpacks.first().contains(it) &&
                            backpacks[1].contains(it) &&
                            backpacks.last().contains(it)
                }
                getPriority(commonType)
            }.toString()
    }

    private fun getPriority(type: Char): Int {
        return if (type.isUpperCase()) {
            type - 'A' + 27
        } else {
            type - 'a' + 1
        }
    }
}

fun main() {
    Day03().printResults()
}