class Day09 : Puzzle {
    override val day = 9

    override fun task1(): String {
        val env = Environment(2)
        moveRope(env)
        return env.numberOfVisitedFields.toString()
    }

    override fun task2(): String {
        val env = Environment(10)
        moveRope(env)
        return env.numberOfVisitedFields.toString()
    }

    private fun moveRope(env: Environment) {
        readDataFile()
            .readLines()
            .map { it.split(" ") }
            .forEach { env.step(it.first(), it.last().toInt()) }
    }

    private class Environment(nKnots: Int) {
        private val knots = mutableListOf<Knot>()
        private val visitedFields = mutableSetOf(Field())
        private val tailIndex = nKnots - 1

        val numberOfVisitedFields
            get() = visitedFields.size

        init {
            IntRange(1, nKnots).forEach { _ ->
                knots.add(Knot())
            }
        }

        fun step(direction: String, length: Int) {
            val head = knots.first()
            when (direction) {
                Direction.UP.code -> head.y += length
                Direction.RIGHT.code -> head.x += length
                Direction.DOWN.code -> head.y -= length
                Direction.LEFT.code -> head.x -= length
            }

            alignKnots()
        }

        private fun alignKnots() {
            var prevKnot = knots.first()

            for ((index, currKnot) in knots.withIndex()) {

                if (!isKnotInRange(prevKnot, currKnot)) {

                    while (!areKnotsInSameColumnOrRow(prevKnot, currKnot) && !isKnotInRange(prevKnot, currKnot)) {
                        if (prevKnot.x > currKnot.x) currKnot.x++ else currKnot.x--
                        if (prevKnot.y > currKnot.y) currKnot.y++ else currKnot.y--

                        if (index == tailIndex) {
                            updateVisitedFields(currKnot)
                        }
                    }

                    while (!isKnotInRange(prevKnot, currKnot)) {
                        if (areKnotsInSameRow(prevKnot, currKnot)) {
                            if (prevKnot.x > currKnot.x) currKnot.x++ else currKnot.x--
                        } else {
                            if (prevKnot.y > currKnot.y) currKnot.y++ else currKnot.y--
                        }

                        if (index == tailIndex) {
                            updateVisitedFields(currKnot)
                        }
                    }
                }
                prevKnot = currKnot
            }
        }

        private fun updateVisitedFields(currKnot: Knot) = visitedFields.add(Field(currKnot.x, currKnot.y))

        private fun isKnotInRange(prevKnot: Knot, currKnot: Knot) =
            currKnot.x in IntRange(prevKnot.x - 1, prevKnot.x + 1) &&
                    currKnot.y in IntRange(prevKnot.y - 1, prevKnot.y + 1)

        private fun areKnotsInSameColumnOrRow(prevKnot: Knot, currKnot: Knot) =
            currKnot.x == prevKnot.x || currKnot.y == prevKnot.y

        private fun areKnotsInSameRow(prevKnot: Knot, currKnot: Knot) = currKnot.y == prevKnot.y

        private enum class Direction(val code: String) {
            UP("U"), RIGHT("R"), DOWN("D"), LEFT("L")
        }

        private class Knot(var x: Int = 1, var y: Int = 1)

        private data class Field(val x: Int = 1, val y: Int = 1)
    }
}

fun main() {
    Day09().printResults()
}
