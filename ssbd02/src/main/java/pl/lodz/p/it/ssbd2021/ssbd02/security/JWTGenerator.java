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

public class JWTGenerator {

    public static String generateJWT(CredentialValidationResult credentialValidationResult) {
        try {
            JWSSigner jwsSigner = new MACSigner(SecretConstants.SECRET);

            JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                    .subject(credentialValidationResult.getCallerPrincipal().getName())
                    .claim(SecretConstants.AUTH, String.join(",", credentialValidationResult.getCallerGroups()))
                    .issuer(SecretConstants.ISSUER)
                    .expirationTime(new Date(new Date().getTime() + SecretConstants.EXPIRATION_TIME))
                    .build();

            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), jwtClaimsSet);
            signedJWT.sign(jwsSigner);

            return signedJWT.serialize();
        } catch (JOSEException e) {
            e.printStackTrace();
        }

        return "";
    }
}
