package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades.interfaces;

import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;

import javax.ejb.Local;

@Local
public interface AccountFacadeLocal {

    Account findByLogin(String login);

    Account findByEmail(String email);
}
