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
import java.util.Objects;

import in.mesway.Models.SubscriptionModel;
import in.mesway.R;
import in.mesway.Rusable.Reusable;
import in.mesway.Rusable.UpdateImgResponseInterface;
import in.mesway.fragments.meals.SubscriptionInterface;

public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.ViewHolder> {
    private final List<SubscriptionModel> subscriptionList;
    private final Context mContext;
    private final SubscriptionInterface subscriptionInterface;
    private static File folder;

    public SubscriptionAdapter(List<SubscriptionModel> subscriptionList, Context mContext, SubscriptionInterface subscriptionInterface) {
        this.subscriptionList = subscriptionList;
        this.mContext = mContext;
        this.subscriptionInterface = subscriptionInterface;
        folder = mContext.getDir("mesway_img", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public SubscriptionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.model_subscription_order, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, mContext);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SubscriptionAdapter.ViewHolder holder, int position) {

        SubscriptionModel subscription = subscriptionList.get(position);
        try {
            holder.loadImage(subscription.getMess_img(), subscription.getMess_id());
        } catch (Exception ignored) {

            subscriptionInterface.otherViewOperations(holder.cond_security_deposit, holder.plan_payment_status, holder.security_deposit_status);
        }
        holder.con_mess_detail.setOnClickListener(view -> subscriptionInterface.messConsClick(holder.con_mess_detail));
        holder.setAll(subscription.getMess_name(), subscription.getMess_address(), subscription.getRejected_reason_value(), subscription.getSubscription_status(), subscription.getPlan_name(), subscription.getPlan_type(), subscription.getStarting_meal(), subscription.getPlan_price(), subscription.getStart_from(), subscription.getValid_upto(), subscription.getPayment_status(), subscription.getTotal_payable_amount(), subscription.getSecurity_deposit_status(), subscription.getSecurity_deposit_amount());


    }

    @Override
    public int getItemCount() {
        return subscriptionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mess_name, subscription_status, mess_address, txt_reason, rejected_reason_value, subscription_for_time, subscription_plan_type, subscription_starting_meal, subscription_plan_price, start_from, valid_upto;
        private final ImageView mess_img, meal_status_side_img;
        private final ConstraintLayout con_mess_detail, cond_security_deposit;
        private final Context context;

        private TextView plan_payment_status, total_payable_amount, security_deposit_status, security_deposit_money_value;

        public ViewHolder(@NonNull View itemView, Context mContext) {
            super(itemView);
            mess_name = itemView.findViewById(R.id.t_mess_name);
            subscription_status = itemView.findViewById(R.id.subscription_status);
            mess_address = itemView.findViewById(R.id.t_mess_address);
            rejected_reason_value = itemView.findViewById(R.id.txt_rejected_reason_value);
            txt_reason = itemView.findViewById(R.id.txt_reason);
            subscription_plan_type = itemView.findViewById(R.id.t_plan_type_value);
            subscription_for_time = itemView.findViewById(R.id.subscription_plan_for);
            subscription_starting_meal = itemView.findViewById(R.id.t_starting_meal_value);
            mess_img = itemView.findViewById(R.id.img_mess);
            meal_status_side_img = itemView.findViewById(R.id.img_subscription_status);
            subscription_plan_price = itemView.findViewById(R.id.t_plan_price_value);
            start_from = itemView.findViewById(R.id.t_start_from);
            valid_upto = itemView.findViewById(R.id.t_expired);
            context = mContext;
            con_mess_detail = itemView.findViewById(R.id.cons_mess_detail);
            cond_security_deposit = itemView.findViewById(R.id.cond_security_deposit);


            plan_payment_status = itemView.findViewById(R.id.t_payment_status_value);
            total_payable_amount = itemView.findViewById(R.id.t_payable_amount_value);
            security_deposit_status = itemView.findViewById(R.id.t_refundable_green);
            security_deposit_money_value = itemView.findViewById(R.id.t_security_price_amount);


        }

        private void loadImage(String img_str, String mess_id) {
            File file = new File(folder, mess_id);
            if (file.exists()) {
                Bitmap mess_bitmap = BitmapFactory.decodeFile(file.getPath());
                mess_img.setImageBitmap(mess_bitmap);

            } else {

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
                                            Toast.makeText(context, "Slow internet connection" , Toast.LENGTH_SHORT).show();


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
        private void setAll(String s_mess_name, String s_mess_address, String s_rejected_reason_value, String s_subscription_status, String s_plan_name, String s_plan_type, String s_starting_meal, String s_plan_price, String s_start_from, String s_valid_upto, String t_payment_status_value, String t_payable_amount_value, String t_refundable_green, String t_security_price_amount) {

            if (t_refundable_green.equals("Refunded")){
                security_deposit_status.setTextColor(ContextCompat.getColor(context,R.color.green));

            }

            if (Objects.equals(t_security_price_amount, "\u20B9 " + 0)){
                cond_security_deposit.setVisibility(View.GONE);
            }

            if (!t_payment_status_value.equals("Not Paid")){
                plan_payment_status.setTextColor(ContextCompat.getColor(context,R.color.green));

            }
            mess_name.setText(s_mess_name);
            mess_address.setText(s_mess_address);
            plan_payment_status.setText(t_payment_status_value);
            total_payable_amount.setText(t_payable_amount_value);
            security_deposit_status.setText(t_refundable_green);
            security_deposit_money_value.setText(t_security_price_amount);
            if (s_subscription_status.equalsIgnoreCase("expired")) {
                subscription_status.setTextColor(ContextCompat.getColor(context, R.color.lunch));
                subscription_status.setText("Expired");
                meal_status_side_img.setVisibility(View.GONE);
            } else if (s_subscription_status.equalsIgnoreCase("pending")) {
                subscription_status.setTextColor(ContextCompat.getColor(context, R.color.grey));
                subscription_status.setText("Pending");
                meal_status_side_img.setVisibility(View.GONE);

            } else if (s_subscription_status.equalsIgnoreCase("rejected")) {
                subscription_status.setTextColor(ContextCompat.getColor(context, R.color.red));
                subscription_status.setText("Rejected");
                meal_status_side_img.setVisibility(View.VISIBLE);
                if (!s_rejected_reason_value.equalsIgnoreCase("none")) {
                    txt_reason.setVisibility(View.VISIBLE);
                    rejected_reason_value.setVisibility(View.VISIBLE);
                    rejected_reason_value.setText(s_rejected_reason_value);
                }

            } else {
                subscription_status.setText("Active");
                meal_status_side_img.setVisibility(View.VISIBLE);

            }

            subscription_plan_type.setText(s_plan_type);
            subscription_plan_price.setText(s_plan_price);
            subscription_for_time.setText(s_plan_name);
            subscription_starting_meal.setText(s_starting_meal);
            start_from.setText(s_start_from);
            valid_upto.setText(s_valid_upto);


        }

    }
}
