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

    Seaport seaport;
    Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        seaport = createSeaport("Warszawa", "WAR", 1L, Timestamp.from(Instant.now()), account,
                account, Timestamp.from(Instant.now()));
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
import static org.junit.jupiter.api.Assertions.*;

class SeaportMapperTest {

    private Seaport seaport;
    private Account accountModifiedBy;
    private Account accountCreatedBy;

    @BeforeEach
    void setUp() {
        accountModifiedBy = new Account();
        accountModifiedBy.setLogin("ModifiedLogin");
        accountCreatedBy = new Account();
        accountCreatedBy.setLogin("CreatedLogin");
        seaport = new Seaport();
        seaport.setCity("City");
        seaport.setCity("ABC");
        seaport.setModificationDate(Timestamp.from(Instant.now()));
        seaport.setModifiedBy(accountModifiedBy);
        seaport.setCreationDate(Timestamp.valueOf("2020-03-25 11:21:15"));
        seaport.setCreatedBy(accountCreatedBy);
        seaport.setVersion(1L);
    }

    @Test
    void createSeaportDetailsDTOFromEntity() {
        SeaportDetailsDTO seaportDetailsDTO = SeaportMapper.createSeaportDetailsDTOFromEntity(seaport);

        assertEquals(seaport.getCity(), seaportDetailsDTO.getCity());
        assertEquals(seaport.getCode(), seaportDetailsDTO.getCode());
        assertEquals(seaport.getModificationDate(), seaportDetailsDTO.getModificationDate());
        assertEquals(seaport.getVersion(), seaportDetailsDTO.getVersion());
        assertEquals(AccountMapper.createAccountGeneralDTOFromEntity(seaport.getModifiedBy()).hashCode(), seaportDetailsDTO.getModifiedBy().hashCode());
        assertEquals(seaport.getCreationDate(), seaportDetailsDTO.getCreationDate());
        assertEquals(AccountMapper.createAccountGeneralDTOFromEntity(seaport.getCreatedBy()).hashCode(), seaportDetailsDTO.getCreatedBy().hashCode());
        assertEquals(seaport.getVersion(), seaportDetailsDTO.getVersion());
    }
}
