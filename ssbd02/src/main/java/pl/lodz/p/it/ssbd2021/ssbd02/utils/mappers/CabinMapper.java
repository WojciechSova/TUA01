package pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers;

import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.CabinDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cabin;

/**
 * Klasa mapująca obiekty kajut pomiędzy encjami a DTO
 *
 * @author Julia Kołodziej
 */
public class CabinMapper {

    /**
     * Metoda mapująca obiekt typu {@link Cabin} na obiekt typu {@link CabinDTO}.
     *
     * @param cabin Obiekt typu {@link Cabin}, który będzie mapowany
     * @return Obiekt typu {@link CabinDTO}
     */
    public static CabinDTO createCabinDTOFromEntity(Cabin cabin) {
        if (cabin == null) {
            return null;
        }
        CabinDTO cabinDTO = new CabinDTO();
        cabinDTO.setCapacity(cabin.getCapacity());
        cabinDTO.setCabinType(CabinTypeMapper.createCabinTypeDTOFromEntity(cabin.getCabinType()));
        cabinDTO.setNumber(cabin.getNumber());
        cabinDTO.setModificationDate(cabin.getModificationDate());
        cabinDTO.setModifiedBy(AccountMapper.createAccountGeneralDTOFromEntity(cabin.getModifiedBy()));
        cabinDTO.setCreationDate(cabin.getCreationDate());
        cabinDTO.setCreatedBy(AccountMapper.createAccountGeneralDTOFromEntity(cabin.getCreatedBy()));
        return cabinDTO;
    }
}
