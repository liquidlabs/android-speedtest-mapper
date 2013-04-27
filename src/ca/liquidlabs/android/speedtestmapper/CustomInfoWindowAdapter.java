package ca.liquidlabs.android.speedtestmapper;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ca.liquidlabs.android.speedtestmapper.model.ConnectionType;
import ca.liquidlabs.android.speedtestmapper.util.Tracer;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

import org.apache.commons.lang3.StringUtils;

public class CustomInfoWindowAdapter implements InfoWindowAdapter {
    
    private static final String LOG_TAG = CustomInfoWindowAdapter.class.getSimpleName();
    private final View mContentsView;
    
    public CustomInfoWindowAdapter(final LayoutInflater inflater){
        mContentsView = inflater.inflate(R.layout.fragment_record_info, null);
    }

    @Override
    public View getInfoContents(Marker marker) {
        Tracer.debug(LOG_TAG, "getInfoContents()");
        renderContents(marker, mContentsView);
        return mContentsView;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        Tracer.debug(LOG_TAG, "getInfoWindow()");
        return null;
    }
    
    private void renderContents(Marker marker, View view) {
        String[] snippetInfo = StringUtils.split(marker.getSnippet(), '|');
        
        TextView downloadSpeed = (TextView) view.findViewById(R.id.txt_info_download);
        downloadSpeed.setText(snippetInfo[1]);
        
        TextView uploadSpeed = (TextView) view.findViewById(R.id.txt_info_upload);
        uploadSpeed.setText(snippetInfo[2]);
        
        // Connection type - update image based on type
        ConnectionType connType = ConnectionType.fromString(snippetInfo[0]);
        if(connType.isWifi()){
            ImageView conntypeImage = (ImageView) view.findViewById(R.id.img_legend_conntype);
            conntypeImage.setImageResource(R.drawable.ic_connection_wifi);
        }
        TextView connTypeTxt = (TextView) view.findViewById(R.id.txt_info_conntype);
        connTypeTxt.setText(snippetInfo[0]);
    }

}
