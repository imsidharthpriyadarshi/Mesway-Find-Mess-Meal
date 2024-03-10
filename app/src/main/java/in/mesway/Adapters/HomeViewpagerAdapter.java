package in.mesway.Adapters;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.bumptech.glide.Glide;
import java.util.List;
import android.os.Handler;
import in.mesway.Models.SliderItems;
import in.mesway.R;

public class HomeViewpagerAdapter extends RecyclerView.Adapter<HomeViewpagerAdapter.HomeViewpagerViewHolder> {
    private final List<SliderItems> sliderItems;
    private final Context context;
    private final ViewPager2 viewPager2;




    public HomeViewpagerAdapter(List<SliderItems> sliderItems,Context context,ViewPager2 viewPager2) {
        this.sliderItems = sliderItems;
        this.context=context;
        this.viewPager2=viewPager2;
    }

    @NonNull
    @Override
    public HomeViewpagerAdapter.HomeViewpagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.model_home_viewpager,parent,false);
        HomeViewpagerViewHolder viewholder = new HomeViewpagerViewHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewpagerAdapter.HomeViewpagerViewHolder holder, int position) {
        SliderItems sliderItem = sliderItems.get(position);

        Handler handler= new Handler(Looper.getMainLooper());
        final int delay = 9000;


      //  holder.imageView.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.photo_viewpager));
        handler.postDelayed(new Runnable(){
            public void run(){
                if (viewPager2.getCurrentItem()==sliderItems.size()-1) {
                    viewPager2.post(runnable);
                    handler.postDelayed(this, delay);

                }
            }
        }, delay);


    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    public static class HomeViewpagerViewHolder extends RecyclerView.ViewHolder{
        private final ImageView imageView;

        public HomeViewpagerViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_slider);

        }
    }

    private final Runnable runnable = new Runnable() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void run() {
            viewPager2.setCurrentItem(0,true);
            notifyDataSetChanged();
        }
    };
}
