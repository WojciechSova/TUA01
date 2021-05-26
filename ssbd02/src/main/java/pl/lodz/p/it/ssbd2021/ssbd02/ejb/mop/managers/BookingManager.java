package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractManager;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.BookingManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Booking;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.TrackerInterceptor;

import javax.annotation.security.PermitAll;
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
    @RolesAllowed({"EMPLOYEE"})
    public List<Booking> getAllBookings() {
        return null;
    }

    @Override
    @PermitAll
    public List<Booking> getAllCurrentBookings() {
        return null;
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public List<Booking> getAllFinishedBookings() {
        return null;
    }

    @Override
    @RolesAllowed({"EMPLOYEE", "CLIENT"})
    public List<Booking> getAllBookingsByAccount(String login) {
        return null;
    }

    @Override
    @RolesAllowed({"EMPLOYEE", "CLIENT"})
    public Booking getBookingByNumber(String code) {
        return null;
    }

    @Override
    @RolesAllowed({"EMPLOYEE", "CLIENT"})
    public Float getRequiredSpaceByVehicleTypeName(String name) {
        return null;
    }

    @Override
    @RolesAllowed({"CLIENT"})
    public void createBooking(Booking booking) {

    }

    @Override
    @RolesAllowed({"CLIENT"})
    public void removeBooking(Booking booking) {

    }
}
