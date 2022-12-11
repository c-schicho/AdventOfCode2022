class Day11 : Puzzle {
    override val day = 11

    override fun task1(): String {
        val monkeys: List<Monkey> = parseFile()

        repeat(20) {
            monkeys.forEach { monkey ->
                while (monkey.hasItem) {
                    monkeys.catchThrownItem(monkey.inspectAndThrowItem(3.0))
                }
            }
        }

        return monkeys.getMonkeyBusiness()
    }

    override fun task2(): String {
        val monkeys: List<Monkey> = parseFile()

        repeat(10_000) {
            monkeys.forEach { monkey ->
                while (monkey.hasItem) {
                    monkeys.catchThrownItem(monkey.inspectAndThrowItem())
                }
            }
        }

        return monkeys.getMonkeyBusiness()
    }

    private fun List<Monkey>.getMonkeyBusiness(): String {
        return this.map { monkey -> monkey.inspectedItems }
            .sorted()
            .takeLast(2)
            .reduce(Long::times)
            .toString()
    }

    private fun List<Monkey>.catchThrownItem(thrownItem: ThrownItem) {
        this[thrownItem.toMonkeyId].items.add(thrownItem.item)
    }

    private fun parseFile(): List<Monkey> {
        val monkeyDescriptions = readDataFile()
            .readText()
            .split("\n\n")

        val monkeys = monkeyDescriptions.map { monkeyDesc -> parseMonkey(monkeyDesc) }
        val divisibleNumbersProduct = monkeyDescriptions
            .map { monkeyDesc -> parseDivisibleNumbers(monkeyDesc) }
            .reduce(Long::times)

        return monkeys.onEach { monkey -> monkey.divisibleNumbersProduct = divisibleNumbersProduct }
    }

    private fun parseMonkey(monkeyDesc: String): Monkey {
        val splitMonkeyDesc = monkeyDesc.split("\n")

        val items = splitMonkeyDesc[1]
            .split(":")
            .last()
            .split(",")
            .map { worryLevel -> Item(worryLevel.trim().toLong()) }
            .toMutableList()

        val worryLevelOpSplitLine = splitMonkeyDesc[2].split(" ")
        val worryLevelOpValue = worryLevelOpSplitLine.last()
        val worryLevelOpSign = worryLevelOpSplitLine[worryLevelOpSplitLine.size - 2]
        val worryLevelOperation = when {
            worryLevelOpValue == "old" && worryLevelOpSign == "*" -> { worryLevel: Long -> worryLevel * worryLevel }
            worryLevelOpValue == "old" && worryLevelOpSign == "+" -> { worryLevel: Long -> worryLevel + worryLevel }
            worryLevelOpSign == "*" -> { worryLevel: Long -> worryLevel * worryLevelOpValue.toLong() }
            else -> { worryLevel: Long -> worryLevel + worryLevelOpValue.toLong() }
        }

        val testNumber = splitMonkeyDesc[3].split(" ").last().toLong()
        val testFun = { worryLevel: Long -> worryLevel % testNumber == 0L }

        val trueMonkeyId = splitMonkeyDesc[4].split(" ").last().toInt()
        val falseMonkeyId = splitMonkeyDesc[5].split(" ").last().toInt()

        return Monkey(items, worryLevelOperation, testFun, trueMonkeyId, falseMonkeyId)
    }

    private fun parseDivisibleNumbers(monkeyDesc: String): Long {
        return monkeyDesc.split("\n")[3].split(" ").last().toLong()
    }

    private class Monkey(
        val items: MutableList<Item>,
        val worryLevelOperation: (worryLevel: Long) -> Long,
        val testFun: (worryLevel: Long) -> Boolean,
        val trueMonkeyId: Int,
        val falseMonkeyId: Int,
    ) {
        var divisibleNumbersProduct: Long? = null
        var inspectedItems: Long = 0
        val hasItem: Boolean
            get() = items.size > 0

        fun inspectAndThrowItem(divider: Double = 1.0): ThrownItem {
            inspectedItems++
            val item = items.removeFirst()

            item.worryLevel =
                kotlin.math.floor(worryLevelOperation(item.worryLevel) / divider).toLong() % divisibleNumbersProduct!!

            val toMonkeyId = if (testFun(item.worryLevel)) trueMonkeyId else falseMonkeyId
            return ThrownItem(item, toMonkeyId)
        }
    }

    private class Item(var worryLevel: Long)

    private data class ThrownItem(val item: Item, val toMonkeyId: Int)
}

fun main() {
    Day11().printResults()
}