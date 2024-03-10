package in.mesway.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import in.mesway.Models.AddressesModel;
import in.mesway.R;
import in.mesway.fragments.details.AddressInterface;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.Viewholder> {
    private final List<AddressesModel> addressesModelList;
    private final Context context;
    private static AddressInterface addressInterface;

    public AddressAdapter(List<AddressesModel> addressesModelList, Context context, AddressInterface addressInterface) {
        this.addressesModelList = addressesModelList;
        this.context = context;
        AddressAdapter.addressInterface = addressInterface;
    }

    @NonNull
    @Override
    public AddressAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_address, parent, false);
        //handel this error hAttempt to read from field 'androidx.recyclerview.widget.RecyclerView
        Viewholder viewholder = new Viewholder(view, context);

        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull AddressAdapter.Viewholder holder, int position) {
        AddressesModel currentAddress = addressesModelList.get(position);
        addressInterface.getAllBtnView(holder.btn_remove, holder.btn_edit, currentAddress.getLocation_id(), currentAddress.getUser_id());

        if (currentAddress.getLandmark() != null) {
            holder.setAllThings(currentAddress.getType_img(), currentAddress.getType_name(), currentAddress.getName(), currentAddress.getAddress(), currentAddress.getLandmark(), currentAddress.getState_pinCode(), currentAddress.getNumber());
        } else {
            holder.setAllThings(currentAddress.getType_img(), currentAddress.getType_name(), currentAddress.getName(), currentAddress.getAddress(), currentAddress.getState_pinCode(), currentAddress.getNumber());

        }
    }

    @Override
    public int getItemCount() {
        return addressesModelList.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {
        private final ImageView type_img;
        private final TextView name_type, name, address, landmark, state_pinCode, number;
        private final MaterialButton btn_edit, btn_remove;
        private final Context mContext;

        public Viewholder(@NonNull View itemView, Context context) {
            super(itemView);
            type_img = itemView.findViewById(R.id.img_type);
            name_type = itemView.findViewById(R.id.t_name_type);
            name = itemView.findViewById(R.id.t_name);
            address = itemView.findViewById(R.id.t_address);
            landmark = itemView.findViewById(R.id.t_landmark);
            state_pinCode = itemView.findViewById(R.id.t_pin_code);
            number = itemView.findViewById(R.id.t_phone_number);
            btn_edit = itemView.findViewById(R.id.btn_edit);
            btn_remove = itemView.findViewById(R.id.main_btn);
            mContext = context;

        }


        private void setAllThings(int i_type_img, String s_name_type, String s_name, String s_address, String s_landmark, String s_state_pinCode, String s_number) {
            type_img.setImageDrawable(ContextCompat.getDrawable(mContext, i_type_img));
            name_type.setText(s_name_type);
            name.setText(s_name);
            address.setText(s_address);
            landmark.setText(s_landmark);
            state_pinCode.setText(s_state_pinCode);
            number.setText(s_number);


        }

        private void setAllThings(int i_type_img, String s_name_type, String s_name, String s_address, String s_state_pinCode, String s_number) {
            landmark.setVisibility(View.GONE);
            type_img.setImageDrawable(ContextCompat.getDrawable(mContext, i_type_img));
            name_type.setText(s_name_type);
            name.setText(s_name);
            address.setText(s_address);
            state_pinCode.setText(s_state_pinCode);
            number.setText(s_number);


        }
    }
}
