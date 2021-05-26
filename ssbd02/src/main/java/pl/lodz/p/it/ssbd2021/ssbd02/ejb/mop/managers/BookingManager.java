package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractManager;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.BookingManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Booking;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.TrackerInterceptor;

import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionSynchronization;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import java.util.List;

/**
 * Manager rezerwacji
 *
 * @author Wojciech Sowa
 */
@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@RolesAllowed({"DEFINITELY_NOT_A_REAL_ROLE"})
@Interceptors(TrackerInterceptor.class)
public class BookingManager extends AbstractManager implements BookingManagerLocal, SessionSynchronization {

    @Override
    public List<Booking> getAllBookings() {
        return null;
    }

    @Override
    public List<Booking> getAllCurrentBookings() {
        return null;
    }

    @Override
    public List<Booking> getAllFinishedBookings() {
        return null;
    }

    @Override
    public List<Booking> getAllBookingsByAccount(String login) {
        return null;
    }

    @Override
    public Booking getBookingByNumber(String code) {
        return null;
    }

    @Override
    public Float getRequiredSpaceByVehicleTypeName(String name) {
        return null;
    }

    @Override
    public void createBooking(Booking booking) {

    }

    @Override
    public void removeBooking(Booking booking) {

    }
}
