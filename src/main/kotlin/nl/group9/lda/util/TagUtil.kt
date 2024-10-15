package nl.group9.lda.util

class TagUtil {
    companion object {
        fun toTeamAndBuild(tag: String): Pair<String, String> {
            val regex = Regex("""([\w-]+)-([\w\\.]+)""")
            val team = regex.find(tag)!!.groups[1]!!.value
            val build = regex.find(tag)!!.groups[2]!!.value
            return Pair(team, build)
        }
    }
}
