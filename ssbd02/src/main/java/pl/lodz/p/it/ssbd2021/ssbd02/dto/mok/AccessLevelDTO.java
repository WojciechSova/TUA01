package pl.lodz.p.it.ssbd2021.ssbd02.dto.mok;

import lombok.*;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;
import java.time.Instant;

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

    private Account modifiedBy;

    private Timestamp creationDate;
}
