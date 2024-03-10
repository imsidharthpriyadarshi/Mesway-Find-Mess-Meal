package in.mesway.Models;

public class MessModel {
    private boolean pure_veg;
    private String meal_rating,how_many_rated,meal_price,meal_time,mess_name,img_meal,mess_served_type,mess_address,mess_id,distance;

    public MessModel(boolean pure_veg, String meal_rating, String how_many_rated, String meal_price, String meal_time, String mess_name, String img_meal,String mess_served_type,String mess_address,String mess_id,String distance) {
        this.pure_veg = pure_veg;
        this.meal_rating = meal_rating;
        this.how_many_rated = how_many_rated;
        this.meal_price = meal_price;
        this.meal_time = meal_time;
        this.mess_name = mess_name;
        this.img_meal = img_meal;
        this.mess_served_type=mess_served_type;
        this.mess_address=mess_address;
        this.mess_id=mess_id;
        this.distance=distance;

    }

    public String getMess_id() {
        return mess_id;
    }

    public void setMess_id(String mess_id) {
        this.mess_id = mess_id;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getMess_served_type() {
        return mess_served_type;
    }

    public void setMess_served_type(String mess_served_type) {
        this.mess_served_type = mess_served_type;
    }

    public String getMess_address() {
        return mess_address;
    }

    public void setMess_address(String mess_address) {
        this.mess_address = mess_address;
    }

    public boolean isPure_veg() {
        return pure_veg;
    }

    public void setPure_veg(boolean pure_veg) {
        this.pure_veg = pure_veg;
    }

    public String getMeal_rating() {
        return meal_rating;
    }

    public void setMeal_rating(String meal_rating) {
        this.meal_rating = meal_rating;
    }

    public String getHow_many_rated() {
        return how_many_rated;
    }

    public void setHow_many_rated(String how_many_rated) {
        this.how_many_rated = how_many_rated;
    }

    public String getMeal_price() {
        return meal_price;
    }

    public void setMeal_price(String meal_price) {
        this.meal_price = meal_price;
    }

    public String getMeal_time() {
        return meal_time;
    }

    public void setMeal_time(String meal_time) {
        this.meal_time = meal_time;
    }

    public String getMess_name() {
        return mess_name;
    }

    public void setMess_name(String mess_name) {
        this.mess_name = mess_name;
    }

    public String getImg_meal() {
        return img_meal;
    }

    public void setImg_meal(String img_meal) {
        this.img_meal = img_meal;
    }
}
