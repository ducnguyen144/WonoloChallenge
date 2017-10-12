package wonolo.sgeoi.ReadContentURL_and_CheckNullString;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class ReadContentURL_CheckNullString {

    public String readContentURL(String theUrl){
        StringBuilder content = new StringBuilder();
        try    {
            URL url = new URL(theUrl);
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null){
                content.append(line + "\n");
            }
            bufferedReader.close();
        }
        catch(Exception e)    {
            e.printStackTrace();
        }
        return content.toString();
    }

    // Check null of String
    public boolean isCheckString(String s){
        if((s!=null) && (s!="") && (s!=" ")) {
            return true;
        }
        return false;
    }
}
