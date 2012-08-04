package util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;

public class HttpUtil {

     public String get(String URL) {
       StringBuffer sb = new StringBuffer();
       HttpClient httpClient = new DefaultHttpClient();
       HttpGet request = new HttpGet(URL);
       HttpParams params = new BasicHttpParams();
       params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, new Integer(2500));
       request.setParams(params);
       try {
           HttpResponse response = httpClient.execute(request);
           int status = response.getStatusLine().getStatusCode();
           if (status != HttpStatus.SC_OK) {
               ByteArrayOutputStream ostream = new ByteArrayOutputStream();
               response.getEntity().writeTo(ostream);
           } else {
               InputStream incontent = response.getEntity().getContent();
               BufferedReader bin = new BufferedReader(new InputStreamReader(incontent));
               String inputLine;
               while ((inputLine = bin.readLine()) != null)
                   sb.append(inputLine);
               bin.close();
               incontent.close();
           }
       }catch (ConnectTimeoutException e ){
            return "Timeout";
       }catch (Exception e){
           e.printStackTrace();
           return "Exception";
       }
       return sb.toString();
   }
}
