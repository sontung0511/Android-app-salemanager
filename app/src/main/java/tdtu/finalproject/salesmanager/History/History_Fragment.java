package tdtu.finalproject.salesmanager.History;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import java.util.List;

import tdtu.finalproject.salesmanager.Account.Account_Info_Fragment;
import tdtu.finalproject.salesmanager.Cart.Cart_Fragment;
import tdtu.finalproject.salesmanager.IOFragment.Logout_Fragment;
import tdtu.finalproject.salesmanager.Menu.Menu_Fragment;
import tdtu.finalproject.salesmanager.NavigationHost;
import tdtu.finalproject.salesmanager.R;

public class History_Fragment extends Fragment {
    private Bundle session;
    private String Account;

    private View historyView;
    public List<String> Cart = new ArrayList<String>();

    private ProgressBar progressBar;
    private TextView noHistory;
    private ConstraintLayout historyinfo_layout;
    private BottomNavigationView navbottom;


    private History his;
    private List<JSONObject> historyList;
    private History_Items_Adapter adapter;
    private RecyclerView history_list;
    private FirebaseFirestore firebaseFirestore;



    // Create View -- End
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = getArguments();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment with the ProductGrid theme
        historyView = inflater.inflate(R.layout.history_fragment, container, false);
        // Cho biết session["user"] giống như php
        Account = session.getString("user");
        // Khai báo giỏ hàng hiện có
        Cart = session.getStringArrayList("CartList");
        // Init variables
        historyinfo_layout = historyView.findViewById(R.id.history_info_cons);
        historyinfo_layout.setVisibility(View.INVISIBLE);

        noHistory = historyView.findViewById(R.id.noHistoryNotify);
        noHistory.setVisibility(View.INVISIBLE);
        // Menu
        botNavSetting();
        // RecyclerView
        createRecyclerView();
        return historyView;
    }
    // Create View -- End

    // RecyclerView---Start
    public void createRecyclerView(){
        // Init
        history_list = historyView.findViewById(R.id.history_recycler);

        // Set history list
        history_list.setHasFixedSize(true);
        // Giới hạn item cho mỗi dòng
        history_list.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false));
        // Load item từ List<String> ra thành 1 adapter
        historyList = new ArrayList<>();
        //Progress Bar
        progressBar = historyView.findViewById(R.id.loading_progress);
        progressBar.setVisibility(View.VISIBLE);
        loadFirebase();
    }

    public void loadFirebase() {
        //Firebase BuyingHistory
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("BuyingHistory")
                .whereEqualTo("username", Account)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    progressBar.setVisibility(View.GONE);
                    // Get all documents in collections
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                    for (DocumentSnapshot d : list) {
                        History hist = d.toObject(History.class);

                        if(hist.getUsername().equals(Account)){
                            his = d.toObject(History.class);
                            his.setId(d.getId());
                        }
                    }
                    getBillInfo(his);
                }
            }
        });
    }

    public void getBillInfo(History tmpHis){
        String allBill_String = tmpHis.getBill();
        List<JSONObject> tmpList =  new ArrayList<>();

        try {
            JSONArray allBill_Array = new JSONArray(allBill_String);
            for (int i = allBill_Array.length() - 1; i>=0 ;i--){
                JSONObject billInfo_Object = new JSONObject( allBill_Array.get(i).toString() );
                tmpList.add(billInfo_Object);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(tmpList.size()>0){
            noHistory.setVisibility(View.INVISIBLE);
            historyList = tmpList;
            adapter = new History_Items_Adapter(historyList,historyinfo_layout); // input = List-String
            // Tạo recycle view vs adapter vừa có
            history_list.setAdapter(adapter);
        }else{
            noHistory.setVisibility(View.VISIBLE);
        }
    }

    // Bottom Item Action
    public void botNavSetting(){
        navbottom = historyView.findViewById(R.id.menubot);
        navbottom.setSelectedItemId(R.id.nav_history);
        navbottom.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_menu:
                        navWithCurrentCart(new Menu_Fragment());
                        break;
                    case R.id.nav_cart:
                        navWithCurrentCart(new Cart_Fragment());
                        break;
                    case R.id.nav_profile:
                        navWithCurrentCart(new Account_Info_Fragment());
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
