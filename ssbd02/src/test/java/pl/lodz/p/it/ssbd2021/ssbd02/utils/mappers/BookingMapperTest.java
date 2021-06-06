package pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.BookingDetailsDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Booking;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cabin;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.CabinType;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cruise;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.VehicleType;

import java.sql.Timestamp;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class BookingMapperTest {

    private Booking booking;
    private Cabin cabin;

    @BeforeEach
    void setUp() {
        cabin = new Cabin();
        cabin.setCabinType(new CabinType());

        booking = new Booking();
        booking.setVersion(1L);
        booking.setCruise(new Cruise());
        booking.setAccount(new Account());
        booking.setNumberOfPeople(1);
        booking.setCabin(cabin);
        booking.setVehicleType(new VehicleType());
        booking.setPrice(1.2);
        booking.setNumber("1234567890");
        booking.setCreationDate(Timestamp.from(Instant.now()));
    }

    @Test
    void createBookingDetailsDTOFromEntity() {
        BookingDetailsDTO bookingDetailsDTO = BookingMapper.createBookingDetailsDTOFromEntity(booking);

        assertEquals(bookingDetailsDTO.getVersion(), booking.getVersion());
//        assertEquals(bookingDetailsDTO.getCruise(), booking.getCruise());
        assertEquals(bookingDetailsDTO.getAccount(), AccountMapper.createAccountGeneralDTOFromEntity(booking.getAccount()));
        assertEquals(bookingDetailsDTO.getNumberOfPeople(), booking.getNumberOfPeople());
        assertEquals(bookingDetailsDTO.getCabin(), CabinMapper.createCabinDTOFromEntity(booking.getCabin()));
//        assertEquals(bookingDetailsDTO.getVehicleType(), booking.getVehicleType());
        assertEquals(bookingDetailsDTO.getPrice(), booking.getPrice());
        assertEquals(bookingDetailsDTO.getNumber(), booking.getNumber());
        assertEquals(bookingDetailsDTO.getCreationDate(), booking.getCreationDate());
    }
}
