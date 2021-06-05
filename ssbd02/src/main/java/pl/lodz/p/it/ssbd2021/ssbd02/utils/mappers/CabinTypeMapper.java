package pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers;

import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.CabinTypeDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.CabinType;

/**
 * Klasa mapująca obiekty typów kajut pomiędzy encjami a DTO
 *
 * @author Julia Kołodziej
 */
public class CabinTypeMapper {

    /**
     * Metoda mapująca obiekt typu {@link CabinType} na obiekt typu {@link CabinTypeDTO}
     *
     * @param cabinType Obiekt typu {@link CabinType}, który będzie mapowany
     * @return Obiekt typu {@link CabinTypeDTO}
     */
    public static CabinTypeDTO createCabinTypeDTOFromEntity(CabinType cabinType) {
        CabinTypeDTO cabinTypeDTO = new CabinTypeDTO();
        cabinTypeDTO.setCabinTypeName(cabinType.getCabinTypeName());
        return cabinTypeDTO;
    }

    /**
     * Metoda mapująca obiekt typu {@link CabinTypeDTO} na obiekt typu {@link CabinType}
     *
     * @param cabinTypeDTO Obiekt typu {@link CabinTypeDTO}, który będzie mapowany
     * @return Obiekt typu {@link CabinType}
     */
    public static CabinType createCabinTypeFromCabinTypeDTO(CabinTypeDTO cabinTypeDTO) {
        CabinType cabin = new CabinType();
        cabin.setCabinTypeName(cabinTypeDTO.getCabinTypeName());
        return cabin;
    }
}
