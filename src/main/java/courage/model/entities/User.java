package courage.model.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface User {

   // @formatter:off
   @Data @Entity(name = "UPLATFORM")
   @NoArgsConstructor @AllArgsConstructor
   class Platform {
      @Id private Integer upid;
      private String upName;
      private String upOther;

      @ElementCollection @Column(name = "u_id")
      @CollectionTable(
         name="US_UP",
         joinColumns = @JoinColumn( name="up_id", referencedColumnName = "upid")
      )
      private Set<Long> accounts = new HashSet<>();
   }

   @Data @Entity(name = "UACCESS")
   @NoArgsConstructor @AllArgsConstructor
   class Access {
      @Id private Integer uaid;
      private String uaName;
      
      @ElementCollection @Column(name = "u_id")
      @CollectionTable(
         name="US_UA",
         joinColumns = @JoinColumn( name="ua_id", referencedColumnName = "uaid")
      )
      private Set<Long> accounts = new HashSet<>();
   }

   @Data @Entity(name = "UROLES")
   @NoArgsConstructor @AllArgsConstructor
   class Role {
      @Id private Integer urid;
      private String role;
      
      @ElementCollection @Column(name = "u_id")
      @CollectionTable(
         name="US_UR",
         joinColumns = @JoinColumn( name="ur_id", referencedColumnName = "urid")
      )
      private Set<Long> accounts = new HashSet<>();
   }
   // @formatter:on
}
