package pl.lodz.p.it.ssbd2021.ssbd02.entities.mop;

import lombok.*;
import lombok.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.validation.VehicleRequiredSpaceValidation;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "Vehicle_type")
@NamedQueries({
        @NamedQuery(name = "VehicleType.findAll", query = "SELECT vt FROM VehicleType vt"),
        @NamedQuery(name = "VehicleType.findById", query = "SELECT vt FROM VehicleType vt WHERE vt.id = :id"),
        @NamedQuery(name = "VehicleType.findByVehicleTypeName", query = "SELECT vt FROM VehicleType vt WHERE vt.vehicleTypeName = :vehicleTypeName")
})
@Data
@NoArgsConstructor
public class VehicleType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotBlank
    @Size(max = 30, message = "City can have a maximum of 30 characters")
    @Column(name = "vehicle_type_name", nullable = false, updatable = false, length = 30)
    private String vehicleTypeName;

    @NotNull
    @VehicleRequiredSpaceValidation
    @Column(name = "required_space", nullable = false, updatable = false)
    private Double requiredSpace;

}
