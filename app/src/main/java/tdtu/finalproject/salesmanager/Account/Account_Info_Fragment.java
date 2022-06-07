package tdtu.finalproject.salesmanager.Account;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import tdtu.finalproject.salesmanager.Cart.Cart_Fragment;
import tdtu.finalproject.salesmanager.History.History_Fragment;
import tdtu.finalproject.salesmanager.IOFragment.Logout_Fragment;
import tdtu.finalproject.salesmanager.Menu.Menu_Fragment;
import tdtu.finalproject.salesmanager.NavigationHost;
import tdtu.finalproject.salesmanager.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class Account_Info_Fragment extends Fragment{
    private String Account;
    private Bundle session;
    private View accinfoView;
    private BottomNavigationView navbottom;

    private ProgressBar progressBar;
    private EditText editText_Nickname,editText_Country,editText_Username,editText_LastUpdated;
    private RadioButton maleBtn,femaleBtn;

    private EditText oldPass, newPass, reEnter_newPass;
    private Button editProfileBtn,saveProfileBtn,saveNewPasswordBtn,cancelChangePassBtn;

    private Button btnProfilePage, btnChangePassPage;
    private FrameLayout frameInfo,framePass;
    private ColorStateList unchoose,choose,editBtn;

    private Accounts acc;
    private List<Accounts> listAcc;
    private AccountAdapter adapter;
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
        accinfoView = inflater.inflate(R.layout.acc_info_fragment, container, false);
        // Cho biết session["user"] giống như php
        Account = session.getString("user");
        // Khai báo giỏ hàng hiện có
        Cart = session.getStringArrayList("CartList");
        // Declare Views
        declareView();
        buttonSetting();
        openProfilePage();
        //Menu
        topNavSetting();
        botNavSetting();
        return accinfoView;
    }

    // View Control -- Start
    public void declareView(){
        progressBar = accinfoView.findViewById(R.id.loading_progress);
        //-------------------------PROFILE EDITING------------------------//
        editText_Nickname = accinfoView.findViewById(R.id.editableNickname);
        editText_Username = accinfoView.findViewById(R.id.notEditable);
        maleBtn = accinfoView.findViewById(R.id.btnMale);
        femaleBtn =accinfoView.findViewById(R.id.btnFemale);
        //-------------------------UN-EDITABLE----------------------------//
        editText_Country = accinfoView.findViewById(R.id.editableCountry);
        editText_LastUpdated = accinfoView.findViewById(R.id.notEditable2);
        //---------------------------BUTTON-------------------------------//
        editProfileBtn = accinfoView.findViewById(R.id.btnEditProfile);
        saveProfileBtn = accinfoView.findViewById(R.id.btnSaveProfile);

        //-------------------------PASSWORD CHANGING------------------------//
        oldPass = accinfoView.findViewById(R.id.edit_oldPass);
        newPass = accinfoView.findViewById(R.id.edit_newPass);
        reEnter_newPass = accinfoView.findViewById(R.id.edit_reEnter);
        //----------------------------BUTTON--------------------------------//
        saveNewPasswordBtn = accinfoView.findViewById(R.id.btnSaveNewPass);
        cancelChangePassBtn = accinfoView.findViewById(R.id.btnCancelChange);

        //-------------------------FRAME-LAYOUT-----------------------------//
        btnProfilePage = accinfoView.findViewById(R.id.btnProfile);
        btnChangePassPage = accinfoView.findViewById(R.id.btnChangePass);
        frameInfo = accinfoView.findViewById(R.id.info_frame);
        framePass = accinfoView.findViewById(R.id.pass_frame);

        //ColorStateList
        choose = ColorStateList.valueOf(Color.argb(255, 0, 71, 255));
        unchoose = ColorStateList.valueOf(Color.argb(00,00,00,00));
        editBtn  = ColorStateList.valueOf(Color.argb(255, 0, 255, 125));;

        //FirebaseFireStore
        firebaseFirestore = FirebaseFirestore.getInstance();
    }
    public void buttonSetting(){
        //----------------------CHANGE FRAMELAYOUT----------------------//
        btnProfilePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProfilePage();
            }
        });

        btnChangePassPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPassPage();
            }
        });

        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfile();
            }
        });
        saveProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProfile();
            }
        });

        cancelChangePassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelChangePass();
            }
        });

        saveNewPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePassword();
            }
        });
    }
    public void openProfilePage(){
        framePass.setVisibility(View.INVISIBLE);
        frameInfo.setVisibility(View.VISIBLE);

        // Create Init Account Information //
        listAcc = new ArrayList<>();
        adapter = new AccountAdapter(listAcc);
        progressBar.setVisibility(View.VISIBLE);
        setAccountInfo();
        //--------------------------------//

        btnProfilePage.setClickable(false);
        btnProfilePage.setBackgroundTintList(choose);
        btnChangePassPage.setClickable(true);
        btnChangePassPage.setBackgroundTintList(unchoose);

        editText_Nickname.setText("");
        editText_Country.setText("");
        editText_Nickname.setError(null);
        editText_Country.setError(null);

        editText_Nickname.setEnabled(false);
        editText_Username.setEnabled(false);
        editText_Country.setEnabled(false);
        maleBtn.setEnabled(false);
        femaleBtn.setEnabled(false);

        editProfileBtn.setClickable(true);
        editProfileBtn.setBackgroundTintList(editBtn);
        saveProfileBtn.setClickable(false);
        saveProfileBtn.setBackgroundTintList(unchoose);

        cancelChangePass();
    }
    public void openPassPage(){
        frameInfo.setVisibility(View.INVISIBLE);
        framePass.setVisibility(View.VISIBLE);
        // Create Init Account Information //
        listAcc = new ArrayList<>();
        adapter = new AccountAdapter(listAcc);
        setAccountInfo();
        //--------------------------------//

        btnProfilePage.setClickable(true);
        btnProfilePage.setBackgroundTintList(unchoose);
        btnChangePassPage.setClickable(false);
        btnChangePassPage.setBackgroundTintList(choose);

        oldPass.setEnabled(true);
        newPass.setEnabled(true);
        reEnter_newPass.setEnabled(true);

        saveNewPasswordBtn.setClickable(true);
        saveNewPasswordBtn.setBackgroundTintList(editBtn);
        cancelChangePassBtn.setClickable(false);
        cancelChangePassBtn.setBackgroundTintList(unchoose);
    }

    public void editProfile(){
        editText_Nickname.setEnabled(true);
        editText_Country.setEnabled(true);
        maleBtn.setEnabled(true);
        femaleBtn.setEnabled(true);

        editProfileBtn.setClickable(true);
        editProfileBtn.setBackgroundTintList(unchoose);
        saveProfileBtn.setClickable(true);
        saveProfileBtn.setBackgroundTintList(editBtn);
    }
    public void saveProfile(){
        // Change field in documents
        String tmpNickname = editText_Nickname.getText().toString().trim();
        String tmpCountry = editText_Country.getText().toString().trim();
        String[] tmpCountryList = {"Vietnam","US","Japan","Korean","China","Singapore"};
        String[] timeZone = {"GMT+7","GMT-5","GMT+9","GMT+9","GMT+8","GMT+8"};
        String tmpSex = maleBtn.isChecked()?"m":"f";
        String tmpTimezone = "";


        Boolean checkCountry = false;
        Boolean checkValidatedToSave = false;

        if(tmpNickname.length()==0)
            tmpNickname = editText_Nickname.getHint().toString();
        if(tmpCountry.length()==0)
            tmpCountry = editText_Country.getHint().toString();
        if(tmpNickname.length()<6)
            editText_Nickname.setError("Nickname must longer than 6 characters.");

        String tmpCountryString = "[";
        for (int i=0;i<tmpCountryList.length;i++){
            if( tmpCountryList[i].equals(tmpCountry) ){
                checkCountry = true;
                tmpTimezone = timeZone[i];
            }else{}

            if(i == tmpCountryList.length-1){
                tmpCountryString += tmpCountryList[i];
            }else{
                tmpCountryString += tmpCountryList[i] + ", ";
            }
        }
        tmpCountryString += "]";


        if(checkCountry==false){
            editText_Country.setError("Country must in "+tmpCountryString);
            return;
        }

        // TimeZone
        TimeZone.setDefault(TimeZone.getTimeZone(tmpTimezone));
        String timeNow = Calendar.getInstance().getTime().toString();

        // Save Info Process
        Accounts tmpacc = new Accounts( acc.getUsername(),
                                        acc.getPassword(),
                                        tmpCountry,tmpNickname,
                                        tmpSex,timeNow
        );
        firebaseFirestore.collection("Users").document(acc.getId())
                .set(tmpacc)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });

        Toast.makeText(getContext(),"Updated Succeed",Toast.LENGTH_SHORT).show();
        // Re-open to load data from database
        openProfilePage();
    }
    public void cancelChangePass(){
        oldPass.setError(null);
        newPass.setError(null);
        reEnter_newPass.setError(null);

        oldPass.setText("");
        newPass.setText("");
        reEnter_newPass.setText("");

        oldPass.clearFocus();
        newPass.clearFocus();
        reEnter_newPass.clearFocus();
    }
    public void savePassword(){
        String tmpOldPass = oldPass.getText().toString().trim();
        String tmpNewPass = newPass.getText().toString().trim();
        String tmpReEnter = reEnter_newPass.getText().toString().trim();
        String[] tmpCountryList = {"Vietnam","US","Japan","Korean","China","Singapore"};
        String[] timeZone = {"GMT+7","GMT-5","GMT+9","GMT+9","GMT+8","GMT+8"};

        if (!tmpOldPass.equals(acc.getPassword())) {
            oldPass.setError("Wrong password.");
            return;
        }

        if(tmpNewPass.contains(" ")) {
            newPass.setError("Password mustn't have spacing");
            return;
        }else if (tmpNewPass.length()<6){
            newPass.setError("Password mustn't have more than 6 characters");
            return;
        }else if(tmpNewPass.equals(tmpOldPass)){
            newPass.setError("Password mustn't same as old pass");
            return;
        }else if(!tmpReEnter.equals(tmpNewPass)){
            reEnter_newPass.setError("MUST SAME AS NEW PASS");
            return;
        }


        String tmpTimeZone="";

        for (int i=0;i<tmpCountryList.length;i++){
            if(tmpCountryList[i].equals(acc.getCountry())){
                tmpTimeZone = timeZone[i];
            }
        }

        // TimeZone
        TimeZone.setDefault(TimeZone.getTimeZone(tmpTimeZone));
        String timeNow = Calendar.getInstance().getTime().toString();
        // Save Info Process
        Accounts tmpacc = new Accounts( acc.getUsername(),
                tmpNewPass,
                acc.getCountry(),acc.getNickname(),
                acc.getSex(), timeNow
        );
        firebaseFirestore.collection("Users")
                .document(acc.getId())
                .set(tmpacc)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {}
                });

        Toast.makeText(getContext(),"Updated Password Succeed",Toast.LENGTH_SHORT).show();
        // Re-open to load data from database
        openPassPage();
        cancelChangePass();
    }

    // View Control -- End


    // Set AccountInfo to View -- Start
    public void setAccountInfo(){
        firebaseFirestore.collection("Users").whereEqualTo("username",Account)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if(!queryDocumentSnapshots.isEmpty()){
                            // Get all documents in collections
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for(DocumentSnapshot d:list){
                                Accounts account_info = d.toObject(Accounts.class);
                                String tmpNickname = account_info.getNickname();
                                String tmpUsername = account_info.getUsername();
                                String tmpSex = account_info.getSex();
                                String tmpCountry = account_info.getCountry();
                                String tmpLastUpdated = account_info.getLastUpdated();

                                editText_Nickname.setHint(tmpNickname);
                                editText_Username.setHint(tmpUsername+"*");

                                if(tmpSex.equals("m")){
                                    maleBtn.setChecked(true);
                                }else{
                                    femaleBtn.setChecked(true);
                                }

                                editText_Country.setHint(tmpCountry);
                                editText_LastUpdated.setText(tmpLastUpdated);

                                acc = d.toObject(Accounts.class);
                                acc.setId(d.getId());
                            }
                        }
                    }
                });
    }
    // Set AccountInfo to View -- End


    //Top Menu - Info / Password
    public void topNavSetting(){

    }

    //Bottom Menu
    public void botNavSetting(){
        navbottom = accinfoView.findViewById(R.id.menubot);
        navbottom.setSelectedItemId(R.id.nav_profile);
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

    // NavigateTo with cart changes
    public void navWithCurrentCart(Fragment cartpage){
        List<String> sendcart = Cart;
        session.putStringArrayList("CartList", (ArrayList<String>) sendcart);
        cartpage.setArguments(session);
        for(int i=0;i<Cart.size();i++){
            String tmp =  Cart.get(i).toString();
        }
        ((NavigationHost) getActivity()).navigateTo(cartpage, false);
    }
}

