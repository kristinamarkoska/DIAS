package mk.tradesense.tradesense.data_transfer;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
