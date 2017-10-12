package wonolo.sgeoi.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import wonolo.sgeoi.DataApp.PostOfMediaSearchData;
import wonolo.sgeoi.DataApp.UserData;
import wonolo.sgeoi.Interface.ITransferDataListener;
import wonolo.sgeoi.ParseJSON.ParseJSONDataUser;
import wonolo.sgeoi.R;

public class SplashScreenActivity extends AppCompatActivity implements ITransferDataListener {

    private Animation mAnimationBlink;
    private Intent mIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mIntent = new Intent(SplashScreenActivity.this,HomeActivity.class);

        // animation blink
        mAnimationBlink = AnimationUtils.loadAnimation(this,R.anim.animation_blink);

        TextView textView = (TextView) findViewById(R.id.tv_wonolo_challenge);
        textView.startAnimation(mAnimationBlink);
        if(isOnline()){
            try {
                new ParseJSONDataUser(this,this).execute();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1500);
                        mIntent.putExtra("IS_INTERNET","NO INTERNET");
                        startActivity(mIntent);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }
    }

    @Override
    public void setUserInformation(UserData user) {
            addUserDataToSharedPreference(user);
            mIntent.putExtra("IS_INTERNET","INTERNET");
            mIntent.putStringArrayListExtra("USER",user.getArrayUserData());
            startActivity(mIntent);
    }

    @Override
    public void setUserInformationMediaSearch(ArrayList<PostOfMediaSearchData> list) {

    }
    public void addUserDataToSharedPreference(UserData userData){
        SharedPreferences sharedPreferences = getSharedPreferences("DataSharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("AVATAR_USER", userData.getmAvatarUser());
        editor.putString("USER_NAME", userData.getmUsername());
        editor.putString("FULL_NAME", userData.getmFullname());
        editor.putString("USER_ID", userData.getmUserID());
        editor.putString("FOLLOWS", userData.getmFollows());
        editor.putString("FOLLOWED_BY", userData.getmFollowed_by());
        editor.putString("MEDIA", userData.getmMedia());
        editor.commit();
    }
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
