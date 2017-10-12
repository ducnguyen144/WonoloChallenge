package wonolo.sgeoi.ParseJSON;


import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import wonolo.sgeoi.DataApp.InstagramData;
import wonolo.sgeoi.DataApp.PostOfMediaSearchData;
import wonolo.sgeoi.Interface.ITransferDataListener;
import wonolo.sgeoi.ReadContentURL_and_CheckNullString.ReadContentURL_CheckNullString;

public class ParseJSONDataMediaSearch extends ReadContentURL_CheckNullString{
    private Context mContext;
    private ITransferDataListener mTransferDataListener;
    private ArrayList<PostOfMediaSearchData> mListPostMediaSearch;
    private double mLatitude;
    private double mLongitude;
    public ParseJSONDataMediaSearch(Context context, double latitude, double longtitude, ITransferDataListener listener) {
        mContext = context;
        mLatitude = latitude;
        mLongitude = longtitude;
        mTransferDataListener = listener;
    }
    public void execute() throws UnsupportedEncodingException {
        new ReadJSON().execute(createUrl());
    }

    private String createUrl() throws UnsupportedEncodingException {
        return InstagramData.MEDIA_SEARCH + "lat=" + mLatitude + "&lng=" + mLongitude
                + "&access_token=" + InstagramData.TOKEN + "&distance=5000";
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
                parseJSonDataUserMediaSearch(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseJSonDataUserMediaSearch(String data) throws JSONException {
        if (data.equals(null)) {
            Toast.makeText(mContext, "Not data", Toast.LENGTH_LONG).show();
        } else {
            mListPostMediaSearch = new ArrayList<>();

            // Object JSON total of content data
            JSONObject jsonTotal = new JSONObject(data);

            // Object JSON of "data"
            JSONArray jsonArrayData = jsonTotal.getJSONArray("data");

            // Array JSON of "data"
            for (int i = 0; i < jsonArrayData.length(); i++) {
                PostOfMediaSearchData userDataMediaRecent = new PostOfMediaSearchData();
                JSONObject jsonUserDataMediaRecent = jsonArrayData.getJSONObject(i);

                JSONObject jsonUser = jsonUserDataMediaRecent.getJSONObject("user");

                JSONObject jsonImages = jsonUserDataMediaRecent.getJSONObject("images");

                JSONObject jsonImages_Low_Resolution = jsonImages.getJSONObject("low_resolution");

                JSONObject jsonLocation;
                JSONObject jsonCaption;

                // Create Object -> check null because when Object JSON not data -> ObjectJSON = null
                Object location = jsonUserDataMediaRecent.get("location");
                Object caption = jsonUserDataMediaRecent.get("caption");

                // If object not location --> next object in array
                if (location != JSONObject.NULL) {
                    jsonLocation = jsonUserDataMediaRecent.getJSONObject("location");
                    if (caption != JSONObject.NULL) {
                        jsonCaption = jsonUserDataMediaRecent.getJSONObject("caption");
                        // Set value of content caption
                        if (jsonCaption.has("text") && isCheckString(jsonCaption.getString("text"))) {
                            userDataMediaRecent.setmContentMediaSearch(jsonCaption.getString("text"));
                        }
                    }
                } else {
                    continue;
                }

                // Set value of ID caption
                if (jsonUserDataMediaRecent.has("id") && isCheckString(jsonUserDataMediaRecent.getString("id"))) {
                    userDataMediaRecent.setmIDCaptionMediaSearch(jsonUserDataMediaRecent.getString("id"));
                }
                // Set value of Latitude
                if (jsonUserDataMediaRecent.has("location") && isCheckString(jsonLocation.getString("latitude"))) {
                    userDataMediaRecent.setmLatitudeMediaSearch(jsonLocation.getDouble("latitude"));
                }

                // Set value of Longitude
                if (jsonUserDataMediaRecent.has("location") && isCheckString(jsonLocation.getString("longitude"))) {
                    userDataMediaRecent.setmLongitudeMediaSearch(jsonLocation.getDouble("longitude"));
                }

                // Set value of NameLocation
                if (jsonUserDataMediaRecent.has("location") && isCheckString(jsonLocation.getString("name"))) {
                    userDataMediaRecent.setmNameLocationMediaSearch(jsonLocation.getString("name"));
                }

                // Set value of Username
                if (jsonUserDataMediaRecent.has("user") && isCheckString(jsonUser.getString("username"))) {
                    userDataMediaRecent.setmUsernameMediaSearch(jsonUser.getString("username"));
                }

                // Set value of AvaterUser
                if (jsonUserDataMediaRecent.has("user") && isCheckString(jsonUser.getString("profile_picture"))) {
                    userDataMediaRecent.setmAvatarUserMediaSearch(jsonUser.getString("profile_picture"));
                }

                // Set value of UserID
                if (jsonUserDataMediaRecent.has("user") && isCheckString(jsonUser.getString("id"))) {
                    userDataMediaRecent.setmUserIDMediaSearch(jsonUser.getString("id"));
                }

                // Set value of Image
                if (jsonImages.has("low_resolution") && isCheckString(jsonImages_Low_Resolution.getString("url"))) {
                    userDataMediaRecent.setmImagePostMediaSearch(jsonImages_Low_Resolution.getString("url"));
                }
                mListPostMediaSearch.add(userDataMediaRecent);
            }
        }
        mTransferDataListener.setUserInformationMediaSearch(mListPostMediaSearch);
    }
}
