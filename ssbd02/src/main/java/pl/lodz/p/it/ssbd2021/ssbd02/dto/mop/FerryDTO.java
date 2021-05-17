package pl.lodz.p.it.ssbd2021.ssbd02.dto.mop;

import lombok.*;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.AbstractDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;

import java.sql.Timestamp;
import java.util.List;

/**
 * Klasa DTO zawierająca promy.
 *
 * @author Artur Madaj
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class FerryDTO extends AbstractDTO {

    private String name;

    private List<CabinDTO> cabins;

    private Integer vehicleCapacity;

    private Integer onDeckCapacity;

    private Timestamp modificationDate;

    private Account modifiedBy;

    private Timestamp creationDate;

    private Account createdBy;
}
