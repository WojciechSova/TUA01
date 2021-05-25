package pl.lodz.p.it.ssbd2021.ssbd02.utils.security;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Klasa przechowująca stałe używane przez klasy w pakiecie security.
 *
 * @author Patryk Kolanek
 */
public class SecurityConstants {

    private static final Logger logger = LogManager.getLogger();

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";

    public static final SecretKey SECRET = generateKey();
    public static final String AUTH = "auth";
    public static final String ZONEINFO = "zoneinfo";
    public static final String ISSUER = "ssbd02";
    public static final String EXP = "exp";

    public static final String GROUP_SPLIT_CONSTANT = ",";

    /**
     * Metoda generująca klucz prywatny służący podpisywaniu.
     * Klucz generowany jest dla algorytmu HmacSHA512.
     * Długość klucza wynosi 512 bitów.
     *
     * @return Obiekt klucza prywatnego.
     */
    private static SecretKey generateKey() {
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance("HmacSHA512");
        } catch (NoSuchAlgorithmException e) {
            logger.log(Level.WARN, e);
        }
        assert keyGenerator != null;
        SecureRandom secureRandom = new SecureRandom();
        keyGenerator.init(512, secureRandom);
        return keyGenerator.generateKey();
    }
}
