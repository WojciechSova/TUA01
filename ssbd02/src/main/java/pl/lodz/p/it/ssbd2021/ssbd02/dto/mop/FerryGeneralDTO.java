package pl.lodz.p.it.ssbd2021.ssbd02.dto.mop;

import lombok.*;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.AbstractDTO;

/**
 * Klasa DTO zawierająca promy.
 * Używana przy wyświetlaniu listy promów.
 *
 * @author Artur Madaj
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class FerryGeneralDTO extends AbstractDTO {

    private String name;

    private Integer vehicleCapacity;

    private Integer onDeckCapacity;
}
