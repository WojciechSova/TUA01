package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractManager;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.*;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.BookingManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.*;
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
import java.sql.Timestamp;
import java.time.Instant;
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
    private AccountMopFacadeLocal accountMopFacadeLocal;

    @Inject
    private CruiseFacadeLocal cruiseFacadeLocal;

    @Inject
    private CabinFacadeLocal cabinFacadeLocal;

    @Inject
    private VehicleTypeFacadeLocal vehicleTypeFacadeLocal;

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
        return Optional.of(bookingFacadeLocal.findAllByAccount(accountMopFacadeLocal.findByLogin(login))).
                orElseThrow(CommonExceptions::createNoResultException);
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public Booking getBookingByNumber(String number) {
        return Optional.ofNullable(bookingFacadeLocal.findByNumber(number))
                .orElseThrow(CommonExceptions::createNoResultException);
    }

    @Override
    @RolesAllowed({"CLIENT"})
    public Booking getBookingByAccountAndNumber(String login, String number) {
        return Optional.ofNullable(bookingFacadeLocal
                .findByAccountAndNumber(accountMopFacadeLocal.findByLogin(login), number))
                .orElseThrow(CommonExceptions::createNoResultException);
    }

    @Override
    @RolesAllowed({"EMPLOYEE", "CLIENT"})
    public Float getRequiredSpaceByVehicleTypeName(String name) {
        return null;
    }

    @Override
    @RolesAllowed({"CLIENT"})
    public void createBooking(Booking booking, String cruiseNumber, String cabinNumber, String login, String vehicleTypeName) {
        //jeśli kajuta jest wolna
        //jeśli na promie są jeszcze miejsca
        //jeśli rejs się jeszcze nie rozpoczął
        //jeśli liczba osób nie przekracza miejsc w kajucie
        //jeśli jest miejsce na pojazd
        Cruise cruise = Optional.ofNullable(cruiseFacadeLocal.findByNumber(cruiseNumber))
                .orElseThrow(CommonExceptions::createNoResultException);
        Cabin cabin = cabinFacadeLocal.findByFerryAndNumber(cruise.getFerry(), cabinNumber);
        Account account = Optional.ofNullable(accountMopFacadeLocal.findByLogin(login))
                .orElseThrow(CommonExceptions::createNoResultException);
        VehicleType vehicleType = Optional.ofNullable(vehicleTypeFacadeLocal.findByName(vehicleTypeName))
                .orElseThrow(CommonExceptions::createNoResultException);

        booking.setAccount(account);
        booking.setCabin(cabin);
        booking.setCruise(cruise);
        booking.setVehicleType(vehicleType);
        booking.setCreationDate(Timestamp.from(Instant.now()));
    }

    @Override
    @RolesAllowed({"CLIENT"})
    public void removeBooking(Booking booking) {

    }
}
