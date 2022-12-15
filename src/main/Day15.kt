import kotlin.math.abs

class Day15 : Puzzle {
    override val day = 15

    override fun task1(): String {
        val sensors = parseFile()
        val minXValue = sensors.minOf(Sensor::minXValue)
        val maxXValue = sensors.maxOf(Sensor::maxXValue)

        return (minXValue..maxXValue).sumOf { x ->
            if (sensors.any { sensor -> sensor.isBaconCannotBePresent(x, 2_000_000) }) 1L else 0L
        }.toString()
    }

    override fun task2(): String {
        val sensors = parseFile()
        val validCoordinateRange = 0..4_000_000

        for (sensor in sensors) {
            val outOfRangeDistance = sensor.taxicabDistance + 1

            for (deltaX in 0..outOfRangeDistance) {
                val deltaY = sensor.taxicabDistance + 1 - deltaX

                for ((directionX, directionY) in listOf(1 to 1, -1 to -1, 1 to -1, -1 to 1)) {
                    val searchX = sensor.position.x + deltaX * directionX
                    val searchY = sensor.position.y + deltaY * directionY

                    if (searchX in validCoordinateRange && searchY in validCoordinateRange) {
                        val isNoSensorInRange = sensors.none { it.isInSensorRange(searchX, searchY) }
                        if (isNoSensorInRange) {
                            return (searchX * 4000000L + searchY).toString()
                        }
                    }
                }
            }
        }

        return (-1L).toString()
    }

    private fun parseFile(): List<Sensor> {
        fun parseYValue(s: String): Int {
            return s.split("=").last().toInt()
        }

        fun parseXValue(s: String): Int {
            return s.split("=")[1].substringBefore(',').toInt()
        }

        return readDataFile()
            .readLines()
            .map { sensorString ->
                val splitSensorString = sensorString.split(":")
                Sensor(
                    position = Coordinate(
                        x = parseXValue(splitSensorString.first()),
                        y = parseYValue(splitSensorString.first())
                    ),
                    closestBacon = Coordinate(
                        x = parseXValue(splitSensorString.last()),
                        y = parseYValue(splitSensorString.last())
                    )
                )
            }
    }

    private class Sensor(val position: Coordinate, val closestBacon: Coordinate) {
        val taxicabDistance: Int = abs(position.x - closestBacon.x) + abs(position.y - closestBacon.y)
        val minXValue: Int = position.x - taxicabDistance
        val maxXValue: Int = position.x + taxicabDistance

        fun isInSensorRange(x: Int, y: Int): Boolean {
            return ((abs(position.x - x) + abs(position.y - y)) <= taxicabDistance)
        }

        fun isBaconCannotBePresent(x: Int, y: Int): Boolean {
            return isInSensorRange(x, y) && (closestBacon.x != x || closestBacon.y != y)
        }
    }

    private class Coordinate(val x: Int, val y: Int)
}

fun main() {
    Day15().printResults()
}