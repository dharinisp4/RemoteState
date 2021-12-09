package com.example.assignment


import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignment.adapter.TrucksAdapter
import com.example.assignment.databinding.ActivityMainBinding
import com.example.assignment.models.TrucksModel
import com.example.assignment.viewModels.TruckViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.example.assignment.utils.NetworkManager

import com.google.android.gms.common.GooglePlayServicesNotAvailableException

import com.google.android.gms.maps.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.example.assignment.utils.Utils
import com.google.android.gms.maps.model.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), View.OnClickListener, OnMapReadyCallback {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: TruckViewModel by viewModels()
    var isList :Boolean = true
    lateinit var trucksAdapter :TrucksAdapter
    var list =ArrayList<TrucksModel.Data>()
    var map: GoogleMap? = null

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        trucksAdapter = TrucksAdapter(this,list)
        binding.rvItems.apply {
            layoutManager=LinearLayoutManager(context)
            adapter=trucksAdapter
        }
        binding.ivRefresh.setOnClickListener(this)
        binding.ivType.setOnClickListener(this)
        binding.ivSearch.setOnClickListener(this)
        callApi()
        setObserver()
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.onResume() // needed to get the map to display immediately


        try {
            MapsInitializer.initialize(this)
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText?.isNotEmpty() == true) {
                    filter(newText)
                } else list.let { trucksAdapter.updateList(it) }
                return true
            }
        })
        }


    fun filter(text: String?) {
        val temp: ArrayList<TrucksModel.Data> = ArrayList()
        for (truck in list) {
            if (text?.let { truck.truckNumber.startsWith(it, true) } == true
            ) {
                temp.add(truck)
            }
        }
       trucksAdapter.updateList(temp)
    }
    private fun callApi()
    {
        if (NetworkManager.isConnected(binding.root, this)) {
            viewModel.getData()
        }
    }
    private fun setObserver()
    {
        viewModel.truckModel.observe(this)
        {
            list.clear()
            list.addAll(it)
            trucksAdapter.notifyDataSetChanged()
            Log.e("trucksList",it.size.toString())
        }
    }

    override fun onClick(p0: View?) {
       when(p0?.id)
       {
           R.id.iv_search->{
               binding.searchView.visibility= VISIBLE
           }
           R.id.iv_type->{

               setViews()

           }
           R.id.iv_refresh->{
               callApi()
           }
       }
    }



private fun setViews()
{
    if (isList)
    {

        binding.rvItems.visibility=GONE
        binding.mapView.visibility=VISIBLE
        binding.ivSearch.visibility=GONE
        binding.searchView.visibility=GONE
        binding.ivType.setImageDrawable(resources.getDrawable(R.drawable.ic_street_map,null))
        isList = false
        binding.mapView!!.getMapAsync(this)
    }
    else{
        binding.rvItems.visibility=VISIBLE
        binding.mapView.visibility=GONE
        binding.ivSearch.visibility=VISIBLE
        binding.ivType.setImageDrawable(resources.getDrawable(R.drawable.ic_list,null))
        isList = true
    }
}

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val boundsIndia = LatLngBounds(LatLng(23.63936, 68.14712), LatLng(28.20453, 97.34466))
        val padding = 0 // offset from edges of the map in pixels
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(boundsIndia, padding)
        map!!.moveCamera(cameraUpdate)
        for (i in 0 until list.size) {
            val latLang = LatLng(list[i].lastWaypoint.lat, list[i].lastWaypoint.lng)
            var icon : BitmapDescriptor?=null
            icon = Utils.drawableToBitmap(
                resources.getDrawable(
                    R.drawable.ic_truck_running, null)
            )?.let {
                BitmapDescriptorFactory.fromBitmap(
                    it
                )
            }
            when(list[i].lastWaypoint.ignitionOn) {
                true -> {
                    if (list[i].lastWaypoint.speed > 0 && list[i].lastRunningState.truckRunningState == 1) {
                        icon = Utils.drawableToBitmap(
                            resources.getDrawable(
                                R.drawable.ic_truck_running,
                                null
                            )
                        )?.let {
                            BitmapDescriptorFactory.fromBitmap(
                                it
                            )
                        }
                    } else {
                        icon = Utils.drawableToBitmap(
                            resources.getDrawable(
                                R.drawable.ic_truck_idle,
                                null
                            )
                        )?.let {
                            BitmapDescriptorFactory.fromBitmap(
                                it
                            )
                        }
                    }
                }
                false -> {
                    if (list[i].lastRunningState.truckRunningState == 0) {
                        icon = Utils.drawableToBitmap(
                            resources.getDrawable(
                                R.drawable.ic_truck_stopped,
                                null
                            )
                        )?.let {
                            BitmapDescriptorFactory.fromBitmap(
                                it
                            )
                        }
                    } else {

                            icon = Utils.drawableToBitmap(
                                resources.getDrawable(
                                    R.drawable.ic_truck_error,
                                    null
                                )
                            )?.let {
                                BitmapDescriptorFactory.fromBitmap(
                                    it
                                )
                            }

                    }
                }

            }
                map!!.addMarker(MarkerOptions().position(latLang).title("Marker").icon(icon))
        }
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }


}