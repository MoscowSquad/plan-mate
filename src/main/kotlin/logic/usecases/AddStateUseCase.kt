package logic.usecases

import logic.models.State
import logic.repositoies.ProjectsRepository

class AddStateUseCase(
    private val projectRepository: ProjectsRepository
) {

    operator fun invoke(state: State): Boolean{
        isValidTitleState(state.title)
        return projectRepository.isExist(state.projectId) // fake
    }

    private fun isValidTitleState(title: String): Boolean{
        return false
    }
}