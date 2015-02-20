package audible.katkov.com.testaudible.async;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import audible.katkov.com.testaudible.model.IBaseModel;


public class NetworkParserTask extends AsyncTask<Void,Void,ResponseModel> {

    private static final String TAG = "BaseParserTask";
    private RequestModel requestModel;

    public NetworkParserTask(RequestModel requestModel){
        this.requestModel = requestModel;
    }

    @Override
    protected ResponseModel doInBackground(Void... voids) {
        String responseString;
        InputStream is;
        ResponseModel responseModel = new ResponseModel();
        IBaseModel baseModel = null;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(requestModel.url);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "n");
            }
            is.close();
            responseString = sb.toString();
            baseModel = requestModel.parser.parse(responseString);
        }catch (ClientProtocolException e){
            responseModel.exception = e;
            WLog.d(TAG, "Client Protocol Exception");
        }catch (JSONException e){
            responseModel.exception = e;
            WLog.d(TAG, "JSON Parser Exception");
        }catch (IOException e){
            responseModel.exception = e;
            WLog.d(TAG, "IOException");
        }finally {
            responseModel.model = baseModel;
            return responseModel;
        }
    }

    @Override
    protected void onPostExecute(ResponseModel responseModel) {
        super.onPostExecute(responseModel);
        if(requestModel.callback != null){
            requestModel.callback.call(responseModel);
        }
    }
}
