package com.example.data

import com.example.R
import com.example.model.FoodCategory
import com.example.model.FoodItem

object CanteenRepository {

  val menuItems: List<FoodItem> = listOf(
    // Meals
    FoodItem(
      id = "m1",
      name = "Crispy Chicken Burger",
      price = 4.49,
      category = FoodCategory.MEALS,
      description = "Juicy golden fried chicken patty, crunchy lettuce, cheddar slice & spicy canteen mayo.",
      imageRes = R.drawable.img_food_burger,
      badge = "Bestseller",
      isVegetarian = false,
      prepTimeMinutes = 8
    ),
    FoodItem(
      id = "m2",
      name = "Teriyaki Rice Bowl",
      price = 5.99,
      category = FoodCategory.MEALS,
      description = "Steamed jasmine rice topped with tender glazed teriyaki chicken, broccoli & sesame.",
      imageRes = R.drawable.img_food_rice_bowl,
      badge = "Chef's Special",
      isVegetarian = false,
      prepTimeMinutes = 10
    ),
    FoodItem(
      id = "m3",
      name = "Margherita Pizza Slice",
      price = 3.99,
      category = FoodCategory.MEALS,
      description = "Freshly baked thin-crust pizza slice loaded with rich marinara, mozzarella cheese & sweet basil.",
      imageRes = R.drawable.img_food_pizza,
      badge = "Popular",
      isVegetarian = true,
      prepTimeMinutes = 7
    ),
    FoodItem(
      id = "m4",
      name = "Creamy Alfredo Pasta",
      price = 5.49,
      category = FoodCategory.MEALS,
      description = "Penne pasta tossed in rich garlic parmesan cream sauce, cracked black pepper & fresh parsley.",
      imageRes = R.drawable.img_food_pasta,
      badge = "Hot Meal",
      isVegetarian = true,
      prepTimeMinutes = 9
    ),

    // Snacks
    FoodItem(
      id = "s1",
      name = "Grilled Club Sandwich",
      price = 3.49,
      category = FoodCategory.SNACKS,
      description = "Triple-layered toasted bread with melted mozzarella, sliced tomatoes, cucumber & herb butter.",
      imageRes = R.drawable.img_food_sandwich,
      badge = "Campus Fav",
      isVegetarian = true,
      prepTimeMinutes = 6
    ),
    FoodItem(
      id = "s2",
      name = "Golden Crispy Fries",
      price = 2.29,
      category = FoodCategory.SNACKS,
      description = "Hot salted french fries served crisp with house garlic mayo and tangy tomato dip.",
      imageRes = R.drawable.img_food_fries,
      badge = "Quick Bite",
      isVegetarian = true,
      prepTimeMinutes = 5
    ),
    FoodItem(
      id = "s3",
      name = "Crispy Spring Rolls",
      price = 2.89,
      category = FoodCategory.SNACKS,
      description = "Golden crunchy rolls stuffed with shredded cabbage, carrots, glass noodles & sweet chili dip.",
      imageRes = R.drawable.img_food_fries,
      badge = "Crunchy",
      isVegetarian = true,
      prepTimeMinutes = 5
    ),
    FoodItem(
      id = "s4",
      name = "Toasted Garlic Bread",
      price = 2.49,
      category = FoodCategory.SNACKS,
      description = "Oven-toasted baguette slices with savory roasted garlic butter, parsley & parmesan sprinkle.",
      imageRes = R.drawable.img_food_sandwich,
      badge = "Warm & Crispy",
      isVegetarian = true,
      prepTimeMinutes = 4
    ),

    // Drinks
    FoodItem(
      id = "d1",
      name = "Iced Lemon Mint Tea",
      price = 1.99,
      category = FoodCategory.DRINKS,
      description = "Freshly brewed chilled black tea with fresh lemon slices, mint leaves and crushed ice.",
      imageRes = R.drawable.img_food_iced_tea,
      badge = "Refreshing",
      isVegetarian = true,
      prepTimeMinutes = 3
    ),
    FoodItem(
      id = "d2",
      name = "Cold Brew Iced Coffee",
      price = 2.79,
      category = FoodCategory.DRINKS,
      description = "Slow-steeped smooth Arabica cold brew with rich creamy milk and vanilla syrup.",
      imageRes = R.drawable.img_food_coffee,
      badge = "Energizer",
      isVegetarian = true,
      prepTimeMinutes = 3
    ),
    FoodItem(
      id = "d3",
      name = "Fresh Berry Smoothie",
      price = 3.29,
      category = FoodCategory.DRINKS,
      description = "Blended strawberries, blueberries, greek yogurt and pure honey served frosty cold.",
      imageRes = R.drawable.img_food_smoothie,
      badge = "Healthy",
      isVegetarian = true,
      prepTimeMinutes = 4
    ),
    FoodItem(
      id = "d4",
      name = "Chilled Mango Fizz",
      price = 2.19,
      category = FoodCategory.DRINKS,
      description = "Sparkling chilled Alphonso mango soda topped with mint sprig and a dash of lime juice.",
      imageRes = R.drawable.img_food_iced_tea,
      badge = "Summer Hit",
      isVegetarian = true,
      prepTimeMinutes = 2
    )
  )

  fun getItemsByCategory(category: FoodCategory): List<FoodItem> {
    return if (category == FoodCategory.ALL) {
      menuItems
    } else {
      menuItems.filter { it.category == category }
    }
  }

  fun getItemById(id: String): FoodItem? {
    return menuItems.find { it.id == id }
  }
}
