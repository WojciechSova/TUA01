package pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.mop;

import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mop.RouteExceptions;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.PersistenceException;

/**
 * Klasa interceptora przechwytująca wyjątki związane z naruszeniem ograniczeń unikalności w tabeli Route.
 *
 * @author Wojciech Sowa
 */
public class RouteInterceptor {

    /**
     * Metoda przechwytująca wywołanie metod, w klasie gdzie użyty został ten interceptor.
     *
     * @param ictx Kontekst wywołania.
     * @return Wynik wywołania metody, którą przechwycił interceptor.
     * @throws Exception Wyjątek złapany w metodzie przechwyconej lub {@link RouteExceptions}
     */
    @AroundInvoke
    public Object intercept(InvocationContext ictx) throws Exception {
        try {
            return ictx.proceed();
        } catch (PersistenceException pe) {

            if (pe.getCause().getCause().toString().contains("start_destination_unique")) {
                throw RouteExceptions.createConflictException(RouteExceptions.ERROR_ROUTE_START_DESTINATION_UNIQUE);
            }

            if (pe.getCause().getCause().toString().contains("route_code_unique")) {
                throw RouteExceptions.createConflictException(RouteExceptions.ERROR_ROUTE_CODE_UNIQUE);
            }

            throw pe;
        }
    }
}
