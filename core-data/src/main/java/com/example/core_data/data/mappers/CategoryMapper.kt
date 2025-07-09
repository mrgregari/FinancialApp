package com.example.core_data.data.mappers

import com.example.core_data.data.dto.CategoryDto
import com.example.core_data.domain.models.Category
import jakarta.inject.Inject

class CategoryMapper @Inject constructor() {

    fun fromDtoToCategory(dto: CategoryDto) = Category(
        id = dto.id,
        name = dto.name,
        icon = dto.emoji,
        isIncome = dto.isIncome
    )
}