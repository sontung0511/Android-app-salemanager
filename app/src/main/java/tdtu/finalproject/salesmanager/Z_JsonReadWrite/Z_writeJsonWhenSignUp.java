package tdtu.finalproject.salesmanager.Z_JsonReadWrite;

import android.content.res.Resources;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class Z_writeJsonWhenSignUp {
    private Resources res;
    private InputStream accountJsonFile,historyJsonFile;
    private static final String TAG = Z_writeJsonWhenSignUp.class.getSimpleName();
    private String  userInfo[];

    Z_writeJsonWhenSignUp(Resources resources, InputStream accountJsonFile, InputStream historyJsonFile,String[] userInfo){
        this.res = resources;
        this.accountJsonFile  = accountJsonFile;
        this.historyJsonFile = historyJsonFile;
        this.userInfo = userInfo;
    }

    public void createForNewUser() throws JSONException {
        String jsonUser_all = new Z_readJson(res,accountJsonFile).getJsonString();
        String jsonHist_all = new Z_readJson(res,historyJsonFile).getJsonString();

        JSONArray jsonArray_user = new JSONArray(jsonUser_all);
        JSONArray jsonArray_hist = new JSONArray(jsonHist_all);

        TimeZone.setDefault(TimeZone.getTimeZone("GMT+7")); // Múi giờ GMT +...
        String timeNow = Calendar.getInstance().getTime().toString();

        // Create new UserInfo
        JSONObject newUser = new JSONObject();
        newUser.put("username",this.userInfo[0]);
        newUser.put("password",this.userInfo[1]);
        newUser.put("nickname",this.userInfo[2]);
        newUser.put("country",this.userInfo[3]);
        newUser.put("birth",this.userInfo[4]);
        newUser.put("sex",this.userInfo[5]);
        newUser.put("lastUpdated",timeNow);
        jsonArray_user.put(newUser);
        //Create new History
        JSONObject newUserHistory = new JSONObject();
        newUserHistory.put("user",this.userInfo[0]);
        newUserHistory.put("bill",new ArrayList<String>());
        jsonArray_hist.put(newUserHistory);

        Log.d("Test SignUp",jsonHist_all.toString()+"\n"+jsonUser_all.toString());
    }
}
