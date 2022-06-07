package tdtu.finalproject.salesmanager.IOFragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
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
import tdtu.finalproject.salesmanager.History.History;
import tdtu.finalproject.salesmanager.History.History_Fragment;
import tdtu.finalproject.salesmanager.History.History_Items_Adapter;
import tdtu.finalproject.salesmanager.Menu.Menu_Fragment;
import tdtu.finalproject.salesmanager.NavigationHost;
import tdtu.finalproject.salesmanager.R;

public class Logout_Fragment extends Fragment {
    private Bundle session;
    private String Account;

    public List<String> Cart = new ArrayList<String>();

    private View logoutView;
    private Button logoutbtn;
    private BottomNavigationView navbottom;
    // Create View -- End
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = getArguments();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment with the ProductGrid theme
        logoutView = inflater.inflate(R.layout.logout_fragment, container, false);
        // Cho biết session["user"] giống như php
        Account = session.getString("user");
        // Khai báo giỏ hàng hiện có
        Cart = session.getStringArrayList("CartList");
        // Button
        logoutbtn = logoutView.findViewById(R.id.btnLogout);
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create Dialog
                createDialog();
            }
        });
        // Menu
        botNavSetting();
        return logoutView;
    }

    // Dialog
    public void createDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.logout_alert,null))
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ((NavigationHost) getActivity()).navigateTo(new Login_Fragment(), false);
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getContext(),"Cancel Logout",Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

        Button btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button btnNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
        layoutParams.weight = 10;
        btnPositive.setLayoutParams(layoutParams);
        btnNegative.setLayoutParams(layoutParams);

    }


    // Bottom Item Action
    public void botNavSetting(){
        navbottom = logoutView.findViewById(R.id.menubot);
        navbottom.setSelectedItemId(R.id.nav_logout);
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
                    case R.id.nav_history:
                        navWithCurrentCart(new History_Fragment());
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
