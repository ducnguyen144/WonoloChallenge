package wonolo.sgeoi;


import com.google.android.gms.maps.model.Marker;

public class InfoWindowRefresher implements com.squareup.picasso.Callback {

    private Marker mMarkerToRefresh;

    public InfoWindowRefresher(Marker markerToRefresh) {
        this.mMarkerToRefresh = markerToRefresh;
    }

    @Override
    public void onSuccess() {
        mMarkerToRefresh.showInfoWindow();
    }

    @Override
    public void onError() {}
}
