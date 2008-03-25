package bg.android;

import android.app.ApplicationContext;
import android.content.SharedPreferences;
import android.telephony.TelephonyProperties;
import android.util.Log;

import com.google.android.maps.Point;

public class Preferences {
	private boolean isInitialized = false;

	public static final int TYPE_UNKNOW = 0;

	public static final int TYPE_AUTO_STOPPEUR = 1;

	public static final int TYPE_VOITURE = 2;

	public static final int LOCALIZATOR_GPS = 0;

	public static final int LOCALIZATOR_MAP = 1;

	public static final String PREFERENCES_NAME = "bgMap";

	private int type = TYPE_AUTO_STOPPEUR;

	private int zoomLevel = 2;

	private Point myLocation = new Point((int) (45.416402 * 1000000), (int) (1.025078 * 1000000));

	private Point centreEcran;

	private int latitudeSpan = 0;

	private int longitudeSpan = 0;

	private String name = "";

	private String destination = "";

	private String prix = "";

	private String xmppAdress = "";

	private static Preferences instance;

	private boolean isHidden = true;
	private boolean isPhoneNumberVisible = true;
	private String phoneNumber="00000000";
	private int localizator = 0;

	private Preferences() {
		instance = this;
	}

	public int getType() {
		return type;
	}

	public static Preferences getInstance() {
		if (instance == null) {
			Log.w("bg", "! Preferences instance is null!!!");
			new Preferences();
		}
		return instance;
	}

	public void init(ApplicationContext aContext) {
		if (isInitialized) {
			return;
		}
		try {
			SharedPreferences settings = aContext.getSharedPreferences(PREFERENCES_NAME, 0);
			int latitudeE6 = settings.getInt("latitudeE6", (int) (45.416402 * 1000000));
			int longitudeE6 = settings.getInt("longitudeE6", (int) (1.025078 * 1000000));
			this.type = settings.getInt("type", TYPE_AUTO_STOPPEUR);
			this.myLocation = new Point(latitudeE6, longitudeE6);
			this.centreEcran = this.myLocation;
			this.latitudeSpan = settings.getInt("latitudeSpan", 50);
			this.longitudeSpan = settings.getInt("longitudeSpan", 50);
			this.zoomLevel = settings.getInt("zoomLevel", 2);
			this.name = settings.getString("name", "");
			this.destination = settings.getString("destination", "");
			this.prix = settings.getString("prix", "");
			this.xmppAdress = settings.getString("xmppAdress", "");
			this.isHidden = settings.getBoolean("isHidden", false);
			this.localizator = settings.getInt("localizator", 0);
			this.phoneNumber = settings.getString("phoneNumber", this.getSystemTelephone());
			this.isPhoneNumberVisible=settings.getBoolean("isPhoneNumberVisible", true);
			isInitialized = true;
		} catch (Exception e) {
			Log.e("bg", "Exception Preference init", e);
		}
		Log.i("bg", "init prferences done : " + this.toString());
	}

	public void setType(ApplicationContext aContext, int type_) {
		this.type = type_;
		put(aContext, "type", type_);
	}

	public Point getMyLocation() {
		return myLocation;
	}

