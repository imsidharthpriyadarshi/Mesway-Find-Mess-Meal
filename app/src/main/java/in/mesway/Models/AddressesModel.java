package in.mesway.Models;

public class AddressesModel {

    private int type_img;
    private String type_name,name,address,landmark,state_pinCode,number,location_id,user_id;

    public AddressesModel(int type_img, String type_name, String name, String address, String landmark, String state_pinCode, String number,String location_id, String user_id) {
        this.type_img = type_img;
        this.type_name = type_name;
        this.name = name;
        this.address = address;
        this.landmark = landmark;
        this.state_pinCode = state_pinCode;
        this.number = number;
        this.location_id=location_id;
        this.user_id = user_id;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getType_img() {
        return type_img;
    }

    public void setType_img(int type_img) {
        this.type_img = type_img;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getState_pinCode() {
        return state_pinCode;
    }

    public void setState_pinCode(String state_pinCode) {
        this.state_pinCode = state_pinCode;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
