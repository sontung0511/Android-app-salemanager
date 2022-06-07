package tdtu.finalproject.salesmanager.Firebase;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class FireBaseProcess {
    private FirebaseFirestore firebaseFirestore;
    private ProgressBar progressBar;

    public FireBaseProcess(ProgressBar progressBar){
        this.progressBar = progressBar;
        this.firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public void createNewAccount(String account,String tmpNickname, String tmpSex,String tmpCountry) {
        progressBar.setVisibility(View.VISIBLE);

//        Map<String, Object> user = new HashMap<>();
//        user.put("first", "Ada");
//        user.put("last", "Lovelace");
//        user.put("born", 1815);
//
//        firebaseFirestore.collection("Users")
//                .add(user)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d("DocumentSnapshot ID: ", documentReference.getId()+"");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d("Error adding document", e+"");
//                    }
//                });
        Log.d("TEST Process",Query.Direction.values().toString());
    }

}
