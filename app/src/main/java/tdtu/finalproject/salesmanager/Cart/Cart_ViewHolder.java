package tdtu.finalproject.salesmanager.Cart;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tdtu.finalproject.salesmanager.R;

public class Cart_ViewHolder extends RecyclerView.ViewHolder{
    public TextView item_name;
    public TextView item_price;
    public TextView item_quantity;

    // Holder cart selector
    public List<String> cartholder = new ArrayList<String>();

    public Cart_ViewHolder(@NonNull View itemView) {
        super(itemView);

        item_name = itemView.findViewById(R.id.cart_item_name);
        item_price = itemView.findViewById(R.id.cart_item_price);
        item_quantity = itemView.findViewById(R.id.cart_item_quantity);
    }
}
