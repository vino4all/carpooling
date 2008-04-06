package bg.android.web;

import java.util.Random;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;


import com.bg.util2.email.smtp.ClientSmtp;
import com.bg.util2.logger.LoggerFactoryBg;


import bg.android.positions.Mobile;
import bg.android.positions.MobilesFactories;

public class BeanService {
    @SuppressWarnings("")
	private static Random random = new Random();

	private static Logger logger = LoggerFactoryBg.getLogger("BeanService");

	private static Logger loggerMessage = LoggerFactoryBg.getLogger("Messages");

	public static final String ACTION_DEFAULT = "actionDefault";

	public static final String ACTION_getCars = "getCars";
	public static final String ACTION_getCarsFromWeb = "getCarsFromWeb";

	public static final String ACTION_SetPosition = "SetLocalization";

	public static final String ACTION_reInitBuffer = "reInitBuffer";

	public static final String ACTION_simulements = "simuElements";

	public static final Object ACTION_check = "check";

	public static final Object ACTION_checkSimple = "checkSimple";

	public static final Object ACTION_setPositionSimu = "setPositionSimu";
	
	public static final Object ACTION_setPositionWeb = "setPositionWeb";

	public static final Object ACTION_sendMessage = "sendMessage";

	public static final Object ACTION_welcome = "welcome"; 

	private HttpServletRequest request;

	private String idAndroid = "0";

	private String retour = "";

	private int latitudeE6 = 0;

	private int longitudeE6 = 0;

	private int latitudeMapCenter = 0;

	private int longitudeMapCenter = 0;


	private int latitudeSpan = 0;

	private int longitudeSpan = 0;

	private String ua = "";

	private String name = "";

	private int type = 0;

	private String prix = "";

	private String destination = "";

	private String comment = "NoComment";

	private String telephone = "";

	private String xmppAdress = "";

	private String message = "";

	private String action = ACTION_DEFAULT;

	private boolean isHidden = false;
	
	private int showOnly=0;

	static {
		logger.info("================================");
	}

	public BeanService() {
		super();
	}

	public BeanService(HttpServletRequest request2) {
		this.parseRequest(request2);
	}

	public void parseRequest(HttpServletRequest request) {
		this.request = request;
		this.idAndroid = getValueOrDefault(request.getParameter("idAndroid"), this.idAndroid);
		this.ua = getValueOrDefault(request.getParameter("ua"), this.ua);
		this.action = getValueOrDefault(request.getParameter("action"), this.action);
		this.latitudeE6 = getValueOrDefaultInteger(request.getParameter("latitudeE6"), 0);
		this.longitudeE6 = getValueOrDefaultInteger(request.getParameter("longitudeE6"), 0);
		this.latitudeMapCenter = getValueOrDefaultInteger(request.getParameter("latitudeMapCenter"), latitudeE6);
		this.longitudeMapCenter = getValueOrDefaultInteger(request.getParameter("longitudeMapCenter"), longitudeE6);
		this.name = getValueOrDefault(request.getParameter("name"), "");
		this.type = getValueOrDefaultInteger(request.getParameter("type"), 0);
		this.showOnly = getValueOrDefaultInteger(request.getParameter("showOnly"), 0);
		this.latitudeSpan = getValueOrDefaultInteger(request.getParameter("latitudeSpan"), 0);
		this.longitudeSpan = getValueOrDefaultInteger(request.getParameter("longitudeSpan"), 0);
		this.prix = getValueOrDefault(request.getParameter("prix"), "");
		this.destination = getValueOrDefault(request.getParameter("destination"), "");
		this.telephone = getValueOrDefault(request.getParameter("telephone"), "");
		this.xmppAdress = getValueOrDefault(request.getParameter("xmppAdress"), "");
		this.message = getValueOrDefault(request.getParameter("message"), "");
		this.isHidden = getValueOrDefaultBoolean(request.getParameter("isHidden"), false);
		logger.info("parseRequest:" + this.toString());
	}

	private boolean getValueOrDefaultBoolean(String v, boolean defaultValue) {
		if (v == null) {
			return defaultValue;
		}
		v = v.trim();
		return v.equalsIgnoreCase("true");
	}

	private String getValueOrDefault(String v, String defaultValue) {
		if (v == null) {
			return defaultValue;
		}
		return v;
	}

