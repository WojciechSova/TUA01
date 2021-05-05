package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.managers.interfaces;

public interface SystemManagerLocal {

    /**
     * Metoda umożliwiająca automatyczne usunięcie kont, które nie zostały w określonym czasie zweryfikowane.
     * Metoda jest wywoływana automatycznie co pewien okres czasu.
     * Obie wartości czasowe podane są w pliku system.properties.
     */
    void removeUnconfirmedAccounts();
}
