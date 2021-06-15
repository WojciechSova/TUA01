package pl.lodz.p.it.ssbd2021.ssbd02.dto.mop;

import lombok.*;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.AbstractDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mok.AccountGeneralDTO;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;

/**
 * Klasa DTO zawierająca rejsy.
 * Używana przy wyświetlaniu szczegółowych danych rejsu.
 *
 * @author Artur Madaj
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class CruiseDetailsDTO extends AbstractDTO {

    @NotBlank
    @Future
    private Timestamp startDate;

    @NotBlank
    @Future
    private Timestamp endDate;

    private RouteGeneralDTO route;

    private FerryGeneralDTO ferry;

    @NotBlank
    @Pattern(regexp = "[A-Z]{6}[0-9]{6}", message = "Cruise number must have 6 capital letters and 6 digits")
    private String number;

    @PastOrPresent
    private Timestamp modificationDate;

    private AccountGeneralDTO modifiedBy;

    @PastOrPresent
    private Timestamp creationDate;

    private AccountGeneralDTO createdBy;
}
