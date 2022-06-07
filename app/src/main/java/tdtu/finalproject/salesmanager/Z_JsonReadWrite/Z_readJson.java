package tdtu.finalproject.salesmanager.Z_JsonReadWrite;

import android.content.res.Resources;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

public class Z_readJson {
    private String jsonString;
    private Resources res;
    private InputStream inputStream;
    private static final String TAG = Z_readJson.class.getSimpleName();


    Z_readJson(Resources resources, InputStream inputStream){
        this.res = resources;
        this.inputStream  = inputStream;
    }


    public String getJsonString(){
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
        jsonString = writer.toString();
        return jsonString;
    }
}
