package tdtu.finalproject.salesmanager.Z_MenuItemJsonDemo;

import android.content.res.Resources;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import tdtu.finalproject.salesmanager.R;

public class Menu_Loader {
    private static final String TAG = Menu_Loader.class.getSimpleName();

    public final String name;
    public final String price;
    public final String type;

    public Menu_Loader(String name, String price, String type) {
        this.name = name;
        this.price = price;
        this.type = type;
    }

    // Return this list to screen : working with "public static"
    public static List<Menu_Loader> tmplist;
    public static String jsonProductsString;

    public static List<Menu_Loader> initMenuList(Resources resources,String listtype){
        InputStream inputStream = resources.openRawResource(R.raw.fdlist);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            int pointer;
            while ((pointer = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, pointer);
            }
        } catch (IOException exception) {
            Log.e(TAG, "Error writing/reading from the JSON file.", exception);
        } finally {
            try {
                inputStream.close();
            } catch (IOException exception) {
                Log.e(TAG, "Error closing the input stream.", exception);
            }
        }

        // Get Json array : type String
        jsonProductsString = writer.toString(); // Array: "[{"name":"...","cost":"...",...},{...}]" : List tất cả món
//        Log.d("Test",jsonProductsString);
        tmplist = new ArrayList<Menu_Loader>();

        switch(listtype){
            case "listall":
                listAll();
                break;
            case "listfood":
                listFood();
                break;
            case "listdrink":
                listDrink();
                break;
        }
        return tmplist;
    }


    // Food -> Drink
    public static void listAll(){
        listFood();
        listDrink();
    }
    public static void listFood(){
        try {
            JSONArray jsonArray = new JSONArray(jsonProductsString); // Đưa các món vào 1 arraylist
            for(int i=0; i<jsonArray.length();i++) {
                String jsonString = jsonArray.getString(i); // "{"name":"...","cost":"...",...}" : Mỗi món
                JSONObject jsonObject = new JSONObject(jsonString); // Mỗi món là 1 jsonObject riêng.
                String Ob_name = jsonObject.getString("name");  // Tên món
                String Ob_cost = jsonObject.getString("price"); // Giá
                String Ob_type = jsonObject.getString("type");  // Thức ăn / Thức uống
                if (Ob_type.equals("f")) {
                    tmplist.add(new Menu_Loader(Ob_name,Ob_cost,Ob_type));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static void listDrink(){
        try {
            JSONArray jsonArray = new JSONArray(jsonProductsString); // Đưa các món vào 1 arraylist
            for(int i=0; i<jsonArray.length();i++) {
                String jsonString = jsonArray.getString(i); // "{"name":"...","cost":"...",...}" : Mỗi món
                JSONObject jsonObject = new JSONObject(jsonString); // Mỗi món là 1 jsonObject riêng.
                String Ob_name = jsonObject.getString("name");  // Tên món
                String Ob_cost = jsonObject.getString("price"); // Giá
                String Ob_type = jsonObject.getString("type");  // Thức ăn / Thức uống
                if (Ob_type.equals("d")) {
                    tmplist.add(new Menu_Loader(Ob_name,Ob_cost,Ob_type));

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
