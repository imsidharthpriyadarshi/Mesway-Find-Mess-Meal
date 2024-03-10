package in.mesway.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
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

import in.mesway.Models.YourOrderHistoryModel;

import in.mesway.R;
import in.mesway.Rusable.Reusable;
import in.mesway.Rusable.UpdateImgResponseInterface;
import in.mesway.fragments.meals.YourOrderHistoryInterface;

public class YourOrderHistoryAdapter extends RecyclerView.Adapter<YourOrderHistoryAdapter.ViewHolder> {
    private final List<YourOrderHistoryModel> yourOrderHistoryList;
    private final Context mContext;
    private final YourOrderHistoryInterface yourOrderHistoryInterface;
    private static File folder ;

    public YourOrderHistoryAdapter(List<YourOrderHistoryModel> yourOrderHistoryList, Context mContext, YourOrderHistoryInterface yourOrderHistoryInterface) {
        this.yourOrderHistoryList = yourOrderHistoryList;
        this.mContext = mContext;
        this.yourOrderHistoryInterface = yourOrderHistoryInterface;
        folder=mContext.getDir("mesway_img",Context.MODE_PRIVATE);

    }

    @NonNull
    @Override
    public YourOrderHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.model_your_order,parent,false);
        return new ViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull YourOrderHistoryAdapter.ViewHolder holder, int position) {
        YourOrderHistoryModel yourOrderHistory= yourOrderHistoryList.get(position);
        try {

            holder.loadImage(yourOrderHistory.getMess_img(),yourOrderHistory.getMess_id());
        }catch (Exception ignored){

        }

        holder.cons_meal_detail.setOnClickListener(view -> yourOrderHistoryInterface.messConsClick(holder.cons_meal_detail));

        holder.setAll(yourOrderHistory.getMess_name(),yourOrderHistory.getMess_address(),yourOrderHistory.getMeal_status(),yourOrderHistory.getMeal_date(),yourOrderHistory.getDelivery_time(),yourOrderHistory.getOrder_type());

    }

    @Override
    public int getItemCount() {
        return yourOrderHistoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView mess_name,t_order_status,mess_address,t_serving_order_type,delivery_time,delivery_date;
        private final ImageView mess_img,meal_status_side_img;
        private final Context context;
        private final ConstraintLayout cons_meal_detail;

        public ViewHolder(@NonNull View itemView,Context mContext) {
            super(itemView);
            mess_name= itemView.findViewById(R.id.t_mess_name);
            t_order_status=itemView.findViewById(R.id.t_order_status);
            mess_address= itemView.findViewById(R.id.t_mess_address);
            t_serving_order_type=itemView.findViewById(R.id.t_order_serving_type);
            delivery_date=itemView.findViewById(R.id.t_order_serving_date);
            delivery_time=itemView.findViewById(R.id.t_delivery_time_value);
            mess_img= itemView.findViewById(R.id.img_mess);
            meal_status_side_img=itemView.findViewById(R.id.img_order_status);
            context=mContext;
            cons_meal_detail= itemView.findViewById(R.id.cons_mess_detail);


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

        @SuppressLint("SetTextI18n")
        private void setAll(String s_mess_name, String s_mess_address, String s_meal_status, String meal_date, String s_delivery_time, String order_type){


            mess_name.setText(s_mess_name);
            mess_address.setText(s_mess_address);
            t_serving_order_type.setText(order_type);

            if (s_meal_status.equalsIgnoreCase("cancel")){
                t_order_status.setTextColor(ContextCompat.getColor(context,R.color.red));
                t_order_status.setText("Cancelled");
                meal_status_side_img.setVisibility(View.GONE);
            }else {
                t_order_status.setText(s_meal_status);
                meal_status_side_img.setVisibility(View.VISIBLE);

            }

            delivery_date.setText(meal_date);
            delivery_time.setText(s_delivery_time);






        }

    }
}
