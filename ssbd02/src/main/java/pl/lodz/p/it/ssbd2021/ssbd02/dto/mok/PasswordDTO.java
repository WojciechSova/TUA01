package pl.lodz.p.it.ssbd2021.ssbd02.dto.mok;


import lombok.*;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.AbstractDTO;

/**
 * Klasa DTO zawierająca stare i nowe hasło użytkownika.
 * Używana przy ustawianiu hasła dla konta.
 *
 * @author Karolina Kowalczyk
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PasswordDTO extends AbstractDTO {

    private String newPassword;

    private String oldPassword;
}
