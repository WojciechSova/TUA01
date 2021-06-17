package pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers;

import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.BookingDetailsDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.BookingGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Booking;

/**
 * Klasa mapująca obiekty portów pomiędzy encjami, a DTO
 *
 * @author Patryk Kolanek
 */
public class BookingMapper {

    /**
     * Metoda mapująca obiekt {@link Booking} na obiekt {@link BookingDetailsDTO}
     *
     * @param booking Obiekt {@link Booking}, który chcemy mapować
     * @return Obiekt typu {@link BookingDetailsDTO}
     */
    public static BookingDetailsDTO createBookingDetailsDTOFromEntity(Booking booking) {
        if (booking == null) {
            return null;
        }

        BookingDetailsDTO bookingDetailsDTO = new BookingDetailsDTO();
        bookingDetailsDTO.setVersion(booking.getVersion());
        bookingDetailsDTO.setCruise(CruiseMapper.createCruiseGeneralDTOFromEntity(booking.getCruise()));
        bookingDetailsDTO.setAccount(AccountMapper.createAccountGeneralDTOFromEntity(booking.getAccount()));
        bookingDetailsDTO.setNumberOfPeople(booking.getNumberOfPeople());
        bookingDetailsDTO.setCabin(CabinMapper.createCabinGeneralDTOFromEntity(booking.getCabin()));
        bookingDetailsDTO.setVehicleType(VehicleTypeMapper.createVehicleTypeDTOFromEntity(booking.getVehicleType()));
        bookingDetailsDTO.setPrice(booking.getPrice());
        bookingDetailsDTO.setNumber(booking.getNumber());
        bookingDetailsDTO.setCreationDate(booking.getCreationDate());
        return bookingDetailsDTO;
    }

    /**
     * Metoda mapująca obiekt typu {@link Booking} na obiekt typu {@link BookingGeneralDTO}.
     *
     * @param booking Obiekt typu {@link Booking}, który będzie mapowany
     * @return Wynik mapowania, obiekt typu {@link BookingGeneralDTO}
     */
    public static BookingGeneralDTO createBookingGeneralDTOFromEntity(Booking booking) {
        if (booking == null) {
            return null;
        }

        BookingGeneralDTO bookingGeneralDTO = new BookingGeneralDTO();
        bookingGeneralDTO.setCruise(CruiseMapper.createCruiseGeneralDTOFromEntity(booking.getCruise()));
        bookingGeneralDTO.setAccount(AccountMapper.createAccountGeneralDTOFromEntity(booking.getAccount()));
        bookingGeneralDTO.setCreationDate(booking.getCreationDate());
        bookingGeneralDTO.setNumber(booking.getNumber());
        bookingGeneralDTO.setVersion(booking.getVersion());
        return bookingGeneralDTO;
    }
}
