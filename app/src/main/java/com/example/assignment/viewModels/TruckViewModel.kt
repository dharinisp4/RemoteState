package com.example.assignment.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.assignment.models.TrucksModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.assignment.repository.TruckRepository
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class TruckViewModel @Inject constructor(
    private val truckRepository: TruckRepository
) : ViewModel() {


    val errorMsg = MutableLiveData<String>()
    val successMsg = MutableLiveData<String>()
    var truckModel = MutableLiveData<List<TrucksModel.Data>>()

    init{

    }

    fun getData()
    {
        truckRepository.getTrucks().enqueue(object :  retrofit2.Callback<TrucksModel> {
            override fun onResponse(call: Call<TrucksModel>, response: Response<TrucksModel>) {
                successMsg.postValue(response.body()?.responseCode?.message)
                if (response.body()?.responseCode?.responseCode==200)
                {
                    truckModel.postValue(response.body()?.data)
                }
            }

            override fun onFailure(call: Call<TrucksModel>, t: Throwable) {
             errorMsg.postValue(t.message)
            }

        })
    }


}