	private int getValueOrDefaultInteger(String v, int defaultValue) {
		if (v == null) {
			return defaultValue;
		}
		v = v.trim();
		if (v.length() == 0) {
			return defaultValue;
		}
		int i = 0;
		try {
			i = Integer.parseInt(v);
		} catch (Exception e) {

		}
		return i;
	}

	public String getAction() {
		return action;
	}

	public void doDefault() {
		this.retour = MobilesFactories.getInstance().getMobilesXML(this.latitudeMapCenter, this.longitudeMapCenter, this.latitudeSpan, this.longitudeSpan,this.showOnly);
		
	}
	
	public void doAcknowledge() {
		this.retour=this.toStringHtml();
	}

	public String getRetour() {
		return retour;
	}

	private String toStringHtml() {
		String r ="";
		r+=" <table>";
		r +=" <tr><td> name </td><td>"+this.name+"</td></tr>";
		r +=" <tr><td> type</td><td>"+this.type+" : "+this.getTypStr()+"</td></tr>";
		r +=" <tr><td> destination</td><td>"+this.destination+"</td></tr>";
		r +=" <tr><td> prix</td><td>"+this.prix+"</td></tr>";
		r +=" <tr><td> tel</td><td>"+this.telephone+"</td></tr>";
		r +=" <tr><td> address</td><td>"+this.xmppAdress+"</td></tr>";
		r +=" <tr><td> latitude</td><td>"+this.latitudeE6+"</td></tr>";
		r +=" <tr><td> longitude</td><td>"+this.longitudeE6+"</td></tr>";
		r+= " </table>";
		return r;
	}
	

	
	
	private String getTypStr() {
		if (this.type== Mobile.TYPE_VOITURE){
			return "Car";
		}
		if (this.type== Mobile.TYPE_AUTO_STOPPEUR){
			return "Pedestrian";
		}
		return "?";
	}

	public String toString() {
		String r = "";
		r += "| idAndroid:" + this.idAndroid + " | action:" + this.action + " | latitudeE6:" + this.latitudeE6 + " | longitudeE6:" + longitudeE6;
		r += " | latitudeSpan: " + latitudeSpan + " | longitudeSpan: " + longitudeSpan;
		r += " | latitudeMapCenter: " + latitudeMapCenter+ " | longitudeMapCenter: " + longitudeMapCenter;
		r += " | name: " + name + " | telephone : " + this.telephone + " | xmppAdress : " + this.xmppAdress;
		r += " | prix:" + this.prix + " | destination:" + this.destination + " | isHidden:" + this.isHidden;
		r += " | showOnly: "+this.showOnly;
		return r;
	}

	public void doSetLocalization() {
		Mobile mobile = MobilesFactories.getInstance().getMobileByIdAndroid(this.idAndroid);
		if ((latitudeE6 == 0) && (longitudeE6 == 0)) {

		} else {
			logger.info("doSetLocalization :" + idAndroid + "  name:" + name);
			mobile.setHidden(this.isHidden);
			mobile.setLatitudeE6(latitudeE6);
			mobile.setLongitudeE6(longitudeE6);
			mobile.setPrix(prix);
			mobile.setDestination(destination);
			mobile.setName(this.name);
			mobile.setIdAndroid(this.idAndroid);
			mobile.setType(this.type);
			mobile.setXmppAdress(xmppAdress);
			mobile.setTelephone(telephone);
			mobile.setUa(ua);
		}
	}
	
	public void doSetLocalizationWeb() {
		this.idAndroid="web_"+this.xmppAdress;
		this.ua="web";
		doSetLocalization();
	}
		
	public void doSetPositionSimu() {
		int idA = random.nextInt();
		;
		this.idAndroid = "Simu_" + idA;
		this.doSetLocalization();
		Mobile mobile = MobilesFactories.getInstance().getMobileByIdAndroid(idAndroid);
		mobile.setUa(Mobile.UA_SIMU);
		this.retour = "setPosition done : " + this;
	}

	public String getComment() {
		return comment;
	}

	public void addComment(String comment_) {
		this.comment += comment_;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void doSendMessage() {
		loggerMessage.info("doSendMessage " + this + " | message:" + message);
		String t = this.toString();
		t += "| message :" + this.message;
		ClientSmtp.getInstance().sendMessage("bertrand.guiral@gmail.com", "Android Message", t);

	}

	public void doWelcome() {
		logger.info("doWelcome ");
		this.retour="welcome";
		this.comment="welcome";
	}

	

}
