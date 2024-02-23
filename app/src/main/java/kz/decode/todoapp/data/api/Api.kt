package kz.decode.todoapp.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Instant

class Api {
    companion object {

        private var instans: Service? = null
        @Synchronized
        fun getInstance() : Service {
            if(instans == null){
                instans = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .build()
                    .create(Service::class.java)
            }
                return instans as Service
        }

       private const val BASE_URL = "https://cat-fact.herokuapp.com"

    }
}