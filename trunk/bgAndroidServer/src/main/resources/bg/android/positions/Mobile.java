package bg.android.positions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.logging.Logger;


import com.bg.util2.logger.LoggerFactoryBg;



public class Mobile {

	private static Logger logger = LoggerFactoryBg.getLogger("Mobile");
	public static Logger logger2 = LoggerFactoryBg.getLogger("MobileIsInsideSpan");
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	static {
		logger.info("==================== Mobile ==============");
		logger2.info("======================= Mobile ===========");
	}
	public static final int TYPE_UNKNOW = 0;

	public static final int TYPE_AUTO_STOPPEUR = 1;

	public static final int TYPE_VOITURE = 2;
	
	public static final String UA_SIMU="simu";

	private int type = TYPE_UNKNOW;

	private String idAndroid = "";

	private int longitudeE6 = 0;

	private int latitudeE6 = 0;

	private String destination = "";

	private String prix = "";

	private String xmppAdress = "";

	private String telephone = "";
	
	private Date date = new Date();

	private String name = "";
	private String ua = "";
	private boolean isHidden = false;

	public Mobile(int type_, String id_android, int longitudeE6_, int latitudeE6_, String destination_, String prix_, String xmppAdress_, String telephone_, String name_) {
		this.type = type_;
		this.idAndroid = id_android;
		this.longitudeE6 = longitudeE6_;
		this.latitudeE6 = latitudeE6_;
		this.destination = destination_;
		this.prix = prix_;
		this.xmppAdress = xmppAdress_;
		this.telephone = telephone_;
		this.name = name_;
	}

	public Mobile() {

	}

