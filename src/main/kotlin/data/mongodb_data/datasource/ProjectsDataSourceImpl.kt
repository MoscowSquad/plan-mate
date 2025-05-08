import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.mongodb_data.datasource.ProjectsDataSource
import data.mongodb_data.dto.AuditLogDto
import logic.models.Project
import java.util.*

class ProjectsDataSourceImpl(
    private val collection: MongoCollection<AuditLogDto>

): ProjectsDataSource{
    override suspend fun addProject(project: Project) {
        TODO("Not yet implemented")
    }

    override suspend fun updateProject(project: Project) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteProject(id: UUID) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllProjects(): List<Project> {
        TODO("Not yet implemented")
    }

    override suspend fun getProjectById(id: UUID): Project {
        TODO("Not yet implemented")
    }

}