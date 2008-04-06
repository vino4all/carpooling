package bg.android.test.simulator;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Random;
import java.util.Vector;

import com.bg.util2.logger.LoggerFactoryBg;


public class MainTestSimulatorCharge implements Runnable {

	private boolean isOn = true;

	private Vector<String> vUrl = new Vector();

	public MainTestSimulatorCharge() {
		for(int i=0; i<20;i++){
			(new  Thread(this)).start();
		}
	}

	private Random random = new Random(125);

	static private java.util.logging.Logger logger = LoggerFactoryBg.getLogger("logsTestAndroidServer.txt");

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		logger.info("===============================  START ===========================");
		System.out.println("MAin start");
		MainTestSimulatorCharge instance = new MainTestSimulatorCharge();
		instance.generateRquest();
	}

	private synchronized void generateRequest(int i) throws Exception {
		int latitudeE6 = random.nextInt(120000000) - 60000000;
		int longitudeE6 = random.nextInt(360000000) - 180000000;
		String xmpp = "bertrand" + i + "@gmail.com";
		String urlStr = gtUrl(xmpp, latitudeE6, longitudeE6);
		this.vUrl.add(urlStr);
		this.notifyAll();
	}

	int ii = 0;

	private void envoiRequest(String urlStr) {
		try {
			long timeStart = System.currentTimeMillis();
			URL url = new URL(urlStr);
			URLConnection connection = url.openConnection();
			int c = 0;
			int nbC = 0;
			StringBuffer buff = new StringBuffer();
			while ((c = connection.getInputStream().read()) != -1) {
				nbC++;
				buff.append((char) c);
			}
			String r = new String(buff);
			String ok = "yyy";
			if (r.toLowerCase().indexOf("exception") >= 0) {
				ok = "exc";
			}
			logger.info("===============================" + r);

			long duree = System.currentTimeMillis() - timeStart;
			ii++;
			System.out.println("generateReques : " + justifiedRight("" + ii, 4) + "| Ok:" + ok + " | nbC : " + nbC + " | duree " + justifiedRight("" + duree, 8) + " | URL:" + url);
		} catch (Exception e) {
			System.out.println("generateReques Exception : " + e.getMessage());
		}

	}

	private void generateRquest() {
		int n = 5000;
		for (int i = 0; i <= n; i++) {
			try {
				generateRequest(i);
			} catch (Exception e) {
				System.out.println("generateREquestErreur :" + e.getMessage());
			}
		}
	}

	private String gtUrlDefault() {
		return gtUrl("bertrand.guiral@gmail.com", 37421902, -122101202);
	}

	private String gtUrl(String xmppAdress, int latitudeE6, int longitudeE6) {
		xmppAdress = URLEncoder.encode(xmppAdress);
		String url = "http://dev.java-consultant.com/bgAndroid/service?";
		url += "action=getCars&ua=Android.bg.1&type=2";
		url += "&idAndroid=" + xmppAdress;
		url += "&latitudeE6=" + latitudeE6;
		url += "&longitudeE6=" + longitudeE6;
		url += "&latitudeMapCenter=" + latitudeE6;
		url += "&longitudeMapCenter=" + longitudeE6;
		url += "&latitudeSpan=422750&longitudeSpan=878906";
		url += "&name=SHubaka";
		url += "&destination=Paris&prix=120+Euros&isHidden=false";
		url += "&xmppAdress=" + xmppAdress;
		url += "&telephone=00000000";
		return url;
	}

	/**
	 * 
	 */
	public static String justifiedRight(String s, int n) {
		String blancs = "";
		if (s == null)
			s = "";
		for (int i = 0; i < n - s.length(); i++) {
			blancs += " ";
		}
		return blancs + s;
	}

	private synchronized void attendre() {
		try {
			this.wait();
		} catch (InterruptedException e) {
		}
	}

	public void run() {
		while (isOn) {
			while (this.vUrl.size() > 0) {
				String urlStr = vUrl.remove(0);
				envoiRequest(urlStr);
			}
		}

	}

}
