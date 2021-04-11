package pl.lodz.p.it.ssbd2021.ssbd02.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;

/**
 * Klasa weryfikująca poprawność tokenu JWT.
 *
 * @author Patryk Kolanek
 */
public class JWTVerifier {

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
            e.printStackTrace();
        }

        return false;
    }
}
