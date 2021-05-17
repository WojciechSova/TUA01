package pl.lodz.p.it.ssbd2021.ssbd02.dto.mop;

import lombok.*;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.AbstractDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;

import java.sql.Timestamp;

/**
 * Klasa DTO zawierająca porty.
 *
 * @author Artur Madaj
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class SeaportDTO extends AbstractDTO {

    private String city;

    private String code;

    private Timestamp modificationDate;

    private Account modifiedBy;

    private Timestamp creationDate;

    private Account createdBy;
}
