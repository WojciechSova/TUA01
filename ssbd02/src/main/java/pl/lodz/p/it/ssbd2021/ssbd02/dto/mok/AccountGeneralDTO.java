package pl.lodz.p.it.ssbd2021.ssbd02.dto.mok;

import lombok.*;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.AbstractDTO;

import java.util.List;

/**
 * Klasa DTO zawierająca dane konta użytkownika.
 * Używana przy wyświetlaniu listy kont.
 *
 * @author Karolina Kowalczyk
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class AccountGeneralDTO extends AbstractDTO {

    private String login;

    private Boolean active;

    private String firstName;

    private String lastName;

    private List<String> accessLevel;
}
