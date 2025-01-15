package mk.tradesense.tradesense.data_transfer;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
}
