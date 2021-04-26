package pl.lodz.p.it.ssbd2021.ssbd02.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Abstrakcyjna klasa DTO zawierająca wersję.
 *
 * @author Patryk Kolanek
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public abstract class AbstractDTO {

    private Long version;
}
