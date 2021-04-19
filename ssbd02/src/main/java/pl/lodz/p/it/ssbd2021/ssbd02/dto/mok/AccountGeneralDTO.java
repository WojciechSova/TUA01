package pl.lodz.p.it.ssbd2021.ssbd02.dto.mok;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Klasa DTO zawierająca dane konta użytkownika.
 * Używana przy wyświetlaniu listy kont.
 *
 * @author Karolina Kowalczyk
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountGeneralDTO {

    private String login;

    private String firstName;

    private String lastName;

    private List<AccessLevelDTO> accessLevel;
}
