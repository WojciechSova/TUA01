package pl.lodz.p.it.ssbd2021.ssbd02.dto.mop;

import lombok.*;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.AbstractDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mok.AccountGeneralDTO;

import java.sql.Timestamp;

/**
 * Klasa DTO zawierająca rezerwacje.
 * Używana przy wyświetlaniu listy rezerwacji.
 *
 * @author Artur Madaj
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class BookingGeneralDTO extends AbstractDTO {

    private CruiseGeneralDTO cruise;

    private AccountGeneralDTO account;

    private String number;

    private Timestamp creationDate;
}
