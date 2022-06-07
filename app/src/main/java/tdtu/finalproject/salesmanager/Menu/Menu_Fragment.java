package tdtu.finalproject.salesmanager.Menu;
import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import tdtu.finalproject.salesmanager.Account.Account_Info_Fragment;
import tdtu.finalproject.salesmanager.Cart.Cart_Fragment;
import tdtu.finalproject.salesmanager.History.History_Fragment;
import tdtu.finalproject.salesmanager.IOFragment.Logout_Fragment;
import tdtu.finalproject.salesmanager.NavigationHost;
import tdtu.finalproject.salesmanager.R;

import java.util.ArrayList;
import java.util.List;

public class Menu_Fragment extends Fragment {
    private String Account;
    private Bundle session;
    private View menuview;

    private ProgressBar progressBar;
    private BottomNavigationView navtop,navbottom;

    private List<Menu_Items> listItems;
    private RecyclerView recyclerView;
    private Menu_ItemsAdapter adapter;
    private FirebaseFirestore firebaseFirestore;

    public List<String> Cart = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = getArguments();
    }
    // Create view
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment with the ProductGrid theme
        menuview = inflater.inflate(R.layout.menu_fragment, container, false);
        // Cho biết session["user"] giống như php
        Account = session.getString("user");
        // Khai báo giỏ hàng hiện có
        Cart = session.getStringArrayList("CartList");
        // Declare other Views
        progressBar = menuview.findViewById(R.id.loading_progress);
        // RecyclerView
        createRecyclerView("all");
        //Menu
        topNavSetting();
        botNavSetting();
        return menuview;
    }

    //Top Menu
    public void topNavSetting(){
        navtop = menuview.findViewById(R.id.menutop);
        navtop.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_all:
                        addToCart(Cart);
                        createRecyclerView("all");
                        break;
                    case R.id.nav_food:
                        addToCart(Cart);
                        createRecyclerView("food");
                        break;
                    case R.id.nav_drink:
                        addToCart(Cart);
                        createRecyclerView("drink");
                        break;
                }
                // Để hiện chữ cho menu, báo vị trí vừa click
                item.setChecked(true);
                return false;
            }
        });
        navtop.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    default:
                        break;
                }
            }
        });

    }

    // Setup Recycler view -- Start
    @SuppressLint("ClickableViewAccessibility")
    public void createRecyclerView(String type){
        recyclerView = menuview.findViewById(R.id.list_menu_item);
        recyclerView.setHasFixedSize(true);
        // Giới hạn item cho mỗi dòng
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false));

        // Tạo Adapter & RecyclerView
        listItems = new ArrayList<>();
        adapter = new Menu_ItemsAdapter(listItems,getContext());
        recyclerView.setAdapter(adapter);

        //FirebaseFireStore
        firebaseFirestore = FirebaseFirestore.getInstance();
        switch (type){
            case "all":
                progressBar.setVisibility(View.VISIBLE);
                getFoodList();
                getDrinkList();
                break;
            case "food":
                progressBar.setVisibility(View.VISIBLE);
                getFoodList();
                break;
            case "drink":
                progressBar.setVisibility(View.VISIBLE);
                getDrinkList();
                break;
        }
    }
    public void getFoodList(){
        firebaseFirestore.collection("Menu").orderBy("price").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        progressBar.setVisibility(View.GONE);
                        if(!queryDocumentSnapshots.isEmpty()){
                            // Get all documents in collections
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for(DocumentSnapshot d:list){
                                Menu_Items menu_items = d.toObject(Menu_Items.class);
                                if(menu_items.getType().equals("f")){
                                    listItems.add(menu_items);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }
    public void getDrinkList(){
        firebaseFirestore.collection("Menu").orderBy("price").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        progressBar.setVisibility(View.GONE);
                        if(!queryDocumentSnapshots.isEmpty()){
                            // Get all documents in collections
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for(DocumentSnapshot d:list){
                                Menu_Items menu_items = d.toObject(Menu_Items.class);
                                if(menu_items.getType().equals("d")){
                                    listItems.add(menu_items);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }
    // Setup Recycler view -- End


    // Đưa đồ vô giỏ
    @SuppressLint("ClickableViewAccessibility")
    public List<String> addToCart(List<String> cart){
        List<String> tmpCart = adapter.getCart();
        for(int i = 0; i<tmpCart.size();i++){
            String tmpitem = tmpCart.get(i).toString();
            cart.add(tmpitem);
        }
        return cart;
    }

    //Bottom Menu
    public void botNavSetting(){
        navbottom = menuview.findViewById(R.id.menubot);
        navbottom.setSelectedItemId(R.id.nav_menu);
        navbottom.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_cart:
                        navWithCurrentCart(new Cart_Fragment());
                        break;
                    case R.id.nav_profile:
                        navWithCurrentCart(new Account_Info_Fragment());
                        break;
                    case R.id.nav_history:
                        navWithCurrentCart(new History_Fragment());
                        break;
                    case R.id.nav_logout:
                        navWithCurrentCart(new Logout_Fragment());
                        break;
                }
                // Để hiện chữ cho menu, báo vị trí vừa click
                item.setChecked(true);
                return false;
            }
        });

        navbottom.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    default:
                        break;
                }
            }
        });
    }

    // NavigateTo with cart changes
    public void navWithCurrentCart(Fragment cartpage){
        List<String> sendcart = addToCart(Cart);
        // Thay đổi giá trị giỏ hàng sau khi add thêm item
        session.putStringArrayList("CartList", (ArrayList<String>) sendcart);
        cartpage.setArguments(session);
        ((NavigationHost) getActivity()).navigateTo(cartpage, false);
    }
}
