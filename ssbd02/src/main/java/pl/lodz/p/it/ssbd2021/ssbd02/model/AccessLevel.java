package pl.lodz.p.it.ssbd2021.ssbd02.model;

import lombok.Data;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.NamedQueries;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "access_level")
@NamedQueries({
        @NamedQuery(name = "AccessLevel.findAll", query = "SELECT l FROM AccessLevel l"),
        @NamedQuery(name = "AccessLevel.findById", query = "SELECT l FROM AccessLevel l WHERE l.id = :id"),
        @NamedQuery(name = "AccessLevel.findByVersion", query = "SELECT l FROM AccessLevel l WHERE l.version = :version"),
        @NamedQuery(name = "AccessLevel.findByLevel", query = "SELECT l FROM AccessLevel l WHERE l.level = :level"),
        @NamedQuery(name = "AccessLevel.findByAccountId", query = "SELECT l FROM AccessLevel l WHERE l.accountId = :accountId"),
        @NamedQuery(name = "AccessLevel.findByActive", query = "SELECT l FROM AccessLevel l WHERE l.active = :active"),
})
@Data
public class AccessLevel implements Serializable {

    @NotNull
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @Setter(lombok.AccessLevel.NONE)
    private Long id;

    @NotNull
    @Column(name = "version", nullable = false, updatable = true)
    private Long version;

    @NotNull
    @Column(name = "level", nullable = false, updatable = false, length = 16)
    private String level;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false, updatable = false, referencedColumnName = "id")
    private Account accountId;

    @NotNull
    @Column(name = "active", nullable = false, updatable = true)
    private boolean active;

}
