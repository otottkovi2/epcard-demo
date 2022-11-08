package com.epcard.plugins

import com.epcard.controllers.ControllerFactory
import com.epcard.models.OriginType
import com.epcard.models.Product
import com.epcard.models.ProductCategory
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import java.lang.Integer.parseInt

fun Application.configureRouting() {

    install(StatusPages) {
        exception<AuthenticationException> { call, cause ->
            call.respond(HttpStatusCode.Unauthorized)
        }
        exception<AuthorizationException> { call, cause ->
            call.respond(HttpStatusCode.Forbidden)
        }

    }
    val controller = ControllerFactory().controller
    routing {
        get("/") {
            call.respondText("poggers it works!")
        }

        get("/api/read"){
            controller.fillList()
            call.respond(controller.products)
        }

        get("/api/read/{id}"){
            val i = parseInt(call.parameters["id"])

            try{
                val product = controller.getProduct(i)
                call.respond(product)
            } catch (e:NotFoundException){
                call.respondText("The requested resource was not found",status = HttpStatusCode.NotFound)
            }


        }

        get("/api/submit"){
            call.respond(HttpStatusCode.BadRequest)
        }

        post("/api/submit"){
            try{
                val product = call.receive<Product>()
                controller.addProduct(product)
            } catch(e:Exception){
                call.respondText("The submitted data is not complete",status = HttpStatusCode.NotAcceptable)
                error("product could not be submitted: " + e.localizedMessage)
            }
            /*controller.addProduct(Product("tej","fak","company",ProductCategory.FOOD,OriginType.IMPORTED,
                pellet = true,
                recycled = true,
                reused = true,
                renewable = true,
                recyclable = true,
                hasInsulation = true,
                co2 = 2f,
                ch4 = 4f,
                reuseWaste = 0
            ))*/
            call.respond(HttpStatusCode.Created)

        }
        // Static plugin. Try to access `/static/index.html`
        /*static("/static") {
            resources("static")
        }*/
        singlePageApplication {
            useResources = true
            filesPath = "static"
            defaultPage = "index.html"
        }
    }
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()
