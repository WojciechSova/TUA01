package pl.lodz.p.it.ssbd2021.ssbd02.dto.mop;

import lombok.*;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.AbstractDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mok.AccountGeneralDTO;

import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * Klasa DTO zawierająca promy.
 * Używana przy wyświetlaniu szczegółów danego promu.
 *
 * @author Artur Madaj
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class FerryDetailsDTO extends AbstractDTO {

    @NotBlank
    @Size(max = 30, message = "Name can have a maximum of 30 characters")
    private String name;

    private List<CabinGeneralDTO> cabins;

    @PositiveOrZero(message = "Ferry vehicle capacity must be positive or zero")
    private Integer vehicleCapacity;

    @Positive(message = "Ferry on deck capacity must be positive")
    private Integer onDeckCapacity;

    private Timestamp modificationDate;

    private AccountGeneralDTO modifiedBy;

    private Timestamp creationDate;

    private AccountGeneralDTO createdBy;
}
