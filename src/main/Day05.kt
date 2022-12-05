class Day05 : Puzzle {
    override val day = 5

    override fun task1(): String {
        val (stacks, commands) = parseFile()
        commands.forEach { command -> executeCommand(command, stacks) }
        return stacks.map { stack -> stack.crates.last() }.joinToString("")
    }

    override fun task2(): String {
        val (stacks, commands) = parseFile()
        commands.forEach { command -> executeCommand(command, stacks, CraneModel.CRATE_MOVER_9001) }
        return stacks.map { stack -> stack.crates.last() }.joinToString("")
    }

    private fun executeCommand(
        command: Command,
        stacks: List<Stack>,
        craneModel: CraneModel = CraneModel.CRATE_MOVER_9000
    ) {
        val (amount, from, to) = command
        val fromStack = stacks[from - 1]
        val cratesToMove = fromStack.crates.takeLast(amount)
        val cratesToMoveInOrder = when (craneModel) {
            CraneModel.CRATE_MOVER_9000 -> cratesToMove.reversed()
            CraneModel.CRATE_MOVER_9001 -> cratesToMove
        }

        stacks[to - 1].crates.addAll(cratesToMoveInOrder)
        fromStack.crates = fromStack.crates.dropLast(amount).toMutableList()
    }

    private fun parseFile(): Pair<List<Stack>, List<Command>> {
        val splittFile = readDataFile().readText().split("\n\n")
        return Pair(
            parseStacks(splittFile.first()),
            splittFile.last()
                .split("\n")
                .map { parseCommand(it) }
        )
    }

    private fun parseStacks(stackString: String): List<Stack> {
        val stackStringList = stackString.split("\n")
        val stacks = stackStringList.last()
            .replace(" ", "")
            .map {
                Stack(
                    index = it.digitToInt(),
                    crates = mutableListOf()
                )
            }

        stackStringList
            .dropLast(1)
            .reversed()
            .forEach { rowString ->
                var stackIndex = 0
                var rowIndex = 1
                while (rowIndex < rowString.length) {
                    val crate = rowString[rowIndex]
                    if (crate != ' ') {
                        stacks[stackIndex].crates.add(crate)
                    }
                    stackIndex++
                    rowIndex += 4
                }
            }

        return stacks
    }

    private fun parseCommand(commandString: String): Command {
        val commands = commandString.split(' ')
        return Command(
            amount = commands[1].toInt(),
            from = commands[3].toInt(),
            to = commands[5].toInt()
        )
    }

    private data class Stack(
        val index: Int,
        var crates: MutableList<Char>
    )

    private data class Command(
        val amount: Int,
        val from: Int,
        val to: Int
    )

    private enum class CraneModel {
        CRATE_MOVER_9000,
        CRATE_MOVER_9001
    }
}

fun main() {
    Day05().printResults()
}