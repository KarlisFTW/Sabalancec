package com.example.sabalancec.Products

import android.os.Parcelable
import com.example.sabalancec.models.Review
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val amount: String = "1kg", // We'll keep this for backwards compatibility
    val imageRes: Int = 0, // Keep for backwards compatibility with existing code
    val image: String = "", // Image URL from API
    val categoryId: String,
    val categoryName: String = "",
    val amountSold: Int = 0,
    val availableQuantity: Int = 0,
    val hasAllergens: Boolean = false,
    val allergenId: Int? = null,
    val rating: Float? = null,
    val providerId: Int? = null,
    val description: String = "",
    val nutritionValues: @RawValue Map<String, String> = mapOf(),
    val reviews: List<Review> = listOf()
) : Parcelable {
    // Constructor to create a product from API data
    constructor(
        id: Int,
        name: String,
        price: Double,
        categoryId: String,
        image: String,
        amountSold: Int,
        availableQuantity: Int,
        hasAllergens: String,
        allergenId: Int?,
        rating: Float?,
        usersId: Int?
    ) : this(
        id = id,
        name = name,
        price = price,
        categoryId = categoryId,
        categoryName = categoryId, // Use categoryId as categoryName
        image = image,
        amountSold = amountSold,
        availableQuantity = availableQuantity,
        hasAllergens = hasAllergens == "N" || hasAllergens == "Y",
        allergenId = allergenId,
        rating = rating,
        providerId = usersId,
        reviews = generateDummyReviews(),
        description = generateProductDescription(name),
        nutritionValues = generateNutritionValues(name)
    )

    companion object {
        private fun generateDummyReviews(): List<Review> {
            val reviewCount = (1..5).random()
            val reviews = mutableListOf<Review>()

            val names = listOf("John D.", "Sarah M.", "Michael R.", "Emma T.", "David L.",
                "Lisa K.", "Robert P.", "Jennifer S.", "William H.", "Karen B.")
            val comments = listOf(
                "Great product! Would buy again.",
                "Fresh and delicious.",
                "Good quality but a bit expensive.",
                "Perfect for my needs!",
                "Very satisfied with my purchase.",
                "Not as good as I expected.",
                "Excellent value for money.",
                "Highly recommended!",
                "Could be better.",
                "Amazing product, fast delivery."
            )

            for (i in 0 until reviewCount) {

                val rating = 1.0f + (Math.random() * 4.0f).toFloat()
                reviews.add(
                    Review(
                        names.random(),
                        rating,
                        comments.random()
                    )
                )
            }

            return reviews
        }

        private fun generateProductDescription(name: String): String {
            return "Premium quality $name. Our products are carefully selected to ensure the best flavor and nutrition. " +
                    "We source our products from trusted suppliers to maintain the highest standards of quality and freshness."
        }

        private fun generateNutritionValues(name: String): Map<String, String> {
            // Default nutritional values
            val defaultNutrition = mapOf(
                "Calories" to "100 kcal",
                "Protein" to "2g",
                "Carbohydrates" to "15g",
                "Fat" to "0.5g",
                "Fiber" to "2g"
            )

            // Specific nutritional values for common products
            return when {
                name.contains("Apple", ignoreCase = true) -> mapOf(
                    "Calories" to "52 kcal",
                    "Protein" to "0.3g",
                    "Carbohydrates" to "14g",
                    "Fat" to "0.2g",
                    "Fiber" to "2.4g"
                )
                name.contains("Banana", ignoreCase = true) -> mapOf(
                    "Calories" to "89 kcal",
                    "Protein" to "1.1g",
                    "Carbohydrates" to "22.8g",
                    "Fat" to "0.3g",
                    "Fiber" to "2.6g"
                )
                name.contains("Carrot", ignoreCase = true) -> mapOf(
                    "Calories" to "41 kcal",
                    "Protein" to "0.9g",
                    "Carbohydrates" to "9.6g",
                    "Fat" to "0.2g",
                    "Fiber" to "2.8g"
                )
                else -> defaultNutrition
            }
        }
    }
}