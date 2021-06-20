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

    private Cabin cabin;
    private VehicleType vehicleType;

    @BeforeEach
    void setUp() {
        cabin = new Cabin();
        cabin.setCabinType(new CabinType());

        vehicleType = new VehicleType();
        vehicleType.setVehicleTypeName("1");
        vehicleType.setRequiredSpace(1d);
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
        Booking booking = getBooking();

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

    @Test
    void createEntityFromBookingDetailsDTO(){
        BookingDetailsDTO bookingDetailsDTO = new BookingDetailsDTO();
        bookingDetailsDTO.setVersion(1L);
        bookingDetailsDTO.setNumberOfPeople(7);

        Booking booking = BookingMapper.createEntityFromBookingDetailsDTO(bookingDetailsDTO);

        assertEquals(1L,  booking.getVersion());
        assertEquals(7, booking.getNumberOfPeople());
    }
}
