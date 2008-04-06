package bg.android.positions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;
import java.util.logging.Logger;


import com.bg.util2.logger.LoggerFactoryBg;
import com.bg.util2.serverWeb.ServletInit;


//  bg.android.positions.MobilesFactories
//  bg.android.positions.MobilesFactories
public class MobilesFactories implements Runnable {

	private Logger logger = LoggerFactoryBg.getLogger("MobilesFactory");
	private Logger loggerError = LoggerFactoryBg.getLogger("Error");

	private Logger loggerCollector = LoggerFactoryBg.getLogger("MobilesCollector");

	private boolean isOn = true;

	private String name = "";

	private Map<String, Mobile> hMobiles = new HashMap<String, Mobile>();

	private static MobilesFactories instance;

	private String debug = "";

	private Thread thread;

	public MobilesFactories() {
		super();
		instance = this;
		initHashTable();
		logger.info("==================== init hMobiles.size : " + hMobiles.size());
		loggerError.info("==================== init hMobiles.size : " + hMobiles.size());
	}

	public void activate() {
		logger.info("==================== activate  : ==========================");
		loggerCollector.info("==================== activate  :======================= ");
		thread = new Thread(this);
		thread.setDaemon(true);
		thread.start();
	}

	public void deActivate() {
		logger.info("==================== de-activate  : ooooooooooooooooooooooooooo");
		loggerCollector.info("oooooooooooooooooooooooooo de-activate ooooooooooooooooooooo");
		try {
			this.isOn = false;
			this.thread.interrupt();

		} catch (RuntimeException e) {
			logger.info("==================== de-activate Exception  : "+ e);
			loggerCollector.info("==================== de-activate Exception  : "+ e);
		}
		this.hMobiles = null;
	}

	public static MobilesFactories getInstance() {
		if (instance == null) {
			instance = new MobilesFactories();
			instance.logger.info("--- ! instance was null ! ---");
		}
		return instance;
	}

	public void add(Mobile mobile) {
		try {
			this.hMobiles.put(mobile.getId(), mobile);
		} catch (Exception e) {
			logger.info("Exception"+e);
			loggerError.info("Exception"+e);
		}
		logger.info("addMobile| hMobiles.size:" + this.hMobiles.size() + " | " + mobile);
	}

	public String getMobilesXML() {
		String r = "<?xml version=\"1.0\" encoding=\"utf-8\" ?> \n";
		try {
			r += "<mobiles>\n";
			Iterator<Mobile> ite = this.hMobiles.values().iterator();
			while (ite.hasNext()) {
				Mobile m = ite.next();
				r += m.toXml() + "\n";
			}
			r += "</mobiles>";
		} catch (Exception e) {
			r +="<Exception> getMobilsXML :"+e.getMessage()+"<Exception>";
			logger.info("Exception"+e);
			loggerError.info("Exception"+e);
		}
		return r;
	}

	public String getMobilesXML(int latitude, int longitude, int latitudeSpan, int longitudeSpan) {
		return getMobilesXML(latitude, longitude, latitudeSpan, longitudeSpan,0);
	}
	public String getMobilesXML(int latitude, int longitude, int latitudeSpan, int longitudeSpan,int showOnly) {
				if (latitudeSpan == 0) {
			return getMobilesXML_closer(latitude, longitude, 5,showOnly);
		} else {
			return getMobilesXML_withSpan(latitude, longitude, latitudeSpan, longitudeSpan,showOnly);
		}
	}

	private String getMobilesXML_closer(int latitude, int longitude, int nbMax, int showOnly) {
		SortedMap<Integer, Mobile> sortedMobiles = new TreeMap<Integer, Mobile>();
		int distance = 50000000;
		Iterator<Mobile> ite = this.hMobiles.values().iterator();
		while (ite.hasNext()) {
			Mobile m = ite.next();
			if (m.isHidden()) {
			} else if (!m.isShowable(showOnly)){
			} else if (sortedMobiles.size() > nbMax) {
				if (m.closerThan(latitude, longitude, distance)) {
					
					sortedMobiles.put(m.distance(latitude, longitude), m);
					Integer key = sortedMobiles.lastKey();
					sortedMobiles.remove(key);
					distance = sortedMobiles.lastKey();
				}
			} else {
				sortedMobiles.put(m.distance(latitude, longitude), m);
			}
		}
		Collection<Mobile> listMobiles = sortedMobiles.values();
		this.logger.info("getMobilesXML_closer: latitude:" + latitude + " longitude: " + longitude + "  nbMAx :" + nbMax + "  nbMobiles: " + listMobiles.size());
		return toXml(listMobiles);
	}

