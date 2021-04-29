package pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers;

import pl.lodz.p.it.ssbd2021.ssbd02.dto.mok.AccessLevelDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mok.AccountGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;

/**
 * Klasa mapująca obiekt {@link AccessLevel} z encji do DTO
 *
 * @author Wojciech Sowa
 */
public class AccessLevelMapper {

    /**
     * Metoda mapująca obiekt {@link AccessLevel} na obiekt {@link AccessLevelDTO}
     * Używana jedynie w pakiecie
     *
     * @param accessLevel Obiekt {@link AccessLevel}, który chcemy mapować
     * @return Obiekt typu {@link AccessLevelDTO}
     */
    static AccessLevelDTO createAccessLevelDTOFromEntity(AccessLevel accessLevel) {
        if (accessLevel == null) {
            return null;
        }
        AccessLevelDTO accessLevelDTO = new AccessLevelDTO();
        accessLevelDTO.setLevel(accessLevel.getLevel());
        accessLevelDTO.setActive(accessLevel.getActive());
        accessLevelDTO.setModificationDate(accessLevel.getModificationDate());
        accessLevelDTO.setModifiedBy(AccountMapper.createAccountGeneralDTOFromEntity(accessLevel.getModifiedBy()));
        accessLevelDTO.setCreationDate(accessLevel.getCreationDate());
        accessLevelDTO.setCreatedBy(AccountMapper.createAccountGeneralDTOFromEntity(accessLevel.getCreatedBy()));
        accessLevelDTO.setVersion(accessLevel.getVersion());
        return accessLevelDTO;
    }
}
