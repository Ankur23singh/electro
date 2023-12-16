package electro.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AddBank {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int accountId;
    private String mobileNumber;
    private String holderName;
    private String bankAccountNumber;
    private String bankIFSC;
    private String userId;

}
