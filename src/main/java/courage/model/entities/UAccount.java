package courage.model.entities;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import courage.model.authHandle.Authorization;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // @formatter:off
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "UAccount")
public class UAccount {

   public final static boolean DIVIDE = true; // divide folder by uid to save files
   public final static String DIRECTORY = "account"; // file storage in folder

   @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long uid = -1L;
   @Column(unique = true)
   private String username;
   @Column(unique = true)
   private String email;
   @JsonIgnore // igrore password for read api
   private String password; // ColumnTransformer
   private String fullname;
	@Column(name = "regtime", updatable = false)
   @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss.SSS")
   private Date regTime = new Date(); // register time
   @Column(name = "ua_id")
   private Integer access = 0; // default AWAITING for access

   // user's images
   @ElementCollection
   @Column(name = "image")
   @CollectionTable(name = "UIMAGE", joinColumns = @JoinColumn(name = "u_id"))
   private Set<String> images = new HashSet<>();

   // user's roles (authorization)
   @ElementCollection 
   @Column(name = "ur_id")
   @CollectionTable(name = "US_UR", joinColumns = @JoinColumn(name = "u_id"))
   private Set<Integer> roles = new HashSet<>(Arrays.asList(0));

   // user's platforms (credentials)
   @ElementCollection
   @Column(name = "up_id")
   @CollectionTable(name = "US_UP", joinColumns = @JoinColumn(name = "u_id"))
   private Set<Integer> platforms = new HashSet<>(Arrays.asList(0));

   public void setAccess(Integer access) {
      this.access = access;
   }
   
   public void setRoles(HashSet<Integer> roles) {
      this.roles = roles;
   }
   
   public void setPlatforms(HashSet<Integer> platforms) {
      this.platforms = platforms;
   }
   
   public void setAccess(Authorization.A access) {
      this.access = access.ordinal();
   }

   public void setRoles(Authorization.R ...roles) {
      this.roles.clear();
      for(Authorization.R r : roles) this.roles.add(r.ordinal());
   }
   
   public void setRoles(Authorization.P ...platforms) {
      this.roles.clear();
      for(Authorization.P p : platforms) this.roles.add(p.ordinal());
   }
   
}
