package logic.usecases

import logic.models.State
import logic.repositoies.ProjectsRepository

class AddStateUseCase(
    private val repository: ProjectsRepository
) {

    operator fun invoke(state: State): Boolean{
        isValidTitleState(state.title)
        return repository.isProjectExist(state.projectId) // fake
    }

    private fun isValidTitleState(title: String): Boolean{
        return false
    }
}