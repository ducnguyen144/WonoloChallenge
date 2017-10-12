package wonolo.sgeoi.ParseJSON;


import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import wonolo.sgeoi.DataApp.InstagramData;
import wonolo.sgeoi.DataApp.UserData;
import wonolo.sgeoi.Interface.ITransferDataListener;
import wonolo.sgeoi.ReadContentURL_and_CheckNullString.ReadContentURL_CheckNullString;

public class ParseJSONDataUser extends ReadContentURL_CheckNullString {

    private Context mContext;
    private ITransferDataListener mTransferDataListener;

    public ParseJSONDataUser(Context context, ITransferDataListener listener){
        mContext = context;
        mTransferDataListener = listener;
    }

    public void execute() throws UnsupportedEncodingException {
        new ReadJSON().execute(createUrl());
    }

    private String createUrl() throws UnsupportedEncodingException {
        return InstagramData.USERS_SELF + InstagramData.TOKEN;
    }

    // Read JSON from web
    private class ReadJSON extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            // Read content of URL
            return readContentURL(params[0]);
        }

        @Override
        protected void onPostExecute(String data) {
            try {
                parseJSonDataUser(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseJSonDataUser(String data) throws JSONException {
        if (data == null) {
            Toast.makeText(mContext,"Not data",Toast.LENGTH_LONG).show();
        } else {
            UserData userData = new UserData();

            // Object JSON total of content data
            JSONObject jsonTotal = new JSONObject(data);

            // Object JSON of "data"
            JSONObject jsonData = jsonTotal.getJSONObject("data");

            // Object JSON of "counter"
            JSONObject jsonCounts = jsonData.getJSONObject("counts");

            // Set value of ID
            if (jsonTotal.has("data") && isCheckString(jsonData.getString("id"))) {
                userData.setmUserID(jsonData.getString("id"));
            }

            // Set value of Username
            if (jsonTotal.has("data") && isCheckString(jsonData.getString("username"))) {
                userData.setmUsername(jsonData.getString("username"));
            }

            // Set value of Avatar
            if (jsonTotal.has("data") && isCheckString(jsonData.getString("profile_picture"))) {
                userData.setmAvatarUser(jsonData.getString("profile_picture"));
            }

            // Set value of Full name
            if (jsonTotal.has("data") && isCheckString(jsonData.getString("full_name"))) {
                userData.setmFullname(jsonData.getString("full_name"));
            }

            // Set value of Media
            if (jsonData.has("counts") && isCheckString(jsonCounts.getString("media"))) {
                userData.setmMedia(jsonCounts.getString("media"));
            }

            // Set value of Follows
            if (jsonData.has("counts") && isCheckString(jsonCounts.getString("follows"))) {
                userData.setmFollows(jsonCounts.getString("follows"));
            }

            // Set value of Followed_by
            if (jsonData.has("counts") && isCheckString(jsonCounts.getString("followed_by"))) {
                userData.setmFollowed_by(jsonCounts.getString("followed_by"));
            }
            mTransferDataListener.setUserInformation(userData);
        }
    }
}
