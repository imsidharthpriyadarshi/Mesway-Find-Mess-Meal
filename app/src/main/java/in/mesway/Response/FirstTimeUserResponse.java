package in.mesway.Response;

import java.util.UUID;

public class FirstTimeUserResponse  {
    private int reg_steps;
    private UUID user_id;
    private String mobile_number;
    private String email;
    private String full_name;
    private String notification_token;

    public int getReg_steps() {
        return reg_steps;
    }

    public UUID getUser_id() {
        return user_id;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public String getEmail() {
        return email;
    }

    public String getFull_name() {
        return full_name;
    }

    public String getNotification_token() {
        return notification_token;
    }
}
