package in.mesway.Response.UpcomingMeal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import in.mesway.Response.Location.Location;
import in.mesway.Response.Location.MessDeliveryBoy;
import in.mesway.Response.Location.MessImage;
import in.mesway.Response.Location.MessLocation;
import in.mesway.Response.Location.MessTime;

public class MessBasicInfo {
    private String mess_id;
    private String mess_name;
    private String mess_phone_number;
    private String mess_email;
    private String email_verified;
    private String number_verified;
    private String mesway_verified;
    private String mess_reg_steps;
    private List<MessLocation> mess_location;
    private List<MessImage> mess_images;
    @SerializedName("mess_time")
    @Expose
    private List<MessTime> messTime;

    @SerializedName("mess_delivery_boy")
    @Expose
    private List<MessDeliveryBoy> messDeliveryBoy;

    public List<MessTime> getMessTime() {
        return messTime;
    }

    public void setMessTime(List<MessTime> messTime) {
        this.messTime = messTime;
    }

    public List<MessDeliveryBoy> getMessDeliveryBoy() {
        return messDeliveryBoy;
    }

    public void setMessDeliveryBoy(List<MessDeliveryBoy> messDeliveryBoy) {
        this.messDeliveryBoy = messDeliveryBoy;
    }

    public String getMess_id() {
        return mess_id;
    }

    public void setMess_id(String mess_id) {
        this.mess_id = mess_id;
    }

    public String getMess_name() {
        return mess_name;
    }

    public void setMess_name(String mess_name) {
        this.mess_name = mess_name;
    }

    public String getMess_phone_number() {
        return mess_phone_number;
    }

    public void setMess_phone_number(String mess_phone_number) {
        this.mess_phone_number = mess_phone_number;
    }

    public String getMess_email() {
        return mess_email;
    }

    public void setMess_email(String mess_email) {
        this.mess_email = mess_email;
    }

    public String getEmail_verified() {
        return email_verified;
    }

    public void setEmail_verified(String email_verified) {
        this.email_verified = email_verified;
    }

    public String getNumber_verified() {
        return number_verified;
    }

    public void setNumber_verified(String number_verified) {
        this.number_verified = number_verified;
    }

    public String getMesway_verified() {
        return mesway_verified;
    }

    public void setMesway_verified(String mesway_verified) {
        this.mesway_verified = mesway_verified;
    }

    public String getMess_reg_steps() {
        return mess_reg_steps;
    }

    public void setMess_reg_steps(String mess_reg_steps) {
        this.mess_reg_steps = mess_reg_steps;
    }

    public List<MessLocation> getMess_location() {
        return mess_location;
    }

    public void setMess_location(List<MessLocation> mess_location) {
        this.mess_location = mess_location;
    }

    public List<MessImage> getMess_images() {
        return mess_images;
    }

    public void setMess_images(List<MessImage> mess_images) {
        this.mess_images = mess_images;
    }
}
