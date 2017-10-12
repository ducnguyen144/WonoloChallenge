package wonolo.sgeoi.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import wonolo.sgeoi.Interface.IOAuthDialogListener;
import wonolo.sgeoi.InstagramDialogLogin;
import wonolo.sgeoi.R;

    /*
    * Due to Instagram API limits too much permissions,
    * only login with a registered account of Instagram API,
    * so will not use LoginActivity and InstagramDialogLogin.
    * */

public class LoginActivity extends AppCompatActivity {

    private InstagramDialogLogin mInstagramDialogLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mInstagramSignInButton = (Button)findViewById(R.id.btn_instagram_sign_in);

        final String url = "https://api.instagram.com/oauth/authorize/?client_id=cc699a15478e495b9644a3d057ccfcab&redirect_uri=https://www.wonolo.com&response_type=token&scope=public_content+follower_list";

        final IOAuthDialogListener listener = new IOAuthDialogListener() {
            @Override
            public void onComplete(String code) {
                if(!code.isEmpty()){
                    mInstagramDialogLogin.dismiss();
                    Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onError(String error) {
                Toast.makeText(getApplication(),"Error",Toast.LENGTH_LONG).show();
            }
        };


        mInstagramSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInstagramDialogLogin = new InstagramDialogLogin(LoginActivity.this,url,listener);
                mInstagramDialogLogin.show();
            }
        }) ;
    }

}
