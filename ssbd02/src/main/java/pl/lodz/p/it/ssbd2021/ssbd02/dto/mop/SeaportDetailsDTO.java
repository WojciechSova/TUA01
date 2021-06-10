package pl.lodz.p.it.ssbd2021.ssbd02.dto.mop;

import lombok.*;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.AbstractDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mok.AccountGeneralDTO;

import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

/**
 * Klasa DTO zawierająca porty.
 *
 * @author Artur Madaj
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class SeaportDetailsDTO extends AbstractDTO {

    @Size(max = 30, message = "City can have a maximum of 30 characters")
    private String city;

    @Pattern(regexp = "[A-Z]{3}", message = "Seaport code must be 3 capital letters")
    private String code;

    @PastOrPresent
    private Timestamp modificationDate;

    private AccountGeneralDTO modifiedBy;

    @PastOrPresent
    private Timestamp creationDate;

    private AccountGeneralDTO createdBy;
}
