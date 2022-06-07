package tdtu.finalproject.salesmanager.Cart;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import tdtu.finalproject.salesmanager.Account.Accounts;
import tdtu.finalproject.salesmanager.History.History;
import tdtu.finalproject.salesmanager.IOFragment.Login_Fragment;
import tdtu.finalproject.salesmanager.IOFragment.Logout_Fragment;
import tdtu.finalproject.salesmanager.History.History_Fragment;
import tdtu.finalproject.salesmanager.Account.Account_Info_Fragment;
import tdtu.finalproject.salesmanager.Menu.Menu_Fragment;
import tdtu.finalproject.salesmanager.NavigationHost;
import tdtu.finalproject.salesmanager.R;

public class Cart_Fragment  extends Fragment {
    private Bundle session;
    private String Account;

    private View cartView;
    public List<String> Cart = new ArrayList<String>();

    private ProgressBar progressBar;
    private TextView notify;
    public Button add,erase,pay;
    private BottomNavigationView navbottom;

    private RecyclerView cartitems_list;
    private FirebaseFirestore firebaseFirestore;
    private History history;
    private Accounts acc;


    // Create View -- End
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = getArguments();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment with the ProductGrid theme
        cartView = inflater.inflate(R.layout.cart_fragment, container, false);
        // Cho biết session["user"] giống như php
        Account = session.getString("user");
        // Khai báo giỏ hàng hiện có
        Cart = session.getStringArrayList("CartList");
        //Others variables
        notify = cartView.findViewById(R.id.noItemNotify);
        // Button Control
        ButtonSetting();
        //Menu
        botNavSetting();
        // RecyclerView
        createRecyclerView();
        return cartView;
    }
    // Create View -- End


    // RecyclerView---Start
    public Cart_Items_Adapter cart_items_adapter;
    public void createRecyclerView(){
        cartitems_list = cartView.findViewById(R.id.cart_items_list);
        cartitems_list.setHasFixedSize(true);
        // Giới hạn item cho mỗi dòng
        cartitems_list.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false));
        // Load item từ List<String> ra thành 1 adapter
        cart_items_adapter = new Cart_Items_Adapter(cartWithoutDuplicate()); // input = List-String
        // Tạo recycle view vs adapter vừa có
        cartitems_list.setAdapter(cart_items_adapter);
        // Setup Button Enabled
        if(Cart.isEmpty()){
            erase.setEnabled(false);
            pay.setEnabled(false);
            notify.setVisibility(View.VISIBLE);
        }
        else{
            erase.setEnabled(true);
            pay.setEnabled(true);
            notify.setVisibility(View.INVISIBLE);
        }
        //Progress Bar
        progressBar = cartView.findViewById(R.id.progressBar);
        loadFirebase();
    }
    public void loadFirebase(){
        //Firebase BuyingHistory
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("BuyingHistory")
                .whereEqualTo("username",Account)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            // Get all documents in collections
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for(DocumentSnapshot d:list){
                                History hist = d.toObject(History.class);

                                history = d.toObject(History.class);
                                history.setId(d.getId());
                            }
                        }
                    }
                });
        firebaseFirestore.collection("Users")
                .whereEqualTo("username",Account)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if(!queryDocumentSnapshots.isEmpty()){
                            // Get all documents in collections
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for(DocumentSnapshot d:list){
                                Accounts accounts = d.toObject(Accounts.class);

                                acc = d.toObject(Accounts.class);
                                acc.setId(d.getId());
                            }
                        }
                    }
                });
    }
    public List<String> cartWithoutDuplicate(){
        List<String> tmpCart = new ArrayList<String>();
        List<Integer> tmpQuantity = new ArrayList<Integer>();
        for(int i = 0;i<Cart.size();i++){
            String tmpitem = Cart.get(i);
            if(tmpCart.contains(tmpitem)){
                int tmpItemPosition = tmpCart.indexOf(tmpitem);
                tmpQuantity.set(tmpItemPosition,(Integer) tmpQuantity.get(tmpItemPosition)+1);
            }else{
                tmpCart.add(tmpitem);
                tmpQuantity.add(1);
            }
        }
        // Total price -- Start
        TextView totalPrice = cartView.findViewById(R.id.sumCost);
        int sum = 0;
        for(int i = 0;i<tmpCart.size();i++){
            String tmpitem = tmpCart.get(i);
            int tmpQuan = tmpQuantity.get(i);
            String[] splititem = tmpitem.split("\\+");
            sum += ( Integer.parseInt(splititem[1]) * tmpQuan);
        }
        totalPrice.setText(sum +" VNĐ");
        // Total price -- End

        // tmpCart with count of duplicated
        for(int i = 0;i<tmpCart.size();i++){
            String tmpitem = tmpCart.get(i);
            String tmpquantity = tmpQuantity.get(i).toString();
            tmpitem = tmpitem+"+"+tmpquantity;
            tmpCart.set(i,tmpitem);
        }
        return tmpCart;
    }
    // RecyclerView---End

    // Button Control -- Start
    public void ButtonSetting(){
        add = cartView.findViewById(R.id.btnbuymore);
        erase = cartView.findViewById(R.id.btnclearcart);
        pay = cartView.findViewById(R.id.btnPay);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navWithCurrentCart(new Menu_Fragment());
            }
        });

        erase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Cart.isEmpty()) {
                    Cart = new ArrayList<String>();
                    createRecyclerView();
                }
            }
        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Cart.isEmpty()) {
                    paymentRequest();
                }
            }
        });
    }
    @SuppressLint("ResourceType")
    public void paymentRequest(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.cart_pay_alert,null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();

        Button tmpRow = dialog.findViewById(R.id.notifyRow);
        TextView tmpTotal = cartView.findViewById(R.id.sumCost);
        String tmpSum = tmpTotal.getText().toString();
        tmpRow.setText("Confirm payment of this bill?\nTotal: "+tmpSum);

        dialogView.findViewById(R.id.payment_accept)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        paymentAccept();
                        dialog.dismiss();
                    }
                });
        dialogView.findViewById(R.id.payment_deny)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
    }
    public void paymentAccept(){
        saveCart(cartWithoutDuplicate());
        Cart = new ArrayList<String>();
        createRecyclerView();
        Toast.makeText(getContext(),"Check History",Toast.LENGTH_SHORT).show();
    }
    public void saveCart(List<String> savingCart){
        String bill = createNewBill();
        progressBar.setVisibility(View.VISIBLE);
        History tmpHis = new History(acc.getUsername(),bill);

        firebaseFirestore.collection("BuyingHistory")
                .document(history.getId())
                .set(tmpHis)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }

    public String createNewBill(){
        List<String> tmpCart = cartWithoutDuplicate();
        String tmpBill = history.getBill();
        try {
            // Add New Bill
            JSONArray jsonArray_Bill = new JSONArray(history.getBill());

            JSONObject jsonObject_newBill = new JSONObject();

            JSONArray jsonArray_itemLists = new JSONArray();

            int tmpTotal = 0;
            for(int i=0;i<tmpCart.size();i++){
                String[]  tmpItem = tmpCart.get(i).split("\\+");
                JSONObject jsonObject_item = new JSONObject();
                jsonObject_item.put("item_name",tmpItem[0]);
                jsonObject_item.put("quantity",tmpItem[2]);
                jsonObject_item.put("priceperone",tmpItem[1]);

                tmpTotal += Integer.parseInt(tmpItem[2])*Integer.parseInt(tmpItem[1]);

                jsonArray_itemLists.put(jsonObject_item);
            }

            jsonObject_newBill.put("itemlist",jsonArray_itemLists);
            jsonObject_newBill.put("total",tmpTotal+"");
            jsonObject_newBill.put("pay_date",timeNow());

            jsonArray_Bill.put(jsonObject_newBill);

            tmpBill = jsonArray_Bill.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tmpBill;
    }

    public String timeNow(){
        String[] tmpCountryList = {"Vietnam","US","Japan","Korean","China","Singapore"};
        String[] timeZone = {"GMT+7","GMT-5","GMT+9","GMT+9","GMT+8","GMT+8"};
        String tmpTimeZone="";
        for (int i=0;i<tmpCountryList.length;i++){
            if(tmpCountryList[i].equals(acc.getCountry())){
                tmpTimeZone = timeZone[i];
            }
        }
        TimeZone.setDefault(TimeZone.getTimeZone(tmpTimeZone));
        String currentTime = Calendar.getInstance().getTime().toString();
        return currentTime;
    }

    // Button Control -- End

    // Bottom Item Action
    public void botNavSetting(){
        navbottom = cartView.findViewById(R.id.menubot);
        navbottom.setSelectedItemId(R.id.nav_cart);
        navbottom.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_menu:
                        navWithCurrentCart(new Menu_Fragment());
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
    public void navWithCurrentCart(Fragment cartpage){
        List<String> sendcart = Cart;
        session.putStringArrayList("CartList", (ArrayList<String>) sendcart);
        cartpage.setArguments(session);
        ((NavigationHost) getActivity()).navigateTo(cartpage, false);
    }
}
