package data.repositories

import logic.models.Task
import logic.repositoies.TasksRepository

class TasksRepositoryImpl :TasksRepository{

    override fun getAll(): List<Task> {
        return listOf()

    }
}