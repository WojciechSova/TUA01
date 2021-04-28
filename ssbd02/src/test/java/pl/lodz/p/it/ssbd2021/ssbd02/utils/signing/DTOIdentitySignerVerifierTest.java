package pl.lodz.p.it.ssbd2021.ssbd02.utils.signing;

import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mok.AccountDetailsDTO;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class DTOIdentitySignerVerifierTest {


    @Test
    void calculateEntitySignature() {
        String algorithm = "\"alg\":\"HS256\"";

        AccountDetailsDTO account = new AccountDetailsDTO();
        account.setVersion(100745463456452700L);
        account.setFirstName("Testowy2");

        String[] separatedETag = DTOIdentitySignerVerifier.calculateDTOSignature(account).split("\\.");
        assertEquals(3, separatedETag.length);
        String alg = new String(Base64.getDecoder().decode(separatedETag[0]));
        String version = new String(Base64.getDecoder().decode(separatedETag[1]));
        assertTrue(alg.contains(algorithm));
        assertTrue(version.contains(account.getVersion().toString()));
    }

    @Test
    void validateEntitySignature() {
        AccountDetailsDTO account = new AccountDetailsDTO();
        account.setVersion(100745463456452700L);
        account.setFirstName("Testowy2");

        String eTag = DTOIdentitySignerVerifier.calculateDTOSignature(account);
        assertTrue(DTOIdentitySignerVerifier.validateDTOSignature(eTag));
        assertFalse(DTOIdentitySignerVerifier.validateDTOSignature("notAnETAg"));
    }

    @Test
    void verifyEntityIntegrity() {
        AccountDetailsDTO account = new AccountDetailsDTO();
        account.setVersion(100745463456452700L);
        account.setFirstName("Testowy2");

        String header = DTOIdentitySignerVerifier.calculateDTOSignature(account);
        assertTrue(DTOIdentitySignerVerifier.verifyDTOIntegrity(header, account));
        account.setVersion(11223454L);
        assertFalse(DTOIdentitySignerVerifier.verifyDTOIntegrity(header, account));
    }
}
