package logic.repositoies

import logic.models.Task

interface TasksRepository {
    fun getAllTasks(): List<Task>
}