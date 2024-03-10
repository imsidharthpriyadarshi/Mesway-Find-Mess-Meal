package in.mesway.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import in.mesway.Models.FoodSearchModel;
import in.mesway.R;
import in.mesway.Rusable.Reusable;
import in.mesway.Rusable.UpdateImgResponseInterface;
import in.mesway.fragments.home.MessCardClickInterface;

public class FoodSearchAdapter extends RecyclerView.Adapter<FoodSearchAdapter.FoodSearchViewholder> {
    private final List<FoodSearchModel> foodList;
    private final MessCardClickInterface mealCardClickInterface;
    private final Context mContext;
    private final List<FoodSearchModel> tempFoodList;
    private static File folder ;


    public FoodSearchAdapter(List<FoodSearchModel> foodList, Context mContext, MessCardClickInterface mealCardClickInterface) {
        this.foodList = foodList;
        this.mContext = mContext;
        tempFoodList= new ArrayList<>(foodList);
        this.mealCardClickInterface=mealCardClickInterface;
        folder=mContext.getDir("mesway_img",Context.MODE_PRIVATE);

    }

    @NonNull
    @Override
    public FoodSearchAdapter.FoodSearchViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.model_mess_search,parent,false);
        FoodSearchViewholder viewHolder = new FoodSearchViewholder(view,mContext);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FoodSearchAdapter.FoodSearchViewholder holder, @SuppressLint("RecyclerView") int position) {
        FoodSearchModel currentFood= foodList.get(position);
        try {
            holder.loadImage(currentFood.getImg_food_search(),currentFood.getMess_id());
        }catch (Exception ignored){


        }
        holder.setT_mess_name(currentFood.getMess_name());
        holder.setMealRating(currentFood.getRating());
        holder.setT_mess_address(currentFood.getMess_address());

        holder.cons_food_item.setOnClickListener(view -> mealCardClickInterface.FoodCardClick(currentFood.getImg_food_search(),position,currentFood.getMess_name(),currentFood.getRating(),currentFood.getMess_id()));

    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class FoodSearchViewholder extends RecyclerView.ViewHolder{
        private final ImageView img_food;
        private final TextView t_mess_name,t_food_rating,t_mess_address;
        private final Context context;
        private final ConstraintLayout cons_food_item;
        public FoodSearchViewholder(@NonNull View itemView,Context mContext) {
            super(itemView);

            img_food=itemView.findViewById(R.id.img_meal);
            t_mess_name=itemView.findViewById(R.id.t_mess_name);
            t_mess_address=itemView.findViewById(R.id.t_mess_address);

            t_food_rating=itemView.findViewById(R.id.t_meal_rating);

            context= mContext;
            cons_food_item=itemView.findViewById(R.id.cons_food_item);

        }

        private void loadImage(String img_str,String mess_id) {
            File file = new File(folder,mess_id);
            if (file.exists()){
                Bitmap mess_bitmap= BitmapFactory.decodeFile(file.getPath());
                img_food.setImageBitmap(mess_bitmap);

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
                        .into(img_food);

            }
        }


        private void setT_mess_name(String messName) {
            t_mess_name.setText(messName);

        }

        private void setT_mess_address(String messAddress) {
            t_mess_address.setText(messAddress);

        }


        private void setMealRating(String mealRating) {
            t_food_rating.setText(mealRating);

        }
    }

    public  Filter getFilter() {
        return filter;
    }
private  final Filter filter = new Filter() {
    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        List<FoodSearchModel> filteredList = new ArrayList<>();
        if (charSequence == null || charSequence.length()==0){

            filteredList.addAll(tempFoodList);

        }else {
            String filterPattern = charSequence.toString().toLowerCase().trim();
            for ( FoodSearchModel item:tempFoodList)  {
                if (item.getMess_name().toLowerCase().contains(filterPattern)){
                    filteredList.add(item);

                }

            }


        }

        FilterResults filterResults = new FilterResults();
        filterResults.values =filteredList;
        return filterResults;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        foodList.clear();
        foodList.addAll((Collection<? extends FoodSearchModel>) filterResults.values);
        notifyDataSetChanged();
    }
};


}
