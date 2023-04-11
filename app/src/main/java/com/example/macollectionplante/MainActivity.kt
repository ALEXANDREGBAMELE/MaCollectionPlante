package com.example.macollectionplante

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.macollectionplante.fragments.AddPlantFragment
import com.example.macollectionplante.fragments.CollectionFragment
import com.example.macollectionplante.fragments.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadFragment(HomeFragment(this),R.string.home_page_title)

        //importer bottom navigation view
        val navigationView = findViewById<BottomNavigationView>(R.id.navigation_view)
        navigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home_page ->{
                    loadFragment(HomeFragment(this),R.string.home_page_title)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.collection_page ->{
                    loadFragment(CollectionFragment(this),R.string.home_page_title)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.add_plant_page ->{
                    loadFragment(AddPlantFragment(this),R.string.home_page_title)
                    return@setOnNavigationItemSelectedListener true
                }
                else ->false
            }
        }

//        loadFragment(HomeFragment(this))

    }
    private fun loadFragment(homeFragment: AddPlantFragment, string: Int) {

        //actualiser le titre de la page
        findViewById<TextView>(R.id.page_title).text = resources.getString(string)

        // charger notre plante repository
        val repo = PlantRepository()
        //metre a jour la liste des plantes
        repo.updateData{
            // injecter le fragment dans notre boite (fragment container)
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, HomeFragment(this))
            transaction.addToBackStack(null)
            transaction.commit()
        }

    }
}
