package tdtu.finalproject.salesmanager.Z_MenuItemJsonDemo;

import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import tdtu.finalproject.salesmanager.R;

// 1 Item
public class Menu_ViewHolder extends RecyclerView.ViewHolder{

    public ImageView fdImage;
    public TextView fdName;
    public TextView fdPrice;

    // Holder cart selector
    public List<String> cartholder = new ArrayList<String>();

    public Menu_ViewHolder(@NonNull View itemView) {
        super(itemView);
        fdImage = itemView.findViewById(R.id.fd_image);
        fdName = itemView.findViewById(R.id.fd_name);
        fdPrice = itemView.findViewById(R.id.fd_price);

        itemView.findViewById(R.id.addtoCart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = fdName.getText().toString();
                String price = fdPrice.getText().toString();
                cartholder.add(name+"+"+price);
//                Log.d("TEST  HOLDER",cartholder.toString());
            }
        });
    }
}
