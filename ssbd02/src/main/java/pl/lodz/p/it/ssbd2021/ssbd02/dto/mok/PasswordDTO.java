package pl.lodz.p.it.ssbd2021.ssbd02.dto.mok;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private String newPassword;

    private String oldPassword;
}