	private String toXml(Collection<Mobile> listMobiles_) {
		String r = "<?xml version=\"1.0\" encoding=\"utf-8\" ?> \n";
		r += "<mobiles>\n";
		Iterator<Mobile> ite = listMobiles_.iterator();
		while (ite.hasNext()) {
			Mobile m = ite.next();
			r += m.toXml() + "\n";
		}
		r += "</mobiles>";
		return r;
	}

	public String getMobilesXML_withSpan(int latitude, int longitude, int latitudeSpan, int longitudeSpan, int showOnly) {
		Collection<Mobile> listM = new ArrayList<Mobile>();
		Iterator<Mobile> ite = this.hMobiles.values().iterator();
		while (ite.hasNext()) {
			Mobile m = ite.next();
			if (m.isHidden()) {
			} else if (m.isInsideSpan(latitude, longitude, latitudeSpan, longitudeSpan)) {
				if (m.isShowable(showOnly)) {
					listM.add(m);
				}
			}
		}
		Mobile.logger2.info("=================================== listM.size: " + listM.size());
		this.logger.info("getMobilesXML_withSpan: latitude:" + latitude + " longitude: " + longitude + "  latitudeSpan :" + latitudeSpan + "  longitudeSpan: " + longitudeSpan + "  nbMobiles: " + listM.size());
		return toXml(listM);
	}

	// s += "<car id=\"1235\" latitude=\"123455\" longitude=\"12345\"
	// destination=\"toulouse\" prix=\"123\" xmppAdress=\"bertrand2@gmail.com\"
	// tel=\"0682426361\"/>";

	private void initHashTable() {
		Mobile m1 = new Mobile(0, "123", 0, 0, "paris", "30", "bertrand.guiral@gmail.com", "0682426361", "toto");
		this.add(m1);
		Mobile m2 = new Mobile(0, "124", 0, 0, "paris", "30", "bertrand.guiral@gmail.com", "0682426361", "titi");
		this.add(m2);
	}

	public Mobile getMobileByIdAndroid(String idAndroid) {
		Mobile mobile = this.hMobiles.get(idAndroid);
		if (mobile == null) {
			// eventuellement regarder en bdd
			// sinon::
			mobile = new Mobile(idAndroid);
			this.add(mobile);

		}
		return mobile;
	}

	public void reInitBuffer() {
		this.hMobiles = new Hashtable<String, Mobile>();
	}

	public int simuElements() {
		int i = 0;
		BufferedReader br=null;
		try {
			File f = ServletInit.getInstance().getFileInWEB_INF("dataSimu.txt");
			FileReader fr = new FileReader(f);
			br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				Mobile m = new Mobile();
				m.parseData(line);
				this.add(m);
				i++;
			}
			br.close();
		} catch (Exception e) {
			this.logger.info("simuElements"+ e);
		}
		return i;
	}

	public Map<String, Mobile> getHMobiles() {
		return hMobiles;
	}

	private void collectMobilesInactif() {
		try {
			long timeStart = System.currentTimeMillis();
			int nbCollected = 0;
			long timeInactifMax = 5L * 60L * 1000L;
			Collection<Mobile> collectionMobilsInactif = new Vector<Mobile>();
			Iterator<Mobile> ite = this.hMobiles.values().iterator();
			while (ite.hasNext()) {
				Mobile m = ite.next();
				if (m == null) {
				} else if (m.isRemovable(timeInactifMax)) {
					collectionMobilsInactif.add(m);
					nbCollected++;
				}
			}
			long duree = System.currentTimeMillis() - timeStart;
			loggerCollector.info("collectMobilesInactif done " + "nbCollected :" + nbCollected + " duree : " + duree + "   map.size: " + this.hMobiles.size());
			Iterator<Mobile> ite2 = collectionMobilsInactif.iterator();
			while (ite2.hasNext()) {
				Mobile m = ite2.next();
				this.hMobiles.remove(m.getId());
			}
		} catch (Exception e) {
			loggerCollector.info("collectMobilesInactif Exception"+ e);
		}
	}

	public void run() {
		loggerCollector.info("==================== Thread started : ");
		while (isOn) {
			try {
				Thread.sleep(60L * 1000L);
				collectMobilesInactif();
			} catch (InterruptedException e) {
			}
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDebug() {
		return debug;
	}

	public void setDebug(String debug) {
		this.debug = debug;
	}

}
