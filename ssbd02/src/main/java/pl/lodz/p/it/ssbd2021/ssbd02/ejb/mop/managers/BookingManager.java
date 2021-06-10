package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractManager;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades.interfaces.AccountFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.BookingFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.BookingManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Booking;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.TrackerInterceptor;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionSynchronization;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.util.List;
import java.util.Optional;

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

    @Inject
    private BookingFacadeLocal bookingFacadeLocal;
    @Inject
    private AccountFacadeLocal accountFacadeLocal;

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public List<Booking> getAllBookings() {
        return Optional.ofNullable(bookingFacadeLocal.findAll()).orElseThrow(CommonExceptions::createNoResultException);
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
    @RolesAllowed({"CLIENT"})
    public List<Booking> getAllBookingsByAccount(String login) {
        return Optional.of(bookingFacadeLocal.findAllByAccount(accountFacadeLocal.findByLogin(login))).
                orElseThrow(CommonExceptions::createNoResultException);
    }

    @Override
    @RolesAllowed({"EMPLOYEE", "CLIENT"})
    public Booking getBookingByNumber(String number) {
        return Optional.ofNullable(bookingFacadeLocal.findByNumber(number))
                .orElseThrow(CommonExceptions::createNoResultException);
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
