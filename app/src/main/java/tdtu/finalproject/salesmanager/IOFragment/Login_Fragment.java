package tdtu.finalproject.salesmanager.IOFragment;

import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import tdtu.finalproject.salesmanager.Account.AccountAdapter;
import tdtu.finalproject.salesmanager.Account.Accounts;
import tdtu.finalproject.salesmanager.Menu.Menu_Fragment;
import tdtu.finalproject.salesmanager.NavigationHost;
import tdtu.finalproject.salesmanager.R;

public class Login_Fragment extends Fragment {
    private TextInputEditText Username,Password;
    private TextInputLayout UserError,PasswordError;

    public MaterialButton btnLog,btnSignUp;
    private FirebaseFirestore firebaseFirestore;

    private AccountAdapter adapter;
    private List<Accounts> list_accounts;


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Đưa fragment vào container "blankpage"
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        // Khai báo
        initVariables(view);
        // Kiểm soát việc nhập input : user / password
        textInputController();
        // Những bắt đầu login khi...
        IORequest();
        return view;
    }

    // Khai báo biến
    public void initVariables(View view){
        // TextInputEditText
        Username = view.findViewById(R.id.username);
        Password = view.findViewById(R.id.password);
        // Thông báo lỗi khi nhấn đăng nhập
        UserError = view.findViewById(R.id.usererror);
        PasswordError = view.findViewById(R.id.passworderror);
        // Khai báo cho các button
        btnLog = view.findViewById(R.id.btnlogin);
        btnSignUp = view.findViewById(R.id.btnsignup);

        // FireBaseFireStore
        list_accounts = new ArrayList<>();
        adapter = new AccountAdapter(list_accounts);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Users").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            // Get all documents in collections
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for(DocumentSnapshot d:list){
                                Accounts accounts = d.toObject(Accounts.class);
                                list_accounts.add(accounts);
                            }

                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    // Kiểm soát các TextInputLayout
    public void textInputController(){
        // Thoát nhập username ... auto xóa password
        Username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focusIn) {
                String tmp = Username.getText().toString();
                if(focusIn){
                    return;
                }else{
                    Password.setText(null);
                    deleteError(PasswordError);
                }
            }
        });

        // Click lại vào ô mật khẩu ... xóa lỗi
        Password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteError(PasswordError);
            }
        });

    }
    public void deleteError(TextInputLayout layout){
        layout.setErrorEnabled(false);
    }

    // Quá trình logIn - Go to SignUp
    public void IORequest(){
        // Click Login
        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doRequest();
            }
        });
        // Click to go SignUp Page
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment nextpage = new SignUp_Fragment();
                ((NavigationHost) getActivity()).navigateTo(nextpage, false);
            }
        });

        // Nghe phím enter khi đang nhập input
        Username.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keycode, KeyEvent keyEvent) {
                deleteError(UserError);
                if(keycode==KeyEvent.KEYCODE_ENTER){
                    doRequest();
                    return false;
                }
                return false;
            }
        });
        Password.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keycode, KeyEvent keyEvent) {
                deleteError(PasswordError);
                if(keycode==KeyEvent.KEYCODE_ENTER){
                    doRequest();
                    return false;
                }
                return false;
            }
        });
    }
    public void doRequest(){
        // Nếu tài khoản / mật khẩu là null , báo lỗi và dừng (ưu tiên tài khoản )
        if(isNull(Username.getText())){
            UserError.setError(getString(R.string.nulluser));
            return;
        }
        if(isNull(Password.getText())){
            PasswordError.setError(getString(R.string.nullpassword));
            return;
        }
        //  Xác nhận tài khoản ứng với mật khẩu
        String user = Username.getText().toString();
        String pass = Password.getText().toString();

        checkAccount(user,pass);
    }
    public void checkAccount(String user,String pass){
        //  Tài khoản hợp lệ : return "Valid"
        if ( isAccountValid(user,pass) == "Valid" ) {
            // Chuyển màn hình tới fragment kế tiếp: Admin / User
            Fragment nextpage = new Menu_Fragment();
            // Gán session
            Bundle bundle = new Bundle();
            bundle.putString("user", user.toString());   // "Key": value
            bundle.putSerializable("CartList", new ArrayList<String>());   // "Default Cart"
            nextpage.setArguments(bundle);
            ((NavigationHost) getActivity()).navigateTo(nextpage, false);
        } else {}
    }


    protected String isAccountValid(String user,String pass) {
        String result = adapter.checkAccount(user, pass);

        // Check username có tồn tại hay khem.
        if(result.equals("Account does not exist")){
            UserError.setError("Not existed");
            PasswordError.setErrorEnabled(false);
            return "Invalid";
        }
        // Check password đúng / sai
        if(pass.toString().trim().length()<6){
            PasswordError.setError("Password need at least 6 characters.");
            UserError.setError(null);
            return "Invalid";
        }

        if(result.equals("Wrong Password")){
            PasswordError.setError("Wrong Password");
            UserError.setErrorEnabled(false);
            return "Invalid";
        }else{
            return "Valid";
        }
    }


    private boolean isNull(Editable text){
        return text.toString().trim().length()==0;
    }
}
