package pl.lodz.p.it.ssbd2021.ssbd02.security;

import pl.lodz.p.it.ssbd2021.ssbd02.managers.auth.AuthManagerLocal;

import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Klasa implementująca magazyn tożsamości.
 *
 * @author Patryk Kolanek
 */
public class DatabaseIdentityStore implements IdentityStore {

    @Inject
    private AuthManagerLocal authManager;

    /**
     * Metoda walidująca poprawność przekazanych danych logowania.
     *
     * @param credential Dane logowania.
     * @return W przypadku podania nieprawdiłowego typu poświadczeń zwraca INVALID_RESULT. W przypadku podania nieprawidłowych danych logowania
     * zwraca INVALID_RESULT. W przypadku podania prawidłowych danych logowania zwraca obiekt typu {@link CredentialValidationResult}.
     */
    @Override
    public CredentialValidationResult validate(Credential credential) {
        if (credential instanceof UsernamePasswordCredential) {
            UsernamePasswordCredential usernamePasswordCredential = (UsernamePasswordCredential) credential;

            List<String> accessGroups = authManager.getAccessLevels(usernamePasswordCredential.getCaller(), usernamePasswordCredential.getPasswordAsString());

            if (!accessGroups.isEmpty()) {
                return new CredentialValidationResult(usernamePasswordCredential.getCaller(), new HashSet<>(accessGroups));
            }

            return CredentialValidationResult.INVALID_RESULT;
        }

        return CredentialValidationResult.NOT_VALIDATED_RESULT;
    }

    /**
     * Metoda zwracająca zestaw grup do których należy użytkownik.
     *
     * @param validationResult Rezultat walidacji danych logowania.
     * @return Zestaw grup do których należy użytkownik.
     */
    @Override
    public Set<String> getCallerGroups(CredentialValidationResult validationResult) {
        return validationResult.getCallerGroups();
    }
}
