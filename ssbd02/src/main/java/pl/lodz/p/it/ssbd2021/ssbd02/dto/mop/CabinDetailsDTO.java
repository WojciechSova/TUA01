package pl.lodz.p.it.ssbd2021.ssbd02.dto.mop;

import lombok.*;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.AbstractDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mok.AccountGeneralDTO;

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

    private Integer capacity;

    private String cabinType;

    private String number;

    private Timestamp modificationDate;

    private AccountGeneralDTO modifiedBy;

    private Timestamp creationDate;

    private AccountGeneralDTO createdBy;
}
