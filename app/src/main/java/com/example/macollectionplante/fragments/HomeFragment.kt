package com.example.macollectionplante.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.macollectionplante.MainActivity
import com.example.macollectionplante.PlantModel
import com.example.macollectionplante.PlantRepository.Singleton.plantList
import com.example.macollectionplante.R
import com.example.macollectionplante.R.*
import com.example.macollectionplante.adapter.PlantAdapter
import com.example.macollectionplante.adapter.PlantItemDecoration

class HomeFragment( private val context : MainActivity) : Fragment(){
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater?.inflate(layout.fragment_home, container,false)

        //recuperer le recycler view
        val horizontalRecyclerView = view?.findViewById<RecyclerView>(R.id.horizontal_recycler_view)
        horizontalRecyclerView?.adapter = PlantAdapter(context,plantList.filter { !it.liked },R.layout.item_horizontal_plant)

        //recuperer le deuxieme recycler view
        val verticalRecyclerView = view?.findViewById<RecyclerView>(R.id.vertical_recycler_view)
        verticalRecyclerView?.adapter = PlantAdapter(context,plantList,R.layout.item_vertical_plant)
        verticalRecyclerView?.addItemDecoration(PlantItemDecoration())

        return view
    }
}