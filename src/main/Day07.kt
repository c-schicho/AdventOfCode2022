class Day07 : Puzzle {
    override val day = 7

    override fun task1(): String {
        val(_, directories) = parseCommandFile()
        return directories
            .map { dir -> dir.calculateTotalSize() }
            .filter { dirSize -> dirSize <= 100_000 }
            .sum()
            .toString()
    }

    override fun task2(): String {
        val (rootDir, directories) = parseCommandFile()
        val freeSpace = 70_000_000 - rootDir.calculateTotalSize()
        val neededSpace = 30_000_000 - freeSpace

        return directories
            .map { dir -> dir.calculateTotalSize() }
            .filter { dirSize -> dirSize >= neededSpace }
            .min()
            .toString()
    }

    private fun parseCommandFile(): ParseResults {
        val lines = readDataFile().readLines()
        val directories = mutableSetOf<Directory>()
        val rootDir = Directory(null, mutableListOf(), mutableListOf())
        var currentDir: Directory? = rootDir

        for (line in lines) {
            if (line == "$ cd ..") {
                currentDir = currentDir?.parent
            } else if (line.startsWith("$ cd")) {
                val newChildDir = Directory(currentDir, mutableListOf(), mutableListOf())
                directories.add(newChildDir)
                currentDir?.children?.add(newChildDir)
                currentDir = newChildDir
            } else if (!line.startsWith("$")) {
                if (line.startsWith("dir")) {
                    currentDir?.children?.add(
                        Directory(currentDir, mutableListOf(), mutableListOf())
                    )
                } else {
                    val fileSize = line.split(' ').first().toLong()
                    currentDir?.files?.add(
                        File(fileSize)
                    )
                }
            }
        }

        return ParseResults(rootDir, directories.toList())
    }

    private data class ParseResults(val rootDir: Directory, val dirList: List<Directory>)

    private class Directory(
        val parent: Directory?,
        val children: MutableList<Directory>,
        val files: MutableList<File>
    ) {
        fun calculateTotalSize(): Long {
            return children.sumOf { childDir -> childDir.calculateTotalSize() } + files.sumOf { file -> file.size }
        }
    }

    private class File(val size: Long)
}

fun main() {
    Day07().printResults()
}