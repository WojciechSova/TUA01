package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.*;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.*;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mop.BookingExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mop.CabinExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mop.CruiseExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mop.FerryExceptions;

import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookingManagerTest {

    private Booking booking1;
    private Booking booking2;
    private Booking booking3;
    @Spy
    private Booking booking4;
    @Spy
    private Cruise cruise = new Cruise();
    @Spy
    private Cabin cabin = new Cabin();
    @Spy
    private CabinType cabinType = new CabinType();
    @Spy
    private VehicleType vehicleType = new VehicleType();
    @Spy
    private Ferry ferry = new Ferry();

    private List<Booking> bookings;
    @Spy
    private List<Booking> bookings2;

    private Account account1;
    private Account account2;

    @Mock
    private BookingFacadeLocal bookingFacadeLocal;
    @Mock
    private AccountMopFacadeLocal accountMopFacadeLocal;
    @Mock
    private CruiseFacadeLocal cruiseFacadeLocal;
    @Mock
    private CabinFacadeLocal cabinFacadeLocal;
    @Mock
    private VehicleTypeFacadeLocal vehicleTypeFacadeLocal;

    @InjectMocks
    private BookingManager bookingManager;

    private final String bookingNumber1 = "1234512345";

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
        account2 = new Account();
        account2.setLogin("login2");
        booking1 = new Booking();
        booking1.setNumber(bookingNumber1);
        booking1.setAccount(account1);
        booking2 = new Booking();
        booking2.setAccount(account2);
        booking3 = new Booking();
        booking3.setAccount(account2);

        booking4 = new Booking();
        booking4.setNumberOfPeople(2);

        bookings = List.of(booking1, booking2, booking3);
        bookings2 = new ArrayList<>();
        bookings2.add(booking1);

        when(accountMopFacadeLocal.findByLogin("login")).thenReturn(account1);

        cruise.setStartDate(Timestamp.from(Instant.now().plusSeconds(99999999L)));
        cabinType.setCabinTypeName("First class");
        cabin.setCabinType(cabinType);
        cabin.setCapacity(5);
        vehicleType.setRequiredSpace(1D);
        ferry.setVehicleCapacity(100);
        ferry.setOnDeckCapacity(100);
        cruise.setFerry(ferry);
    }

    @Test
    void createBookingWithCabin() {
        when(cruiseFacadeLocal.findByNumber(any())).thenReturn(cruise);
        when(cabinFacadeLocal.findByFerryAndNumber(any(), any())).thenReturn(cabin);
        when(accountMopFacadeLocal.findByLogin(any())).thenReturn(account2);
        when(vehicleTypeFacadeLocal.findByName(any())).thenReturn(vehicleType);
        when(bookingFacadeLocal.getSumVehicleSpaceByCruise(cruise)).thenReturn(15D);
        when(cabinFacadeLocal.findOccupiedCabinsOnCruise(cruise)).thenReturn(new ArrayList<>());
        when(bookingFacadeLocal.findAll()).thenReturn(bookings2);
        int initialLength = bookings2.size();

        doAnswer(invocation -> {
            booking4.setNumber("numbernumb");
            booking4.setPrice(123D);
            booking4.setAccount(account2);
            booking4.setCabin(cabin);
            booking4.setCruise(cruise);
            booking4.setVehicleType(vehicleType);
            booking4.setCreationDate(Timestamp.from(Instant.now()));
            bookings2.add(booking4);
            return null;
        }).when(bookingFacadeLocal).create(any());

        assertDoesNotThrow(() -> bookingManager.createBooking(1, "ABCDEF000000", "A123", "Login2", "Car"));
        assertEquals(initialLength + 1, bookings2.size());
        verify(bookingFacadeLocal).create(any());
    }

    @Test
    void createBookingWithoutCabin() {
        when(cruiseFacadeLocal.findByNumber(any())).thenReturn(cruise);
        when(accountMopFacadeLocal.findByLogin(any())).thenReturn(account2);
        when(vehicleTypeFacadeLocal.findByName(any())).thenReturn(vehicleType);
        when(bookingFacadeLocal.getSumVehicleSpaceByCruise(cruise)).thenReturn(15D);
        when(bookingFacadeLocal.getSumNumberOfPeopleByCruise(cruise)).thenReturn(1L);
        when(bookingFacadeLocal.findAll()).thenReturn(bookings2);
        int initialLength = bookings2.size();

        doAnswer(invocation -> {
            booking4.setNumber("numbernumb");
            booking4.setPrice(123D);
            booking4.setAccount(account2);
            booking4.setCabin(null);
            booking4.setCruise(cruise);
            booking4.setVehicleType(vehicleType);
            booking4.setCreationDate(Timestamp.from(Instant.now()));
            bookings2.add(booking4);
            return null;
        }).when(bookingFacadeLocal).create(any());

        assertDoesNotThrow(() -> bookingManager.createBooking(1, "ABCDEF000000", "", "Login2", "Car"));
        assertEquals(initialLength + 1, bookings2.size());
        verify(bookingFacadeLocal).create(any());
    }

    @Test
    void createBookingCruiseStartedException() {
        cruise.setStartDate(Timestamp.from(Instant.now().minusSeconds(99999999L)));
        when(cruiseFacadeLocal.findByNumber(any())).thenReturn(cruise);
        when(accountMopFacadeLocal.findByLogin(any())).thenReturn(account2);
        when(vehicleTypeFacadeLocal.findByName(any())).thenReturn(vehicleType);

        assertThrows(CruiseExceptions.class, () -> bookingManager.createBooking(1, "a", "a", "a", "a"));
    }

    @Test
    void createBookingVehicleCapacityException() {
        ferry.setVehicleCapacity(1);
        vehicleType.setRequiredSpace(2.5);
        when(cruiseFacadeLocal.findByNumber(any())).thenReturn(cruise);
        when(accountMopFacadeLocal.findByLogin(any())).thenReturn(account2);
        when(vehicleTypeFacadeLocal.findByName(any())).thenReturn(vehicleType);
        when(bookingFacadeLocal.getSumVehicleSpaceByCruise(cruise)).thenReturn(15D);

        assertThrows(FerryExceptions.class, () -> bookingManager.createBooking(1, "a", "a", "a", "a"));
    }

    @Test
    void createBookingCabinCapacityException() {
        cabin.setCapacity(1);
        when(cruiseFacadeLocal.findByNumber(any())).thenReturn(cruise);
        when(cabinFacadeLocal.findByFerryAndNumber(any(), any())).thenReturn(cabin);
        when(accountMopFacadeLocal.findByLogin(any())).thenReturn(account2);
        when(vehicleTypeFacadeLocal.findByName(any())).thenReturn(vehicleType);
        when(bookingFacadeLocal.getSumVehicleSpaceByCruise(cruise)).thenReturn(15D);
        when(bookingFacadeLocal.findAll()).thenReturn(bookings2);
        when(cabinFacadeLocal.findOccupiedCabinsOnCruise(cruise)).thenReturn(new ArrayList<>());

        assertThrows(CabinExceptions.class, () -> bookingManager.createBooking(10, "a", "a", "a", "a"));
    }

    @Test
    void createBookingCabinOccupiedException() {
        when(cruiseFacadeLocal.findByNumber(any())).thenReturn(cruise);
        when(cabinFacadeLocal.findByFerryAndNumber(any(), any())).thenReturn(cabin);
        when(accountMopFacadeLocal.findByLogin(any())).thenReturn(account2);
        when(vehicleTypeFacadeLocal.findByName(any())).thenReturn(vehicleType);
        when(bookingFacadeLocal.getSumVehicleSpaceByCruise(cruise)).thenReturn(15D);
        when(bookingFacadeLocal.findAll()).thenReturn(bookings2);
        when(cabinFacadeLocal.findOccupiedCabinsOnCruise(cruise)).thenReturn(List.of(cabin));

        assertThrows(CabinExceptions.class, () -> bookingManager.createBooking(1, "a", "a", "a", "a"));
    }

    @Test
    void createBookingFerryOnDeckException() {
        ferry.setOnDeckCapacity(1);
        when(cruiseFacadeLocal.findByNumber(any())).thenReturn(cruise);
        when(accountMopFacadeLocal.findByLogin(any())).thenReturn(account2);
        when(vehicleTypeFacadeLocal.findByName(any())).thenReturn(vehicleType);
        when(bookingFacadeLocal.getSumVehicleSpaceByCruise(cruise)).thenReturn(15D);
        when(bookingFacadeLocal.getSumNumberOfPeopleByCruise(cruise)).thenReturn(1L);
        when(bookingFacadeLocal.findAll()).thenReturn(bookings2);

        assertThrows(FerryExceptions.class, () -> bookingManager.createBooking(10, "a", "a", "a", "a"));
    }


    @Test
    void getAllBookings() {
        when(bookingFacadeLocal.findAll()).thenReturn(bookings);

        assertEquals(bookings, bookingManager.getAllBookings());
        assertEquals(bookings.get(0).getAccount(), bookingManager.getAllBookings().get(0).getAccount());
        assertEquals(bookings.get(1).getAccount(), bookingManager.getAllBookings().get(1).getAccount());
    }

    @Test
    void getAllBookingsByAccount() {
        List<Booking> byAccount2 = bookings.subList(1, 3);
        when(bookingFacadeLocal.findAllByAccount(account2)).thenReturn(byAccount2);
        when(accountMopFacadeLocal.findByLogin("login2")).thenReturn(account2);

        assertEquals(byAccount2, bookingManager.getAllBookingsByAccount("login2"));
        assertEquals(2, bookingManager.getAllBookingsByAccount("login2").size());
        assertEquals(account2, bookingManager.getAllBookingsByAccount("login2").get(0).getAccount());
        assertEquals(account2, bookingManager.getAllBookingsByAccount("login2").get(1).getAccount());
    }

    @Test
    void getBookingByNumber() {
        when(bookingFacadeLocal.findByNumber(bookingNumber1)).thenReturn(booking1);

        Booking foundBooking = bookingManager.getBookingByNumber(bookingNumber1);

        assertEquals(booking1, foundBooking);
        assertEquals(booking1.getNumber(), foundBooking.getNumber());
        assertEquals(booking1.hashCode(), foundBooking.hashCode());
    }

    @Test
    void getBookingByNumberException() {
        String nonExistentNumber = "3453766425";
        doAnswer(invocationOnMock -> {
            throw CommonExceptions.createNoResultException();
        }).when(bookingFacadeLocal).findByNumber(nonExistentNumber);

        assertThrows(CommonExceptions.class, () -> bookingManager.getBookingByNumber(nonExistentNumber));
    }

    @Test
    void getBookingByAccountAndNumber() {
        when(bookingFacadeLocal.findByAccountAndNumber(account1, bookingNumber1)).thenReturn(booking1);

        Booking foundBooking = bookingManager.getBookingByAccountAndNumber("login", bookingNumber1);

        assertEquals(booking1, foundBooking);
        assertEquals(booking1.getNumber(), foundBooking.getNumber());
        assertEquals(booking1.hashCode(), foundBooking.hashCode());
    }

    @Test
    void removeBooking() {
        Cruise cruise = new Cruise();
        cruise.setStartDate(Timestamp.from(Instant.now().plusSeconds(60 * 60)));
        booking1.setCruise(cruise);
        when(bookingFacadeLocal.findByAccountAndNumber(account1, bookingNumber1)).thenReturn(booking1);

        bookingManager.removeBooking("login", bookingNumber1);

        verify(bookingFacadeLocal).remove(booking1);
    }

    @Test
    void removeBookingException() {
        Cruise cruise = new Cruise();
        cruise.setStartDate(Timestamp.from(Instant.now().minusSeconds(60 * 60)));
        booking1.setCruise(cruise);
        when(bookingFacadeLocal.findByAccountAndNumber(account1, bookingNumber1)).thenReturn(booking1);

        BookingExceptions bookingExceptions = assertThrows(BookingExceptions.class, () -> bookingManager.removeBooking("login", bookingNumber1));

        assertEquals(Response.Status.CONFLICT.getStatusCode(), bookingExceptions.getResponse().getStatus());
        assertEquals(BookingExceptions.ERROR_CANNOT_CANCEL_BOOKING, bookingExceptions.getResponse().getEntity());
    }


}
