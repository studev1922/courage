package courage.model.entities.user;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "UROLES")
public class Role {
    @Id
    private Integer urid;
    @Column(unique = true)
    private String role;

    @ElementCollection(fetch = FetchType.LAZY)
    @Column(name = "u_id")
    @CollectionTable(name = "US_UR", joinColumns = @JoinColumn(name = "ur_id", referencedColumnName = "urid"))
    private Set<Long> accounts = new HashSet<>();
}