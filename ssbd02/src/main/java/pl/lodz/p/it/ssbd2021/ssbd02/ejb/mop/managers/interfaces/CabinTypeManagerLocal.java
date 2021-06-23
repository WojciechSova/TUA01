package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces;

import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.CabinType;

import javax.ejb.Local;

/**
 * Lokalny interfejs managera typów kajut
 *
 * @author Wojciech Sowa
 */
@Local
public interface CabinTypeManagerLocal {

    /**
     * Metoda wyszukująca typ kajuty o danej nazwie.
     * @param name Nazwa typu kajuty
     * @return Typ kajuty {@link CabinType}
     */
    CabinType getCabinTypeByName(String name);
}
