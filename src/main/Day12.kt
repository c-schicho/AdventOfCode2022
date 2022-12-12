class Day12 : Puzzle {
    override val day = 12

    override fun task1(): String {
        val gridWorld = parseFile()
        val startPosition = gridWorld.flatten().find { it.start }!!
        return findShortestDistance(
            gridWorld = gridWorld,
            start = startPosition,
            neighborSelectionTest = { current, neighbor -> neighbor.height <= current.height + 1 },
            terminateTest = { it.end }
        ).toString()
    }

    override fun task2(): String {
        val gridWorld = parseFile()
        val endPosition = gridWorld.flatten().find { it.end }!!
        return findShortestDistance(
            gridWorld = gridWorld,
            start = endPosition,
            neighborSelectionTest = { current, neighbor -> neighbor.height >= current.height - 1 },
            terminateTest = { it.height == 0 }
        ).toString()
    }

    private fun parseFile(): List<List<Position>> {
        return readDataFile()
            .readLines()
            .mapIndexed { rowIndex, rowString ->
                rowString.mapIndexed { colIndex, colChar ->
                    val height = when (colChar) {
                        'S' -> 0
                        'E' -> 25
                        else -> colChar - 'a'
                    }
                    Position(
                        row = rowIndex,
                        col = colIndex,
                        height = height,
                        start = colChar == 'S',
                        end = colChar == 'E'
                    )
                }
            }
    }

    private fun findShortestDistance(
        gridWorld: List<List<Position>>,
        start: Position,
        neighborSelectionTest: (current: Position, neighbor: Position) -> Boolean,
        terminateTest: (current: Position) -> Boolean
    ): Long {
        fun Position.getNeighbors(): List<Position> {
            val currentPosition = this
            val currentRow = currentPosition.row
            val currentCol = currentPosition.col
            val maxRow = gridWorld.size - 1
            val maxCol = gridWorld.first().size - 1
            val neighbors = mutableListOf<Position>()

            if (currentRow > 0) {
                val aboveNeighbor = gridWorld[currentRow - 1][currentCol]
                if (neighborSelectionTest(currentPosition, aboveNeighbor)) {
                    neighbors.add(gridWorld[currentRow - 1][currentCol])
                }
            }

            if (currentRow < maxRow) {
                val belowNeighbor = gridWorld[currentRow + 1][currentCol]
                if (neighborSelectionTest(currentPosition, belowNeighbor)) {
                    neighbors.add(gridWorld[currentRow + 1][currentCol])
                }
            }

            if (currentCol > 0) {
                val leftNeighbor = gridWorld[currentRow][currentCol - 1]
                if (neighborSelectionTest(currentPosition, leftNeighbor)) {
                    neighbors.add(gridWorld[currentRow][currentCol - 1])
                }
            }

            if (currentCol < maxCol) {
                val rightNeighbor = gridWorld[currentRow][currentCol + 1]
                if (neighborSelectionTest(currentPosition, rightNeighbor)) {
                    neighbors.add(gridWorld[currentRow][currentCol + 1])
                }
            }

            return neighbors
        }

        val openSet = mutableSetOf(start)
        val requiredSteps = mutableMapOf<Position, Long>()
        requiredSteps[start] = 0

        while (openSet.isNotEmpty()) {
            val current = requiredSteps.filter { openSet.contains(it.key) }.minBy { it.value }.key

            if (terminateTest(current)) {
                return requiredSteps[current]!!
            }

            openSet.remove(current)

            for (neighbor in current.getNeighbors()) {
                val requiredStepsToNeighbor = requiredSteps[current]!! + 1L
                if (requiredStepsToNeighbor < (requiredSteps[neighbor] ?: Long.MAX_VALUE)) {
                    requiredSteps[neighbor] = requiredStepsToNeighbor
                    openSet.add(neighbor)
                }
            }
        }
        return -1L
    }

    private data class Position(val row: Int, val col: Int, val height: Int, val start: Boolean, val end: Boolean)
}

fun main() {
    Day12().printResults()
}