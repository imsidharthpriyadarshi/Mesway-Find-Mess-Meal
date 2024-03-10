package in.mesway.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import in.mesway.Models.LocationModels;
import in.mesway.R;

public class LocationListAdapter extends ArrayAdapter<LocationModels> {

    public LocationListAdapter(@NonNull Context context, ArrayList<LocationModels> arrayList) {
        super(context,0, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View currentItemView= convertView;
        if (currentItemView==null){
            currentItemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.model_location_search,parent,false);

        }

        LocationModels currentLocationModel=getItem(position);

        TextView main_location= currentItemView.findViewById(R.id.t_location_main);
        TextView secondary_location= currentItemView.findViewById(R.id.t_location_secondary);
        main_location.setText(currentLocationModel.getMain_location());
        secondary_location.setText(currentLocationModel.getSecondary_location());

        return currentItemView;


    }
}
