package logic.usecases

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TasksUseCaseTest{
    @BeforeEach
    fun setup(){

    }
// Add task test cases
    @Test
    fun `should return task ID and Task state when add task without any issue `(){}
    @Test
    fun `should Throw exception when user title is null `(){}
    @Test
    fun `should Throw exception when state ID is null `(){}
    @Test
    fun `should Throw exception when user state is null `(){}

    // Edit task test cases
    @Test
    fun `should return task ID and Task state when edit task without any issue `(){}
    @Test
    fun `should Throw exception when user title is null during editing `(){}
    @Test
    fun `should Throw exception when task  ID is null during editing `(){}
    @Test
    fun `should Throw exception when there is no tasks found to edit `(){}

    // Delete task test cases
    @Test
    fun `should return task ID when task deleted successfully `(){}
    @Test
    fun `should Throw exception when task  ID is null during deleting `(){}
    @Test
    fun `should Throw exception when there is no tasks found to delete`(){}

    // change task state test cases
    @Test
    fun `should return task ID when state changed successfully `(){}
    @Test
    fun `should Throw exception when task ID is null during change state `(){}
    @Test
    fun `should Throw exception when state ID is null during change state `(){}
    @Test
    fun `should Throw exception when title is null during change state `(){}
    @Test
    fun `should Throw exception when there is no tasks found to change state`(){}

    // Delete all tasks test cases
    @Test
    fun `should return true when all tasks deleted successfully `(){}
    @Test
    fun `should Throw exception when there is no tasks found `(){}
    @Test
    fun `should return exception when user is not admin`(){}

    // Get task by ID
    @Test
    fun `should return task model when found task successfully `(){}
    @Test
    fun `should Throw exception when there is no tasks found during search`(){}
    @Test
    fun `should Throw exception when state ID is null during change search `(){}
}