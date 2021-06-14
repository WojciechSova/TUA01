package pl.lodz.p.it.ssbd2021.ssbd02.dto.mop;

import lombok.*;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.AbstractDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

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

    @NotBlank
    @Size(max = 30, message = "Name can have a maximum of 30 characters")
    private String name;

    @PositiveOrZero(message = "Ferry vehicle capacity must be positive or zero")
    private Integer vehicleCapacity;

    @Positive(message = "Ferry on deck capacity must be positive")
    private Integer onDeckCapacity;
}
