package pl.lodz.p.it.ssbd2021.ssbd02.dto.mop;

import lombok.*;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.AbstractDTO;

/**
 * Klasa DTO zawierająca trasy.
 * Używana przy wyświetlania listy tras.
 *
 * @author Daniel Łondka
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class RouteGeneralDTO extends AbstractDTO {

    private SeaportGeneralDTO start;

    private SeaportGeneralDTO destination;

    private String code;
}
