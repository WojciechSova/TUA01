package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.managers.interfaces;

/**
 * Lokalny interfejs managera systemu
 *
 * @author Artur Madaj
 */
public interface SystemManagerLocal {

    /**
     * Metoda umożliwiająca automatyczne usunięcie kont, które nie zostały w pewnym,
     * określonym w pliku system.properties czasie zweryfikowane.
     * Metoda jest wywoływana automatycznie co godzinę.
     */
    void removeUnconfirmedAccounts();
}
