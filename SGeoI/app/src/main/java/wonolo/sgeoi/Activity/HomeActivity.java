package wonolo.sgeoi.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import wonolo.sgeoi.R;

    /*
    * Instagram limit permisstion, only add user to "Sandbox", that user must also registered to
    * Instagram API and that user must public information. This is condition of get information
    * from user.
    *
    * Describes how the app works:
    * 1. SplashScreenActivity get JSON data of account.
    * 2. Transmits this data -> HomeActivity. Here, get JSON to get all userID of the Follows.
    * 3. When click button "Go To Map" -> MapActivity.
    * 4. Here, get all post of the Follows from userID and add marker.
    * 5. When click marker -> show information of post.
    * 6. When click information of post -> show detail information of post (InfoWindowDetailActivity).
    * */

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private CircleImageView mAvatarUsernameHome;
    private TextView mUsernameHome;
    private TextView mFullnameHome;
    private TextView mMediaHome;
    private TextView mFollowsHome;
    private TextView mFollowed_byHome;
    private Button mGotoMapButtonHome;
    private String mUserIDHome;
    private ArrayList<String> mArrayUserDataHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAvatarUsernameHome = (CircleImageView) findViewById(R.id.imgv_avatar_user);
        mUsernameHome = (TextView) findViewById(R.id.tv_username);
        mFullnameHome = (TextView) findViewById(R.id.tv_fullname);
        mMediaHome = (TextView) findViewById(R.id.tv_media);
        mFollowsHome = (TextView) findViewById(R.id.tv_follows);
        mFollowed_byHome = (TextView) findViewById(R.id.tv_followed_by);
        mGotoMapButtonHome = (Button) findViewById(R.id.btn_gotomap);

        try {
            Bundle bundle = getIntent().getExtras();
            mArrayUserDataHome = getIntent().getStringArrayListExtra("USER");

            // if no internet -> get file sharedPreferences
            if(bundle.getString("IS_INTERNET").equals("NO INTERNET") && !isOnline()){
                Toast.makeText(this,"NO INTERNET",Toast.LENGTH_LONG).show();
                try {
                    SharedPreferences sharedPreferences = getSharedPreferences("DataSharedPreferences", Context.MODE_PRIVATE);
                    mUserIDHome = sharedPreferences.getString("USER_ID","");
                    mUsernameHome.setText(sharedPreferences.getString("USER_NAME",""));
                    mFullnameHome.setText(sharedPreferences.getString("FULL_NAME",""));
                    mFollowsHome.setText(sharedPreferences.getString("FOLLOWS",""));
                    mFollowed_byHome.setText(sharedPreferences.getString("FOLLOWED_BY",""));
                    mMediaHome.setText(sharedPreferences.getString("MEDIA",""));
                }
                catch (NullPointerException e){
                    e.printStackTrace();
                }
            } else {
                try {
                    Picasso.with(getApplication()).load(mArrayUserDataHome.get(0)).into(mAvatarUsernameHome);
                    mUsernameHome.setText(mArrayUserDataHome.get(1));
                    mFullnameHome.setText(mArrayUserDataHome.get(2));
                    mUserIDHome = mArrayUserDataHome.get(3);
                    mFollowsHome.setText(mArrayUserDataHome.get(4));
                    mFollowed_byHome.setText(mArrayUserDataHome.get(5));
                    mMediaHome.setText(mArrayUserDataHome.get(6));
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }

        // Set click button go to Map --> function onClick
        mGotoMapButtonHome.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_gotomap:
                Intent intent = new Intent(HomeActivity.this,MapActivity.class);
                if ( isOnline()){
                    intent.putExtra("USER_ID", mUserIDHome);
                    startActivity(intent);
                    break;
                } else {
                    Toast.makeText(this,"NO INTERNET",Toast.LENGTH_LONG).show();
                }
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
