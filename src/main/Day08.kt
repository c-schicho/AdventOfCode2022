class Day08 : Puzzle {
    override val day = 8

    override fun task1(): String {
        val forestLines = readDataFile().readLines()
        val visibleTrees = mutableSetOf<Tree>()
        val yMax = forestLines.size - 1
        val xMax = forestLines.first().length - 1
        var maxHeight: Int?

        for ((y, treeLine) in forestLines.withIndex()) {
            maxHeight = null
            for ((x, tree) in treeLine.withIndex()) {
                val treeHeight = tree.digitToInt()
                if (maxHeight == null || treeHeight > maxHeight) {
                    maxHeight = treeHeight
                    visibleTrees.add(Tree(x, y, treeHeight))

                    if (maxHeight == 9) {
                        break
                    }
                }
            }
        }

        for (y in (0..yMax).reversed()) {
            maxHeight = null
            for (x in (0..xMax).reversed()) {
                val treeHeight = forestLines[y][x].digitToInt()
                if (maxHeight == null || treeHeight > maxHeight) {
                    maxHeight = treeHeight
                    visibleTrees.add(Tree(x, y, treeHeight))

                    if (maxHeight == 9) {
                        break
                    }
                }
            }
        }

        for (x in 0..xMax) {
            maxHeight = null
            for (y in 0..yMax) {
                val treeHeight = forestLines[y][x].digitToInt()
                if (maxHeight == null || treeHeight > maxHeight) {
                    maxHeight = treeHeight
                    visibleTrees.add(Tree(x, y, treeHeight))

                    if (maxHeight == 9) {
                        break
                    }
                }
            }
        }

        for (x in (0..xMax).reversed()) {
            maxHeight = null
            for (y in (0..yMax).reversed()) {
                val treeHeight = forestLines[y][x].digitToInt()
                if (maxHeight == null || treeHeight > maxHeight) {
                    maxHeight = treeHeight
                    visibleTrees.add(Tree(x, y, treeHeight))

                    if (maxHeight == 9) {
                        break
                    }
                }
            }
        }

        return visibleTrees.size.toString()
    }

    override fun task2(): String {
        val forestLines = readDataFile().readLines()
        val scenicScores = mutableSetOf<Long>()

        for ((currY, treeLine) in forestLines.withIndex()) {
            for ((currX, tree) in treeLine.withIndex()) {
                if (currY == 0 || currX == 0) {
                    continue
                }

                val view = TreeHouseView()
                val yMax = forestLines.size - 1
                val xMax = treeLine.length - 1
                val considerationTreeHeight = tree.digitToInt()

                for (x in currX + 1..xMax) {
                    view.right++
                    val treeHeight = treeLine[x].digitToInt()
                    if (treeHeight >= considerationTreeHeight) {
                        break
                    }
                }

                for (x in (0 until currX).reversed()) {
                    view.left++
                    val treeHeight = treeLine[x].digitToInt()
                    if (treeHeight >= considerationTreeHeight) {
                        break
                    }
                }

                for (y in currY + 1..yMax) {
                    view.down++
                    val treeHeight = forestLines[y][currX].digitToInt()
                    if (treeHeight >= considerationTreeHeight) {
                        break
                    }
                }

                for (y in (0 until currY).reversed()) {
                    view.up++
                    val treeHeight = forestLines[y][currX].digitToInt()
                    if (treeHeight >= considerationTreeHeight) {
                        break
                    }
                }

                scenicScores.add(view.calculateScenicScore())
            }
        }

        return scenicScores.max().toString()
    }

    private data class Tree(val x: Int, val y: Int, val height: Int)

    private class TreeHouseView {
        var up: Long = 0
        var right: Long = 0
        var down: Long = 0
        var left: Long = 0

        fun calculateScenicScore(): Long = up * right * down * left
    }
}

fun main() {
    Day08().printResults()
}
