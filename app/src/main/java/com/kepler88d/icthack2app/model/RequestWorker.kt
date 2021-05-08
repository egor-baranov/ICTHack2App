package com.kepler88d.icthack2app.model

import com.kepler88d.icthack2app.model.data.Project
import com.kepler88d.icthack2app.model.data.User
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RequestWorker {
    private val client = HttpClient {
        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = "testtesttest.zapto.org"
            }
        }
    }

    fun getUserById(id: Int, handler: (User) -> Unit) {
        GlobalScope.launch {
            handler(client.get {
                url("/users/getById")
                parameter("id", id.toString())
            })
        }
    }

    fun getProjectById(id: Int, handler: (Project) -> Unit) {
        GlobalScope.launch {
            handler(client.get {
                url("/projects/getById")
                parameter("id", id.toString())
            })
        }
    }

    fun getProjectList(handler: (List<Project>) -> Unit) {
        GlobalScope.launch {
            handler(client.get {
                url("/projects/list")
            })
        }
    }

    fun addProject(name: String, description: String, githubProjectLink: String, ownerId: Int) {
        GlobalScope.launch {
            client.post<String> {
                url("/projects/add")
                parameter("name", name)
                parameter("description", description)
                parameter("githubProjectLink", githubProjectLink)
                parameter("ownerId", ownerId.toString())
            }
        }
    }

    fun registerUser(
        id: Int,
        firstName: String,
        lastName: String,
        password: String,
        profileDescription: String,
        specialization: String,
        rating: Float,
        githubProfileLink: String,
        handler: (String) -> Unit
    ) {
        GlobalScope.launch {
            handler(client.post {
                url("/users/register")
                parameter("id", id.toString())
                parameter("firstName", firstName)
                parameter("lastName", lastName)
                parameter("password", password)
                parameter("profileDescription", profileDescription)
                parameter("specialization", specialization)
                parameter("rating", rating.toString())
                parameter("githubProfileLink", githubProfileLink)
            })
        }
    }

    fun authorizeUser(id: Int, password: String, handler: (User) -> Unit) {
        GlobalScope.launch {
            handler(User.fromJsonString(client.post {
                url("/users/login")
                parameter("id", id.toString())
                parameter("password", password)
            }))
        }
    }
}