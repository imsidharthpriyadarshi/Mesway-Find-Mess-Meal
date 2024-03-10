package in.mesway.Response;

import java.util.UUID;

public class OtpResponse {
    private String access_token;
    private String token_type;
    private int reg_steps;
    private UUID user_id;
    private String mobile_no;
    private String email;

    public String getAccess_token() {
        return access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public int getReg_steps() {
        return reg_steps;
    }

    public UUID getUser_id() {
        return user_id;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public String getEmail() {
        return email;
    }
}
