package com.example.macollectionplante.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.macollectionplante.MainActivity
import com.example.macollectionplante.PlantModel
import com.example.macollectionplante.PlantRepository
import com.example.macollectionplante.PlantRepository.Singleton.downloadUri
import com.example.macollectionplante.R
import java.util.UUID

class AddPlantFragment(
    private val context : MainActivity,
) : Fragment() {
    private var file : Uri? = null
    private var uploadedImage : ImageView? = null
    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater?.inflate(R.layout.fragment_add_plant, container,false)

        //recuperer uploadedImage pour lui associer son composant
        uploadedImage = view?.findViewById(R.id.preview_image)

        //recuperer le button pour charger l'image
        val pikupImageButton = view?.findViewById<Button>(R.id.upload_button)
        //lorsqu'on click sur le button ca ouvre les images du telephone
        pikupImageButton?.setOnClickListener{
            pikupImage()
        }
        //recuperer le buton confirm
        val confirmButton = view?.findViewById<Button>(R.id.confirm_button)
        confirmButton?.setOnClickListener{ sendForm(view) }
        return view
    }

    private fun sendForm(view: View) {

        //heberger sur le bucket
        val repo = PlantRepository()
        repo.uploadImage(file!!){
            val plantName = view.findViewById<EditText>(R.id.name_input).text.toString()
            val plantDescription = view.findViewById<EditText>(R.id.description_input).text.toString()
            val grow = view.findViewById<Spinner>(R.id.grow_spinner_input).selectedItem.toString()
            val water = view.findViewById<Spinner>(R.id.water_spinner_input).selectedItem.toString()
            val downloadImageUrl = downloadUri

            //creer un nouvel objet plante
            val plant = PlantModel(
                UUID.randomUUID().toString(),
                plantName,
                plantDescription,
                downloadImageUrl.toString(),
                grow,
                water
            )


            //envoyer dans la base de donnee
            repo.InsertPlant(plant)
        }


    }

    private fun pikupImage() {
        val intent = Intent()
        intent.type = "image/"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), 47)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 47 && requestCode == Activity.RESULT_OK){
            //verifier si les donnees sont nulles
            if (data == null || data.data == null) return
            //recuperer image
            val file = data.data
            //mettre a jour l'apercu de l'image
            uploadedImage?.setImageURI(file)


        }
    }
}