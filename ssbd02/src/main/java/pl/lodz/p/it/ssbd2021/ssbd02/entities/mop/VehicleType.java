package pl.lodz.p.it.ssbd2021.ssbd02.entities.mop;

import lombok.*;
import lombok.AccessLevel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "Vehicle_type")
@NamedQueries({
        @NamedQuery(name = "VehicleType.findAll", query = "SELECT vt FROM VehicleType vt"),
        @NamedQuery(name = "VehicleType.findById", query = "SELECT vt FROM VehicleType vt WHERE vt.id = :id"),
        @NamedQuery(name = "VehicleType.findByVehicleTypeName", query = "SELECT vt FROM VehicleType vt WHERE vt.vehicleTypeName = :vehicleTypeName"),
        @NamedQuery(name = "VehicleType.findByRequiredSpace", query = "SELECT vt FROM VehicleType vt WHERE vt.requiredSpace = :requiredSpace")
})
@Data
@NoArgsConstructor
public class VehicleType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotNull
    @Column(name = "vehicle_type_name", nullable = false, updatable = false, length = 30)
    private String vehicleTypeName;

    @NotNull
    @Column(name = "required_space", nullable = false, updatable = false)
    private Double requiredSpace;

}
