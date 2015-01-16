package in.heythere.heythere;

/**
 * This is our Jason Parser System. v1
 *
 * This class will help heythere main app to parse JSON data from the server.
 *
 * Created by rajeev on 28-Dec-14.
 */



        import org.apache.http.HttpEntity;
        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStream;
        import org.apache.http.HttpResponse;
        import java.io.InputStreamReader;
        import  java.io.UnsupportedEncodingException;
        import java.util.List;
        import org.apache.http.NameValuePair;
        import org.apache.http.client.ClientProtocolException;
        import org.apache.http.client.entity.UrlEncodedFormEntity;
        import org.apache.http.client.methods.HttpGet;
        import org.apache.http.client.methods.HttpPost;
        import org.apache.http.client.utils.URLEncodedUtils;
        import org.apache.http.impl.client.DefaultHttpClient;
        import org.json.JSONException; import org.json.JSONObject;
        import android.util.Log;

public class JSONParser {
    static InputStream is = null;
    static JSONObject jsonObj ;


    static String json = "";

    // default no argument constructor for jsonpaser class
    public JSONParser() { }
    public JSONObject getJSONFromUrl(final String url)
    { // Making HTTP request
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            // Executing POST request & storing the response from server locally.
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        catch (ClientProtocolException e)
        { e.printStackTrace();
        } catch (IOException e)
        { e.printStackTrace();
        } try
    { // Create a BufferedReader
        BufferedReader reader = new BufferedReader(new InputStreamReader( is, "iso-8859-1"), 8);
        // Declaring string builder
        StringBuilder str = new StringBuilder();
        // string to store the JSON object.
        String strLine = null;
        // Building while we have string !equal null.
        while ((strLine = reader.readLine()) != null)
        {
            str.append(strLine + "\n");
        }
        // Close inputstream.
        is.close();
        // string builder data conversion to string.
        json = str.toString();
    } catch (Exception e)
    { Log.e("Error", " something wrong with converting result " + e.toString()); }
        // Try block used for pasrseing String to a json object
        try {/*
//My mod start
            int start = json.indexOf("{");
            int last = json.lastIndexOf("}");
            if (start!=-1&&last!=-1) {
                jsonObj = new JSONObject(json.substring(start, last + 1));
            }
           //my mod stop
*/
            //Old code:
            jsonObj = new JSONObject(json);
        }
        catch (JSONException e)
        { Log.e("json Parsering", "" + e.toString()); }
        // Returning json Object.
        return jsonObj;
    }
    public JSONObject makeHttpRequest(String url, String method, List<NameValuePair> params)
    {
        Log.d("Inside Json", "making http request");
        // Make HTTP request
        try {
            // checking request method
            Log.d("Inside Json", "Checking req Method");
            if(method == "POST")
            { // now defaultHttpClient object
                Log.d("Inside Json", "POST is the http request!");
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params));
                Log.d("Inside Json", "Executing httppost!");
                HttpResponse httpResponse = httpClient.execute(httpPost);
                Log.d("Inside Json", "Getting HTTP  entery");
                HttpEntity httpEntity = httpResponse.getEntity();
                Log.d("Inside Json", "Getting Content");
                is = httpEntity.getContent();
                Log.d("Inside Json", "Got Content in 'is'");
            }

        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        try
        { BufferedReader reader = new BufferedReader(new InputStreamReader( is, "iso-8859-1"), 8);
            StringBuilder str = new StringBuilder();
            String strLine = null;
            while ((strLine = reader.readLine()) != null)
            {
                str.append(strLine + "\n");
            } is.close(); json = str.toString();
        }
        catch (Exception e)
        { }
        // now will try to parse the string into JSON object
        try { jsonObj = new JSONObject(json);
        } catch (JSONException e)
        { }
        return jsonObj;
    }
}


