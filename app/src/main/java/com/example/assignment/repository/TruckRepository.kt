package com.example.assignment.repository

import com.example.assignment.apis.ApiInterface
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TruckRepository @Inject constructor(private val apiInterface: ApiInterface) {
    fun getTrucks()=apiInterface.getTruckData()
}