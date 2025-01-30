import com.itera.model.Thingy
import com.itera.plugins.configureSerialization
import com.itera.plugins.configureSimpleRequests
import com.itera.plugins.configureThingyRequests
import com.itera.repository.ThingyRepository
import com.itera.service.ThingyService
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.testApplication
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test

class SampleTests {
    @Test
    fun `test simple GET request`() =
        testApplication {
            application {
                configureSimpleRequests()
            }

            val response = client.get("/get_1")
            response.status shouldBe HttpStatusCode.OK
            response.headers[HttpHeaders.ContentType] shouldContain "text/plain"
            response.bodyAsText() shouldBe "Get 1"
        }

    @Test
    fun `test with content negotiation`() {
        val repository = mockk<ThingyRepository>()

        every { repository.all() } returns listOf(Thingy(1, "Thing 1"))

        testApplication {
            application {
                configureSerialization()
                configureThingyRequests(ThingyService(repository))
            }

            val client =
                createClient {
                    install(ContentNegotiation) {
                        json()
                    }
                }

            val response = client.get("/thingies/")
            response.status shouldBe HttpStatusCode.OK
            response.headers[HttpHeaders.ContentType] shouldContain "application/json"

            val thingies = response.body<List<Thingy>>()
            thingies.size shouldBe 1
            thingies.first() shouldBe Thingy(1, "Thing 1")
        }
    }
}
