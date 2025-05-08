import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.mongodb_data.datasource.ProjectsDataSource
import kotlinx.coroutines.flow.toList
import logic.models.Project
import java.util.*

class ProjectsDataSourceImpl(
    private val collection: MongoCollection<Project>

): ProjectsDataSource{
    override suspend fun addProject(project: Project) {
        TODO("Not yet implemented")
    }

    override suspend fun updateProject(project: Project) {
        val filter = Filters.eq("id", project.id)
        collection.replaceOne(filter, project)
    }

    override suspend fun deleteProject(id: UUID) {
        val filter = Filters.eq("id", id.toString())
        collection.deleteOne(filter)
    }

    override suspend fun getAllProjects(): List<Project> {
        return collection.find().toList()
    }

    override suspend fun getProjectById(id: UUID): Project {
        TODO("Not yet implemented")
    }

}