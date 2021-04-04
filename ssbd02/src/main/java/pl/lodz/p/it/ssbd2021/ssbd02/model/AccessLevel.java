package pl.lodz.p.it.ssbd2021.ssbd02.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "level", discriminatorType = DiscriminatorType.STRING)
@Table(name = "Access_level")
@NamedQueries({
        @NamedQuery(name = "AccessLevel.findAll", query = "SELECT l FROM AccessLevel l"),
        @NamedQuery(name = "AccessLevel.findById", query = "SELECT l FROM AccessLevel l WHERE l.id = :id"),
        @NamedQuery(name = "AccessLevel.findByVersion", query = "SELECT l FROM AccessLevel l WHERE l.version = :version"),
        @NamedQuery(name = "AccessLevel.findByLevel", query = "SELECT l FROM AccessLevel l WHERE l.level = :level"),
        @NamedQuery(name = "AccessLevel.findByAccountId", query = "SELECT l FROM AccessLevel l WHERE l.accountId = :accountId"),
        @NamedQuery(name = "AccessLevel.findByActive", query = "SELECT l FROM AccessLevel l WHERE l.active = :active"),
        @NamedQuery(name = "AccessLevel.findByModificationDate", query = "SELECT l FROM AccessLevel l WHERE l.modificationDate = :modificationDate"),
        @NamedQuery(name = "AccessLevel.findByModifiedBy", query = "SELECT l FROM AccessLevel l WHERE l.modifiedBy = :modifiedBy"),
        @NamedQuery(name = "AccessLevel.findByCreationDate", query = "SELECT l FROM AccessLevel l WHERE creationDate = :creationDate")
})
@Data
@NoArgsConstructor
public class AccessLevel implements Serializable {

    @NotNull
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @Setter(lombok.AccessLevel.NONE)
    private Long id;

    @NotNull
    @Version
    @Setter(lombok.AccessLevel.NONE)
    @Getter(lombok.AccessLevel.NONE)
    @Column(name = "version", nullable = false, updatable = true)
    private Long version;

    @NotNull
    @Column(name = "level", nullable = false, updatable = false, length = 16)
    private String level;

    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "account_id", nullable = false, updatable = false, referencedColumnName = "id")
    private Account accountId;

    @NotNull
    @Column(name = "active", nullable = false, updatable = true)
    private boolean active;

    @Column(name = "modification_date", nullable = true, updatable = true)
    private Timestamp modificationDate;

    @ManyToOne
    @JoinColumn(name = "modified_by", nullable = true, updatable = true, referencedColumnName = "id")
    private Account modifiedBy;

    @NotNull
    @Column(name = "creation_date", nullable = false, updatable = false)
    private Timestamp creationDate;

}
