package pl.lodz.p.it.ssbd2021.ssbd02.dto.mop;

import lombok.*;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.AbstractDTO;

/**
 * Klasa DTO zawierająca kajuty
 * Używana przy wyświetlaniu listy kajut dla danego promu.
 *
 * @author Julia Kołodziej
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class CabinGeneralDTO extends AbstractDTO {

    private Integer capacity;

    private String cabinType;

    private String number;
}
