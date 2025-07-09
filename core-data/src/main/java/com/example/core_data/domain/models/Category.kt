package com.example.core_data.domain.models

data class Category(
    val id: Int = UNDEFINED_ID,
    val name: String,
    val icon: String,
    val isIncome: Boolean
) {

    fun doesMatchSearchQuery(query: String) : Boolean {
        val matchingStringsCombinations = listOf(
            name,
            "${name.first()}"
        )
        return matchingStringsCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }

    companion object {
        const val UNDEFINED_ID = 0
    }
}