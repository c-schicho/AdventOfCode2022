object Utils {

    fun getDataPath(day: Int): String {
        val dayString = day.toString().padStart(2, '0')
        return "data/$dayString.txt"
    }
}