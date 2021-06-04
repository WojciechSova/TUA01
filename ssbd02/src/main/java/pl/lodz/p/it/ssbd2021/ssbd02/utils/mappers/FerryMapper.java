package pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers;

import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.FerryGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Ferry;

/**
 * Klasa mapująca obiekty promów pomiędzy encjami a DTO
 *
 * @author Artur Madaj
 */
public class FerryMapper {

    /**
     * Metoda mapująca obiekt typu {@link Ferry} na obiekt typu {@link FerryGeneralDTO}.
     *
     * @param ferry Obiekt typu {@link Ferry}, który będzie mapowany.
     * @return Obiekt typu {@link FerryGeneralDTO}
     */
    public static FerryGeneralDTO createFerryGeneralDTOFromEntity(Ferry ferry) {
        if (ferry == null) {
            return null;
        }

        FerryGeneralDTO ferryGeneralDTO = new FerryGeneralDTO();
        ferryGeneralDTO.setName(ferry.getName());
        ferryGeneralDTO.setOnDeckCapacity(ferry.getOnDeckCapacity());
        ferryGeneralDTO.setVehicleCapacity(ferry.getVehicleCapacity());
        ferryGeneralDTO.setVersion(ferry.getVersion());
        return ferryGeneralDTO;
    }
}
