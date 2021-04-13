package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractFacadeInterface;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cruise;

import javax.ejb.Local;

@Local
public interface CruiseFacadeLocal extends AbstractFacadeInterface<Cruise> {

    Cruise findByNumber(String number);
}
