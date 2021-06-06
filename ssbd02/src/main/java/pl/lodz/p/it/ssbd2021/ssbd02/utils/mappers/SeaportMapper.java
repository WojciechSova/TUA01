package pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers;

import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.SeaportDetailsDTO;
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

    /**
     * Metoda mapująca obiekt {@link Seaport} na obiekt {@link SeaportDetailsDTO}
     *
     * @param seaport Obiekt {@link Seaport}, który chcemy mapować
     * @return Obiekt typu {@link SeaportDetailsDTO}
     */
    public static SeaportDetailsDTO createSeaportDetailsDTOFromEntity(Seaport seaport) {
        if (seaport == null) {
            return null;
        }
        SeaportDetailsDTO seaportDetailsDTO = new SeaportDetailsDTO();
        seaportDetailsDTO.setVersion(seaport.getVersion());
        seaportDetailsDTO.setCity(seaport.getCity());
        seaportDetailsDTO.setCode(seaport.getCode());
        seaportDetailsDTO.setModificationDate(seaport.getModificationDate());
        seaportDetailsDTO.setModifiedBy(AccountMapper.createAccountGeneralDTOFromEntity(seaport.getModifiedBy()));
        seaportDetailsDTO.setCreationDate(seaport.getCreationDate());
        seaportDetailsDTO.setCreatedBy(AccountMapper.createAccountGeneralDTOFromEntity(seaport.getCreatedBy()));
        return seaportDetailsDTO;
    }
}
