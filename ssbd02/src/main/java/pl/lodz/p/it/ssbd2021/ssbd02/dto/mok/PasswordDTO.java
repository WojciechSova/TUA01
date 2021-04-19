package pl.lodz.p.it.ssbd2021.ssbd02.dto.mok;


import lombok.*;

/**
 * Klasa DTO zawierająca stare i nowe hasło użytkownika.
 * Używana przy ustawianiu hasła dla konta.
 *
 * @author Karolina Kowalczyk
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordDTO {

    private String newPassword;

    private String oldPassword;
}
