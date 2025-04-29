package logic.repositoies

import logic.models.Task

interface TasksRepository {
    fun gelAllTasks(): List<Task>
}