package pl.lodz.p.it.ssbd2021.ssbd02.dto.mop;

import lombok.*;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.AbstractDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mok.AccountGeneralDTO;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.sql.Timestamp;

/**
 * Klasa DTO zawierająca kajuty.
 * Używana do wyświetlania szczegółów danej kajuty
 *
 * @author Artur Madaj
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class CabinDetailsDTO extends AbstractDTO {

    @Positive(message = "Cabin capacity must be positive")
    private Integer capacity;

    private String cabinType;

    @Pattern(regexp = "[A-Z][0-9]{3}", message = "Cabin number must be a capital letter and 3 digits")
    private String number;

    private Timestamp modificationDate;

    private AccountGeneralDTO modifiedBy;

    private Timestamp creationDate;

    private AccountGeneralDTO createdBy;
}
