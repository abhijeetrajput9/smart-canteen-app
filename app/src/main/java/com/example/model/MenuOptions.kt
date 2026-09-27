package com.example.model

enum class FoodViewMode(val label: String) {
  CARDS("Card View"),
  LIST("List View")
}

enum class MenuSortOption(val displayName: String) {
  DEFAULT("Featured"),
  PRICE_LOW_TO_HIGH("Price: Low to High"),
  PRICE_HIGH_TO_LOW("Price: High to Low"),
  NAME_A_TO_Z("Name: A to Z")
}
