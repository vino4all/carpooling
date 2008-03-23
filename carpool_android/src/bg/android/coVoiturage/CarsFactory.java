package bg.android.coVoiturage;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.telephony.TelephonyProperties;
import android.util.Log;
import bg.android.ActivityDetail;
import bg.android.ActivityMap;
import bg.android.Common;
import bg.android.Preferences;
import bg.android.ServiceXmppSender;

import com.google.android.maps.Point;

public class CarsFactory implements Runnable {

	public static final long PERIOD_REFRESH = 5L * 60L * 1000L;

	public static final String UserAgent = "Android.bg.1";

	private ActivityMap activityMap;

	private static CarsFactory instance = new CarsFactory();

	private String urlStr_ = "http://dev.java-consultant.com/bgAndroid/service";

	private SortedSet<Car> listCars = new TreeSet<Car>();

	private static final String ACTION_SetLocalization = "SetLocalization";

	private static final String ACTION_getCars = "getCars";

	private static final String ACTION_sendMessage = "sendMessage";

	private String idAndroidDefault = System.getProperty("gsm.sim.line1.number");

	private Thread thread;

	private Vector<String> queue = new Vector<String>();

	private boolean isOn = true;

	public CarsFactory() {
		super();
		this.thread = new Thread(this);
		this.thread.setDaemon(true);
		this.thread.start();
		if (idAndroidDefault == null) {
			Random random = new Random(20L);
			idAndroidDefault = "Dev000" + random.nextLong();
		}
	}

	public static CarsFactory getInstance() {
		return instance;
	}

	public static CarsFactory getInstance(ActivityMap activityMAp) {
		instance.activityMap = activityMAp;
		return instance;
	}

	public static CarsFactory getInstance(ActivityDetail activityDetail) {
		// TODO Auto-generated method stub
		return instance;
	}
	@SuppressWarnings("deprecation")
	public void sendMessage(String message) {
		message = URLEncoder.encode(message);
		String urlStr_ = getUrlRequesAction(ACTION_sendMessage, "&message=" + message);
		wsRequestToURL(urlStr_);

	}

	public void wsRequestGetCars() {
		String urlStr_ = getUrlRequesAction(ACTION_getCars, "");
		wsRequestToURL(urlStr_);
	}

	@SuppressWarnings("deprecation")
	private String getUrlRequesAction(String action, String args) {
		Common.getInstance().setConnectingServer(true);
		Preferences p = Preferences.getInstance();
		int type = p.getType();
		Point myLocation = p.getMyLocation();
		Point mapCenter = p.getCentreEcran();
		int latitudeE6 = myLocation.getLatitudeE6();
		int longitudeE6 = myLocation.getLongitudeE6();
		int latitudeMapCenter = mapCenter.getLatitudeE6();
		int longitudeMapCenter = mapCenter.getLongitudeE6();
		int latitudeSpan = p.getLatitudeSpan();
		int longitudeSpan = p.getLongitudeSpan();
		String r = "?action=" + action;
		r += "&ua=" + UserAgent;
		r += "&type=" + type;
		r += "&idAndroid=" + URLEncoder.encode(getIdAndroid());
		r += "&latitudeE6=" + latitudeE6 + "&longitudeE6=" + longitudeE6;
		r += "&latitudeMapCenter=" + latitudeMapCenter + "&longitudeMapCenter=" + longitudeMapCenter;
		r += "&latitudeSpan=" + latitudeSpan + "&longitudeSpan=" + longitudeSpan;
		r += "&name=" + URLEncoder.encode(p.getName());
		r += "&destination=" + URLEncoder.encode(p.getDestination());
		r += "&prix=" + URLEncoder.encode(p.getPrix());
		r += "&isHidden=" + Preferences.getInstance().isHidden();
		String xmpp = null;
		ServiceXmppSender sXmppSender = ServiceXmppSender.getInstance();
		if (sXmppSender == null) {
			xmpp = Preferences.getInstance().getXmppAdress();
		} else {
			xmpp = sXmppSender.getUserName();
		}
		Log.i("bg", "xmpp: " + xmpp);
		if (xmpp == null) {
		} else if (xmpp.length() == 0) {
		} else {
			r += "&xmppAdress=" + xmpp;
		}

		r += "&telephone=" + getTelephone();
		r += args;
		return urlStr_ + r;
	}

