package pl.lodz.p.it.ssbd2021.ssbd02.dto.mop;

import lombok.*;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.AbstractDTO;

import java.sql.Timestamp;

/**
 * Klasa DTO zawierająca rejsy.
 * Używana przy wyświetlania listy rejsów.
 *
 * @author Artur Madaj
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class CruiseGeneralDTO extends AbstractDTO {

    private Timestamp startDate;

    private Timestamp endDate;

    private RouteGeneralDTO route;

    private FerryGeneralDTO ferry;

    private String number;

    private Double popularity;

}
