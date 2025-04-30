package data.repositories

import logic.models.Task
import logic.repositoies.TasksRepository

class TasksRepositoryImpl :TasksRepository{
    override fun getAllTasks(): List<Task> {
        return listOf()

    }
}