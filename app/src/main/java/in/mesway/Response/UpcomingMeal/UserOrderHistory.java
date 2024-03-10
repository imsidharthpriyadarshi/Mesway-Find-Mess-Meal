package in.mesway.Response.UpcomingMeal;

public class UserOrderHistory {
    private String order_id;

    private String mess_id;

    private String user_id;

    private String subs_id;

    private  String delivery_time;

    private String delivery_date;

    private String meal_type;

    private String status;

    private MessBasicInfo mess_basic_info;

    public MessBasicInfo getMess_basic_info() {
        return mess_basic_info;
    }

    public void setMess_basic_info(MessBasicInfo mess_basic_info) {
        this.mess_basic_info = mess_basic_info;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getMess_id() {
        return mess_id;
    }

    public void setMess_id(String mess_id) {
        this.mess_id = mess_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSubs_id() {
        return subs_id;
    }

    public void setSubs_id(String subs_id) {
        this.subs_id = subs_id;
    }

    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getMeal_type() {
        return meal_type;
    }

    public void setMeal_type(String meal_type) {
        this.meal_type = meal_type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UserOrderHistory{" +
                "order_id='" + order_id + '\'' +
                ", mess_id='" + mess_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", subs_id='" + subs_id + '\'' +
                ", delivery_time='" + delivery_time + '\'' +
                ", delivery_date='" + delivery_date + '\'' +
                ", meal_type='" + meal_type + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

}
