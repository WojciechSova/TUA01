package pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.mok;

import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mok.AccessLevelExceptions;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.PersistenceException;

/**
 * Klasa interceptora przechwytująca wyjątki związane z naruszeniem ograniczeń unikalności w tabeli Access_level.
 *
 * @author Kacper Świercz
 */
public class AccessLevelInterceptor {

    /**
     * Metoda przechwytująca wywołanie metod, w klasie gdzie użyty został ten interceptor.
     *
     * @param ictx Kontekst wywołania.
     * @return Wynik wywołania metody, którą przechwycił interceptor.
     * @throws Exception Wyjątek złapany w metodzie przechwyconej lub {@link AccessLevelExceptions}
     */
    @AroundInvoke
    public Object intercept(InvocationContext ictx) throws Exception {
        try {
            return ictx.proceed();
        } catch (PersistenceException pe) {

            if (pe.getCause().getCause().toString().contains("account_level_unique")) {
                throw AccessLevelExceptions.createConflictException(AccessLevelExceptions.ERROR_ACCOUNT_LEVEL_UNIQUE);
            }

            throw pe;
        }
    }
}
