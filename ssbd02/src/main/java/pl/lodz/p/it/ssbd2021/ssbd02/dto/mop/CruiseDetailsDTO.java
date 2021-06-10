package pl.lodz.p.it.ssbd2021.ssbd02.dto.mop;

import lombok.*;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.AbstractDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mok.AccountGeneralDTO;

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

    private Timestamp startDate;

    private Timestamp endDate;

    private RouteGeneralDTO route;

    private FerryGeneralDTO ferry;

    private String number;

    private Timestamp modificationDate;

    private AccountGeneralDTO modifiedBy;

    private Timestamp creationDate;

    private AccountGeneralDTO createdBy;
}
