package tdtu.finalproject.salesmanager.IOFragment;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tdtu.finalproject.salesmanager.Account.AccountAdapter;
import tdtu.finalproject.salesmanager.Account.Accounts;
import tdtu.finalproject.salesmanager.Country.Country;
import tdtu.finalproject.salesmanager.NavigationHost;
import tdtu.finalproject.salesmanager.R;


public class SignUp_Fragment extends Fragment {
    private View signUpView;

    private ScrollView mainPage;
    private ConstraintLayout subPage;

    private ProgressBar progressBar;
    private EditText username,pass,repass;
    private EditText nickname, country;
    private RadioButton btnMale,btnFemale;
    private Button signUpBTN,backPage;

    private AccountAdapter adapter;
    private List<Accounts> list_accounts;
    private FirebaseFirestore firebaseFirestore;



    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Đưa fragment vào container "blankpage"
        signUpView = inflater.inflate(R.layout.signup_fragment, container, false);
        // Khai báo
        initVariables();

        return signUpView;
    }

    // Khai báo biến
    public void initVariables(){
        // Variables
        mainPage = signUpView.findViewById(R.id.signUpMainPage);
        subPage = signUpView.findViewById(R.id.signUpSubPage);

        backPage = signUpView.findViewById(R.id.backPage1);

        username = signUpView.findViewById(R.id.inputUsername);
        pass = signUpView.findViewById(R.id.inputPassword);
        repass = signUpView.findViewById(R.id.inputReEnterPassword);

        nickname = signUpView.findViewById(R.id.inputNickname);
        btnMale = signUpView.findViewById(R.id.radioMale);
        btnFemale = signUpView.findViewById(R.id.radiaFemale);
        country = signUpView.findViewById(R.id.inputCountry);

        signUpBTN  = signUpView.findViewById(R.id.btnSignUp);
        progressBar = signUpView.findViewById(R.id.progressBarSignUp);

        // Init status
        subPage.setVisibility(View.INVISIBLE);
        btnMale.setChecked(true);
        btnFemale.setChecked(false);
        progressBar.setVisibility(View.INVISIBLE);

        backPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NavigationHost) getActivity()).navigateTo(new Login_Fragment(), false);
            }
        });

        signUpBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSignUpRequest();
            }
        });
    }

    // Quá trình signUp
    public void doSignUpRequest(){
        String tmpUsername  = username.getText().toString();
        String tmpPass  = pass.getText().toString();
        String tmpRepass  = repass.getText().toString();
        String tmpNickname  = nickname.getText().toString();
        String tmpCountry  = country.getText().toString();
        String tmpGender  = btnMale.isChecked()?"m":"f";

        Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
        Matcher matcherUsername = pattern.matcher(tmpUsername);
        Matcher matcherPass = pattern.matcher(tmpPass);

        // Valid Input
        if(matcherUsername.find()){
            username.setError("Valid character: a-z A-z 0-9");
            return;
        }
        if(matcherPass.find()){
            pass.setError("Valid character: a-z A-z 0-9");
            return;
        }

        // Check Corrected
        if(username.length()<6){
            username.setError("Username must longer than 6 characters.");
            return;
        }
        if(tmpPass.length()<6){
            pass.setError("Password must longer than 6 characters.");
            return;
        }
        if(tmpRepass.equals(tmpPass)==false){
            repass.setError("Valid character: a-z A-z 0-9");
            return;
        }

        // Check not null
        if(tmpNickname.trim().length()==0){
            nickname.setError("Nickname cannot be null.");
            return;
        }

        firebaseProcess_1(tmpUsername,tmpCountry);
    }

    // Check Account Existed
    public void firebaseProcess_1(String tmpUsername,String tmpCountry){
        // FireBaseFireStore
        list_accounts = new ArrayList<>();
        adapter = new AccountAdapter(list_accounts);

        // First Process : check User
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Users").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if(!queryDocumentSnapshots.isEmpty()){
                            // Get all documents in collections
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for(DocumentSnapshot d:list){
                                Accounts accounts = d.toObject(Accounts.class);
                                list_accounts.add(accounts);
                            }
                            adapter.notifyDataSetChanged();


                            Boolean check = adapter.isExisted(tmpUsername);
                            if(check){
                                username.setError("Username has existed.");
                            }else{
                                // Next Process : Get timeZone
                                firebaseProcess_2(tmpCountry);
                            }

                        }
                    }
                });
    }

    // Get TimeZone
    public  void firebaseProcess_2(String tmpCountry){
        firebaseFirestore.collection("Country")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            // Get all documents in collections
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            Boolean check = false;
                            String tmpTimeZone = "GMT+7";
                            for(DocumentSnapshot d:list){
                                Country firebaseCountry = d.toObject(Country.class);

                                if(firebaseCountry.getCountryname().equals(tmpCountry)){
                                    tmpTimeZone = firebaseCountry.getTimezone();
                                    check = true;
                                    break;
                                }
                            }

                            if(check){
                                TimeZone.setDefault(TimeZone.getTimeZone(tmpTimeZone));
                                String timeNow = Calendar.getInstance().getTime().toString();
                                addNewAccount(timeNow);
                            }else{
                                country.setError("Invalid");
                            }
                        }
                    }
                });
    }


    public void addNewAccount(String timeNow){
        String tmpUsername  = username.getText().toString();
        String tmpPass  = pass.getText().toString();
        String tmpNickname  = nickname.getText().toString();
        String tmpCountry  = country.getText().toString();
        String tmpGender  = btnMale.isChecked()?"m":"f";

        Map<String, Object> account = new HashMap<>();
        account.put("username", tmpUsername);
        account.put("password",tmpPass);
        account.put("country", tmpCountry);
        account.put("nickname", tmpNickname);
        account.put("sex", tmpGender);
        account.put("lastUpdated", timeNow);
        firebaseFirestore.collection("Users").add(account);

        Map<String, Object> history = new HashMap<>();
        history.put("bill","[]");
        history.put("username",tmpUsername);
        firebaseFirestore.collection("BuyingHistory").add(history);

        username.setText("");
        pass.setText("");
        repass.setText("");
        nickname.setText("");
        country.setText("");
        mainPage.setVisibility(View.GONE);
        subPage.setVisibility(View.VISIBLE);
        backPage = signUpView.findViewById(R.id.backPage2);
        backPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NavigationHost) getActivity()).navigateTo(new Login_Fragment(), false);
            }
        });
    }
}
