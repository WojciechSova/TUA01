package pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.mok;

import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.OneTimeUrlExceptions;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.PersistenceException;

/**
 * Klasa interceptora przechwytująca wyjątki związane z naruszeniem ograniczeń unikalności w tabeli One_time_url.
 *
 * @author Kacper Świercz
 */
public class OneTimeUrlInterceptor {

    /**
     * Metoda przechwytująca wywołanie metod, w klasie gdzie użyty został ten interceptor.
     *
     * @param ictx Kontekst wywołania.
     * @return Wynik wywołania metody, którą przechwycił interceptor.
     * @throws Exception Wyjątek złapany w metodzie przechwyconej lub {@link OneTimeUrlExceptions}
     */
    @AroundInvoke
    public Object intercept(InvocationContext ictx) throws Exception {
        try {
            return ictx.proceed();
        } catch (PersistenceException pe) {

            if (pe.getCause().getCause().toString().contains("one_time_url_url_unique")) {
                throw OneTimeUrlExceptions.createExceptionConflict(OneTimeUrlExceptions.ERROR_ONE_TIME_URL_URL_UNIQUE);
            }

            if (pe.getCause().getCause().toString().contains("one_time_url_account_action_type_unique")) {
                throw OneTimeUrlExceptions.createExceptionConflict(OneTimeUrlExceptions.ERROR_ONE_TIME_URL_ACCOUNT_ACTION_TYPE_UNIQUE);
            }

            if (pe.getCause().getCause().toString().contains("new_email_unique")) {
                throw OneTimeUrlExceptions.createExceptionConflict(OneTimeUrlExceptions.ERROR_NEW_EMAIL_UNIQUE);
            }

            throw pe;
        }
    }
}
