package bg.android;

import java.util.Iterator;
import java.util.List;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.maps.Point;

public class ServiceLocalisationBg extends Service implements Runnable {

	
	boolean isOn = true;
	private long period=60L*1000L;
	private Location location;
	private static ServiceLocalisationBg instance; 
	
	public ServiceLocalisationBg() {
		super();
		instance = this;
		Thread t = new Thread(this);
		t.start();
	}
	
	
	@Override
	protected void onStart(int startId, Bundle arguments) {
		super.onStart(startId, arguments);
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.isOn=false;
		this.awake();
	}


	@Override
	protected void onCreate() {
		super.onCreate();
	}


	
	
	
	private synchronized void attendre(long time){
		try {
			wait(time);
		} catch (Exception e) {			
		}
	}
	
	private synchronized void awake(){
		notifyAll();
	}
	
	public Point processGPS() throws Exception{
		Location location = this.processGPSLocation();
		return getPointFromLocation(location);		
	}
	
	private Point getPointFromLocation(Location location){
		int latitudeGPS = (int) (location.getLatitude() * 1000000);
		int longitudeGPS = (int) (location.getLongitude() * 1000000);
		Point p = new Point(latitudeGPS,longitudeGPS);
		return p;
	}

	
	private synchronized Location  processGPSLocation() throws Exception{
		try {
			Object o = this.getSystemService(Context.LOCATION_SERVICE);
			LocationManager locationManager = (LocationManager) o;
			List<LocationProvider> listProvidr = locationManager.getProviders();
			Iterator<LocationProvider> ite = listProvidr.iterator();
			while(ite.hasNext()){				
				LocationProvider lp = ite.next();
				Log.i("bg","---- locationProvider : "+lp+"   "+lp.getName());
			}
			
			Criteria criteria = new Criteria();
			criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
			criteria.setCostAllowed(true);
			int accuracy_metres = 1000;
			criteria.setAccuracy(accuracy_metres);
			LocationProvider locationProvider=null;
			try {
				locationProvider = locationManager.getBestProvider(criteria);
			} catch (Exception e) {
				// Ne marche jamais
				Log.i("bg","geBestProvider Exception",e);
			}
			if ((locationProvider==null) && (listProvidr.size()>0)){
				locationProvider=listProvidr.get(0);
			}
			if (locationProvider==null){
				//String message = "No Location Provider";
				throw new Exception("! no Location Provider !");
			}
			Log.i("bg","locationProvider "+locationProvider);
			String providerName = locationProvider.getName();
			Location location_ = locationManager.getCurrentLocation(providerName);
			Log.i("bg","location: longitude: "+location_.getLongitude()+"  latitude "+location_.getLatitude());
			this.location=location_;
			return this.location;
		} catch (Exception e) {
			Log.e("bg","getLocationMAnager ",e);
			throw e;			
		}
	}
	
	
	

	@Override
	public void run() {
		while(isOn){
			attendre(period);
			Log.i("bg","ServiceLocalisationBg thread period:"+period+" ms");
			try {
				if (Preferences.getInstance().isLocateByGPS()){
					Point p = this.processGPS();					
					Preferences.getInstance().setMyLocation(this, p);
				}
			} catch (Exception e) {
				Log.w("bg","ServiceLocalisationBg Exception",e);
			}
		}
	}


	public static ServiceLocalisationBg getInstance() {
		return instance;
	}


	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
