package pl.lodz.p.it.ssbd2021.ssbd02.dto.mok;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import java.sql.Timestamp;
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

    private String password;

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

    private AccountGeneralDTO modifiedBy;

    private Timestamp creationDate;

    private Timestamp lastKnownGoodLogin;

    private String lastKnownGoodLoginIp;

    private Timestamp lastKnownBadLogin;

    private String lastKnownBadLoginIp;

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
