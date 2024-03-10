package in.mesway.Response;

import java.util.UUID;

public class LoginSignupResponse {
    private UUID user_id;
    private String mobile_number;

    public UUID getUser_id() {
        return user_id;
    }

    public String getMobile_number() {
        return mobile_number;
    }
}
