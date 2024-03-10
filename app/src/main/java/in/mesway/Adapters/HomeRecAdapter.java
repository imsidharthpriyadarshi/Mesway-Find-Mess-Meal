package in.mesway.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.card.MaterialCardView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import in.mesway.Models.MessModel;
import in.mesway.R;
import in.mesway.Rusable.Reusable;
import in.mesway.Rusable.UpdateImgResponseInterface;
import in.mesway.fragments.home.MessCardClickInterface;

public class HomeRecAdapter extends RecyclerView.Adapter<HomeRecAdapter.HomeRecViewHolder> {

    private final List<MessModel> messModelList;
    private final Context context;
    private final MessCardClickInterface mealCardClickInterface;
    private final List<MessModel> tempMessModelList;
    private static File folder;

    public HomeRecAdapter(List<MessModel> messModelList, Context context, MessCardClickInterface mealCardClickInterface) {
        this.messModelList = messModelList;
        this.context = context;
        this.tempMessModelList = new ArrayList<>(messModelList);
        this.mealCardClickInterface = mealCardClickInterface;
        folder = context.getDir("mesway_img", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public HomeRecAdapter.HomeRecViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_home_rec, parent, false);
        return new HomeRecViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeRecAdapter.HomeRecViewHolder holder, @SuppressLint("RecyclerView") int position) {
        MessModel meal = messModelList.get(position);
        holder.root_card.setOnClickListener(view -> mealCardClickInterface.mealCardClick(meal.getImg_meal(),position, holder.img_meal, holder.mess_name, holder.mess_for, holder.mess_address, holder.meal_rating, holder.rating_ln, holder.img_rating_star, holder.how_many_rated, meal.getMess_name(), meal.getMeal_rating(), meal.getMess_address(), meal.getMess_served_type(), meal.getHow_many_rated(), meal.getMess_id()));
        holder.setMessName(meal.getMess_name());
        holder.setMealRating(meal.getMeal_rating());
        holder.setHowManyRated(meal.getHow_many_rated());
        holder.setMealPrice(meal.getMeal_price());
        holder.setMealTime(meal.getMeal_time());
        holder.setPureVeg(meal.isPure_veg());
        holder.setDistance(meal.getDistance());
        holder.setMess_for(meal.getMess_served_type());
        holder.setMess_address(meal.getMess_address());


        holder.loadImage(meal.getImg_meal(), meal.getMess_id());

    }

    @Override
    public int getItemCount() {
        return messModelList.size();
    }

    public static class HomeRecViewHolder extends RecyclerView.ViewHolder {
        private final ImageView img_meal;
        private final ImageView img_rating_star;
        private final TextView mess_name, meal_rating, meal_price, how_many_rated, meal_time, mess_for, mess_address, distance;
        private final LinearLayout pure_veg, rating_ln;
        private final Context contexts;
        private final MaterialCardView root_card;


        public HomeRecViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            img_meal = itemView.findViewById(R.id.img_meal);
            mess_name = itemView.findViewById(R.id.t_mess_name);
            meal_rating = itemView.findViewById(R.id.t_meal_rating);
            meal_price = itemView.findViewById(R.id.t_starts_from_price);
            how_many_rated = itemView.findViewById(R.id.t_how_many_rated);
            meal_time = itemView.findViewById(R.id.t_time);
            mess_for = itemView.findViewById(R.id.t_provided_meal_type);
            mess_address = itemView.findViewById(R.id.t_mess_address);
            pure_veg = itemView.findViewById(R.id.ln_pure_veg);
            distance = itemView.findViewById(R.id.txt_distance_km);
            rating_ln = itemView.findViewById(R.id.ln_meal_rating);
            root_card = itemView.findViewById(R.id.meal_card);
            img_rating_star = itemView.findViewById(R.id.img_rating_star);
            contexts = context;

        }


        private void loadImage(String img_str, String mess_id) {
            File file = new File(folder, mess_id);
            if (file.exists()) {
                Bitmap mess_bitmap = BitmapFactory.decodeFile(file.getPath());
                img_meal.setImageBitmap(mess_bitmap);

            } else {

                Glide.with(contexts)
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
                                            Toast.makeText(contexts, "Slow internet connection" , Toast.LENGTH_SHORT).show();


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

                                    Toast.makeText(contexts, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }


                                return false;
                            }
                        })
                        .into(img_meal);

            }
        }

     /*   private Bitmap convertIntoBitmap() {
            try {
                BitmapDrawable drawable = (BitmapDrawable) img_meal.getDrawable();
                return drawable.getBitmap();
            } catch (Exception e) {
                Toast.makeText(contexts, "Wait slow internet", Toast.LENGTH_SHORT).show();
                return null;
            }


        }*/

        private void setMessName(String name) {
            mess_name.setText(name);

        }

        private void setDistance(String mDistance) {
            distance.setText(mDistance);
        }

        private void setMealRating(String mealRating) {
            meal_rating.setText(mealRating);

        }


        private void setMess_for(String messFor) {
            mess_for.setText(messFor);

        }

        private void setMess_address(String messAddress) {
            mess_address.setText(messAddress);

        }

        private void setHowManyRated(String howManyRated) {
            how_many_rated.setText(howManyRated);

        }

        private void setMealPrice(String mealPrice) {
            meal_price.setText(mealPrice);

        }

        private void setMealTime(String mealTime) {
            meal_time.setText(mealTime);

        }

        private void setPureVeg(boolean is_veg) {
            if (is_veg) {
                pure_veg.setVisibility(View.VISIBLE);
            } else {
                pure_veg.setVisibility(View.INVISIBLE);

            }
        }

    }

    public Filter getFilter() {
        return filter;
    }

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<MessModel> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(tempMessModelList);
            } else {
                String filterPattern = charSequence.toString().toLowerCase();
                for (MessModel item : tempMessModelList) {
                    if (filterPattern.equals("veg")) {
                        if (item.isPure_veg()) {
                            filteredList.add(item);
                        }
                    }

                    if (filterPattern.equals("nonveg")){
                        if (!item.isPure_veg()) {
                            filteredList.add(item);
                        }

                    }

                    if (filterPattern.equals("4.0")){

                        if (Double.parseDouble(item.getMeal_rating())>4){
                            filteredList.add(item);
                        }

                    }
                }
                if (filteredList.isEmpty()){
                    Toast.makeText(context, "Sorry, No Mess found", Toast.LENGTH_SHORT).show();

                }
            }
            FilterResults filterResults= new FilterResults();
            filterResults.values=filteredList;

            return filterResults;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            messModelList.clear();
            messModelList.addAll((Collection<? extends MessModel>) filterResults.values);
            notifyDataSetChanged();
        }
    };


}
