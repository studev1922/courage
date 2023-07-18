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
@Entity(name = "UACCESS")
public class Access {
    @Id
    private Integer uaid;
    @Column(unique = true)
    private String uaName;

    @ElementCollection(fetch = FetchType.LAZY)
    @Column(name = "uid")
    @CollectionTable(name = "UACCOUNT", joinColumns = @JoinColumn(name = "ua_id", referencedColumnName = "uaid"))
    private Set<Long> accounts = new HashSet<>();
}
