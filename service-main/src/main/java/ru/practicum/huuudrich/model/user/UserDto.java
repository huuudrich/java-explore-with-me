package ru.practicum.huuudrich.model.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    Long id;

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, max = 250, message = "Name should be between 2 and 250 characters")
    String name;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    @Size(min = 6, max = 254, message = "Email should be between 2 and 254 characters")
    String email;
}
