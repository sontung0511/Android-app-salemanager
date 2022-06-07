package tdtu.finalproject.salesmanager.Menu;

import android.content.Context;

import android.net.Uri;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import tdtu.finalproject.salesmanager.R;

import java.util.ArrayList;
import java.util.List;



public class Menu_ItemsAdapter extends RecyclerView.Adapter<Menu_ItemsAdapter.Menu_ItemsViewHolder> {
    private List<Menu_Items> menulist;
    private StorageReference mStore;
    private Context context;

    public Menu_ItemsAdapter(List<Menu_Items> menuList,Context context) {
        this.menulist = menuList;
        this.context = context;
    }

    @NonNull
    @Override
    public Menu_ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Menu_ItemsViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item_card,parent,false)
        );
    }


    public List<Menu_ItemsViewHolder> viewHolders_list = new ArrayList<>();

    @Override
    public void onBindViewHolder(@NonNull Menu_ItemsViewHolder holder, int position) {
        Menu_Items item = this.menulist.get(position);

        holder.itemName.setText(item.getName());
        holder.itemPrice.setText(item.getPrice()+"");

        String name = item.getName().toLowerCase().replaceAll("\\s","");
        String path = "menu/"+name;

        mStore = FirebaseStorage.getInstance().getReference().child(path+".jpg");
        mStore.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                    public void onSuccess(Uri uri) {
                        //Load image using Picasso
                        Picasso.with(holder.itemIMG.getContext())
                                .load(uri)
                                .fit()
                                .transform(new CropCircleTransformation())
                                .into(holder.itemIMG);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(holder.itemIMG.getContext(),"Add "+name+" image: Failed",Toast.LENGTH_SHORT).show();
                    }
                });

        viewHolders_list.add(holder);
    }

    @Override
    public int getItemCount() {
        if(this.menulist.isEmpty()){
            return 0;
        }else{
            return this.menulist.size();
        }
    }

    public List<String> cart = new ArrayList<String>();

    public List<String> getCart() {
        for(int i = 0 ; i< viewHolders_list.size();i++) {
            List<String> tmp = viewHolders_list.get(i).cartholder;
            for(int j=0; j<tmp.size();j++){
                cart.add(tmp.get(j).toString());
            }
        }
        return cart;
    }

    class Menu_ItemsViewHolder extends RecyclerView.ViewHolder{
        TextView itemName,itemPrice;
        ImageView itemIMG;
        ImageButton addItem;

        // Holder cart selector
        public List<String> cartholder = new ArrayList<String>();

        public Menu_ItemsViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.fd_name);
            itemPrice = itemView.findViewById(R.id.fd_price);
            itemIMG = itemView.findViewById(R.id.fd_image);
            addItem = itemView.findViewById(R.id.addtoCart);

            addItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = itemName.getText().toString();
                    String price = itemPrice.getText().toString();
                    cartholder.add(name+"+"+price);
                }
            });
        }
    }
}


