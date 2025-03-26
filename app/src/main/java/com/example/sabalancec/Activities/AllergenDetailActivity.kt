package com.example.sabalancec.Activities

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.sabalancec.Data.Allergen
import com.example.sabalancec.R

class AllergenDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_allergen_detail)

        val allergen = intent.getParcelableExtra<Allergen>("allergen") ?: return

        // Toolbar setup
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.title = allergen.name
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        // Content setup
        val image = findViewById<ImageView>(R.id.img_allergen)
        val name = findViewById<TextView>(R.id.txt_name)
        val description = findViewById<TextView>(R.id.txt_description)
        val reaction = findViewById<TextView>(R.id.txt_reaction)

        image.setImageResource(R.drawable.milk)
        name.text = allergen.name
        description.text = getDescriptionFor(allergen)
        reaction.text = getReactionFor(allergen)
    }

    private fun getDescriptionFor(allergen: Allergen): String {
        return "This is a placeholder description for ${allergen.name}."
    }

    private fun getReactionFor(allergen: Allergen): String {
        return "This is a placeholder reaction for ${allergen.name}."
    }
}
