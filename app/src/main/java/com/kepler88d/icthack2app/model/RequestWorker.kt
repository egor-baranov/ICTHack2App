package com.kepler88d.icthack2app.model

import android.util.Log
import com.kepler88d.icthack2app.model.data.Project
import com.kepler88d.icthack2app.model.data.User
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RequestWorker {
    companion object {
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
            githubProfileLink: String,
            handler: (User) -> Unit,
            errorHandler: () -> Unit
        ) {
            GlobalScope.launch {
                try {
                    handler(User.fromJsonString(client.post {
                        url("/users/register")
                        parameter("id", id.toString())
                        parameter("firstName", firstName)
                        parameter("lastName", lastName)
                        parameter("password", password)
                        parameter("profileDescription", profileDescription)
                        parameter("githubProfileLink", githubProfileLink)
                    }))
                } catch (e: Exception) {
                    errorHandler()
                    Log.e("Project Error", e.message.toString())
                }
            }
        }

        fun authorizeUser(
            id: Int,
            handler: (User) -> Unit,
            password: String,
            errorHandler: () -> Unit
        ) {
            GlobalScope.launch {
                try {
                    handler(User.fromJsonString(client.post {
                        url("/users/login")
                        parameter("id", id.toString())
                        parameter("password", password)
                    }))
                } catch (e: Exception) {
                    errorHandler()
                    Log.e("Project Error", e.message.toString())
                }
            }
        }
    }
}