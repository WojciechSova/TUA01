package pl.lodz.p.it.ssbd2021.ssbd02.dto;

import lombok.*;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.signing.SignableDTO;

import javax.json.bind.annotation.JsonbTransient;

/**
 * Abstrakcyjna klasa DTO zawierająca wersję.
 *
 * @author Patryk Kolanek
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public abstract class AbstractDTO implements SignableDTO {

    @Override
    @JsonbTransient
    public Long getSignablePayload() {
        return version;
    }
    private Long version;
}
