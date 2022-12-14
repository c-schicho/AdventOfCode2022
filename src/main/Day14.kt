class Day14 : Puzzle {
    override val day = 14

    override fun task1(): String {
        return runTask1Simulation(readGridFromFile()).toString()
    }

    override fun task2(): String {
        return runTask2Simulation(readGridFromFile(addFloor = true)).toString()
    }

    private fun runTask1Simulation(grid: List<MutableList<Char>>): Long {
        fun moveSand(startX: Int, startY: Int): Boolean {
            try {
                return when (GridElement.EMPTY.sign) {
                    grid[startY + 1][startX] -> moveSand(startX, startY + 1)
                    grid[startY + 1][startX - 1] -> moveSand(startX - 1, startY + 1)
                    grid[startY + 1][startX + 1] -> moveSand(startX + 1, startY + 1)
                    grid[startY][startX] -> {
                        grid[startY][startX] = GridElement.SAND.sign
                        return true
                    }
                    else -> return false
                }
            } catch (ex: IndexOutOfBoundsException) {
                return false
            }
        }

        var sandUnits = 0L
        var sandInGrid = true
        val startX = grid.first().indexOf(GridElement.SOURCE.sign)

        while (sandInGrid) {
            sandInGrid = moveSand(startX, 0)

            if (sandInGrid) {
                sandUnits++
            }
        }

        return sandUnits
    }

    private fun runTask2Simulation(grid: List<MutableList<Char>>): Long {
        fun moveSand(startX: Int, startY: Int) {
            when {
                grid[startY + 1][startX] == GridElement.EMPTY.sign -> moveSand(startX, startY + 1)
                grid[startY + 1][startX - 1] == GridElement.EMPTY.sign -> moveSand(startX - 1, startY + 1)
                grid[startY + 1][startX + 1] == GridElement.EMPTY.sign -> moveSand(startX + 1, startY + 1)
                grid[startY][startX] == GridElement.EMPTY.sign || grid[startY][startX] == GridElement.SOURCE.sign -> {
                    grid[startY][startX] = GridElement.SAND.sign
                }
            }
        }

        var sandUnits = 0L
        var reachedSource = false
        val startX = grid.first().indexOf(GridElement.SOURCE.sign)

        while (!reachedSource) {
            moveSand(startX, 0)
            sandUnits++
            reachedSource = grid[0][startX] == GridElement.SAND.sign
        }

        return sandUnits
    }

    private fun readGridFromFile(addFloor: Boolean = false): List<MutableList<Char>> {
        val rockCoordinates = parseFile()
        var minXValue = rockCoordinates.flatten().minOf { it.x }
        var maxXValue = rockCoordinates.flatten().maxOf { it.x }
        var maxYValue = rockCoordinates.flatten().maxOf { it.y }

        if (addFloor) {
            minXValue -= 500
            maxXValue += 500
            maxYValue += 2
        }

        val xRange = maxXValue - minXValue
        val grid = List(maxYValue + 1) { MutableList(xRange + 1) { GridElement.EMPTY.sign } }
        grid[0][500 - minXValue] = GridElement.SOURCE.sign

        if (addFloor) {
            grid.last().fill(GridElement.ROCK.sign)
        }

        for (rockLineCoordinates in rockCoordinates) {
            for (coordinateIdx in 0 until rockLineCoordinates.lastIndex) {
                val (currX, currY) = rockLineCoordinates[coordinateIdx]
                val (nextX, nextY) = rockLineCoordinates[coordinateIdx + 1]

                if (currX == nextX) {
                    val rockYRange = if (currY < nextY) currY..nextY else nextY..currY
                    for (yIdx in rockYRange) {
                        grid[yIdx][currX - minXValue] = GridElement.ROCK.sign
                    }
                } else {
                    val rockXRange = if (currX < nextX) currX..nextX else nextX..currX
                    for (xIdx in rockXRange) {
                        grid[currY][xIdx - minXValue] = GridElement.ROCK.sign
                    }
                }
            }
        }
        return grid
    }

    private fun parseFile(): List<List<Coordinate>> {
        return readDataFile()
            .readLines()
            .map { line ->
                line.split(" -> ").map { value ->
                    val coordinateValues = value.split(",").map(String::toInt)
                    Coordinate(
                        x = coordinateValues.first(),
                        y = coordinateValues.last()
                    )
                }
            }
    }

    private data class Coordinate(val x: Int, val y: Int)

    private enum class GridElement(val sign: Char) {
        EMPTY('.'), SAND('o'), ROCK('#'), SOURCE('+')
    }
}

fun main() {
    Day14().printResults()
}