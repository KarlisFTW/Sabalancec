package com.example.sabalancec.Activities

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.sabalancec.Data.Allergen
import com.example.sabalancec.R

class AllergenDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_allergen_detail)

        val allergen = intent.getParcelableExtra<Allergen>("allergen") ?: return

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
        return "This is a placeholder description for ${allergen.name}. Update this from API or local DB."
    }

    private fun getReactionFor(allergen: Allergen): String {
        return "Possible allergic reactions to ${allergen.name}. Replace with real data later."
    }
}
