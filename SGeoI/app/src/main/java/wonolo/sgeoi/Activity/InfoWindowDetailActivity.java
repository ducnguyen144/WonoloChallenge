package wonolo.sgeoi.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import wonolo.sgeoi.R;

public class InfoWindowDetailActivity extends AppCompatActivity {

    private CircleImageView mAvatarUserDetail;
    private ImageView mImageDetail;
    private TextView mUserNameDetail;
    private TextView mNameLocationDetail;
    private TextView mContentDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_window_detail);

        mAvatarUserDetail = (CircleImageView) findViewById(R.id.imgv_avatar_user_detail);
        mImageDetail = (ImageView) findViewById(R.id.imgv_image_detail);
        mUserNameDetail = (TextView) findViewById(R.id.tv_username_detail);
        mNameLocationDetail = (TextView) findViewById(R.id.tv_name_location_detail);
        mContentDetail = (TextView) findViewById(R.id.tv_content_detail);

        try {
            Bundle bundle = getIntent().getExtras();
            Picasso.with(getApplication()).load(bundle.getString("AVATAR_DETAIL")).into(mAvatarUserDetail);
            Picasso.with(getApplication()).load(bundle.getString("IMAGE_POST_DETAIL")).into(mImageDetail);
            mUserNameDetail.setText(bundle.getString("USERNAME_DETAIL"));
            mNameLocationDetail.setText("Check in: " + bundle.getString("NAME_LOCATION_DETAIL"));
            if(!bundle.getString("CONTENT_DETAIL").equals("")){
                mContentDetail.setText("Caption:\n" + "         " + bundle.getString("CONTENT_DETAIL"));
            } else {
                mContentDetail.setBackgroundResource(R.color.colorHome);
            }
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }


    }
}
