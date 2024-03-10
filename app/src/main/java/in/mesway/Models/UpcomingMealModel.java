package in.mesway.Models;

public class UpcomingMealModel {
    private String mess_img,mess_name,mess_address,upcoming_meal_type,delivery_status,delivery_boy_name,delivery_time_range,meal_cancel_max_time,delivery_mobile_number,mess_id,delivery_boy_id,subs_id;

    private String plan_payable_price;
    private int delivered_meal;
    public UpcomingMealModel(String mess_img, String mess_name, String mess_address, String upcoming_meal_type, String delivery_status, String delivery_boy_name, String delivery_time_range, String meal_cancel_max_time, String delivery_mobile_number, String mess_id, String delivery_boy_id, String subs_id,String plan_payable_price,int delivered_meal) {
        this.mess_img = mess_img;
        this.mess_name = mess_name;
        this.mess_address = mess_address;
        this.upcoming_meal_type = upcoming_meal_type;
        this.delivery_status = delivery_status;
        this.delivery_boy_name = delivery_boy_name;
        this.delivery_time_range = delivery_time_range;
        this.meal_cancel_max_time = meal_cancel_max_time;
        this.delivery_mobile_number = delivery_mobile_number;
        this.mess_id = mess_id;
        this.delivery_boy_id = delivery_boy_id;
        this.subs_id = subs_id;
        this.plan_payable_price=plan_payable_price;
        this.delivered_meal=delivered_meal;
    }


    public int getDelivered_meal() {
        return delivered_meal;
    }

    public void setDelivered_meal(int delivered_meal) {
        this.delivered_meal = delivered_meal;
    }

    public String getPlan_payable_price() {
        return plan_payable_price;
    }

    public void setPlan_payable_price(String plan_payable_price) {
        this.plan_payable_price = plan_payable_price;
    }

    public String getMess_id() {
        return mess_id;
    }

    public void setMess_id(String mess_id) {
        this.mess_id = mess_id;
    }

    public String getDelivery_boy_id() {
        return delivery_boy_id;
    }

    public void setDelivery_boy_id(String delivery_boy_id) {
        this.delivery_boy_id = delivery_boy_id;
    }

    public String getSubs_id() {
        return subs_id;
    }

    public void setSubs_id(String subs_id) {
        this.subs_id = subs_id;
    }

    public String getDelivery_mobile_number() {
        return delivery_mobile_number;
    }

    public void setDelivery_mobile_number(String delivery_mobile_number) {
        this.delivery_mobile_number = delivery_mobile_number;
    }

    public String getMeal_cancel_max_time() {
        return "You can cancel "+upcoming_meal_type+" till "+meal_cancel_max_time;
    }

    public void setMeal_cancel_max_time(String meal_cancel_max_time) {
        this.meal_cancel_max_time = meal_cancel_max_time;
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

    public String getUpcoming_meal_type() {
        return upcoming_meal_type;
    }

    public void setUpcoming_meal_type(String upcoming_meal_type) {
        this.upcoming_meal_type = upcoming_meal_type;
    }

    public String getDelivery_status() {
        return delivery_status;
    }

    public void setDelivery_status(String delivery_status) {
        this.delivery_status = delivery_status;
    }

    public String getDelivery_boy_name() {
        return delivery_boy_name;
    }

    public void setDelivery_boy_name(String delivery_boy_name) {
        this.delivery_boy_name = delivery_boy_name;
    }

    public String getDelivery_time_range() {
        return delivery_time_range;
    }

    public void setDelivery_time_range(String delivery_time_range) {
        this.delivery_time_range = delivery_time_range;
    }
}
