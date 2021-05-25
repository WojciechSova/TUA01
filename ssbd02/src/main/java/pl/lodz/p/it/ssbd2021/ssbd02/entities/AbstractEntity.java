package pl.lodz.p.it.ssbd2021.ssbd02.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

@ToString
@MappedSuperclass
public abstract class AbstractEntity {

    public abstract Long getId();

    @NotNull
    @Column(name = "version", nullable = false, updatable = true)
    @Version
    @Getter
    @Setter
    private Long version;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.getId() != null ? this.getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (object.getClass() != this.getClass()) {
            return false;
        }
        AbstractEntity other = (AbstractEntity) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    /**
     * Metoda zwraca ciąg znaków zawierający najważniejsze informacje o encji.
     *
     * @return Łańcuch znaków, który przedstawia najważniejsze informacje o encji
     */
    public String getSummary() {
        return " Entity id: " + getId() + " version: " + getVersion();
    }
}

