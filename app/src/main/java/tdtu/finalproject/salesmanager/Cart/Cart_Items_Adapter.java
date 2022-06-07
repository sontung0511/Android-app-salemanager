package tdtu.finalproject.salesmanager.Cart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tdtu.finalproject.salesmanager.R;

public class Cart_Items_Adapter extends RecyclerView.Adapter<Cart_ViewHolder>{
    private List<String> items_List;

    public Cart_Items_Adapter(List<String> items_List) {
        this.items_List = items_List;
    }

    @NonNull
    @Override
    public Cart_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_card, parent, false);
        return new Cart_ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull Cart_ViewHolder holder, int position) {
        if (items_List != null && position < items_List.size()) {
            String tmpname = items_List.get(position);
            String tmpsplit[] = tmpname.split("\\+");
            holder.item_name.setText(tmpsplit[0]);
            holder.item_price.setText(tmpsplit[1]);
            holder.item_quantity.setText(tmpsplit[2]);
        }
    }

    @Override
    public int getItemCount() {
        return items_List.size();
    }
}
