package pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.SeaportGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.SeaportDetailsDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Seaport;

import java.sql.Timestamp;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SeaportMapperTest {

    private Seaport seaport;
    private Seaport seaport2;
    private Account accountModifiedBy;
    private Account accountCreatedBy;
    Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        seaport = createSeaport("Warszawa", "WAR", 1L, Timestamp.from(Instant.now()), account,
                account, Timestamp.from(Instant.now()));
        accountModifiedBy = new Account();
        accountModifiedBy.setLogin("ModifiedLogin");
        accountCreatedBy = new Account();
        accountCreatedBy.setLogin("CreatedLogin");
        seaport2 = new Seaport();
        seaport2.setCity("City");
        seaport2.setCity("ABC");
        seaport2.setModificationDate(Timestamp.from(Instant.now()));
        seaport2.setModifiedBy(accountModifiedBy);
        seaport2.setCreationDate(Timestamp.valueOf("2020-03-25 11:21:15"));
        seaport2.setCreatedBy(accountCreatedBy);
        seaport2.setVersion(1L);
    }

    private Seaport createSeaport(String city, String code, Long version, Timestamp creationDate, Account createdBy,
                                   Account modifiedBy, Timestamp modificationDate) {
        Seaport seaport = new Seaport();
        seaport.setCity(city);
        seaport.setCode(code);
        seaport.setVersion(version);
        seaport.setCreationDate(creationDate);
        seaport.setCreatedBy(createdBy);
        seaport.setModificationDate(modificationDate);
        seaport.setModifiedBy(modifiedBy);
        return seaport;
    }

    @Test
    void createSeaportGeneralDTOFromEntities() {
        SeaportGeneralDTO seaportGeneralDTO = SeaportMapper.createSeaportGeneralDTOFromEntities(seaport);

        assertEquals(seaport.getVersion(), seaportGeneralDTO.getVersion());
        assertEquals(seaport.getCity(), seaportGeneralDTO.getCity());
        assertEquals(seaport.getCode(), seaportGeneralDTO.getCode());
    }

    @Test
    void createSeaportDetailsDTOFromEntity() {
        SeaportDetailsDTO seaportDetailsDTO = SeaportMapper.createSeaportDetailsDTOFromEntity(seaport2);

        assertEquals(seaport2.getCity(), seaportDetailsDTO.getCity());
        assertEquals(seaport2.getCode(), seaportDetailsDTO.getCode());
        assertEquals(seaport2.getModificationDate(), seaportDetailsDTO.getModificationDate());
        assertEquals(seaport2.getVersion(), seaportDetailsDTO.getVersion());
        assertEquals(AccountMapper.createAccountGeneralDTOFromEntity(seaport2.getModifiedBy()).hashCode(), seaportDetailsDTO.getModifiedBy().hashCode());
        assertEquals(seaport2.getCreationDate(), seaportDetailsDTO.getCreationDate());
        assertEquals(AccountMapper.createAccountGeneralDTOFromEntity(seaport2.getCreatedBy()).hashCode(), seaportDetailsDTO.getCreatedBy().hashCode());
        assertEquals(seaport2.getVersion(), seaportDetailsDTO.getVersion());
    }
}
