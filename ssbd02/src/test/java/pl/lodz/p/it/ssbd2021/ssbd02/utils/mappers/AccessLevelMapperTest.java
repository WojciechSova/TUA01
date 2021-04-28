package pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mok.AccessLevelDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;

import java.sql.Timestamp;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class AccessLevelMapperTest {
    AccessLevel accessLevel;
    Account accountModifiedBy;
    Account accountCreatedBy;

    @BeforeEach
    void setUp() {
        accountModifiedBy = new Account();
        accountModifiedBy.setLogin("ModifiedLogin");
        accountCreatedBy = new Account();
        accountCreatedBy.setLogin("CreatedLogin");
        accessLevel = new AccessLevel();
        accessLevel.setLevel("ADMIN");
        accessLevel.setVersion(1L);
        accessLevel.setModificationDate(Timestamp.from(Instant.now()));
        accessLevel.setModifiedBy(accountModifiedBy);
        accessLevel.setCreationDate(Timestamp.valueOf("2020-03-25 11:21:15"));
        accessLevel.setCreatedBy(accountCreatedBy);
    }

    @Test
    void createAccessLevelDTOFromEntity() {
        AccessLevelDTO accLvlDTO = AccessLevelMapper.createAccessLevelDTOFromEntity(accessLevel);

        assertEquals(accessLevel.getLevel(), accLvlDTO.getLevel());
        assertTrue(accLvlDTO.getActive());
        assertEquals(accessLevel.getModificationDate(), accLvlDTO.getModificationDate());
        assertEquals(accessLevel.getVersion(), accLvlDTO.getVersion());
        assertEquals(AccountMapper.createAccountGeneralDTOFromEntity(accessLevel.getModifiedBy()).hashCode(), accLvlDTO.getModifiedBy().hashCode());
        assertEquals(accessLevel.getCreationDate(), accLvlDTO.getCreationDate());
        assertEquals(AccountMapper.createAccountGeneralDTOFromEntity(accessLevel.getCreatedBy()).hashCode(), accLvlDTO.getCreatedBy().hashCode());
    }
}
