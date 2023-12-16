package electro.model.userDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {

    private String phoneNumber;

    private String password;


    private String inviteCode;
}