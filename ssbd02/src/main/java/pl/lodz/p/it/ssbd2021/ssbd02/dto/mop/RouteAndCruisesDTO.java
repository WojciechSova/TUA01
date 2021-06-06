package pl.lodz.p.it.ssbd2021.ssbd02.dto.mop;

import lombok.*;

import java.util.List;

/**
 * Klasa DTO zawierająca trasę i jej rejsy.
 * Używana przy wyświetlaniu rejsów na danej trasie.
 *
 * @author Wojciech Sowa
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class RouteAndCruisesDTO {

    private RouteDetailsDTO route;

    private List<CruiseGeneralDTO> cruises;

}
