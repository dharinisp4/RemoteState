package com.example.assignment.apis


import com.example.assignment.models.TrucksModel
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {


    @GET("https://api.mystral.in/tt/mobile/logistics/searchTrucks?auth-company=PCH&companyId=33&deactivated=false&key=g2qb5jvucg7j8skpu5q7ria0mu&q-expand=true&q-include=lastRunningState,lastWaypoint")
    fun getTruckData() : Call<TrucksModel>


}