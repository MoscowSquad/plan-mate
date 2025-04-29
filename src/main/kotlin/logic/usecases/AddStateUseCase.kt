package logic.usecases

import logic.models.Project
import logic.models.State

class AddStateUseCase {

    operator fun invoke(state: State): Boolean{
        return false
    }

    private fun isProjectExist(project: Project): Boolean{
        return false
    }

    private fun isValidTitleState(title: String): Boolean{
        return false
    }
}