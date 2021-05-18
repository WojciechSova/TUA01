package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces;

import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Seaport;

import javax.ejb.Local;
import java.util.List;

/**
 * Lokalny interfejs managera portów
 *
 * @author Wojciech Sowa
 */
@Local
public interface SeaportManagerLocal {

    List<Seaport> getAllSeaports();

    Seaport getSeaportByCode(String code);

    void createSeaport(Seaport seaport);

    void updateSeaport(Seaport seaport, String modifiedBy);

    void removeSeaport(Seaport seaport);
}
