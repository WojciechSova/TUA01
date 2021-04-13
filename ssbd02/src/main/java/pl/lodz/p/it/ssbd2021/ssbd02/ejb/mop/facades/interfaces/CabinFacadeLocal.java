package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractFacadeInterface;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cabin;

import javax.ejb.Local;

@Local
public interface CabinFacadeLocal extends AbstractFacadeInterface<Cabin> {

    Cabin findByNumber(String number);
}
