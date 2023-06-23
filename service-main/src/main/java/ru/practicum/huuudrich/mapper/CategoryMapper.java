package ru.practicum.huuudrich.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.huuudrich.model.category.Category;
import ru.practicum.huuudrich.model.category.CategoryDto;

import java.util.List;

@Mapper
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    Category toEntity(CategoryDto dto);

    List<CategoryDto> toListCategoryDto(List<Category> categories);

    CategoryDto toDto(Category category);
}
