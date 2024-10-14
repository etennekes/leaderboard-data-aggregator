package nl.group9.lda.aggregator

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.annotation.PostConstruct
import nl.group9.lda.aggregator.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*


@Service
class DataAggregatorService(val objectMapper: ObjectMapper) {

    @Value("\${app.feed.folder}")
    private lateinit var feedFolder: String

    private val requested: MutableMap<Int, DataAggregatorCommand> = HashMap()
    private val fulfilled: MutableMap<DataAggregatorCommand, AggregatedData> = HashMap()

    @PostConstruct
    fun aggregate() {
        println("Called: aggregate")

        val command = createCommand()
        if (fulfilled.containsKey(command)) {
            return
        }
        if (requested.containsValue(command)) {
            return
        }
        newAggregate(command)
    }

    @Synchronized
    private fun newAggregate(command: DataAggregatorCommand) {
        if (requested.containsValue(command)) {
            return
        }
        println("Called: newAggregate")
        val newIncrement = latestIncrement() + 1
        requested[newIncrement] = command
        fulfilled[command] = createAggregate(command)

        println("Processed aggregation.")
        println(newIncrement)
        println(requested.size)
        println(fulfilled.size)
    }

    private fun createAggregate(command: DataAggregatorCommand): AggregatedData {
        val list = command.directories.toList().map { load(it) }

        return AggregatedData(
            collectTaggedResultsList(list, DataType.OVERALL, null),
            collectTaggedResultsList(list, DataType.SYN_CPQ, null),
            collectTaggedResultsList(list, DataType.REAL_CPQ, null),
            collectTaggedResultsList(list, DataType.SYN_RPQ, null),
            collectTaggedResultsList(list, DataType.REAL_RPQ, null),
            collectTeamList(list),
            null
        )
    }

    private fun collectTeamList(list: List<DirectoryData>): Map<String, List<TeamTagResults>> {

        val teamAndBuilds = list.map { data -> TagUtil.toTeamAndBuild(data.directory) }
        val teams = teamAndBuilds.toList().map { teamAndBuild -> teamAndBuild.first }.sorted().distinct()

        val map = mutableMapOf<String, List<TeamTagResults>>()
        for (team in teams) {
            val teamTagResultList = mutableListOf<TeamTagResults>()
            for (data in list) {
                val tag = data.directory
                val teamAndBuild = TagUtil.toTeamAndBuild(tag)
                if (teamAndBuild.first == team) {
                    teamTagResultList.add(
                        TeamTagResults(
                            tag,
                            teamAndBuild.second,
                            collectTaggedResultsList(list, DataType.SYN_CPQ, tag),
                            collectTaggedResultsList(list, DataType.REAL_CPQ, tag),
                            collectTaggedResultsList(list, DataType.SYN_RPQ, tag),
                            collectTaggedResultsList(list, DataType.REAL_RPQ, tag),
                        )
                    )
                }
            }
            map.put(team, teamTagResultList.toList())
        }

        return map
    }

    private fun collectTaggedResultsList(
        list: List<DirectoryData>,
        dataType: DataType,
        tag: String?
    ): List<TaggedResults> {
        return list
            .map { data ->
                TaggedResultsBuilder(
                    data.directory,
                    data.fileDataList[dataType.name]!!
                ).build()
            }
            .filter { result -> tag?.let { tag == result.tag } ?: true }
            .sortedBy { result -> result.data.score }
    }

    private fun load(directory: String): DirectoryData {
        val location = feedFolder + File.separator + directory + File.separator
        val list = listOf(
            loadFile(location + "output-real-cpq.json", DataType.REAL_CPQ),
            loadFile(location + "output-real-rpq.json", DataType.REAL_RPQ),
            loadFile(location + "output-syn-cpq.json", DataType.SYN_CPQ),
            loadFile(location + "output-syn-rpq.json", DataType.SYN_RPQ)
        )
        val scores: List<BigDecimal> = list.map { fileData -> fileData.score }
        val sum = scores.sumOf { it }
        val average = sum.divide(BigDecimal(4), 2, RoundingMode.HALF_UP)

        val list4 = FileDataBuilder(-1L, -1L, -1L, average).build(DataType.OVERALL)

        return DirectoryData(
            directory,
            mapOf(
                Pair(list[0].dataType, list[0]),
                Pair(list[1].dataType, list[1]),
                Pair(list[2].dataType, list[2]),
                Pair(list[3].dataType, list[3]),
                Pair(list4.dataType, list4)
            )
        )
    }

    private fun loadFile(fileName: String, dataType: DataType): FileData {
        val jsonString: String = File(fileName).readText(Charsets.UTF_8)
        val fileDataBuilder = objectMapper.readValue(jsonString, FileDataBuilder::class.java)
        return fileDataBuilder.build(dataType)
    }

    private fun createCommand(): DataAggregatorCommand {
        val file = File(feedFolder)
        val directories1: Array<out String> =
            file.list { current, name -> File(current, name).isDirectory }!!.sortedArray()
        return DataAggregatorCommand(directories1)
    }

    fun latestIncrement(): Int {
        return requested.maxByOrNull { it.key }?.key ?: -1
    }

    fun getIncrement(increment: Int): AggregatedData {
        return fulfilled[requested[increment]!!]!!
    }
}
