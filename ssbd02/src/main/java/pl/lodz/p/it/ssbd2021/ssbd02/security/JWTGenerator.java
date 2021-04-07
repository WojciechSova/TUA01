package pl.lodz.p.it.ssbd2021.ssbd02.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import javax.security.enterprise.identitystore.CredentialValidationResult;
import java.util.Date;

/**
 * Klasa generująca token JWT.
 *
 * @author Patryk Kolanek
 */
public class JWTGenerator {

    /**
     * Metoda generująca token JWT.
     *
     * @param credentialValidationResult Obiekt zawierający poświadczenia dla którego wygenerowany ma zostać token JWT.
     * @return Wygenerowany token JWT.
     */
    public static String generateJWT(CredentialValidationResult credentialValidationResult) {
        try {
            JWSSigner jwsSigner = new MACSigner(SecurityConstants.SECRET);

            JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);

            JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                    .subject(credentialValidationResult.getCallerPrincipal().getName())
                    .claim(SecurityConstants.AUTH, String.join(",", credentialValidationResult.getCallerGroups()))
                    .issuer(SecurityConstants.ISSUER)
                    .expirationTime(new Date(new Date().getTime() + SecurityConstants.EXPIRATION_TIME))
                    .build();

            SignedJWT signedJWT = new SignedJWT(jwsHeader, jwtClaimsSet);
            signedJWT.sign(jwsSigner);

            return signedJWT.serialize();
        } catch (JOSEException e) {
            e.printStackTrace();
        }

        return "";
    }
}
