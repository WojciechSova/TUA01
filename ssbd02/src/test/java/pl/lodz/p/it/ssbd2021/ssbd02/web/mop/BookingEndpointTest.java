package pl.lodz.p.it.ssbd2021.ssbd02.web.mop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.BookingGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.BookingManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Booking;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers.BookingMapper;

import javax.ws.rs.core.Response;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class BookingEndpointTest {

    @Mock
    private BookingManagerLocal bookingManagerLocal;
    @InjectMocks
    private BookingEndpoint bookingEndpoint;

    private Booking booking1;
    private Booking booking2;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);

        booking1 = new Booking();
        booking1.setNumber("ABC");
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
}
