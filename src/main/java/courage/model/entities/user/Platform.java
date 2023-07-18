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
@Entity(name = "UPLATFORM")
public class Platform {
    @Id
    private Integer upid;
    @Column(unique = true)
    private String upName;
    private String upOther;

    @ElementCollection(fetch = FetchType.LAZY)
    @Column(name = "u_id")
    @CollectionTable(name = "US_UP", joinColumns = @JoinColumn(name = "up_id", referencedColumnName = "upid"))
    private Set<Long> accounts = new HashSet<>();
}
