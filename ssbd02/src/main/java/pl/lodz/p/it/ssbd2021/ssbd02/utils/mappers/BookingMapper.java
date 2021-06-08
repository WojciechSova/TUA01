package pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers;

import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.BookingDetailsDTO;
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
}
