package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.BookingFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Booking;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class BookingManagerTest {

    private Booking booking1;
    private Booking booking2;

    private List<Booking> bookings;

    private Account account1;
    private Account account2;

    @Mock
    private BookingFacadeLocal bookingFacadeLocal;

    @InjectMocks
    private BookingManager bookingManager;


    @BeforeEach
    void initMocks() {
        booking1 = new Booking();
        booking1.setAccount(account1);
        booking2 = new Booking();
        booking2.setAccount(account2);

        bookings = List.of(booking1, booking2);

        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllBookings() {
        when(bookingFacadeLocal.findAll()).thenReturn(bookings);

        assertEquals(bookings, bookingManager.getAllBookings());
        assertEquals(bookings.get(0).getAccount(), bookingManager.getAllBookings().get(0).getAccount());
        assertEquals(bookings.get(1).getAccount(), bookingManager.getAllBookings().get(1).getAccount());
    }
}
