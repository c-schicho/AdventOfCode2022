class Day04 : Puzzle {
    override val day = 4

    override fun task1(): String {
        return readDataFile()
            .readLines()
            .sumOf { elfAssignments ->
                val (firstElfRangeSet, secondElfRangeSet) = getElfRangeSets(elfAssignments)
                val rangeSetsIntersection = firstElfRangeSet.intersect(secondElfRangeSet)
                if (rangeSetsIntersection == firstElfRangeSet || rangeSetsIntersection == secondElfRangeSet) 1L else 0L
            }.toString()
    }

    override fun task2(): String {
        return readDataFile()
            .readLines()
            .sumOf { elfAssignments ->
                val (firstElfRangeSet, secondElfRangeSet) = getElfRangeSets(elfAssignments)
                if (firstElfRangeSet.intersect(secondElfRangeSet).isNotEmpty()) 1L else 0L
            }.toString()
    }

    private fun getElfRangeSets(elfAssignments: String): Pair<Set<Int>, Set<Int>> {
        val elfRangeSets = elfAssignments
            .split(',')
            .map { rangeString ->
                val assignmentRange = rangeString
                    .split('-')
                    .map(String::toInt)
                IntRange(assignmentRange.first(), assignmentRange.last()).toSet()
            }

        return Pair(elfRangeSets.first(), elfRangeSets.last())
    }
}

fun main() {
    Day04().printResults()
}