package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractManager;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.*;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.BookingManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.*;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mop.CabinExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mop.CruiseExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mop.FerryExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.TrackerInterceptor;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionSynchronization;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        double price = 0;
        Cruise cruise = cruiseFacadeLocal.findByNumber(cruiseNumber);
        Cabin cabin = null;
        if (cabinNumber != null) {
            cabin = cabinFacadeLocal.findByFerryAndNumber(cruise.getFerry(), cabinNumber);
        }
        Account account = accountMopFacadeLocal.findByLogin(login);
        VehicleType vehicleType = vehicleTypeFacadeLocal.findByName(vehicleTypeName);

        if (cruise.getStartDate().compareTo(Timestamp.from(Instant.now())) <= 0) {
            throw CruiseExceptions.createConflictException("Cruise has already started");
        }
        double sumOfVehiclesSpace = bookingFacadeLocal.getSumVehicleSpaceByCruise(cruise);
        if (cruise.getFerry().getVehicleCapacity() < sumOfVehiclesSpace + vehicleType.getRequiredSpace()) {
            throw FerryExceptions.createConflictException("There is not enough for a vehicle space on the ferry");
        }
        if (cabin != null) {
            if (booking.getNumberOfPeople() <= cabin.getCapacity()) {
                throw CabinExceptions.createConflictException("Number of people is greater than cabin's capacity");
            }
            if (cabin.getCabinType().getCabinTypeName().equals("Disabled class")) {
                price += 120 * cabin.getCapacity();
            }
            if (cabin.getCabinType().getCabinTypeName().equals("First class")) {
                price += 300 * cabin.getCapacity();
            }
            if (cabin.getCabinType().getCabinTypeName().equals("Second class")) {
                price += 200 * cabin.getCapacity();
            }
            if (cabin.getCabinType().getCabinTypeName().equals("Third class")) {
                price += 100 * cabin.getCapacity();
            }
            //sprawdzenie czy jest wolna
        } else {
            int sumOfPeople = bookingFacadeLocal.getSumNumberOfPeopleByCruise(cruise);
            if (cruise.getFerry().getOnDeckCapacity() < sumOfPeople + booking.getNumberOfPeople()) {
                throw FerryExceptions.createConflictException("There is not enough space on the ferry's deck");
            }
            price += 30 * booking.getNumberOfPeople();
        }

        price += vehicleType.getRequiredSpace() * 200;
        String number = "";
////////////////////
        booking.setPrice(price);
        booking.setAccount(account);
        booking.setCabin(cabin);
        booking.setCruise(cruise);
        booking.setVehicleType(vehicleType);
        booking.setCreationDate(Timestamp.from(Instant.now()));

        bookingFacadeLocal.create(booking);
    }

    @Override
    @RolesAllowed({"CLIENT"})
    public void removeBooking(Booking booking) {

    }
}
