package electro.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Random;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private int id;

    private String phoneNumber;

    private String password;


    private String inviteCode;

    private int random5DiditNumber;

    @PrePersist
    public void prePersist() {
        // Random 5-digit number generation
        Random random = new Random();
        int randomNumber = random.nextInt(90000) + 10000;
        this.random5DiditNumber = randomNumber;
    }




}
