package pl.lodz.p.it.ssbd2021.ssbd02.dto.mok;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.AbstractDTO;

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
@EqualsAndHashCode(callSuper = true)
public class AccountGeneralDTO extends AbstractDTO {

    private String login;

    private Boolean active;

    private String firstName;

    private String lastName;

    private List<String> accessLevel;
}
