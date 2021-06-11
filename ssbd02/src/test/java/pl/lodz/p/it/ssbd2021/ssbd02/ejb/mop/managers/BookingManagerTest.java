package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.AccountMopFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.BookingFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Booking;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class BookingManagerTest {

    private Booking booking1;
    private Booking booking2;
    private Booking booking3;

    private List<Booking> bookings;

    private Account account1;
    private Account account2;

    @Mock
    private BookingFacadeLocal bookingFacadeLocal;
    @Mock
    private AccountMopFacadeLocal accountMopFacadeLocal;

    @InjectMocks
    private BookingManager bookingManager;

    private final String bookingNumber1 = "1234512345";

    @BeforeEach
    void initMocks() {
        account2 = new Account();
        account2.setLogin("login2");
        booking1 = new Booking();
        booking1.setNumber(bookingNumber1);
        booking1.setAccount(account1);
        booking2 = new Booking();
        booking2.setAccount(account2);
        booking3 = new Booking();
        booking3.setAccount(account2);

        bookings = List.of(booking1, booking2, booking3);

        MockitoAnnotations.openMocks(this);
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
        List<Booking> byAccount2 = bookings.subList(1,3);
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
        when(bookingFacadeLocal.findByNumber(nonExistentNumber)).thenReturn(null);

        assertThrows(CommonExceptions.class, () -> bookingManager.getBookingByNumber(nonExistentNumber));
    }

    @Test
    void getBookingByAccountAndNumber() {
        when(bookingFacadeLocal.findByAccountAndNumber(account1, bookingNumber1)).thenReturn(booking1);
        when(accountMopFacadeLocal.findByLogin("login")).thenReturn(account1);

        Booking foundBooking = bookingManager.getBookingByAccountAndNumber("login", bookingNumber1);

        assertEquals(booking1, foundBooking);
        assertEquals(booking1.getNumber(), foundBooking.getNumber());
        assertEquals(booking1.hashCode(), foundBooking.hashCode());
    }
}
