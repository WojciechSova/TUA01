package pl.lodz.p.it.ssbd2021.ssbd02.utils.signing;

import javax.ws.rs.NameBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Adnotacja zapewniająca sprawdzenie obecności i poprawności przekazanego w żądaniu podpisu.
 *
 * @author Karolina Kowalczyk
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface DTOSignatureValidatorFilterBinding {
}
