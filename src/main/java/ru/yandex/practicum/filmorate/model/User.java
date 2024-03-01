package ru.yandex.practicum.filmorate.model;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
public class User {
    @NonNull
    private int id;
    @Email
    private String email;
    @NotBlank
    @Pattern(regexp = "\\S+")
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
}
