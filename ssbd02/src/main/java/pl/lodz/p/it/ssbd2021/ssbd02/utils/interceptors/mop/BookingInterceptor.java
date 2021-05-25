package pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.mop;

import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mop.BookingExceptions;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.PersistenceException;

/**
 * Klasa interceptora przechwytująca wyjątki związane z naruszeniem ograniczeń unikalności w tabeli Booking.
 *
 * @author Wojciech Sowa
 */
public class BookingInterceptor {

    /**
     * Metoda przechwytująca wywołanie metod, w klasie gdzie użyty został ten interceptor.
     *
     * @param ictx Kontekst wywołania.
     * @return Wynik wywołania metody, którą przechwycił interceptor.
     * @throws Exception Wyjątek złapany w metodzie przechwyconej lub {@link BookingExceptions}
     */
    @AroundInvoke
    public Object intercept(InvocationContext ictx) throws Exception {
        try {
            return ictx.proceed();
        } catch (PersistenceException pe) {

            if (pe.getCause().getCause().toString().contains("booking_number_unique")) {
                throw BookingExceptions.createConflictException(BookingExceptions.ERROR_BOOKING_NUMBER_UNIQUE);
            }

            if (pe.getCause().getCause().toString().contains("number_of_people_greater_than_zero")) {
                throw BookingExceptions.createNotAcceptableException(BookingExceptions.ERROR_BOOKING_NUMBER_OF_PEOPLE_GREATER_THAN_ZERO);
            }

            if (pe.getCause().getCause().toString().contains("price_greater_than_zero")) {
                throw BookingExceptions.createNotAcceptableException(BookingExceptions.ERROR_BOOKING_PRICE_GREATER_THAN_ZERO);
            }

            throw pe;
        }
    }
}
