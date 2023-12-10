package ru.solarev.taskmanagementapi.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Авторизация")
public class JwtRequest {
    @Schema(description = "Email пользователя", example = "ivan@mail.ru")
    @NotNull(message = "Email не должен быть пустым")
    private String email;

    @Schema(description = "Пароль пользователя", example = "123456")
    @NotNull(message = "Пароль не должен быть пустым")
    private String password;
}
