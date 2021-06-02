package pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers;

import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.SeaportGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Seaport;

/**
 * Klasa mapująca obiekty portów pomiędzy encjami a DTO
 *
 * @author Karolina Kowalczyk
 */
public class SeaportMapper {

    /**
     * Metoda mapująca obiekt {@link Seaport} na obiekt {@link SeaportGeneralDTO}
     *
     * @param seaport Obiekt {@link Seaport}, który chcemy mapować
     * @return Obiekt typu {@link SeaportGeneralDTO}
     */
    public static SeaportGeneralDTO createSeaportGeneralDTOFromEntities(Seaport seaport) {
        SeaportGeneralDTO seaportGeneralDTO = new SeaportGeneralDTO();
        seaportGeneralDTO.setVersion(seaport.getVersion());
        seaportGeneralDTO.setCity(seaport.getCity());
        seaportGeneralDTO.setCode(seaport.getCode());
        return seaportGeneralDTO;
    }

}
