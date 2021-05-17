package pl.lodz.p.it.ssbd2021.ssbd02.dto.mop;

import lombok.*;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.AbstractDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Ferry;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Route;

import java.sql.Timestamp;

/**
 * Klasa DTO zawierająca rejsy.
 *
 * @author Artur Madaj
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class CruiseDTO extends AbstractDTO {

    private Timestamp startDate;

    private Timestamp endDate;

    private Route route;

    private Ferry ferry;

    private String number;

    private Timestamp modificationDate;

    private Account modifiedBy;

    private Timestamp creationDate;

    private Account createdBy;
}
