package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import org.apache.commons.lang3.RandomStringUtils;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractManager;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.*;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.BookingManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Booking;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cabin;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cruise;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.VehicleType;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mop.BookingExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mop.CabinExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mop.CruiseExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mop.FerryExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.TrackerInterceptor;

import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionSynchronization;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Properties;

/**
 * Manager rezerwacji
 *
 * @author Wojciech Sowa
 */
@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
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

    private static final Properties prop = new Properties();

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public List<Booking> getAllBookings() {
        return bookingFacadeLocal.findAll();
    }

    @Override
    @RolesAllowed({"CLIENT"})
    public List<Booking> getAllBookingsByAccount(String login) {
        return bookingFacadeLocal.findAllByAccount(accountMopFacadeLocal.findByLogin(login));
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public Booking getBookingByNumber(String number) {
        return bookingFacadeLocal.findByNumber(number);
    }

    @Override
    @RolesAllowed({"CLIENT"})
    public Booking getBookingByAccountAndNumber(String login, String number) {
        return bookingFacadeLocal.findByAccountAndNumber(accountMopFacadeLocal.findByLogin(login), number);
    }

    @Override
    @RolesAllowed({"CLIENT"})
    public void createBooking(int numberOfPeople, String cruiseNumber, String cabinNumber, String login, String vehicleTypeName) {
        Booking booking = new Booking();
        booking.setVersion(0L);
        booking.setNumberOfPeople(numberOfPeople);
        double price = 0;
        Cruise cruise = cruiseFacadeLocal.findByNumber(cruiseNumber);
        Cabin cabin = null;

        if (!cabinNumber.equals("")) {
            cabin = cabinFacadeLocal.findByFerryAndNumber(cruise.getFerry(), cabinNumber);
        }

        Account account = accountMopFacadeLocal.findByLogin(login);
        VehicleType vehicleType = vehicleTypeFacadeLocal.findByName(vehicleTypeName);

        if (cruise.getStartDate().compareTo(Timestamp.from(Instant.now())) <= 0) {
            throw CruiseExceptions.createConflictException(CruiseExceptions.ERROR_CRUISE_ALREADY_STARTED);
        }

        double sumOfVehiclesSpace = bookingFacadeLocal.getSumVehicleSpaceByCruise(cruise);

        if (cruise.getFerry().getVehicleCapacity() < sumOfVehiclesSpace + vehicleType.getRequiredSpace()) {
            throw FerryExceptions.createConflictException(CruiseExceptions.ERROR_CRUISE_NOT_ENOUGH_SPACE_FOR_VEHICLE_ON_FERRY);
        }

        price = getPrice(booking, price, cruise, cabin, vehicleType);

        String number = RandomStringUtils.random(10, false, true);

        booking.setNumber(number);
        booking.setPrice(price);
        booking.setAccount(account);
        booking.setCabin(cabin);
        booking.setCruise(cruise);
        booking.setVehicleType(vehicleType);
        booking.setCreationDate(Timestamp.from(Instant.now()));

        while (true) {
            try {
                bookingFacadeLocal.create(booking);
                cruise.setPopularity(calculatePopularity(cruise));
                cruiseFacadeLocal.edit(cruise);
                break;
            } catch (ConstraintViolationException e) {
                number = RandomStringUtils.random(10, false, true);
                booking.setNumber(number);
            }
        }

        logger.info("The user with login {} created the reservation with number {}",
                login, number);
    }

    @RolesAllowed({"CLIENT"})
    private double getPrice(Booking booking, double price, Cruise cruise, Cabin cabin, VehicleType vehicleType) {
        int disabledPrice = 100;
        int thirdPrice = 80;
        int secondPrice = 150;
        int firstPrice = 200;
        int vehiclePrice = 100;
        int onDeckPrice = 30;
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("booking.properties")) {
            prop.load(input);
            disabledPrice = Integer.parseInt(prop.getProperty("price.disabled_class"));
            thirdPrice = Integer.parseInt(prop.getProperty("price.third_class"));
            secondPrice = Integer.parseInt(prop.getProperty("price.second_class"));
            firstPrice = Integer.parseInt(prop.getProperty("price.first_class"));
            onDeckPrice = Integer.parseInt(prop.getProperty("price.on_deck"));
            vehiclePrice = Integer.parseInt(prop.getProperty("price.vehicle"));
        } catch (IOException e) {
            logger.warn(e);
        }

        if (cabin != null) {
            if (booking.getNumberOfPeople() > cabin.getCapacity()) {
                throw CabinExceptions.createConflictException("Number of people is greater than cabin's capacity");
            }
            if (cabinFacadeLocal.findOccupiedCabinsOnCruise(cruise).contains(cabin)) {
                throw CabinExceptions.createConflictException("Cabin is already occupied");
            }
            if (cabin.getCabinType().getCabinTypeName().equals("Disabled class")) {
                price += disabledPrice * cabin.getCapacity();
            }
            if (cabin.getCabinType().getCabinTypeName().equals("First class")) {
                price += firstPrice * cabin.getCapacity();
            }
            if (cabin.getCabinType().getCabinTypeName().equals("Second class")) {
                price += secondPrice * cabin.getCapacity();
            }
            if (cabin.getCabinType().getCabinTypeName().equals("Third class")) {
                price += thirdPrice * cabin.getCapacity();
            }
        } else {
            long sumOfPeople = bookingFacadeLocal.getSumNumberOfPeopleByCruise(cruise);

            if (cruise.getFerry().getOnDeckCapacity() < sumOfPeople + booking.getNumberOfPeople()) {
                throw FerryExceptions.createConflictException("There is not enough space on the ferry's deck");
            }
            price += onDeckPrice * booking.getNumberOfPeople();
        }
        price += vehicleType.getRequiredSpace() * vehiclePrice;
        return price;
    }


    @Override
    @RolesAllowed({"CLIENT"})
    public void removeBooking(String login, String number) {
        Booking bookingFromDB = bookingFacadeLocal.findByAccountAndNumber(accountMopFacadeLocal.findByLogin(login), number);
        if (bookingFromDB.getCruise().getStartDate().before(Timestamp.from(Instant.now()))) {
            throw BookingExceptions.createConflictException(BookingExceptions.ERROR_CANNOT_CANCEL_BOOKING);
        }
        bookingFacadeLocal.remove(bookingFromDB);

        Cruise cruise = cruiseFacadeLocal.findByNumber(bookingFromDB.getCruise().getNumber());
        if (cruise.getStartDate().compareTo(Timestamp.from(Instant.now())) > 0) {
            cruise.setPopularity(calculatePopularity(cruise));
            cruiseFacadeLocal.edit(cruise);
            logger.info("The popularity of cruise {} has been recalculated",
                    cruise.getNumber());
        }

        logger.info("The user with login {} cancelled the reservation with number {}",
                login, number);
    }

    @Override
    @RolesAllowed({"EMPLOYEE", "CLIENT"})
    public double calculatePopularity(Cruise cruise) {

        double taken = cabinFacadeLocal.findOccupiedCabinsOnCruise(cruise).stream().mapToInt(Cabin::getCapacity).sum();
        double all = cabinFacadeLocal.findCabinsOnCruise(cruise).stream().mapToInt(Cabin::getCapacity).sum();

        taken += bookingFacadeLocal.getSumNumberOfPeopleByCruise(cruise);
        all += cruise.getFerry().getOnDeckCapacity();

        double popularity = taken / all * 100;

        if (popularity > 100) {
            return 100;
        }
        return popularity;
    }
}
