package courage.model.entities;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.ObtainVia;

// @formatter:off
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SecondaryTable(name = "US_UA", 
   pkJoinColumns = @PrimaryKeyJoinColumn(
      name = "u_id", referencedColumnName = "uid"
   )
)
public class UAccount {

   @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long uid;
   private String username;
   private String password;
   private String fullname;
   private String email;

   // user access by UACCESS's id
   @ObtainVia
   @Column(table = "US_UA", name = "ua_id")
   private Integer access = 0;

   // user's images
   @ObtainVia 
   @ElementCollection
   @Column(name = "image")
   @CollectionTable( name = "UIMAGE", 
      joinColumns = @JoinColumn(name = "u_id")
   )
   private Set<String> images = new HashSet<>(Arrays.asList("default.png"));

   // user's roles (authorization)
   @ObtainVia
   @ElementCollection 
   @Column(name = "ur_id")
   @CollectionTable( name = "US_UR", 
      joinColumns = @JoinColumn(name = "u_id")
   )
   private Set<Integer> roles = new HashSet<>(Arrays.asList(0));

   // user's platforms (credentials)
   @ObtainVia
   @ElementCollection
   @Column(name = "up_id")
   @CollectionTable( name = "US_UP", 
      joinColumns = @JoinColumn(name = "u_id")
   ) private Set<Integer> platforms = new HashSet<>(Arrays.asList(0));
}
