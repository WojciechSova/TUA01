package pl.lodz.p.it.ssbd2021.ssbd02.dto.mop;

import lombok.*;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.AbstractDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.CabinType;

import java.sql.Timestamp;

/**
 * Klasa DTO zawierająca kajuty.
 *
 * @author Artur Madaj
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class CabinDTO extends AbstractDTO {

    private Integer capacity;

    private CabinType cabinType;

    private String number;

    private Timestamp modificationDate;

    private Account modifiedBy;

    private Timestamp creationDate;

    private Account createdBy;
}
