package com.example.macollectionplante.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.macollectionplante.MainActivity
import com.example.macollectionplante.PlantModel
import com.example.macollectionplante.PlantPopup
import com.example.macollectionplante.R

class PlantAdapter(
    val context:MainActivity,
    private val plantList:List<PlantModel>,
    private val layoutId:Int
    ):RecyclerView.Adapter<PlantAdapter.ViewHolder>() {

    //boit pour tranger tous les composants a controller
    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        //image de la plant
        val plantImage = view.findViewById<ImageView>(R.id.image_item)
        val plantName :TextView? = view.findViewById(R.id.name_item)
        val plantDescription :TextView? = view.findViewById(R.id.description_item)
        val starIcon = view.findViewById<ImageView>(R.id.star_icon)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(layoutId,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = plantList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //recuperer les informations de la plante
        val currentPlant = plantList[position]
        //utiliser glide pour recuperer l'image a partir de son lien ->composant
       Glide.with(context).load(Uri.parse(currentPlant.imageUrl)).into(holder.plantImage)
        //metre a jour le nom de l'image
        holder.plantName?.text = currentPlant.name
        //metre a jour la description de la plante
        holder.plantDescription?.text = currentPlant.description

        //verifier si la plante a ete liker ou nom
        if(currentPlant.liked){
            holder.starIcon.setImageResource(R.drawable.ic_star)
        }
        else{
            holder.starIcon.setImageResource(R.drawable.ic_unstar)
        }
        //rajouter une interaction sur cette etoile
        holder.starIcon.setOnClickListener{
            //inverser si le button est like ou non
            currentPlant.liked = !currentPlant.liked
            //metre a jour l'objet plante

        }
    //interaction lors du click sur une plante
        holder.itemView.setOnClickListener{
            //afficher la popup
            PlantPopup(this,currentPlant).show()

        }
    }
}