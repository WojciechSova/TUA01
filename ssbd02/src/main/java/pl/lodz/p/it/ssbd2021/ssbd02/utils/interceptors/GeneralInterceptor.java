package pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors;

import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.GeneralException;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

/**
 * Klasa interceptora przechwytująca wyjątki nieprzewidziane w aplikacji.
 *
 * @author Kacper Świercz
 */
public class GeneralInterceptor {

    /**
     * Metoda przechwytująca wywołanie metod, w klasie gdzie użyty został ten interceptor.
     *
     * @param ictx Kontekst wywołania.
     * @return Wynik wywołania metody, którą przechwycił interceptor.
     */
    @AroundInvoke
    public Object intercept(InvocationContext ictx) {
        try {
            return ictx.proceed();
        } catch (GeneralException generalException) {
            throw generalException;
        } catch (Exception e) {
            throw CommonExceptions.createUnknownException();
        }
    }
}
