package tdtu.finalproject.salesmanager.Z_MenuItemJsonDemo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import tdtu.finalproject.salesmanager.R;

public class Menu_Items_Adapter extends RecyclerView.Adapter<Menu_ViewHolder> {
    private List<Menu_Loader> fdList;
    Menu_Items_Adapter(List<Menu_Loader> fdList) {
        this.fdList = fdList;
    }

    @NonNull
    @Override
    public Menu_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item_card, parent, false);
        return new Menu_ViewHolder(layoutView);
    }

    // List chứa các viewholder.  Sau khi binding 1 viewholder, + viewholder đó vào để sử dụng lại phía sau
    public List<Menu_ViewHolder> viewHolders_list = new ArrayList<Menu_ViewHolder>();

    @Override
    public void onBindViewHolder(@NonNull Menu_ViewHolder holder, int position) {
        // Đưa dữ liệu vào các view tương ứng
        if (fdList != null && position < fdList.size()) {
            Menu_Loader fd = fdList.get(position);
            holder.fdName.setText(fd.name);
            holder.fdPrice.setText(fd.price);
//            holder.fdImage
            // + holder vào viewHolders_list
            viewHolders_list.add(holder);
        }
    }

    @Override
    public int getItemCount() {
        return fdList.size();
    }

    public List<String> cart = new ArrayList<String>();

    public List<String> getCart() {
        for(int i = 0 ; i< viewHolders_list.size();i++) {
            List<String> tmp = viewHolders_list.get(i).cartholder;
            for(int j=0; j<tmp.size();j++){
                cart.add(tmp.get(j).toString());
            }
//            Log.d("TEST  Adapter",cart.toString());
        }
        return cart;
    }
}