	private String getTelephone() {
		String tel = System.getProperty(TelephonyProperties.PROPERTY_LINE1_NUMBER);
		if (tel == null) {
			return "00000000";
		}
		return tel;

	}

	/**
	 * I rerturn xmpp name. Yet, it must be work, even this id is not available
	 * 
	 * @return
	 */
	public String getIdAndroid() {
		String id = Preferences.getInstance().getXmppAdress();
		if (id == null) {
			return this.idAndroidDefault;
		} else if (id.trim().length() == 0) {
			return this.idAndroidDefault;
		} else {
			return id;
		}
	}

	public void wsRequestSetLocalisation() {
		String urlStr_ = getUrlRequesAction(ACTION_SetLocalization, "");
		wsRequestToURL(urlStr_);
	}

	private void wsRequestToURL(String strURL) {
		this.queue.add(strURL);
		this.reveil();
	}

	private void wsRequestToURLThreaded(String strURL) {
		Log.i("bg", "wsRequest: " + strURL);
		URL url;
		URLConnection urlConn = null;
		try {
			url = new URL(strURL);
			urlConn = url.openConnection();
			Document doc = null;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.parse(urlConn.getInputStream());

			parseXml(doc);
		} catch (Exception ioe) {

			Log.i("bg", "wsRequest 2", ioe);
		}
		Log.i("bg", "wsRequest: done ");
		Common.getInstance().setConnectingServer(false);

	}

	protected void parseXml(Document doc) {
		NodeList nodeList = doc.getElementsByTagName("car");
		int gameSize = nodeList.getLength();

		SortedSet<Car> listCarPersistent = this.getListCarPersistantOnly();
		for (int i = 0; i < gameSize; i++) {
			Element elementCar = (Element) nodeList.item(i);
			String idAndroid = elementCar.getAttribute("idAndroid");
			Car car = getCarById(idAndroid);
			if (car == null) {
				car = new Car();
			}
			car.update(elementCar);

			Log.i("bg", " car :::::" + car);
			listCarPersistent.add(car);
		}

		this.listCars = listCarPersistent;
		updateListener();
	}

	public Car getCarById(String idAndroid) {
		return this.getCarById(this.listCars, idAndroid);
	}

	public Car getCarById(SortedSet<Car> list_, String idAndroid) {
		if (idAndroid == null) {
			return null;
		}
		idAndroid = idAndroid.trim();
		Iterator<Car> ite = list_.iterator();
		while (ite.hasNext()) {
			Car c = ite.next();
			String iA = c.idAndroid();
			if (iA == null) {
			} else if (iA.equals(idAndroid)) {
				return c;
			}
		}
		return null;
	}

	private SortedSet<Car> getListCarPersistantOnly() {
		SortedSet<Car> listCarsNew = new TreeSet<Car>();
		Iterator<Car> ite = this.listCars.iterator();
		while (ite.hasNext()) {
			Car car = ite.next();
			if (car.isPersistant()) {
				listCarsNew.add(car);
			}
		}
		return listCarsNew;
	}

	private void updateListener() {
		if (this.activityMap != null) {
			this.activityMap.updateCarList(this.listCars);
		}
	}

	public List<Car> getListCarsClone() {
		List<Car> listClone = new ArrayList<Car>();
		for (Car c : listCars) {
			listClone.add(c);
		}
		return listClone;
	}

	public void selectCar(Car car) {
		if (car == null) {
			return;
		}
		Car c = this.getCarSelected();
		if (c != null) {
			c.setSelected(false);
		}
		car.setSelected(true);
	}

	public Car selectNext2() {
		Car c = selectNext2(Common.getInstance().getShow());
		return c;
	}

	public Car selectPrevious2() {
		Car c = selectPrevious2(Common.getInstance().getShow());
		return c;
	}

	public Car getCarSelected() {
		return this.getCarSelectedInList(this.listCars);
	}

	private Car getCarSelectedInList(SortedSet<Car> list) {
		Iterator<Car> itr = list.iterator();
		while (itr.hasNext()) {
			Car c = itr.next();
			if (c.isSelected()) {
				return c;
			}
		}
		return null;
	}

