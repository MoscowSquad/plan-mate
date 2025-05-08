package data.mongodb_data.datasource

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import logic.models.Project
import java.util.*

class ProjectsDataSourceImpl(
    private val collection: MongoCollection<Project>

): ProjectsDataSource{
    override suspend fun addProject(project: Project):Boolean {
        val addedProject = collection.insertOne(project)
        return addedProject.wasAcknowledged()
    }


    override suspend fun updateProject(project: Project):Boolean {
        val filter = Filters.eq("id", project.id)
        val updatedProject =collection.replaceOne(filter, project)
        return updatedProject.modifiedCount>0
    }

    override suspend fun deleteProject(id: UUID):Boolean {
        val filter = Filters.eq("id", id.toString())
        val deletedProject =collection.deleteOne(filter)
        return deletedProject.deletedCount>0
    }

    override suspend fun getAllProjects(): List<Project> {
        return collection.find().toList()
    }

    override suspend fun getProjectById(id: UUID): Project {
        val filter = Filters.eq("entityId", id.toString())
        return collection.find(filter).first()
    }

}