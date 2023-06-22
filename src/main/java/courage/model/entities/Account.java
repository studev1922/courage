package courage.model.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Account {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long uid;
   private String username;
   private String password;
   private String fullname;
   private String email;
}
