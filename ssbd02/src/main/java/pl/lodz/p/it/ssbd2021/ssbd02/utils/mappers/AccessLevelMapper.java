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
        AccessLevelDTO accessLevelDTO = new AccessLevelDTO(accessLevel.getLevel(), accessLevel.getActive(), accessLevel.getModificationDate(),
                AccountMapper.createAccountGeneralDTOFromEntity(accessLevel.getModifiedBy()), accessLevel.getCreationDate(),
                AccountMapper.createAccountGeneralDTOFromEntity(accessLevel.getCreatedBy()));
        accessLevelDTO.setVersion(accessLevel.getVersion());
        return accessLevelDTO;
    }
}
