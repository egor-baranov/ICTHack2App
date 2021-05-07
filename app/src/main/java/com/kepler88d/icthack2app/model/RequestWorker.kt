package com.kepler88d.icthack2app.model

import android.util.Log
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.get
//import io.ktor.client.features.json.*
//import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*

class RequestWorker {
    private val client = HttpClient {
//        install(JsonFeature){
//            serializer = KotlinxSerializer()
//        }
        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = "testtesttest.zapto.org"
            }
        }
    }

    suspend fun getUsersList() = client.get<List<User>>{
        url("/users/list")
    }

    suspend fun getUserById(id: Int) = client.get<String>{
        url("/users/getById")
        parameter("id", id.toString())
    }

    suspend fun getProjectsList() = client.get<String>{
        url("/projects/list")
    }

    suspend fun getProjectById(id: Int) = client.get<String>{
        url("/projects/getById")
        parameter("id", id.toString())
    }

    suspend fun getRepliesList() = client.get<List<Reply>>{
        url("/reply/list")
    }

    suspend fun getReplyById(id: Int) = client.get<Reply>{
        url("/reply/getById")
        parameter("id", id.toString())
    }



}