package electro.service;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonInclude;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {
    private String status;
    private Integer userId; // Use Integer to allow null values

    public LoginResponse(String status, Integer userId) {
        this.status = status;
        this.userId = userId;
    }


}

