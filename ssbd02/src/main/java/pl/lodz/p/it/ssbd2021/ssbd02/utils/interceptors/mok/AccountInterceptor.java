package pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.mok;

import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.AccountExceptions;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.PersistenceException;

/**
 * Klasa interceptora przechwytująca wyjątki związane z naruszeniem ograniczeń unikalności w tabeli Account oraz Personal_data.
 *
 * @author Kacper Świercz
 */
public class AccountInterceptor {

    /**
     * Metoda przechwytująca wywołanie metod, w klasie gdzie użyty został ten interceptor.
     *
     * @param ictx Kontekst wywołania.
     * @return Wynik wywołania metody, którą przechwycił interceptor.
     * @throws Exception Wyjątek złapany w metodzie przechwyconej lub AccountExceptions
     */
    @AroundInvoke
    public Object intercept(InvocationContext ictx) throws Exception {
        try {
            return ictx.proceed();
        } catch (PersistenceException pe) {

            if (pe.getCause().getCause().toString().contains("login_unique")) {
                throw AccountExceptions.createExceptionConflict(AccountExceptions.ERROR_LOGIN_UNIQUE);
            }

            if (pe.getCause().getCause().toString().contains("email_unique")) {
                throw AccountExceptions.createExceptionConflict(AccountExceptions.ERROR_EMAIL_UNIQUE);
            }

            if (pe.getCause().getCause().toString().contains("phone_number_unique")) {
                throw AccountExceptions.createExceptionConflict(AccountExceptions.ERROR_PHONE_NUMBER_UNIQUE);
            }

            throw pe;
        }
    }
}
