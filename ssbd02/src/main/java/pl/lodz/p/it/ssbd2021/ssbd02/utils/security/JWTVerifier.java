package pl.lodz.p.it.ssbd2021.ssbd02.utils.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;

/**
 * Klasa weryfikująca poprawność tokenu JWT.
 *
 * @author Patryk Kolanek
 */
public class JWTVerifier {

    private static final Logger logger = LogManager.getLogger();

    /**
     * Metoda sprawdzająca czy token JWT jest poprawny.
     *
     * @param jwt Token JWT.
     * @return Zwraca prawdę, gdy token jest poprawny, w przeciwnym razie fałsz.
     */
    public static boolean validateJwt(String jwt) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(jwt);
            JWSVerifier jwsVerifier = new MACVerifier(SecurityConstants.SECRET);
            return signedJWT.verify(jwsVerifier);
        } catch (JOSEException | ParseException e) {
            logger.warn(e);
        }

        return false;
    }
}
