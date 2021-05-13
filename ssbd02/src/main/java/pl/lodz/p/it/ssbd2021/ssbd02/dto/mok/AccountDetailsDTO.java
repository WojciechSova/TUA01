package pl.lodz.p.it.ssbd2021.ssbd02.dto.mok;

import lombok.*;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.AbstractDTO;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * Klasa DTO zawierająca dane konta użytkownika.
 * Używana przy wyświetlaniu szczegółów konta użytkownika.
 *
 * @author Karolina Kowalczyk
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class AccountDetailsDTO extends AbstractDTO {

    @NotBlank
    @Size(max = 30, message = "First name must have less than 30 characters")
    private String login;

    @Size(min = 8, message = "Password must not have less than 8 characters")
    private String password;

    private Boolean active;

    private Boolean confirmed;

    @Size(max = 30, message = "First name must have less than 30 characters")
    private String firstName;

    @Size(max = 50, message = "Last name must have less than 50 characters")
    private String lastName;

    @Email
    @Size(max = 70, message = "Email must have less than 70 characters")
    private String email;

    @Size(min = 11, max = 11, message = "Phone number must have 11 characters")
    private String phoneNumber;

    private List<AccessLevelDTO> accessLevel;

    @Size(max = 5)
    private String language;

    @Size(max = 6, min = 6)
    private String timeZone;

    private Timestamp modificationDate;

    private AccountGeneralDTO modifiedBy;

    private Timestamp creationDate;

    private Timestamp lastKnownGoodLogin;

    @Size(max = 39)
    private String lastKnownGoodLoginIp;

    private Timestamp lastKnownBadLogin;

    @Size(max = 39)
    private String lastKnownBadLoginIp;

    @Min(value = 0)
    @Max(value = 3)
    private int numberOfBadLogins;

    @JsonbTransient
    public String getPassword() {
        return password;
    }

    @JsonbProperty
    public void setPassword(String password) {
        this.password = password;
    }
}
