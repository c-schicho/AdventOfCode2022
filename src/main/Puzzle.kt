import java.io.File

interface Puzzle {
    val day: Int

    fun task1(): String

    fun task2(): String

    fun readDataFile(): File = File(Utils.getDataPath(day))

    fun printResults() {
        println("### DAY $day / TASK 1 ###")
        println(task1())

        println("### DAY $day / TASK 2 ###")
        println(task2())
    }
}