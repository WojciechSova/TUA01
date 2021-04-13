package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractFacadeInterface;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;

import javax.ejb.Local;

@Local
public interface AccountMopFacadeLocal extends AbstractFacadeInterface<Account> {

    Account findByLogin(String login);

    Account findByEmail(String email);
}
