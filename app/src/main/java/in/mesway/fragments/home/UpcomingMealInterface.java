package in.mesway.fragments.home;

import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public interface UpcomingMealInterface {
    void deliveryBoyCall(View btn_view,int position,String delivery_boy_phone_number,String delivery_boy_id);
    void mealCancel(View btn_view,int position,String subs_id,String mess_id,String upcoming_meal_type);
    void messConsClick(View cons_view,String mess_id);
    void getCodeBtnClick(ProgressBar progressBar,TextView delivery_code, String subs_id, String meal_type);
        }
