package in.mesway.fragments.home;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public interface MessCardClickInterface {
    void mealCardClick(String img_meal_address,int position, ImageView imageView, TextView mess_name, TextView mess_for, TextView mess_address, TextView mess_rating, LinearLayout linearLayout_rating,ImageView rating_star,TextView how_many_rating, String meal_items, String rating,String s_mess_address,String s_mess_for,String s_how_many_rated,String mess_id);
    void FoodCardClick(String img_meal_address,int position, String meal_items,String rating,String mess_id);


}
