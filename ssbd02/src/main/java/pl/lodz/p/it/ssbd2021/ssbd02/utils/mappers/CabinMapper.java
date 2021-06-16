package pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers;

import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.CabinDetailsDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.CabinGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cabin;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.CabinType;

/**
 * Klasa mapująca obiekty kajut pomiędzy encjami a DTO
 *
 * @author Julia Kołodziej
 */
public class CabinMapper {

    /**
     * Metoda mapująca obiekt typu {@link Cabin} na obiekt typu {@link CabinDetailsDTO}.
     *
     * @param cabin Obiekt typu {@link Cabin}, który będzie mapowany
     * @return Obiekt typu {@link CabinDetailsDTO}
     */
    public static CabinDetailsDTO createCabinDetailsDTOFromEntity(Cabin cabin) {
        if (cabin == null) {
            return null;
        }
        CabinDetailsDTO cabinDTO = new CabinDetailsDTO();
        cabinDTO.setVersion(cabin.getVersion());
        cabinDTO.setCapacity(cabin.getCapacity());
        if (cabin.getCabinType() != null) {
            cabinDTO.setCabinType(cabin.getCabinType().getCabinTypeName());
        }
        cabinDTO.setNumber(cabin.getNumber());
        cabinDTO.setModificationDate(cabin.getModificationDate());
        cabinDTO.setModifiedBy(AccountMapper.createAccountGeneralDTOFromEntity(cabin.getModifiedBy()));
        cabinDTO.setCreationDate(cabin.getCreationDate());
        cabinDTO.setCreatedBy(AccountMapper.createAccountGeneralDTOFromEntity(cabin.getCreatedBy()));
        return cabinDTO;
    }

    /**
     * Metoda mapująca obiekt typu {@link Cabin} na obiekt typu {@link CabinGeneralDTO}.
     *
     * @param cabin Obiekt typu {@link Cabin}, który będzie mapowany
     * @return Obiekt typu {@link CabinGeneralDTO}
     */
    public static CabinGeneralDTO createCabinGeneralDTOFromEntity(Cabin cabin) {
        if (cabin == null) {
            return null;
        }
        CabinGeneralDTO cabinDTO = new CabinGeneralDTO();
        cabinDTO.setVersion(cabin.getVersion());
        cabinDTO.setCapacity(cabin.getCapacity());
        if (cabin.getCabinType() != null) {
            cabinDTO.setCabinType(cabin.getCabinType().getCabinTypeName());
        }
        cabinDTO.setNumber(cabin.getNumber());
        return cabinDTO;
    }

    /**
     * Metoda mapująca obiekty typu {@link CabinDetailsDTO} i {@link CabinType} na obiekt typu {@link Cabin}.
     *
     * @param cabinDetailsDTO Obiekt typu {@link CabinDetailsDTO}, który będzie mapowany
     * @param cabinType Obiekty typu {@link CabinType}, określający typ mapowanej kajuty
     * @return Obiekt typu {@link Cabin}
     */
    public static Cabin createEntityFromCabinDetailsDTO(CabinDetailsDTO cabinDetailsDTO, CabinType cabinType){
        if(cabinDetailsDTO == null){
            return null;
        }
        Cabin cabin = new Cabin();
        cabin.setVersion(cabinDetailsDTO.getVersion());
        cabin.setCapacity(cabinDetailsDTO.getCapacity());
        cabin.setCabinType(cabinType);
        cabin.setNumber(cabinDetailsDTO.getNumber());
        return cabin;
    }
}
