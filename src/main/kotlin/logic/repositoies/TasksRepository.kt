package logic.repositoies

import logic.models.Task

interface TasksRepository {
    fun getAll(): List<Task>
}