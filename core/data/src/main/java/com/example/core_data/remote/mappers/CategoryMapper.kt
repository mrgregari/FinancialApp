package com.example.core_data.remote.mappers

import com.example.core_data.remote.dto.CategoryDto
import jakarta.inject.Inject
import com.example.core_domain.models.Category

class CategoryMapper @Inject constructor() {

    fun fromDtoToCategory(dto: CategoryDto) = Category(
        id = dto.id,
        name = dto.name,
        icon = dto.emoji,
        isIncome = dto.isIncome
    )
}