package in.mesway.Models;

public class YourOrderHistoryModel {

    private String mess_img,mess_name,mess_address,meal_status,meal_date,delivery_time,order_type,mess_id;

    public YourOrderHistoryModel(String mess_img, String mess_name, String mess_address, String meal_status, String meal_date, String delivery_time,String order_type,String mess_id) {
        this.mess_img = mess_img;
        this.mess_name = mess_name;
        this.mess_address = mess_address;
        this.meal_status = meal_status;
        this.meal_date = meal_date;
        this.delivery_time = delivery_time;
        this.order_type=order_type;
        this.mess_id=mess_id;
    }

    public String getMess_id() {
        return mess_id;
    }

    public void setMess_id(String mess_id) {
        this.mess_id = mess_id;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getMess_img() {
        return mess_img;
    }

    public void setMess_img(String mess_img) {
        this.mess_img = mess_img;
    }

    public String getMess_name() {
        return mess_name;
    }

    public void setMess_name(String mess_name) {
        this.mess_name = mess_name;
    }

    public String getMess_address() {
        return mess_address;
    }

    public void setMess_address(String mess_address) {
        this.mess_address = mess_address;
    }

    public String getMeal_status() {
        return meal_status;
    }

    public void setMeal_status(String meal_status) {
        this.meal_status = meal_status;
    }

    public String getMeal_date() {
        return meal_date;
    }

    public void setMeal_date(String meal_date) {
        this.meal_date = meal_date;
    }

    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }
}
