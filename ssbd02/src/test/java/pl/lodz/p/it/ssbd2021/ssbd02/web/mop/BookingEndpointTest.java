package pl.lodz.p.it.ssbd2021.ssbd02.web.mop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.BookingDetailsDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.BookingManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Booking;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.GeneralException;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers.BookingMapper;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class BookingEndpointTest {

    @Spy
    private Booking booking1;
    @InjectMocks
    private BookingEndpoint bookingEndpoint;
    @Mock
    private BookingManagerLocal bookingManager;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
        booking1 = new Booking();
    }

    @Test
    void getBooking() {
        when(bookingManager.getBookingByNumber("1234567890")).thenReturn(booking1);

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
}
