package pl.lodz.p.it.ssbd2021.ssbd02.web.mop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.BookingDetailsDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.BookingGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.BookingManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Booking;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cabin;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.GeneralException;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers.BookingMapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.nio.file.attribute.UserPrincipal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingEndpointTest {

    @Mock
    private Booking booking1;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private UserPrincipal userPrincipal;
    @Mock
    private BookingManagerLocal bookingManagerLocal;
    @InjectMocks
    private BookingEndpoint bookingEndpoint;


    private Booking booking2;
    private Booking booking3 = new Booking();
    private Account account1;
    private Cabin cabin = new Cabin();
    private final List<Booking> bookings = new ArrayList<>();


    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
        booking1 = new Booking();
        account1 = new Account();
        account1.setLogin("Login");
        booking1.setNumber("ABC");
        booking1.setAccount(account1);
        booking2 = new Booking();
        booking2.setNumber("CBA");
        booking3.setNumberOfPeople(2);
    }

    @Test
    void getBooking() {
        when(bookingManagerLocal.getBookingByNumber("1234567890")).thenReturn(booking1);

        BookingDetailsDTO bookingDetailsDTO = BookingMapper.createBookingDetailsDTOFromEntity(booking1);

        assertAll(
                () -> assertEquals(bookingDetailsDTO.hashCode(), bookingEndpoint.getBooking("1234567890")
                        .getEntity().hashCode()),
                () -> assertEquals(Response.Status.OK.getStatusCode(), bookingEndpoint.getBooking("1234567890")
                        .getStatus())
        );

        GeneralException exception = Assertions.assertThrows(CommonExceptions.class,
                () -> bookingEndpoint.getBooking(null));

        Assertions.assertEquals(400, exception.getResponse().getStatus());
        Assertions.assertEquals(CommonExceptions.ERROR_CONSTRAINT_VIOLATION, exception.getResponse().getEntity());

        exception = Assertions.assertThrows(CommonExceptions.class,
                () -> bookingEndpoint.getBooking("1234"));

        Assertions.assertEquals(400, exception.getResponse().getStatus());
        Assertions.assertEquals(CommonExceptions.ERROR_CONSTRAINT_VIOLATION, exception.getResponse().getEntity());
    }

    @Test
    void getOwnBooking() {
        when(bookingManagerLocal.getBookingByAccountAndNumber("login", "1234567890")).thenReturn(booking1);
        when(securityContext.getUserPrincipal()).thenReturn(userPrincipal);
        when(securityContext.getUserPrincipal().getName()).thenReturn("login");

        BookingDetailsDTO bookingDetailsDTO = BookingMapper.createBookingDetailsDTOFromEntity(booking1);

        assertAll(
                () -> assertEquals(bookingDetailsDTO.hashCode(), bookingEndpoint.getOwnBooking(securityContext, "1234567890")
                        .getEntity().hashCode()),
                () -> assertEquals(Response.Status.OK.getStatusCode(), bookingEndpoint.getOwnBooking(securityContext, "1234567890")
                        .getStatus())
        );

        GeneralException exception = Assertions.assertThrows(CommonExceptions.class,
                () -> bookingEndpoint.getOwnBooking(securityContext, null));

        Assertions.assertEquals(400, exception.getResponse().getStatus());
        Assertions.assertEquals(CommonExceptions.ERROR_CONSTRAINT_VIOLATION, exception.getResponse().getEntity());

        exception = Assertions.assertThrows(CommonExceptions.class,
                () -> bookingEndpoint.getOwnBooking(securityContext, "1234"));

        Assertions.assertEquals(400, exception.getResponse().getStatus());
        Assertions.assertEquals(CommonExceptions.ERROR_CONSTRAINT_VIOLATION, exception.getResponse().getEntity());
    }

    @Test
    void getAllBookings() {
        when(bookingManagerLocal.getAllBookings()).thenReturn(List.of(booking1, booking2));

        Response response = bookingEndpoint.getAllBookings();

        assertEquals(2, ((List<BookingGeneralDTO>) (response.getEntity())).size());
        assertEquals(BookingMapper.createBookingGeneralDTOFromEntity(booking1), ((List<BookingGeneralDTO>) (response.getEntity())).get(0));
        assertEquals(BookingMapper.createBookingGeneralDTOFromEntity(booking2), ((List<BookingGeneralDTO>) (response.getEntity())).get(1));
        assertEquals(200, response.getStatus());
    }

    @Test
    void getOwnBookings() {
        when(bookingManagerLocal.getAllBookingsByAccount(account1.getLogin())).thenReturn(List.of(booking1));
        when(securityContext.getUserPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getName()).thenReturn(account1.getLogin());

        Response response = bookingEndpoint.getOwnBookings(securityContext);

        assertEquals(1, ((List<BookingGeneralDTO>) (response.getEntity())).size());
        assertEquals(BookingMapper.createBookingGeneralDTOFromEntity(booking1), ((List<BookingGeneralDTO>) (response.getEntity())).get(0));
        assertEquals(200, response.getStatus());
    }

    @Test
    void cancelBooking() {
        when(securityContext.getUserPrincipal()).thenReturn(userPrincipal);
        when(securityContext.getUserPrincipal().getName()).thenReturn("login");
        doAnswer(invocationOnMock -> null).when(bookingManagerLocal).removeBooking("login", booking1.getNumber());

        bookingEndpoint.cancelBooking(booking1.getNumber(), securityContext);
        verify(bookingManagerLocal).removeBooking("login", booking1.getNumber());

        booking1.setNumber(null);
        GeneralException noNumber = assertThrows(GeneralException.class,
                () -> bookingEndpoint.cancelBooking(booking1.getNumber(), securityContext));
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), noNumber.getResponse().getStatus());
    }


    @Test
    void addBookingWithCabin() {
        when(securityContext.getUserPrincipal()).thenReturn(userPrincipal);
        when(securityContext.getUserPrincipal().getName()).thenReturn("login");

        Response response = bookingEndpoint.addBooking("ABCDEF123456", "A123", "Car",
                1, securityContext);

        assertEquals(Response.Status.ACCEPTED.getStatusCode(), response.getStatus());
    }

    @Test
    void addBookingWithoutCabin() {
        when(securityContext.getUserPrincipal()).thenReturn(userPrincipal);
        when(securityContext.getUserPrincipal().getName()).thenReturn("login");

        Response response = bookingEndpoint.addBooking("ABCDEF123456", "", "Car",
                1, securityContext);

        assertEquals(Response.Status.ACCEPTED.getStatusCode(), response.getStatus());
    }

    @Test
    void addBookingExceptions() {
        when(securityContext.getUserPrincipal()).thenReturn(userPrincipal);
        when(securityContext.getUserPrincipal().getName()).thenReturn("login");

        assertThrows(CommonExceptions.class, () -> bookingEndpoint.addBooking("sdgthgd", "A123", "Car",
                1, securityContext));

        assertThrows(CommonExceptions.class, () -> bookingEndpoint.addBooking("ABCDEF123456", "A1dfg23", "Car",
                1, securityContext));

        assertThrows(CommonExceptions.class, () -> bookingEndpoint.addBooking("ABCDEF123456", "A123", "Cdafgar",
                1, securityContext));

        booking3.setNumberOfPeople(-5);
        assertThrows(CommonExceptions.class, () -> bookingEndpoint.addBooking("ABCDEF123456", "A123", "Car",
                -5, securityContext));

    }
}
