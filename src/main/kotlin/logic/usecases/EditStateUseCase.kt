package logic.usecases

import logic.models.State
import logic.repositoies.ProjectsRepository

class EditStateUseCase(
    private val projectsRepository: ProjectsRepository
){

    operator fun invoke(state: State, title: String): State {
        return state.copy(title = title) // fake for now
    }

    private fun isValidTitle(title: String) {
        if (title.isBlank()) {
            throw IllegalArgumentException("State title cannot be blank")
        }
    }

}