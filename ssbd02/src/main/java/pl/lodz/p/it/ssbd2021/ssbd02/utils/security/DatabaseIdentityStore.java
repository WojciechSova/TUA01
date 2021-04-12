package pl.lodz.p.it.ssbd2021.ssbd02.utils.security;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.auth.managers.interfaces.AuthManagerLocal;

import javax.enterprise.context.RequestScoped;
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
@RequestScoped
public class DatabaseIdentityStore implements IdentityStore {

    @Inject
    private AuthManagerLocal authManagerLocal;

    /**
     * Metoda walidująca poprawność przekazanych danych logowania.
     *
     * @param credential Dane logowania.
     * @return W przypadku podania nieprawdiłowego typu poświadczeń zwraca INVALID_RESULT. W przypadku podania nieprawidłowych danych logowania
     * zwraca INVALID_RESULT. W przypadku podania prawidłowych danych logowania zwraca obiekt typu {@link CredentialValidationResult}.
     */
    @Override
    public CredentialValidationResult validate(Credential credential) {
        if (!(credential instanceof UsernamePasswordCredential)) {
            return CredentialValidationResult.NOT_VALIDATED_RESULT;
        }

        UsernamePasswordCredential usernamePasswordCredential = (UsernamePasswordCredential) credential;

        List<String> accessGroups = authManagerLocal.getAccessLevels(usernamePasswordCredential.getCaller(), usernamePasswordCredential.getPasswordAsString());

        if (accessGroups.isEmpty()) {
            return CredentialValidationResult.INVALID_RESULT;
        }

        return new CredentialValidationResult(usernamePasswordCredential.getCaller(), new HashSet<>(accessGroups));
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
