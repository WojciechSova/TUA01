package pl.lodz.p.it.ssbd2021.ssbd02.web.mop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.BookingGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.BookingManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Booking;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers.BookingMapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.nio.file.attribute.UserPrincipal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class BookingEndpointTest {

    @Mock
    private BookingManagerLocal bookingManagerLocal;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private UserPrincipal userPrincipal;
    @InjectMocks
    private BookingEndpoint bookingEndpoint;

    private Booking booking1;
    private Booking booking2;
    private Account account1;


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
}
