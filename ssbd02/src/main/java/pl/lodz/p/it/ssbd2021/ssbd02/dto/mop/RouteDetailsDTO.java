package pl.lodz.p.it.ssbd2021.ssbd02.dto.mop;

import lombok.*;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.AbstractDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mok.AccountGeneralDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;
import java.util.List;

/**
 * Klasa DTO zawierająca trasy.
 * Używana przy wyświetlaniu szczegółowych danych trasy.
 *
 * @author Artur Madaj
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class RouteDetailsDTO extends AbstractDTO {

    private SeaportGeneralDTO start;

    private SeaportGeneralDTO destination;

    private List<CruiseGeneralDTO> cruises;

    @NotBlank
    @Pattern(regexp = "[A-Z]{6}", message = "Route code must have 6 capital letters")
    private String code;

    private Timestamp creationDate;

    private AccountGeneralDTO createdBy;
}
