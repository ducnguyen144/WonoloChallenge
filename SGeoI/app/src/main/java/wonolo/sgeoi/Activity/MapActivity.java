package wonolo.sgeoi.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import wonolo.sgeoi.DataApp.PostOfMediaSearchData;
import wonolo.sgeoi.DataApp.UserData;
import wonolo.sgeoi.GPSTracker;
import wonolo.sgeoi.InfoWindowRefresher;
import wonolo.sgeoi.Interface.ITransferDataListener;
import wonolo.sgeoi.ParseJSON.ParseJSONDataMediaSearch;
import wonolo.sgeoi.R;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnCameraMoveListener,GoogleMap.OnCameraIdleListener,
        GoogleMap.OnInfoWindowClickListener, ITransferDataListener {

    private String mUserIDPost;
    private GoogleMap mMap;
    private List<Marker> mMarkers;
    private ProgressDialog mProgressDialogMap;
    private ImageView mImagePost;
    private TextView mUsernamePost;
    private TextView mContentPost;
    private View mViewContentMarker;
    private String mContent[];
    private GPSTracker mGPSTracker;
    private LatLng mCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Dialog util loaded map
        mProgressDialogMap = new ProgressDialog(this);
        mProgressDialogMap.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mProgressDialogMap.setMessage("Loading...");
        mProgressDialogMap.show();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.myMap);
        mapFragment.getMapAsync(this);

        try {
            Bundle bundle =  getIntent().getExtras();
            mUserIDPost = bundle.getString("USER_ID");
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mGPSTracker = new GPSTracker(MapActivity.this);
        if(mGPSTracker.canGetLocation()){
            mCurrentLocation = new LatLng(mGPSTracker.getLatitude(),mGPSTracker.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mCurrentLocation,13));
        } else {
            mGPSTracker.showSettingsAlert();
        }

        mMap.setMyLocationEnabled(true);
        mMap.setOnCameraMoveListener(this);
        mMap.setOnCameraIdleListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                mViewContentMarker = getLayoutInflater().inflate(R.layout.custom_info_window,null);
                mImagePost = (ImageView) mViewContentMarker.findViewById(R.id.imgv_image_post);
                mUsernamePost = (TextView) mViewContentMarker.findViewById(R.id.tv_username_post);
                mContentPost = (TextView) mViewContentMarker.findViewById(R.id.tv_content_post);

                mContent = marker.getSnippet().split("=");
                mUsernamePost.setText(marker.getTitle());
                if(!mContent[3].equals("null")){
                    mContentPost.setText(mContent[3]);
                } else {
                    mContentPost.setText("");
                }
                Picasso.with(getApplication()).load(mContent[2]).into(mImagePost);
                return mViewContentMarker;
            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                marker.showInfoWindow();

                // Use this method -> fix click 2 times to show image, auto show image when loaded.
                Picasso.with(getApplication()).load(mContent[2]).into(mImagePost,new InfoWindowRefresher(marker));
                return true;
            }
        });
    }

    @Override
    public void setUserInformation(UserData user) {

    }

    @Override
    public void setUserInformationMediaSearch(ArrayList<PostOfMediaSearchData> list) {
        addMarkerAccordingToTheScreen(list);
    }

    @Override
    public void onCameraMove() {
    }

    @Override
    public void onCameraIdle() {
        mProgressDialogMap.dismiss();
        LatLng centerScreenLocation = mMap.getCameraPosition().target;
        try {
            new ParseJSONDataMediaSearch(this,centerScreenLocation.latitude,centerScreenLocation.longitude,this).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        String[] content_detail = marker.getSnippet().split("=");

        if (content_detail.length != 0){
            Intent intent = new Intent(MapActivity.this,InfoWindowDetailActivity.class);
            intent.putExtra("USERNAME_DETAIL",marker.getTitle());
            intent.putExtra("AVATAR_DETAIL",content_detail[0]);
            intent.putExtra("NAME_LOCATION_DETAIL",content_detail[1]);
            intent.putExtra("IMAGE_POST_DETAIL",content_detail[2]);
            if(!content_detail[3].equals("null")){
                intent.putExtra("CONTENT_DETAIL",content_detail[3]);
            } else {
                intent.putExtra("CONTENT_DETAIL","");
            }

            startActivity(intent);
        }
    }
    private void addMarkerAccordingToTheScreen(ArrayList<PostOfMediaSearchData> list) {
        // Get Lat/Lng 4 corner of screen
        LatLng bottomLeft = mMap.getProjection().getVisibleRegion().nearLeft;
        LatLng topLeft = mMap.getProjection().getVisibleRegion().farLeft;
        LatLng bottomRight = mMap.getProjection().getVisibleRegion().nearRight;
        LatLng topRight = mMap.getProjection().getVisibleRegion().farRight;

        mMarkers = new ArrayList<>();
        for(int j=0 ; j < list.size(); j++){
            PostOfMediaSearchData post = list.get(j);
            double latitude = post.getmLatitudeMediaSearch();
            double longitude = post.getmLongitudeMediaSearch();

            // if lat/lng stay at screen -> add marker
            if( (latitude <= topLeft.latitude) && ( latitude >= bottomLeft.latitude)
                    && (longitude >= topLeft.longitude) && (longitude <= topRight.longitude)){
                LatLng location = new LatLng(post.getmLatitudeMediaSearch(),post.getmLongitudeMediaSearch());

                // if my marker -> marker color -> blue
                // else marker color -> red
                if (post.getmUserIDMediaSearch().equals(mUserIDPost)){
                    mMarkers.add(mMap.addMarker(new MarkerOptions()
                            .position(location)
                            .title(post.getmUsernameMediaSearch())
                            .snippet(post.getmAvatarUserMediaSearch() + "=" + post.getmNameLocationMediaSearch()
                                    + "=" + post.getmImagePostMediaSearch() + "=" + post.getmContentMediaSearch())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))));
                } else {
                    mMarkers.add(mMap.addMarker(new MarkerOptions()
                            .position(location)
                            .title(post.getmUsernameMediaSearch())
                            .snippet(post.getmAvatarUserMediaSearch() + "=" + post.getmNameLocationMediaSearch()
                                    + "=" + post.getmImagePostMediaSearch() + "=" + post.getmContentMediaSearch())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))));
                }
            }
        }
    }
}
