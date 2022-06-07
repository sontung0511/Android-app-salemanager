package tdtu.finalproject.salesmanager.Account;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder> {
    private List<Accounts> accountsList;

    public AccountAdapter() {}

    public AccountAdapter(List<Accounts> listAccounts){
        this.accountsList = listAccounts;
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new AccountViewHolder(
//                LayoutInflater.from(context).inflate(R.layout.account,parent,false)
//        );
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
        Accounts user = this.accountsList.get(position);
    }

    @Override
    public int getItemCount() {
        return accountsList.size();
    }

    public String checkAccount(String name,String pass){
        boolean checkPass = false,checkUser = false;

        for(int i=0;i<getItemCount();i++){
            if(accountsList.get(i).getUsername().equals(name)){
                checkUser=true;
                if(accountsList.get(i).getPassword().equals(pass)){
                    checkPass=true;
                }else{}
                break;
            }
        }

        if(checkUser==false){
            return "Account does not exist";
        }else{
            if(checkPass==false){
                return "Wrong Password";
            }else {
                return "Validated";
            }
        }
    }

    public Boolean isExisted(String username){
        for(int i=0;i<getItemCount();i++){
            String tmpUsername = accountsList.get(i).getUsername().toLowerCase();
            String tmpusername = username.toLowerCase();
            if(tmpUsername.equals(tmpusername)){
                return true;
            }else{}
        }

        return false;
    }

    class AccountViewHolder extends RecyclerView.ViewHolder{
        TextView textViewName,textViewPass;

        public AccountViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

