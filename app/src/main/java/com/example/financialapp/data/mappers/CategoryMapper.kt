package com.example.financialapp.data.mappers

import com.example.financialapp.data.dto.CategoryDto
import com.example.financialapp.domain.models.Category
import javax.inject.Inject

class CategoryMapper @Inject constructor() {

    fun fromDtoToCategory(dto: CategoryDto) = Category(
        id = dto.id,
        name = dto.name,
        icon = dto.emoji,
        isIncome = dto.isIncome
    )
}