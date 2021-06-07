package pl.lodz.p.it.ssbd2021.ssbd02.dto.mop;

import lombok.*;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.AbstractDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class SeaportGeneralDTO extends AbstractDTO {

    @NotBlank
    @Size(max = 30, message = "City can have a maximum of 30 characters")
    private String city;

    @NotBlank
    @Pattern(regexp = "[A-Z]{3}", message = "Seaport code must be 3 capital letters")
    private String code;
}
