package pl.lodz.p.it.ssbd2021.ssbd02.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Klasa DTO zawierająca dane uwierzytelniające użytkownika.
 * Używana przy odbieraniu danych przez endpoint obsługujący uwierzytelnianie.
 *
 * @author Kacper Świercz
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CredentialsDTO {

    private String login;
    private String password;
}
