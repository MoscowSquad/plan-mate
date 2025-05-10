package data.mongodb_data.datasource
/*
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.csv_data.dto.UserDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import java.util.UUID
import kotlin.test.Test

class UserDataSourceImplTest {
private lateinit var collection: MongoCollection<UserDto>
private lateinit var userDataSourceImpl: UserDataSourceImpl

@BeforeEach
fun setUp(){
    collection= mockk()
    userDataSourceImpl= UserDataSourceImpl(collection)
}

    @Test
    fun `addUser should return true`() = runTest {
        val user = UserDto(
            id = UUID.randomUUID().toString(),
            name = "Test User",
            hashedPassword = "hashed123",
            role = "Admin",
            projectIds = listOf()
        )

        coEvery { collection.insertOne(user) } returns Unit

        val result = userDataSourceImpl.addUser(user)

        assertTrue(result)
    }


}

 */