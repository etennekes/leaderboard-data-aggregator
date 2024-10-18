package nl.group9.lda.util

class TagUtil {
    companion object {
        fun toTagAndTeamAndBuild(tag: String): Triple<String, String, String> {
            val regex = Regex("""([\w- ]+)-([\w \\.]+)""")
            val team = regex.find(tag)!!.groups[1]!!.value.replace(' ', '-')
            val build = regex.find(tag)!!.groups[2]!!.value.replace(' ', '-')

            val tagBuilder = StringBuilder()

            if (team.length >= 10) {
                tagBuilder.append(team.substring(0, 10) + "∴")
            } else {
                tagBuilder.append(team)
            }

            tagBuilder.append("-")

            if (build.length >= 6) {
                tagBuilder.append(build.substring(0, 3) + "∴")
            } else {
                tagBuilder.append(build)
            }
            return Triple(tagBuilder.toString(), team, build)
        }
    }
}
