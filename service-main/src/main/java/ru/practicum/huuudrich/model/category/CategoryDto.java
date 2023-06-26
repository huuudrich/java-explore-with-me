package ru.practicum.huuudrich.model.category;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryDto {
    Long id;

    @NotBlank(message = "Category name cannot be blank")
    @Size(min = 1, max = 50, message = "Category name should be between 1 and 50 characters")
    String name;
}
