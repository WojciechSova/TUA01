package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades.interfaces;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractFacadeInterface;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.AccessLevel;

import javax.ejb.Local;
import java.util.List;

@Local
public interface AccessLevelFacadeLocal extends AbstractFacadeInterface<AccessLevel> {

    List<AccessLevel> findByLogin(String login);
}
