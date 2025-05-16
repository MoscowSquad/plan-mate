package data.mongodb_data.datasource

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.data_source.ProjectsDataSource
import data.mongodb_data.dto.ProjectDto
import data.mongodb_data.mappers.toUUID
import domain.util.ProjectNotFoundException
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import java.util.*

class ProjectsDataSourceImpl(
    private val collection: MongoCollection<ProjectDto>
) : ProjectsDataSource {
    override suspend fun addProject(project: ProjectDto): Boolean {
        return collection.insertOne(project).wasAcknowledged()
    }

    override suspend fun updateProject(project: ProjectDto): Boolean {
        val existingProject = getProjectById(project.id.toUUID())

        val updatedProject = project.copy(objectId = existingProject.objectId)
        return collection.replaceOne(Filters.eq(ProjectDto::id.name, project.id), updatedProject).modifiedCount > 0
    }

    override suspend fun deleteProject(id: UUID):Boolean {
        val filter = Filters.eq(ProjectDto::id.name, id.toString())
        return collection.deleteOne(filter).deletedCount>0
    }

    override suspend fun getAllProjects(): List<ProjectDto> {
        return collection.find().toList()
    }

    override suspend fun getProjectById(id: UUID): ProjectDto {
        val filter = Filters.eq(ProjectDto::id.name, id.toString())
        return collection.find(filter).firstOrNull()?:throw ProjectNotFoundException(id)
    }

}