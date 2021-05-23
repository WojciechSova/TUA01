package pl.lodz.p.it.ssbd2021.ssbd02.utils.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import javax.security.enterprise.identitystore.CredentialValidationResult;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.Properties;

/**
 * Klasa generująca token JWT.
 *
 * @author Patryk Kolanek
 */
public class JWTGenerator {

    private static final Properties prop = new Properties();
    private static int expirationTime;
    private static String issuer;

    /**
     * Metoda generująca token JWT.
     *
     * @param credentialValidationResult Obiekt zawierający poświadczenia dla którego wygenerowany ma zostać token JWT.
     * @param timezone                   Strefa czasowa dodawana do tokenu JWT
     * @return Wygenerowany token JWT.
     */
    public static String generateJWT(CredentialValidationResult credentialValidationResult, String timezone) {
        try {
            JWSSigner jwsSigner = new MACSigner(SecurityConstants.SECRET);

            JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

            try (InputStream input = JWTGenerator.class.getClassLoader().getResourceAsStream("security.properties")) {
                prop.load(input);
                expirationTime = Integer.parseInt(prop.getProperty("security.token.expiration.time"));
                issuer = prop.getProperty("security.token.issuer");
            } catch (IOException e) {
                expirationTime = 600000;
                issuer = "ssbd02";
                e.printStackTrace();
            }

            JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                    .subject(credentialValidationResult.getCallerPrincipal().getName())
                    .claim(SecurityConstants.AUTH, String.join(SecurityConstants.GROUP_SPLIT_CONSTANT, credentialValidationResult.getCallerGroups()))
                    .issuer(issuer)
                    .expirationTime(new Date(new Date().getTime() + expirationTime))
                    .claim(SecurityConstants.ZONEINFO, timezone)
                    .build();

            SignedJWT signedJWT = new SignedJWT(jwsHeader, jwtClaimsSet);
            signedJWT.sign(jwsSigner);

            return signedJWT.serialize();
        } catch (JOSEException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * Metoda aktualizująca token JWT
     *
     * @param serializedJWT Aktualny token JWT
     * @param accessLevels  Aktualne poziomy dostępu użytkownika
     * @param timezone      Strefa czasowa dodawana do tokenu JWT
     * @return Zaktualizowany token JWT
     */
    public static String updateJWT(String serializedJWT, String accessLevels, String timezone) {
        try {
            JWSSigner jwsSigner = new MACSigner(SecurityConstants.SECRET);
            JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

            SignedJWT previousSignedJWT = SignedJWT.parse(serializedJWT);
            JWTClaimsSet previousJWTClaimsSet = previousSignedJWT.getJWTClaimsSet();

            try (InputStream input = JWTGenerator.class.getClassLoader().getResourceAsStream("security.properties")) {
                prop.load(input);
                expirationTime = Integer.parseInt(prop.getProperty("security.token.expiration.time"));
            } catch (IOException e) {
                expirationTime = 600000;
                e.printStackTrace();
            }

            JWTClaimsSet newJWTClaimsSet = new JWTClaimsSet.Builder()
                    .subject(previousJWTClaimsSet.getSubject())
                    .claim(SecurityConstants.AUTH, accessLevels)
                    .issuer(previousJWTClaimsSet.getIssuer())
                    .expirationTime(new Date(new Date().getTime() + expirationTime))
                    .claim(SecurityConstants.ZONEINFO, timezone)
                    .build();

            SignedJWT signedJWT = new SignedJWT(jwsHeader, newJWTClaimsSet);
            signedJWT.sign(jwsSigner);
            return signedJWT.serialize();
        } catch (JOSEException | ParseException e) {
            e.printStackTrace();
        }

        return "";
    }
}
