package courage.model.dto;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
public class UserLogin {

    private String unique;
    private String username;
    private String password;

    public void setUnique(String unique) {
        this.username = this.unique = unique;
    }

    public void setUsername(String username) {
        this.unique = this.username = username;
    }
}
