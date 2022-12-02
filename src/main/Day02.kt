import kotlin.math.absoluteValue

class Day02 : Puzzle {
    override val day = 2

    override fun task1(): String {
        return readDataFile()
            .readLines()
            .sumOf { roundString ->
                val round = roundString
                    .split(" ")
                    .map { it.toCharArray().first() }
                getPlayerScore(playerMove = round.last(), opponentMove = round.first())
            }.toString()
    }

    override fun task2(): String {
        return readDataFile()
            .readLines()
            .sumOf { roundString ->
                val round = roundString
                    .split(" ")
                    .map { it.toCharArray().first() }
                getPlayerScoreForDesiredOutcome(desiredOutcome = round.last(), opponentMove = round.first())
            }.toString()
    }

    private fun getPlayerScoreForDesiredOutcome(desiredOutcome: Char, opponentMove: Char): Int {
        val playerMove: Char = when (desiredOutcome) {
            'Y' -> opponentMove + 23
            'X' -> getPlayerMove(1, opponentMove)
            else -> getPlayerMove(2, opponentMove)
        }
        return getPlayerScore(playerMove, opponentMove)
    }

    private fun getPlayerMove(rotationDelta: Int, opponentMove: Char): Char {
        val elements = arrayOf('X', 'Y', 'Z')
        val rotatedElements = arrayOf(*elements)
        elements.forEachIndexed { index, element ->
            rotatedElements[(index + rotationDelta) % elements.size] = element
        }
        return rotatedElements[opponentMove - 'A']
    }

    private fun getPlayerScore(playerMove: Char, opponentMove: Char) =
        getOutcomeScore(playerMove, opponentMove) + getShapeScore(playerMove)

    private fun getOutcomeScore(playerMove: Char, opponentMove: Char): Int {
        return when {
            (playerMove == 'X' && opponentMove == 'C') ||
                    (playerMove == 'Y' && opponentMove == 'A') ||
                    (playerMove == 'Z' && opponentMove == 'B') -> 6

            (playerMove == 'X' && opponentMove == 'A') ||
                    (playerMove == 'Y' && opponentMove == 'B') ||
                    (playerMove == 'Z' && opponentMove == 'C') -> 3
            else -> 0
        }
    }

    private fun getShapeScore(shape: Char) = ('W' - shape).absoluteValue
}

fun main() {
    Day02().printResults()
}