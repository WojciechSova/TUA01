package pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mok.AccountDetailsDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mok.AccountGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class AccountMapperTest {

    Account account;
    Account accountModifiedBy;
    Account accountCreatedBy;
    AccessLevel accessLevel1;
    AccessLevel accessLevel2;
    AccessLevel accessLevel3;
    AccountDetailsDTO accountDetailsDTO;
    AccountGeneralDTO accountGeneralDTO;

    @BeforeEach
    void setUp() {
        accountModifiedBy = new Account();
        accountModifiedBy.setLogin("ModifiedLogin");
        accountCreatedBy = new Account();
        accountCreatedBy.setLogin("CreatedLogin");
        account = createAccount();
        accessLevel1 = createAccessLevel("ADMIN", account, Timestamp.from(Instant.now()), accountModifiedBy,
                Timestamp.valueOf("2020-03-25 11:21:15"), accountCreatedBy);
        accessLevel2 = createAccessLevel("EMPLOYEE", account, Timestamp.from(Instant.now()), accountModifiedBy,
                Timestamp.valueOf("2020-03-25 12:21:15"), accountCreatedBy);
        accessLevel3 = createAccessLevel("CLIENT", account, Timestamp.from(Instant.now()), accountModifiedBy,
                Timestamp.valueOf("2020-03-25 13:21:15"), accountCreatedBy);
        accountDetailsDTO = createAccountDetailsDTO();
        accountGeneralDTO = createAccountGeneralDTO();
    }

    private Account createAccount() {
        Account acc = new Account();
        acc.setLogin("ExampleLogin");
        acc.setPassword("P@ssword");
        acc.setActive(true);
        acc.setConfirmed(true);
        acc.setFirstName("Annabelle");
        acc.setLastName("Washington");
        acc.setEmail("example@example.com");
        acc.setPhoneNumber("48123456788");
        acc.setLanguage("PL");
        acc.setTimeZone("PL");
        acc.setModificationDate(Timestamp.from(Instant.now()));
        acc.setModifiedBy(accountModifiedBy);
        acc.setCreationDate(Timestamp.valueOf("2020-03-21 11:21:15"));
        acc.setLastKnownGoodLogin(Timestamp.valueOf("2020-03-25 11:21:15"));
        acc.setLastKnownGoodLoginIp("111.111.111.111");
        acc.setLastKnownBadLogin(Timestamp.valueOf("2020-03-26 11:21:15"));
        acc.setLastKnownBadLoginIp("222.222.222.222");
        acc.setNumberOfBadLogins(2);
        return acc;
    }

    private AccountDetailsDTO createAccountDetailsDTO() {
        AccountDetailsDTO acc = new AccountDetailsDTO();
        acc.setLogin("ExampleLogin");
        acc.setPassword("P@ssword");
        acc.setFirstName("Annabelle");
        acc.setLastName("Washington");
        acc.setEmail("example@example.com");
        acc.setPhoneNumber("48123456789");
        acc.setLanguage("PL");
        acc.setTimeZone("PL");
        return acc;
    }

    private AccountGeneralDTO createAccountGeneralDTO() {
        AccountGeneralDTO acc = new AccountGeneralDTO();
        acc.setLogin("ExampleLogin");
        acc.setFirstName("Annabelle");
        acc.setLastName("Washington");
        return acc;
    }

    private AccessLevel createAccessLevel(String level, Account account, Timestamp modificationDate, Account modifiedBy,
                                          Timestamp creationDate, Account createdBy) {
        AccessLevel al = new AccessLevel();
        al.setVersion(1L);
        al.setLevel(level);
        al.setAccount(account);
        al.setModificationDate(modificationDate);
        al.setModifiedBy(modifiedBy);
        al.setCreationDate(creationDate);
        al.setCreatedBy(createdBy);
        return al;
    }

    @Test
    void createAccountGeneralDTOFromEntities() {
        AccountGeneralDTO accGenDTO = AccountMapper.createAccountGeneralDTOFromEntities(Pair.of(account,
                Arrays.asList(accessLevel1, accessLevel2, accessLevel3)));

        assertEquals(account.getVersion(), accGenDTO.getVersion());
        assertEquals(account.getLogin(), accGenDTO.getLogin());
        assertEquals(account.getActive(), accGenDTO.getActive());
        assertEquals(account.getFirstName(), accGenDTO.getFirstName());
        assertEquals(account.getLastName(), accGenDTO.getLastName());
        assertEquals(Arrays.asList("ADMIN", "EMPLOYEE", "CLIENT"), accGenDTO.getAccessLevel());
    }

    @Test
    void createAccountGeneralDTOFromEntity() {
        AccountGeneralDTO accGenDTO = AccountMapper.createAccountGeneralDTOFromEntity(account);

        assertEquals(account.getVersion(), accGenDTO.getVersion());
        assertEquals(account.getLogin(), accGenDTO.getLogin());
        assertEquals(account.getActive(), accGenDTO.getActive());
        assertEquals(account.getFirstName(), accGenDTO.getFirstName());
        assertEquals(account.getLastName(), accGenDTO.getLastName());
        assertNull(accGenDTO.getAccessLevel());
    }

    @Test
    void createAccountDetailsDTOFromEntities() {
        AccountDetailsDTO accDetDTO = AccountMapper.createAccountDetailsDTOFromEntities(Pair.of(account,
                Collections.singletonList(accessLevel3)));

        assertEquals(account.getVersion(), accDetDTO.getVersion());
        assertEquals(account.getLogin(), accDetDTO.getLogin());
        assertEquals(account.getPassword(), accDetDTO.getPassword());
        assertEquals(account.getActive(), accDetDTO.getActive());
        assertEquals(account.getConfirmed(), accDetDTO.getConfirmed());
        assertEquals(account.getFirstName(), accDetDTO.getFirstName());
        assertEquals(account.getLastName(), accDetDTO.getLastName());
        assertEquals(account.getEmail(), accDetDTO.getEmail());
        assertEquals(account.getPhoneNumber(), accDetDTO.getPhoneNumber());
        assertEquals(Collections.singletonList(AccessLevelMapper.createAccessLevelDTOFromEntity(accessLevel3)).hashCode(),
                accDetDTO.getAccessLevel().hashCode());
        assertEquals(account.getLanguage(), accDetDTO.getLanguage());
        assertEquals(account.getTimeZone(), accDetDTO.getTimeZone());
        assertEquals(account.getModificationDate(), accDetDTO.getModificationDate());
        assertEquals(AccountMapper.createAccountGeneralDTOFromEntity(account.getModifiedBy()), accDetDTO.getModifiedBy());
        assertEquals(account.getCreationDate(), accDetDTO.getCreationDate());
        assertEquals(account.getLastKnownGoodLogin(), accDetDTO.getLastKnownGoodLogin());
        assertEquals(account.getLastKnownGoodLoginIp(), accDetDTO.getLastKnownGoodLoginIp());
        assertEquals(account.getLastKnownBadLogin(), accDetDTO.getLastKnownBadLogin());
        assertEquals(account.getLastKnownBadLoginIp(), accDetDTO.getLastKnownBadLoginIp());
        assertEquals(account.getNumberOfBadLogins(), accDetDTO.getNumberOfBadLogins());
    }

    @Test
    void createAccountFromAccountDetailsDTO() {
        Account mappedAcc = AccountMapper.createAccountFromAccountDetailsDTO(accountDetailsDTO);

        assertEquals(accountDetailsDTO.getVersion(), mappedAcc.getVersion());
        assertEquals(accountDetailsDTO.getLogin(), mappedAcc.getLogin());
        assertEquals(accountDetailsDTO.getPassword(), mappedAcc.getPassword());
        assertEquals(accountDetailsDTO.getFirstName(), mappedAcc.getFirstName());
        assertEquals(accountDetailsDTO.getLastName(), mappedAcc.getLastName());
        assertEquals(accountDetailsDTO.getEmail(), mappedAcc.getEmail());
        assertEquals(accountDetailsDTO.getLanguage(), mappedAcc.getLanguage());
        assertEquals(accountDetailsDTO.getTimeZone(), mappedAcc.getTimeZone());
        assertEquals(accountDetailsDTO.getPhoneNumber(), mappedAcc.getPhoneNumber());
    }

    @Test
    void createAccountFromAccountGeneralDTO() {
        Account mappedAcc = AccountMapper.createAccountFromAccountGeneralDTO(accountGeneralDTO);

        assertEquals(accountGeneralDTO.getVersion(), mappedAcc.getVersion());
        assertEquals(accountGeneralDTO.getLogin(), mappedAcc.getLogin());
        assertEquals(accountGeneralDTO.getFirstName(), mappedAcc.getFirstName());
        assertEquals(accountGeneralDTO.getLastName(), mappedAcc.getLastName());
    }

    @Test
    void createAccountDetailsDTOWhenModifiedByIsNull() {
        account.setModifiedBy(null);
        AccountDetailsDTO accDetDTO = AccountMapper.createAccountDetailsDTOFromEntities(Pair.of(account,
                Collections.singletonList(accessLevel3)));
        assertNull(accDetDTO.getModifiedBy());
    }

    @Test
    void createAccountDetailsDTOWhenAccessLevelsIsEmpty() {
        AccountDetailsDTO accDetDTO = AccountMapper.createAccountDetailsDTOFromEntities(Pair.of(account,
                Collections.emptyList()));
        assertEquals(Collections.emptyList(), accDetDTO.getAccessLevel());
    }

    @Test
    void createAccountGeneralDTOWhenAccessLevelsIsEmpty() {
        AccountGeneralDTO accGenDTO = AccountMapper.createAccountGeneralDTOFromEntities(Pair.of(account,
                Collections.emptyList()));
        assertEquals(Collections.emptyList(), accGenDTO.getAccessLevel());
    }
}
