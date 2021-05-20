package pl.lodz.p.it.ssbd2021.ssbd02.entities.mop;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "cabin_type")
@NamedQueries({
        @NamedQuery(name = "CabinType.findAll", query = "SELECT ct FROM CabinType ct"),
        @NamedQuery(name = "CabinType.findById", query = "SELECT ct FROM CabinType ct WHERE ct.id = :id"),
        @NamedQuery(name = "CabinType.findByCabinTypeName", query = "SELECT ct FROM CabinType ct WHERE ct.cabinTypeName = :cabinTypeName")
})
@Data
@NoArgsConstructor
public class CabinType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotBlank
    @Size(max = 30, message = "Cabin type name can have a maximum of 30 characters")
    @Column(name = "cabin_type_name", nullable = false, updatable = false, length = 30)
    private String cabinTypeName;

}
