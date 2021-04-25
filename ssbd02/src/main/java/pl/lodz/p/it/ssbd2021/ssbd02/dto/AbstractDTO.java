package pl.lodz.p.it.ssbd2021.ssbd02.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Abstrakcyjna klasa DTO zawierająca wersję.
 *
 * @author Patryk Kolanek
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractDTO {

    private Long version;
}