	public Mobile(String idAndroid_) {
		this.idAndroid = idAndroid_;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getId() {
		return idAndroid;
	}

	public void setId(String id) {
		this.idAndroid = id;
	}

	public int getLongitudeE6() {
		return longitudeE6;
	}

	public void setLongitudeE6(int longitudeE6) {
		this.longitudeE6 = longitudeE6;
	}

	public int getLatitudeE6() {
		return latitudeE6;
	}

	public void setLatitudeE6(int latitudeE6) {
		this.latitudeE6 = latitudeE6;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getPrix() {
		return prix;
	}

	public void setPrix(String prix) {
		this.prix = prix;
	}

	public String getXmppAdress() {
		return xmppAdress;
	}

	public void setXmppAdress(String xmppAdress) {
		this.xmppAdress = xmppAdress;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String toXml() {
		// s += "<car id=\"1235\" latitudeE6=\"123455\" longitudeE6=\"12345\"
		// destination=\"toulouse\" prix=\"123\"
		// xmppAdress=\"bertrand2@gmail.com\" tel=\"0682426361\"/>";
		String s = "<car ";
		s += "idAndroid=\"" + idAndroid + "\" ";
		s += "type=\"" + type + "\" ";
		s += "latitudeE6=\"" + latitudeE6 + "\" ";
		s += "longitudeE6=\"" + longitudeE6 + "\" ";
		s += "destination=\"" + destination + "\" ";
		s += "xmppAdress=\"" + xmppAdress + "\" ";
		s += "tel=\"" + telephone + "\" ";
		s += "name=\"" + name + "\" ";
		s += "prix=\""+prix+"\" ";
		s += "ua=\""+ua+"\" ";
		s += "/>";
		return s;
	}

	public String toString() {
		return " idAndroid: " + idAndroid + " | Type: " + type + " | latitudeE6: " + latitudeE6 + " | longitudeE6: " + longitudeE6+" | xmppAdress: "+xmppAdress+" | telephone: "+this.telephone+" | destination: "+this.destination+" | prix:"+this.prix+" | ua:"+this.ua+" | date: "+simpleDateFormat.format(date)+" | isHidden:"+isHidden;
	}

	
	public boolean isInsideSpan(int lati, int longi, int latitudeSpan, int longitudeSpan){
		int delta_latitude = Math.abs(this.latitudeE6-lati);
		int delta_longi    = Math.abs(this.longitudeE6-longi);
		boolean isLatitudeOK = delta_latitude<latitudeSpan;		
		boolean isLongitudeOK = delta_longi<longitudeSpan;
		boolean isOk = isLatitudeOK && isLongitudeOK;
		logger2.info("| isOk "+isOk+" | latitdeE6: "+this.latitudeE6+" | longitudeE6: "+this.longitudeE6+" | lati: "+lati+"| longi: "+longi+" |delta_latitude:"+delta_latitude+" | delta_longi: "+delta_longi+" | isLatitudeOK: "+isLatitudeOK+" | isLongitudeOK:"+isLongitudeOK);
		return isOk;
	}
	// idAndroid:1030 | action:SetLocalization | latitudeE6:44185586 |
	// longitudeE6:44185586 | name:tot | type:1

	public void parseData(String line) {
		StringTokenizer st = new StringTokenizer(line, "|");
		while (st.hasMoreTokens()) {
			String t = st.nextToken().trim();
			if (t ==null) {
			}else if (t.length() == 0) {
			} else {
				StringTokenizer st2 = new StringTokenizer(t, ":");
				String name = st2.nextToken().trim();
				String value = st2.nextToken().trim();
				if (name.equals("idAndroid")){
					this.idAndroid=value;
				}else if (name.equals("latitudeE6")){
					this.latitudeE6=Integer.parseInt(value);
				}else if (name.equals("longitudeE6")){
					this.longitudeE6=Integer.parseInt(value);
				}else if (name.equals("type")){
					this.type=Integer.parseInt(value);
				}else if (name.equals("name")){
					this.name=value;
				}else if (name.equals("xmppAdress")){
					this.xmppAdress=value;
				}else if (name.equals("telephone")){
					this.telephone=value;
				}else if (name.equals("destination")){
					this.destination=value;
				}else if (name.equals("prix")){
					this.prix=value;
				}else if (name.equals("ua")){
					this.ua=value;
				}
			}
		}
		this.logger.info("ParseDAta : ParseDAta | "+this);
	}

	public Integer distance(int latitude, int longitude) {  
		return Math.abs(this.latitudeE6-latitude) + Math.abs(this.longitudeE6-longitude);
	}

	public boolean closerThan(int latitude, int longitude, int distance) {
		return  this.distance(latitude, longitude)<distance;
	}

	public String getIdAndroid() {
		return idAndroid;
	}

	public void setIdAndroid(String idAndroid) {
		this.idAndroid = idAndroid;
	}
	
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUa() {
		return ua;
	}

	public void setUa(String ua) {
		this.ua = ua;
	}



	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean isHidden() {
		return isHidden;
	}

	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	public static String toStringHtmlTrTittle(String s) {
		return "<tr>"+s+" <td> id Android </td> <td> Type </td> <td> latitudeE6  </td> <td> longitudeE6</td> <td> xmppAdress  </td> <td> telephone</td> <td>  destination </td> <td>  prix</td> <td>  ua:</td> <td>  date: </td> <td>  isHidden</td> </tr>";
	}	
	public String toStringHtmlTr(String s) {
		return "<tr>"+s+" <td> " + idAndroid + " </td> <td>  " + type + " </td> <td> " + latitudeE6 + " </td> <td> " + longitudeE6+" </td> <td>  "+xmppAdress+" </td> <td>  "+this.telephone+" </td> <td>  "+this.destination+" </td> <td>  "+this.prix+" </td> <td> "+this.ua+"</td> <td>  "+simpleDateFormat.format(date)+"</td> <td>  "+isHidden+" </td> </tr>";

		
	}

	public boolean isRemovable(long timeInactifMax) { 
		long timeInactif = System.currentTimeMillis()- this.date.getTime();
		return (timeInactif >= timeInactifMax);
	}

	public boolean isSimu() {
		int i = this.ua.toLowerCase().indexOf("simu");
		return i>=0;
	}

	public boolean isShowable(int showOnly) {
		if (showOnly==TYPE_UNKNOW){
			return true;
		}		
		return showOnly==this.type;
	}

	
}
