package com.example.financialapp.data.mappers

import com.example.financialapp.data.models.CategoryDTO
import com.example.financialapp.domain.models.Category
import javax.inject.Inject

class CategoryMapper @Inject constructor() {

    fun fromDto(dto: CategoryDTO) = Category(
        id = dto.id,
        name = dto.name,
        icon = dto.emoji,
        isIncome = dto.isIncome
    )
}