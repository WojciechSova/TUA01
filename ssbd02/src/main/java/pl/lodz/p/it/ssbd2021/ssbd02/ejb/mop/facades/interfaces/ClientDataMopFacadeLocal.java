package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces;

import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.ClientData;

import javax.ejb.Local;

@Local
public interface ClientDataMopFacadeLocal {

    ClientData findByLogin(String login);
}
