package pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers;

import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.CruiseDetailsDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.CruiseGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cruise;

/**
 * Klasa mapująca obiekty rejsów pomiędzy encjami a DTO
 *
 * @author Wojciech Sowa
 */
public class CruiseMapper {

    /**
     * Metoda mapująca obiekt typu {@link Cruise} na obiekt typu {@link CruiseGeneralDTO}.
     *
     * @param cruise Obiekt typu {@link Cruise}, który będzie mapowany
     * @return Wynik mapowania, obiekt typu {@link CruiseGeneralDTO}
     */
    public static CruiseGeneralDTO createCruiseGeneralDTOFromEntity(Cruise cruise) {
        if (cruise == null) {
            return null;
        }

        CruiseGeneralDTO cruiseGeneralDTO = new CruiseGeneralDTO();
        cruiseGeneralDTO.setStartDate(cruise.getStartDate());
        cruiseGeneralDTO.setEndDate(cruise.getEndDate());
        cruiseGeneralDTO.setFerry(FerryMapper.createFerryGeneralDTOFromEntity(cruise.getFerry()));
        cruiseGeneralDTO.setNumber(cruise.getNumber());
        cruiseGeneralDTO.setVersion(cruise.getVersion());
        return cruiseGeneralDTO;
    }

    /**
     * Metoda mapująca obiekt typu {@link Cruise} na obiekt typu {@link CruiseDetailsDTO}.
     *
     * @param cruise Obiekt typu {@link Cruise}, który będzie mapowany
     * @return Wynik mapowania, obiekt typu {@link CruiseDetailsDTO}
     */
    public static CruiseDetailsDTO createCruiseDetailsDTOFromEntity(Cruise cruise) {
        if (cruise == null) {
            return null;
        }

        CruiseDetailsDTO cruiseDetailsDTO = new CruiseDetailsDTO();
        cruiseDetailsDTO.setStartDate(cruise.getStartDate());
        cruiseDetailsDTO.setEndDate(cruise.getEndDate());
        cruiseDetailsDTO.setRoute(RouteMapper.createRouteGeneralDTOFromEntity(cruise.getRoute()));
        cruiseDetailsDTO.setFerry(FerryMapper.createFerryGeneralDTOFromEntity(cruise.getFerry()));
        cruiseDetailsDTO.setNumber(cruise.getNumber());
        cruiseDetailsDTO.setModificationDate(cruise.getModificationDate());
        cruiseDetailsDTO.setModifiedBy(AccountMapper.createAccountGeneralDTOFromEntity(cruise.getModifiedBy()));
        cruiseDetailsDTO.setCreationDate(cruise.getCreationDate());
        cruiseDetailsDTO.setCreatedBy(AccountMapper.createAccountGeneralDTOFromEntity(cruise.getCreatedBy()));
        cruiseDetailsDTO.setVersion(cruise.getVersion());
        return cruiseDetailsDTO;

    }
}
