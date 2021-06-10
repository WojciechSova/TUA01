package pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers;

import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.BookingGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Booking;

public class BookingMapper {

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
