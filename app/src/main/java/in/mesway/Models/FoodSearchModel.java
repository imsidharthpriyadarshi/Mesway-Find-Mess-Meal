package in.mesway.Models;

public class FoodSearchModel {
    private String img_food_search;
    private String mess_name,mess_address;
    private String rating,mess_id;


    public FoodSearchModel(String img_food_search, String mess_name, String mess_address, String rating, String mess_id) {
        this.img_food_search = img_food_search;
        this.mess_name = mess_name;
        this.mess_address = mess_address;
        this.rating = rating;
        this.mess_id = mess_id;

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

    public String getMess_address() {
        return mess_address;
    }

    public void setMess_address(String mess_address) {
        this.mess_address = mess_address;
    }

    public String getImg_food_search() {
        return img_food_search;
    }

    public void setImg_food_search(String img_food_search) {
        this.img_food_search = img_food_search;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
