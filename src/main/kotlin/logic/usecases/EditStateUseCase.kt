package logic.usecases

import logic.models.State
import java.util.*

class EditStateUseCase {

    operator fun invoke(state: State): State{
        return state
    }

    private fun isValidState(state: State) {
        validateTitle(state.title)
        validateProject(state.projectId)
    }

    private fun validateTitle(title: String) {
        if (title.isBlank()) {
            throw IllegalArgumentException("State title cannot be blank")
        }
    }

    private fun validateProject(projectId: UUID) {
        if (!isProjectExist(projectId)) {
            throw IllegalArgumentException("State does not belong to a valid project")
        }
    }

    private fun isProjectExist(projectId: UUID): Boolean{
        return false
    }
}