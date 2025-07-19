package com.example.core_data.mappers

import com.example.core_data.local.entity.CategoryEntity
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

    fun fromDtoToEntity(dto: CategoryDto) = CategoryEntity(
        id = dto.id,
        name = dto.name,
        icon = dto.emoji,
        isIncome = dto.isIncome
    )

    fun fromEntityToCategory(entity: CategoryEntity) = Category(
        id = entity.id,
        name = entity.name,
        icon = entity.icon,
        isIncome = entity.isIncome
    )
}