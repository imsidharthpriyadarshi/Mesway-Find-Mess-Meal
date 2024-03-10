package in.mesway.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import in.mesway.Models.UpcomingMealModel;
import in.mesway.R;
import in.mesway.Rusable.Reusable;
import in.mesway.Rusable.UpdateImgResponseInterface;
import in.mesway.fragments.home.UpcomingMealInterface;

public class UpcomingMealAdapter extends RecyclerView.Adapter<UpcomingMealAdapter.ViewHolder> {
    private final List<UpcomingMealModel> upcomingMealList;
    private final Context mContext;
    private final UpcomingMealInterface upcomingMealInterface;
    private static File folder ;

    public UpcomingMealAdapter(List<UpcomingMealModel> upcomingMealList, Context mContext,UpcomingMealInterface upcomingMealInterface) {
        this.upcomingMealList = upcomingMealList;
        this.mContext = mContext;
        this.upcomingMealInterface=upcomingMealInterface;
        folder=mContext.getDir("mesway_img",Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public UpcomingMealAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(mContext).inflate(R.layout.model_upcoming_orders,parent,false);
        return new ViewHolder(view,mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingMealAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        UpcomingMealModel upcomingMeal=upcomingMealList.get(position);
        try {

            holder.loadImage(upcomingMeal.getMess_img(),upcomingMeal.getMess_id());
        }catch (Exception ignored){}
        holder.upcoming_meal_cancel_btn.setOnClickListener(view -> upcomingMealInterface.mealCancel(holder.upcoming_meal_cancel_btn,position,upcomingMeal.getSubs_id(),upcomingMeal.getMess_id(),upcomingMeal.getUpcoming_meal_type()));
       holder.delivery_call_btn.setOnClickListener(view -> upcomingMealInterface.deliveryBoyCall(holder.delivery_call_btn,position,upcomingMeal.getDelivery_mobile_number(),upcomingMeal.getDelivery_boy_id()));

       holder.cons_mess_detail.setOnClickListener(view -> upcomingMealInterface.messConsClick(holder.cons_mess_detail,upcomingMeal.getMess_id()));

       holder.btn_get_code.setOnClickListener(view -> upcomingMealInterface.getCodeBtnClick(holder.progressBar,holder.txt_delivery_code_value,upcomingMeal.getSubs_id(),upcomingMeal.getUpcoming_meal_type()));
       
       holder.setAll(upcomingMeal.getMess_name(),upcomingMeal.getMess_address(),upcomingMeal.getUpcoming_meal_type(),upcomingMeal.getDelivery_status(),upcomingMeal.getDelivery_boy_name(),upcomingMeal.getDelivery_time_range(),upcomingMeal.getMeal_cancel_max_time(),upcomingMeal.getDelivered_meal(),upcomingMeal.getPlan_payable_price());
    }

    @Override
    public int getItemCount() {
        return upcomingMealList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView mess_name,upcoming_meal_status,mess_address,upcoming_meal_type,delivery_boy_name,delivery_time_range,meal_cancel_max_time;
        private final ImageView mess_img,meal_status_side_img;
        private final Button delivery_call_btn,upcoming_meal_cancel_btn;
        private final Context context;


        private final ConstraintLayout cons_mess_detail;

        private final ProgressBar progressBar;
        private final TextView txt_delivery_code,txt_delivery_code_value;
        private final Button btn_get_code;

        private ConstraintLayout cons_pay_today;
        private TextView payable_price_value;
        public ViewHolder(@NonNull View itemView,Context mContext) {
            super(itemView);

            mess_name= itemView.findViewById(R.id.t_mess_name);
            upcoming_meal_status=itemView.findViewById(R.id.t_upcoming_meal_status);
            mess_address= itemView.findViewById(R.id.t_mess_address);
            upcoming_meal_type=itemView.findViewById(R.id.t_upcoming_meal_type);
            delivery_boy_name=itemView.findViewById(R.id.t_delivery_boy_name);
            delivery_time_range=itemView.findViewById(R.id.t_delivery_time_range);
            meal_cancel_max_time=itemView.findViewById(R.id.t_meal_cancel_max_time);
            mess_img= itemView.findViewById(R.id.img_mess_meal);
            meal_status_side_img=itemView.findViewById(R.id.img_upcoming_meal_status);
            delivery_call_btn=itemView.findViewById(R.id.btn_call_delivery);
            upcoming_meal_cancel_btn=itemView.findViewById(R.id.btn_cancel_today);
            context=mContext;
            cons_mess_detail= itemView.findViewById(R.id.cons_mess_detail);
            txt_delivery_code=itemView.findViewById(R.id.txt_order_code);
            txt_delivery_code_value=itemView.findViewById(R.id.txt_order_code_value);
            btn_get_code=itemView.findViewById(R.id.btn_get_code);
            progressBar=itemView.findViewById(R.id.get_code_progressBar);

            cons_pay_today=itemView.findViewById(R.id.cons_pay_today);
            payable_price_value=itemView.findViewById(R.id.t_plan_price_value);


        }

        private void loadImage(String img_str,String mess_id) {
            File file = new File(folder,mess_id);
            if (file.exists()){
                Bitmap mess_bitmap= BitmapFactory.decodeFile(file.getPath());
                mess_img.setImageBitmap(mess_bitmap);

            }else {

                Glide.with(context)
                        .asBitmap()
                        .load(img_str)
                        .timeout(600000).listener(new RequestListener<Bitmap>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                UpdateImgResponseInterface updateImgResponseInterface= new UpdateImgResponseInterface() {
                                    @Override
                                    public void getResponse(String url, Integer response_code) {
                                        if (response_code==200){
                                            loadImage(url,mess_id);
                                        }else {
                                            Toast.makeText(context, "Check your internet connection" , Toast.LENGTH_SHORT).show();


                                        }

                                    }
                                };


                                Reusable.update_aws_img_url("poster",mess_id,updateImgResponseInterface);

                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {

                                File file = new File(folder, mess_id);

                                try {
                                    FileOutputStream fos = new FileOutputStream(file);
                                    resource.compress(Bitmap.CompressFormat.JPEG, 100, fos);


                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();

                                    Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }



                                return false;
                            }
                        })
                        .into(mess_img);

            }
        }


        private void setAll(String s_mess_name, String s_mess_address, String s_upcoming_meal_type, String s_delivery_status, String s_delivery_boy_name, String s_delivery_time_range, String s_meal_cancel_max_time,int delivered_meal,String plan_payable_price){


            if (delivered_meal==0){
                payable_price_value.setText(plan_payable_price);
                cons_pay_today.setVisibility(View.VISIBLE);
            }else {

                cons_pay_today.setVisibility(View.GONE);
            }
            
            mess_name.setText(s_mess_name);
            mess_address.setText(s_mess_address);
            upcoming_meal_type.setText(s_upcoming_meal_type);
            
            if (s_delivery_status.equalsIgnoreCase("cancelled")){
                upcoming_meal_status.setTextColor(ContextCompat.getColor(context,R.color.red));
                upcoming_meal_status.setText(s_delivery_status);
                meal_status_side_img.setVisibility(View.GONE);
                delivery_call_btn.setEnabled(false);
                btn_get_code.setEnabled(false);
                txt_delivery_code_value.setTextColor(ContextCompat.getColor(context,R.color.grey));
                txt_delivery_code.setTextColor(ContextCompat.getColor(context,R.color.grey));
                upcoming_meal_cancel_btn.setEnabled(false);
                upcoming_meal_cancel_btn.setTextColor(ContextCompat.getColor(context, R.color.card_background));

            }else if (s_delivery_status.equalsIgnoreCase("upcoming")){
                upcoming_meal_status.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
                upcoming_meal_status.setText(s_delivery_status);
                meal_status_side_img.setVisibility(View.GONE);
                delivery_call_btn.setEnabled(true);
                upcoming_meal_cancel_btn.setEnabled(true);

            }else {
                upcoming_meal_status.setText(s_delivery_status);
                meal_status_side_img.setVisibility(View.VISIBLE);
                delivery_call_btn.setEnabled(false);
                upcoming_meal_cancel_btn.setEnabled(false);
                upcoming_meal_cancel_btn.setTextColor(ContextCompat.getColor(context, R.color.card_background));
                btn_get_code.setEnabled(false);
                txt_delivery_code_value.setTextColor(ContextCompat.getColor(context,R.color.grey));
                txt_delivery_code.setTextColor(ContextCompat.getColor(context,R.color.grey));

            }
            
            delivery_boy_name.setText(s_delivery_boy_name);
            delivery_time_range.setText(s_delivery_time_range);
            meal_cancel_max_time.setText(s_meal_cancel_max_time);
            
            
            
            

        }



    }


}