	public void setZoomLevelAndSpan(ApplicationContext aContext, int latitudeSpan_, int longitudeSpan_, int zoomLevel_) {
		this.latitudeSpan = latitudeSpan_;
		this.longitudeSpan = longitudeSpan_;
		this.zoomLevel = zoomLevel_;
		Log.i("bg", "setZoomLevelAndSpan latitudeSpan_: " + latitudeSpan + "  longitudeSpan_:" + longitudeSpan + " zoomLevel:" + zoomLevel);
		SharedPreferences settings = aContext.getSharedPreferences(PREFERENCES_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("latitudeSpan", latitudeSpan);
		editor.putInt("longitudeSpan", longitudeSpan);
		editor.putInt("zoomLevel", zoomLevel);
		editor.commit();
	}

	public void setMyLocation(ApplicationContext aContext, Point myLocation_) {
		this.myLocation = myLocation_;
		SharedPreferences settings = aContext.getSharedPreferences(PREFERENCES_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		this.myLocation = myLocation_;
		editor.putInt("latitudeE6", this.myLocation.getLatitudeE6());
		editor.putInt("longitudeE6", this.myLocation.getLongitudeE6());
		editor.commit();
	}

	public void setLocalizator(ApplicationContext aContext, int localizator_) {
		this.localizator = localizator_;
		put(aContext, "localizator", this.localizator);
	}

	public void setZoomLevel(ApplicationContext aContext, int level) {
		this.zoomLevel = level;
		put(aContext, "zoomLevel", this.zoomLevel);
	}

	public int getZoomLevel() {
		return zoomLevel;
	}

	public int getLatitudeSpan() {
		return latitudeSpan;
	}

	public int getLongitudeSpan() {
		return longitudeSpan;
	}

	public String getName() {
		return name;
	}

	public void setName(ApplicationContext aContext, String name) {
		this.name = name;
		putString(aContext, "name", this.name);
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(ApplicationContext aContext, String destination) {
		this.destination = destination;
		putString(aContext, "destination", this.destination);
	}

	public String getPrix() {
		return prix;
	}

	public void setPrix(ApplicationContext aContext, String prix) {
		this.prix = prix;
		putString(aContext, "prix", this.prix);
	}

	public String toString() {
		return "Preferences name: " + this.name + "  dest: " + this.destination + "  prix:" + this.prix + " latitude: " + this.myLocation.getLatitudeE6() + "  longitude: " + this.myLocation.getLongitudeE6() + " xmppAdress: " + this.xmppAdress;
	}

	private void putString(ApplicationContext aContext, String key, String value) {
		SharedPreferences settings = aContext.getSharedPreferences(PREFERENCES_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(key, value);
		Log.i("bg", "Preference setString :" + key + " : " + value);
		editor.commit();
	}

	private void putInt(ApplicationContext aContext, String key, int value) {
		SharedPreferences settings = aContext.getSharedPreferences(PREFERENCES_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(key, value);
		Log.i("bg", "Preference setString :" + key + " : " + value);
		editor.commit();
	}

	private void putBoolean(ApplicationContext aContext, String key, boolean value) {
		SharedPreferences settings = aContext.getSharedPreferences(PREFERENCES_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(key, value);
		Log.i("bg", "Preference setString :" + key + " : " + value);
		editor.commit();
	}

	private void put(ApplicationContext aContext, String key, int value) {
		SharedPreferences settings = aContext.getSharedPreferences(PREFERENCES_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(key, value);
		Log.i("bg", "Preference setInt :" + key + " : " + value);
		editor.commit();
	}

	public boolean isInsideMapSpan(int latitudeE6, int longitudeE6) {
		if (this.centreEcran == null) {
			Log.w("bg", "centreEcran is null!");
			centreEcran = myLocation;
		}
		int delta_latitude = Math.abs(latitudeE6 - centreEcran.getLatitudeE6());
		int delta_longi = Math.abs(longitudeE6 - centreEcran.getLongitudeE6());
		boolean isLatitudeOK = (2 * delta_latitude) < this.latitudeSpan;
		boolean isLongitudeOK = (2 * delta_longi) < this.longitudeSpan;
		boolean isOk = isLatitudeOK && isLongitudeOK;
		// Log.i("bg"," isInsideMapSpan "+isOk+"
		// delta_latitude:"+delta_latitude+" latitudeSpan: "+this.latitudeSpan+"
		// zoomLevel:"+this.zoomLevel);
		return isOk;
	}

	public Point getCentreEcran() {
		if (this.centreEcran == null) {
			return this.myLocation;
		}
		return this.centreEcran;
	}

	public void setCentreEcran(Point centreEcran, int latitudeSpan, int longitudeSpan) {
		this.centreEcran = centreEcran;
		this.latitudeSpan = latitudeSpan;
		this.longitudeSpan = longitudeSpan;
	}

	public String getXmppAdress() {
		return this.xmppAdress;
	}

	public void setXmppAdress(ApplicationContext aContext, String myXmppAdress) {
		this.xmppAdress = myXmppAdress;
		this.putString(aContext, "xmppAdress", myXmppAdress);
	}

	public boolean isHidden() {
		return isHidden;
	}

	public void setHidden(ApplicationContext aContext, boolean isHidden) {
		this.isHidden = isHidden;
		this.putBoolean(aContext, "isHidden", isHidden);
	}

	public void setHidden_flipflop(ApplicationContext aContext) {
		this.isHidden = !this.isHidden;
		this.putBoolean(aContext, "isHidden", isHidden);
	}

	public boolean isLocateByGPS() {
		return this.localizator == LOCALIZATOR_GPS;
	}

	public boolean isLocateByMAP() {
		return this.localizator == LOCALIZATOR_MAP;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(ApplicationContext aContext,String phoneNumber) {
		this.phoneNumber = phoneNumber;
		this.putString(aContext, "xmppAdress", phoneNumber);
	}

	public boolean isPhoneNumberVisible() {
		return isPhoneNumberVisible;
	}

	public void setPhoneNumberVisible(ApplicationContext aContext,boolean isPhoneNumberVisible) {
		this.isPhoneNumberVisible = isPhoneNumberVisible;
		this.putBoolean(aContext, "isPhoneNumberVisible", isPhoneNumberVisible);
	}

	private static String getSystemTelephone() {
		String tel = System.getProperty(TelephonyProperties.PROPERTY_LINE1_NUMBER);
		if (tel == null) {
			return "00000000";
		}
		return tel;

	}
}
