package nl.group9.lda.aggregator

import jakarta.annotation.PostConstruct
import nl.group9.lda.aggregation.Aggregation
import nl.group9.lda.aggregation.AggregationId
import nl.group9.lda.aggregation.Aggregations
import nl.group9.lda.directory.DirectoryService
import nl.group9.lda.directorydata.DirectoryData
import nl.group9.lda.directorydata.DirectoryDataService
import nl.group9.lda.filedata.DataType
import nl.group9.lda.filedata.TaggedResults
import nl.group9.lda.filedata.TaggedResultsBuilder
import nl.group9.lda.filedata.TeamTagResults
import nl.group9.lda.increment.Increment
import nl.group9.lda.increment.IncrementId
import nl.group9.lda.increment.Increments
import nl.group9.lda.util.TagUtil
import org.springframework.stereotype.Service

@Service
class AggregatorService(
    val directoryService: DirectoryService,
    val directoryDataService: DirectoryDataService,
    val aggregations: Aggregations,
    val increments: Increments,
) {

    @PostConstruct
    fun aggregate() {
        val aggregationId = createAggregationId()
        if (aggregations.exists(aggregationId)) {
            return
        }
        newAggregate(aggregationId)
    }

    @Synchronized
    private fun newAggregate(aggregationId: AggregationId) {
        if (aggregations.exists(aggregationId)) {
            return
        }
        println("Reading data for " + aggregationId.directories.size + " directories.")
        val aggregation = createAggregation(aggregationId)
        aggregations.store(aggregationId, aggregation)

        val incrementId = increments.nextId()
        increments.store(incrementId, Increment(aggregationId))
        println("Data made available with increment number: " + incrementId.number)
    }

    private fun createAggregation(aggregationId: AggregationId): Aggregation {
        val list = aggregationId.directories.toList().mapNotNull { directoryDataService.load(it) }.toList()

        return Aggregation(
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
            map[team] = teamTagResultList.toList()
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

    private fun createAggregationId(): AggregationId {
        return AggregationId(directoryService.load())
    }

    fun getIncrement(increment: Int): Aggregation {
        return aggregations.load(
            increments.load(IncrementId(increment)).aggregationId
        )
    }

    fun latestIncrement(): Int {
        return increments.latestId().number
    }
}
