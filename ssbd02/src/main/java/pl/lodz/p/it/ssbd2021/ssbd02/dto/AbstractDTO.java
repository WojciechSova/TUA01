package pl.lodz.p.it.ssbd2021.ssbd02.dto;

import lombok.*;

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
public abstract class AbstractDTO {

    private Long version;
}
