package pl.lodz.p.it.ssbd2021.ssbd02.dto.mok;

import lombok.*;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * Klasa DTO zawierająca dane konta użytkownika.
 * Używana przy wyświetlaniu szczegółów konta użytkownika.
 *
 * @author Karolina Kowalczyk
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDetailsDTO {

    private String login;

    private Boolean active;

    private Boolean confirmed;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private List<AccessLevelDTO> accessLevel;

    private String language;

    private String timeZone;

    private Timestamp modificationDate;

    private Account modifiedBy;

    private Timestamp creationDate;

    private Timestamp lastKnownGoodLogin;

    private String lastKnownGoodLoginIp;

    private Timestamp lastKnownBadLogin;

    private String lastKnownBadLoginIp;

    private int numberOfBadLogins;
}
