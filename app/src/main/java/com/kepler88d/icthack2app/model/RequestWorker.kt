package com.kepler88d.icthack2app.model

import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.kepler88d.icthack2app.model.data.Project
import com.kepler88d.icthack2app.model.data.User
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.reflect.Type

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
                handler(User.fromJsonString(client.get {
                    url("/users/getById")
                    parameter("id", id.toString())
                }))
            }
        }

        fun getProjectById(id: Int, handler: (Project) -> Unit) {
            GlobalScope.launch {
                handler(Project.fromJsonString(client.get {
                    url("/projects/getById")
                    parameter("id", id.toString())
                }))
            }
        }

        fun getProjectList(handler: (List<Project>) -> Unit, errorHandler: () -> Unit = {}) {
            GlobalScope.launch {
                try {
                    val listType: Type = object : TypeToken<List<Project?>?>() {}.type
                    handler(GsonBuilder().create().fromJson(client.get<String> {
                        url("/projects/list")
                    }, listType))
                } catch (e: Exception) {
                    errorHandler()
                    Log.e("Project Error", e.message.toString())
                }
            }
        }

        fun addProject(
            name: String,
            description: String,
            githubProjectLink: String,
            ownerId: Int,
            vacancyList: MutableList<String>
        ) {
            GlobalScope.launch {
                client.post<String> {
                    url("/projects/add")
                    parameter("name", name)
                    parameter("description", description)
                    parameter("githubProjectLink", githubProjectLink)
                    parameter("ownerId", ownerId.toString())
                    parameter(
                        "vacancy", vacancyList
                            .groupingBy { it }
                            .eachCount()
                            .toString()
                            .replace("=", ":")
                            .replace(", ", ",")
                            .drop(1)
                            .dropLast(1)
                    )
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