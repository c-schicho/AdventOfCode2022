class Day13 : Puzzle {
    override val day = 13

    override fun task1(): String {
        return parseFile()
            .withIndex()
            .filter { (_, packet) -> compareData(packet.left, packet.right) < 0 }
            .sumOf { (index, _) -> index + 1 }
            .toString()
    }

    override fun task2(): String {
        val firstDivider = listOf(listOf(2))
        val secondDivider = listOf(listOf(6))
        val dividers = listOf(firstDivider, secondDivider)

        return parseFile()
            .flatMap { packet -> listOf(packet.left, packet.right) }
            .asSequence()
            .plus(dividers)
            .sortedWith { l1, l2 -> compareData(l1, l2) }
            .withIndex()
            .filter { (_, data) ->
                compareData(data, firstDivider) == 0 || compareData(data, secondDivider) == 0
            }.map { (index, _) -> index + 1 }
            .reduce { acc, i -> acc * i }
            .toString()
    }

    private fun parseFile(): List<DataPacket> {
        return readDataFile()
            .readText()
            .split("\n\n")
            .map(String::lines)
            .map { (left, right) ->
                DataPacket(
                    left = parseRawDataList(left),
                    right = parseRawDataList(right)
                )
            }
    }

    private fun parseRawDataList(listString: String): List<*> {
        if (listString == EMPTY_LIST) {
            return listOf<String>()
        }

        val listElements = listString.substring(1 until listString.lastIndex)
        return parseTopLevel(listElements)
            .map { element -> element.toIntOrNull() ?: parseRawDataList(element) }
    }

    private fun parseTopLevel(listString: String): List<String> {
        var elementString = listString
        val elementList = mutableListOf<String>()

        while (elementString.isNotEmpty()) {
            if (elementString.first() != BRACKETS.OPENING.sign) {
                elementList.add(elementString.substringBefore(","))
            } else {
                var index = 1
                var openBrackets = 1
                while (openBrackets > 0) {
                    when (elementString[index]) {
                        BRACKETS.OPENING.sign -> openBrackets++
                        BRACKETS.CLOSING.sign -> openBrackets--
                    }
                    index++
                }
                elementList.add(elementString.substring(0, index))
                elementString = elementString.substring(index)
            }
            elementString = elementString.substringAfter(",", "")
        }

        return elementList
    }

    private fun compareData(left: List<*>, right: List<*>): Int {
        for ((index, leftElement) in left.withIndex()) {
            if (index > right.lastIndex) {
                return 1
            }

            val rightElement = right[index]
            val comparisonValue = when (leftElement) {
                is Int -> when (rightElement) {
                    is Int -> leftElement - rightElement
                    else -> compareData(listOf(leftElement), asList(rightElement))
                }

                else -> when (rightElement) {
                    is Int -> compareData(asList(leftElement), listOf(rightElement))
                    else -> compareData(asList(leftElement), asList(rightElement))
                }
            }

            if (comparisonValue != 0) {
                return comparisonValue
            }
        }
        return left.size - right.size
    }

    private fun asList(element: Any?) = element as List<*>

    private data class DataPacket(val left: List<*>, val right: List<*>)

    private enum class BRACKETS(val sign: Char) {
        OPENING('['), CLOSING(']')
    }

    companion object {
        private const val EMPTY_LIST = "[]"
    }
}

fun main() {
    Day13().printResults()
}