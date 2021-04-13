package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractFacadeInterface;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Ferry;

import javax.ejb.Local;

@Local
public interface FerryFacadeLocal extends AbstractFacadeInterface<Ferry> {

    Ferry findByName(String name);
}
