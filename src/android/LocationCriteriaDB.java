package criteriadb_plugin_cordova;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

//import criteriadb_plugin_cordova.LocationCriteriaDB.DataBaseHelper;/***borrar esto cuando este en el plugin*/
//import criteriadb_plugin_cordova.LocationCriteriaDB.MyLocation;/**borrar esto cuando este en el plugin*/

/**
 * This class echoes a string called from JavaScript.
 */
public class LocationCriteriaDB extends CordovaPlugin {

    CallbackContext newCallbackContext;
    LocationManager locationManager;
    private static final int REQUEST_CODE_ENABLE_PERMISSION = 55433;
    private int SECOND = 1000;
    private DataBaseHelper db;
    private static final String TAG="LocationCriteriaDB";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        Log.e(TAG,"init Plugin");
        db = new DataBaseHelper(cordova.getContext());
        newCallbackContext=callbackContext;//set to newCallbackContext the callbackContext for use anywhere in this class
        locationManager = (LocationManager) cordova.getActivity().getSystemService(Context.LOCATION_SERVICE);//Assign us the identifier we will use (LOCATION_SERVICE) in this case
        if (action.equals("startTrackLocation")) {
            Log.e(TAG,"init if(startTrackLocation)");
            String options = args.getString(0);
            this.startTrackLocation(options,newCallbackContext);
            return true;
        }
        if(action.equals("stopTrackLocation")){
            this.stopTrackLocation(newCallbackContext);
            return true;
        }
        if(action.equals("getDB")){
            this.getDB(newCallbackContext);
            return true;
        }
        if(action.equals("deleteAllDB")){
            this.deleteAllDB(newCallbackContext);
            return true;
        }
        return false;
    }


    /**This class delete all data into database*/
    private void deleteAllDB(CallbackContext callbackContext) {
        db.deleteAllLocation();
    }

    /**This class get all data into database of the person's location saved*/
    private void getDB(CallbackContext callbackContext) {
        List<MyLocation> allLocation = db.getAllLocation();
        String result = "[";
        for(MyLocation current : allLocation) {
            result += "{'lat':'"+current.getLatitude()+"','lng':'"+current.getLongitude()+"'}";
            Log.e(TAG,result);
        }
        result +="]";

        callbackContext.success(result);

    }

    /**This class stop the track*/
    private void stopTrackLocation(CallbackContext callbackContext) {
        locationManager.removeUpdates(locationListenerBest);
    }

    /**This class will track the person's location and store it in an internal database (SQLite)*/
    private void startTrackLocation(String message, CallbackContext callbackContext) {
        Log.e(TAG,"init startTrackLocation");
        if(!cordova.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) && !cordova.hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)){
            cordova.requestPermissions(this,REQUEST_CODE_ENABLE_PERMISSION,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION});
            initTracker();
        }else{
            initTracker();
        }
    }

    @SuppressLint("MissingPermission")
    private void initTracker(){
        Log.e(TAG,"init initTracker");
        Criteria criteria=new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);//Puede ser una opcion del JSONObject
        criteria.setBearingRequired(true);//Puede ser una opcion del JSONObject
        criteria.setCostAllowed(false);
        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);//Puede ser una opcion del JSONObject

        String provider = locationManager.getBestProvider(criteria,true);

        if(provider != null){
            Log.e(TAG,"there are provider");
            locationManager.requestLocationUpdates(provider, 5 * SECOND, 10,locationListenerBest);
        }else{
            Log.e(TAG,"there are provider");
        }
        Log.e(TAG,"end initTracker");
    }

    private final LocationListener locationListenerBest = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG,"onLocationChanged");
            MyLocation myLocation=new MyLocation(String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()));
            if(db.insertMyLocation(myLocation) != 0){
                Log.e(TAG,"insert location");
            }else{
                Log.e(TAG,"not register location");
            }

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };


}
