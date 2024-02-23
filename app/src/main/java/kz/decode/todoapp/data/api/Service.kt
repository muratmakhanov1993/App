package kz.decode.todoapp.data.api

import kz.decode.todoapp.data.entity.Fact
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface Service {
    @GET("facts/random")
   suspend fun getRandomFact() : Response<Fact>

   @GET("facts/{factID}")
   suspend fun getFactInfoById(@Path("factID") id: String):Response<Fact>

   @GET("/facts/random?animal_type=cat&amount=2")
   suspend fun getFactAnimal():Response<List<Fact>>

}