	private SortedSet<Car> getList(int type) {
		SortedSet<Car> listCars2 = new TreeSet<Car>();
		Iterator<Car> itr = this.listCars.iterator();
		while (itr.hasNext()) {
			Car c = itr.next();

			if (c.isHide()) {
			} else if ((type == Common.SHOW_CAR_ONLY) && (!c.isTypeCar())) {
			} else if ((type == Common.SHOW_TRAVELLERS_ONLY) && (!c.isTypeAutostoppeur())) {
			} else if (!c.isVisibleOnMap()) {
			} else if (type == Common.SHOW_NOTHING) {
			} else {
				listCars2.add(c);
			}
		}
		return listCars2;
	}

	private Car selectNext2(int type) {
		Car carSelectedOld = this.getCarSelected();
		SortedSet<Car> listCars2 = getList(type);
		Car carNewSelected = selectNextCar2(listCars2);
		if (carSelectedOld == null) {
		} else if (carNewSelected == null) {
		} else if (carSelectedOld != carNewSelected) {
			carSelectedOld.setSelected(false);
		}
		return carNewSelected;
	}

	private Car selectPrevious2(int type) {
		Car carSelectedOld = this.getCarSelected();
		SortedSet<Car> listCars2 = getList(type);
		Car carNewSelected = selectPreviousCar2(listCars2);
		if (carSelectedOld == null) {
		} else if (carNewSelected == null) {
		} else if (carSelectedOld != carNewSelected) {
			carSelectedOld.setSelected(false);
		}
		return carNewSelected;
	}

	private Car selectNextCar2(SortedSet<Car> list) {
		if (list.size() == 0) {
			Log.i("bg", "selectNext2 list.size==0");
			return null;
		}
		Iterator<Car> itr = list.iterator();
		Car c = null;
		while (itr.hasNext()) {
			c = itr.next();
			if (c.isSelected()) {
				Car carToSelect;
				c.setSelected(false);

				if (itr.hasNext()) {

					carToSelect = itr.next();
				} else {
					carToSelect = listCars.first();
				}
				carToSelect.setSelected(true);
				return carToSelect;
			}
		}
		Car cc = list.first();
		cc.setSelected(true);
		return cc;
	}

	private Car selectPreviousCar2(SortedSet<Car> listCars2) {
		if (listCars2.size() == 0) {
			return null;
		}
		Iterator<Car> itr = listCars2.iterator();
		Car c = null;
		Car cPrevious = this.listCars.last();
		while (itr.hasNext()) {
			c = itr.next();
			if (c.isSelected()) {
				c.setSelected(false);
				cPrevious.setSelected(true);
				return cPrevious;
			}
			cPrevious = c;
		}
		cPrevious.setSelected(true);
		return cPrevious;
	}

	public void showAll() {
		Iterator<Car> itr = this.listCars.iterator();
		while (itr.hasNext()) {
			Car car = itr.next();
			car.setHiden(false);
		}

	}

	public Car getNewCar(String name, String xmppAddress, String destination, String latitudeE6Str, String longitudeE6Str, String idAndroid) {
		int latitudeE6 = 0;
		int longitudeE6 = 0;
		try {
			latitudeE6 = Integer.parseInt(latitudeE6Str);
			longitudeE6 = Integer.parseInt(longitudeE6Str);
		} catch (Exception e) {

		}
		return getNewCar(name, xmppAddress, destination, latitudeE6, longitudeE6, idAndroid);

	}

	public Car getNewCar(String name, String xmppAddress, String destination, int latitudeE6, int longitudeE6, String idAndroid) {
		Car car = new Car();
		car.setName(name);
		car.setDestination(destination);
		car.setXmppAdress(xmppAddress);
		car.setIdAndroid(idAndroid);
		this.listCars.add(car);
		return car;
	}

	private synchronized void attendre(long time) throws Exception {
		wait(time);
	}

	private synchronized void reveil() {
		notify();
	}

	public void run() {
		while (isOn) {
			try {
				if (this.queue.size() == 0) {
					this.attendre(PERIOD_REFRESH);
				}

				Log.i("bg", "run awake  queue.size :" + this.queue.size() + "  ooooooooooooooooooooooooo");
				if (this.queue.size() == 0) {
					String urlStr_ = getUrlRequesAction(ACTION_getCars, "");
					this.queue.add(urlStr_);
				}
				if (this.queue.size() == 0) {
				} else {
					String strURL = this.queue.remove(0);
					Log.i("bg", "run start :" + strURL);
					wsRequestToURLThreaded(strURL);
				}
			} catch (Exception e) {
				Log.i("bg", "CArFactory.run", e);
			}
		}
	}
}
