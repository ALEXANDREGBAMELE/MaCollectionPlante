package com.example.macollectionplante

import android.net.Uri
import com.example.macollectionplante.PlantRepository.Singleton.databaseRef
import com.example.macollectionplante.PlantRepository.Singleton.downloadUri
import com.example.macollectionplante.PlantRepository.Singleton.plantList
import com.example.macollectionplante.PlantRepository.Singleton.storageReference
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.net.URI
import java.util.UUID
import javax.security.auth.callback.Callback

class PlantRepository {
    object Singleton {
        //donner le lien pour aceder au bucket
        private val BUCKET_URL: String = "gs://macollectionplante.appspot.com"

        //se connecter a notre espace de stockage
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(BUCKET_URL)
        //se connecter a la reference plante
        val databaseRef = FirebaseDatabase.getInstance().getReference("plants")

        //une liste qui va contenire nos plante
        val plantList= arrayListOf<PlantModel>()
        //une var qui permet de contenir le lien de l'image courante
        var downloadUri :Uri? = null
    }

    fun updateData(callback: () -> Unit){
        //absorber les donnees depuis la databaseRef ->Liste des plantes
        databaseRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //retirer les ancienne plantes
                plantList.clear()
                //RECOLTER LA LISTE
                for(ds in snapshot.children){
                    //construire un objet plante
                    val plant = ds.getValue(PlantModel::class.java)
                    //verifier si la plante n'est pas null
                    if(plant != null){
                        //ajouter la plante a notere liste
                        plantList.add(plant)
                    }
                }
                //actionner le callback
                callback()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }
    //une fonction pour envoyer les fichiers sur le starage
    fun uploadImage(file : Uri?, callback: () -> Unit){
        //verifier que ce fichier n'est pas null
        if (file != null){
            val fileName  = UUID.randomUUID().toString() + ".jpg"
            val ref = storageReference.child(fileName)
            val uploadTask = ref.putFile(file)
            //demarrer la tache d'envoie
           uploadTask.continueWith(Continuation<UploadTask.TaskSnapshot, Task<Uri>>{ task ->
               if (!task.isSuccessful){
                   task.exception?.let { throw it }
               }
               return@Continuation ref.downloadUrl
           }).addOnCompleteListener{ task ->
               //verifier si tout a bien fonctionner
               if(task.isSuccessful){
                   //recuperer l'image
                   downloadUri= task.result
                   callback()
               }
           }
        }
    }

    //metre a jour la plante dans la db
    fun updatePlant(plant : PlantModel) = databaseRef.child(plant.id).setValue(plant)

    //inserer une nouvelle plante dans la base de donnee
    fun InsertPlant(plant : PlantModel) = databaseRef.child(plant.id).setValue(plant)

    //supprimer une plante de la base
    fun deletePlant(plant : PlantModel) = databaseRef.child(plant.id).removeValue()

}