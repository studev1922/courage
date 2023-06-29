package courage.model.entities;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;

import org.hibernate.annotations.ColumnTransformer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.ObtainVia;

@Data // @formatter:off
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "UAccount")
@SecondaryTable(name = "US_UA", 
   pkJoinColumns = @PrimaryKeyJoinColumn (
      name = "u_id", referencedColumnName = "uid"
   )
)
public class UAccount {

   public final static boolean DIVIDE = true; // divide folder to save files
   public final static String DIRECTORY = "account"; // file storage in folder

   @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long uid = -1L;
   @Column(unique = true)
   private String username;
   @Column(unique = true)
   private String email;
   @Column(updatable = false) // avoid update password
   @ColumnTransformer(write = "PWDENCRYPT(?)")
   private String password;
   private String fullname;

   // user access by UACCESS's id
   @ObtainVia
   @Column(table = "US_UA", name = "ua_id")
   private Integer access = 0;

   // user's images
   @ElementCollection(fetch = FetchType.EAGER)
   @Column(name = "image")
   @CollectionTable( name = "UIMAGE", 
      joinColumns = @JoinColumn(name = "u_id")
   )
   private Set<String> images = new HashSet<>();

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
