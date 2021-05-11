package pl.lodz.p.it.ssbd2021.ssbd02.dto.mok;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Klasa DTO zawierająca stare i nowe hasło użytkownika.
 * Używana przy ustawianiu hasła dla konta.
 *
 * @author Karolina Kowalczyk
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PasswordDTO {

    @NotBlank
    @Size(min = 8, message = "Password must not have less than 8 characters")
    private String oldPassword;

    @NotBlank
    @Size(min = 8, message = "Password must not have less than 8 characters")
    private String newPassword;
}
