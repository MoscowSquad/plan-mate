package data.mongodb_data.datasource

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.mongodb_data.dto.ProjectDto
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import logic.util.ProjectNotFoundException
import java.util.*

class ProjectsDataSourceImpl(
    private val collection: MongoCollection<ProjectDto>

) : ProjectsDataSource {
    override suspend fun addProject(project: ProjectDto): Boolean {
        return collection.insertOne(project).wasAcknowledged()
    }


    override suspend fun updateProject(project: ProjectDto): Boolean {
        val filter = Filters.eq("id", project.id)
        return collection.replaceOne(filter, project).modifiedCount>0
    }

    override suspend fun deleteProject(id: UUID):Boolean {
        val filter = Filters.eq("id", id.toString())
        return collection.deleteOne(filter).deletedCount>0
    }

    override suspend fun getAllProjects(): List<ProjectDto> {
        return collection.find().toList()
    }

    override suspend fun getProjectById(id: UUID): ProjectDto {
        val filter = Filters.eq("entityId", id.toString())
        return collection.find(filter).firstOrNull()?:throw ProjectNotFoundException(id)
    }

}