package pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.BookingDetailsDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.BookingGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Booking;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cabin;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.CabinType;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cruise;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.VehicleType;

import java.sql.Timestamp;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookingMapperTest {

    private Booking booking;
    private Cabin cabin;
    private VehicleType vehicleType;

    @BeforeEach
    void setUp() {
        cabin = new Cabin();
        cabin.setCabinType(new CabinType());

        vehicleType = new VehicleType();
        vehicleType.setVehicleTypeName("1");
        vehicleType.setRequiredSpace(1d);

        booking = new Booking();
        booking.setVersion(1L);
        booking.setCruise(new Cruise());
        booking.setAccount(new Account());
        booking.setNumberOfPeople(1);
        booking.setCabin(cabin);
        booking.setVehicleType(vehicleType);
        booking.setPrice(1.2);
        booking.setNumber("1234567890");
        booking.setCreationDate(Timestamp.from(Instant.now()));
    }

    private Booking getBooking() {
        Booking booking = new Booking();
        booking.setCruise(new Cruise());
        booking.setAccount(new Account());
        booking.setNumberOfPeople(5);
        booking.setCabin(new Cabin());
        booking.setVehicleType(new VehicleType());
        booking.setPrice(20.);
        booking.setNumber("ABC");
        booking.setCreationDate(Timestamp.from(Instant.now()));
        booking.setVersion(20L);

        return booking;
    }

    @Test
    void createBookingDetailsDTOFromEntity() {
        BookingDetailsDTO bookingDetailsDTO = BookingMapper.createBookingDetailsDTOFromEntity(booking);

        assertEquals(bookingDetailsDTO.getVersion(), booking.getVersion());
        assertEquals(bookingDetailsDTO.getCruise(), CruiseMapper.createCruiseGeneralDTOFromEntity(booking.getCruise()));
        assertEquals(bookingDetailsDTO.getAccount(), AccountMapper.createAccountGeneralDTOFromEntity(booking.getAccount()));
        assertEquals(bookingDetailsDTO.getNumberOfPeople(), booking.getNumberOfPeople());
        assertEquals(bookingDetailsDTO.getCabin(), CabinMapper.createCabinGeneralDTOFromEntity(booking.getCabin()));
        assertEquals(bookingDetailsDTO.getVehicleType(), VehicleTypeMapper.createVehicleTypeDTOFromEntity(booking.getVehicleType()));
        assertEquals(bookingDetailsDTO.getPrice(), booking.getPrice());
        assertEquals(bookingDetailsDTO.getNumber(), booking.getNumber());
        assertEquals(bookingDetailsDTO.getCreationDate(), booking.getCreationDate());
    }

    @Test
    void createBookingGeneralDTOFromEntity() {
        Booking booking = getBooking();

        BookingGeneralDTO bookingGeneralDTO = BookingMapper.createBookingGeneralDTOFromEntity(booking);

        assertEquals(CruiseMapper.createCruiseGeneralDTOFromEntity(booking.getCruise()), bookingGeneralDTO.getCruise());
        assertEquals(AccountMapper.createAccountGeneralDTOFromEntity(booking.getAccount()), bookingGeneralDTO.getAccount());
        assertEquals(booking.getNumber(), bookingGeneralDTO.getNumber());
        assertEquals(booking.getCreationDate(), bookingGeneralDTO.getCreationDate());
        assertEquals(booking.getVersion(), bookingGeneralDTO.getVersion());
    }
}
