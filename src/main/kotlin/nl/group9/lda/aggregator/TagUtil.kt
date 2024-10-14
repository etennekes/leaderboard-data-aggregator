package nl.group9.lda.aggregator

class TagUtil {
    companion object {
        fun toTeamAndBuild(tag: String): Pair<String, Int> {
            val regex = Regex("""([\w-]+)[-]([\w]+)""")
            val team = regex.find(tag)!!.groups[1]!!.value
            val build = regex.find(tag)!!.groups[2]!!.value.toInt()
            return Pair(team, build)
        }
    }

}
