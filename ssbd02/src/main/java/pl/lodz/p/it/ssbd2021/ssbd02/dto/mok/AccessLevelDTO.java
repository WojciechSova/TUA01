package pl.lodz.p.it.ssbd2021.ssbd02.dto.mok;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * Klasa DTO zawierająca poziomy konta użytkownika.
 * Używana przy wyświetlaniu listy kont oraz szczegółów konta użytkownika.
 *
 * @author Karolina Kowalczyk
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessLevelDTO {

    private String level;

    private Boolean active;

    private Timestamp modificationDate;

    private AccountGeneralDTO modifiedBy;

    private Timestamp creationDate;
}
