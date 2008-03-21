package bg.android.coVoiturage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Element;

import bg.android.Preferences;
import bg.android.messages.Message;

public class Car implements Comparable<Car>{

	// <car id="1234" latitude="123455" longitude="12345" destination="toulouse" prix="123" xmmppAdress="bertrand2@gmail.com" tel="0682426361" />

	public static final int TYPE_UNKNOW = 0;

	public static final int TYPE_AUTO_STOPPEUR = 1;

	public static final int TYPE_VOITURE = 2;
	private String idAndroid;
	
	private String name;

	private int latitudeE6;

	private int longitudeE6;

	private String destination;

	private String prix ="";

	private String xmppAdress;

	private String tel;
	
	private boolean selected=false;
	
	private int type=0;
	
	private boolean isPersistant=false;
	
	private boolean isHiden=false;
	
	
	private List<Message> listMessages = new ArrayList<Message>(); 
	/**
	 * User-Agent
	 */
	private String ua="";

	public Car() {
		super();
	}

	public void update(Element elementCar) {
		this.idAndroid = elementCar.getAttribute("idAndroid");
		this.destination = elementCar.getAttribute("destination");
		this.ua = elementCar.getAttribute("ua");		
		this.latitudeE6= getElementAttributAsInt(elementCar,"latitudeE6");
		this.longitudeE6= getElementAttributAsInt(elementCar,"longitudeE6");
		this.prix= elementCar.getAttribute("prix");
		this.xmppAdress= elementCar.getAttribute("xmppAdress");
		this.tel= elementCar.getAttribute("tel");
		this.name= elementCar.getAttribute("name");
		String typeStr = elementCar.getAttribute("type");
		this.type = parseInt(typeStr, 0);
	}
	
	private int parseInt(String s, int d){
		if (s == null){
			return d;
		}
		try {
			return Integer.parseInt(s);
		} catch (Exception e) {
			return d;
		}
	}
	
	private int getElementAttributAsInt(Element element, String tag){
		String s = element.getAttribute(tag);
		if (s==null){
			return 0;
		}else if (s.length()==0){
			return 0;
		}else {
			return Integer.parseInt(s.trim());
		}
	}

	public String toString() {
		//return " Car:" + id + "  " + xmppAdress + "  " + tel;
		return " Car:  id:" + idAndroid +" name:"+name+" latitudeE6 :"+latitudeE6+" longitudeE6 : "+longitudeE6+"  destination:"+this.destination+"  prix:"+this.prix+"  isPersistant:"+isPersistant;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDestination() {
		return destination;
	}

	public String getPrix() {
		return prix;
	}

	public String getXmppAdress() {
		return xmppAdress;
	}

	public String getTel() {
		if (tel == null){
			return "";
		}
		return tel;
	}

	public int getLatitude() {
		return this.latitudeE6;
	}

	public int getLongitude() {
		return this.longitudeE6;
	}

	public boolean isSelected() {
		return this.selected;
	}

	public void setSelected(boolean selected_) {
		this.selected = selected_;
	}

	public int getType() {
		return type;
	}

	public boolean isTypeUnknow() {		
		return this.type==TYPE_UNKNOW;
	}

	public boolean isTypeCar() {
		return this.type==TYPE_VOITURE;
	}
	
	
	

	public boolean isTypeAutostoppeur() {
		return this.type==TYPE_AUTO_STOPPEUR;
	}

	public String getTypeStr_____() {
		if (isTypeUnknow()){
			return "??";
		}
		if (isTypeCar()){
			return "Voiture";
		}
		if (isTypeAutostoppeur()){
			return "Passager";
		}
		return "";
	}
	
	
	
	public String getMapTittleStr() {
		return  "  " + this.getName() + " : " + this.getPrix()+" to "+this.destination;		
	}
	
	
	

	@Override
	public int compareTo(Car c) {		
		return (c.getLongitude()-this.longitudeE6);
	}
	
	public boolean isAndroid() {
		if (this.ua==null){
			return false;
		}
		return this.ua.trim().equals(CarsFactory.UserAgent);
	}

	public boolean isPersistant() {
		return isPersistant;
	}

	public String getHistoriqueMessages(String  labelNbDeMessages) {
		String s = labelNbDeMessages+" : "+this.listMessages.size();
		Iterator<Message> ite = this.listMessages.iterator();
		while(ite.hasNext()){
			Message m =  ite.next();
			s += "\n"+m.toStringDisplay();
		}
		return s;
	}
	

	public void addHistoriqueMessages(Message message) {
		this.listMessages.add(message);
	}

	public String idAndroid() {
		return idAndroid;
	}

	public boolean isHide() {
		return isHiden;
	}

	public void setHiden(boolean isHide_) {
		this.isHiden = isHide_;
		if (this.isHiden){
			this.isPersistant=true;
		}
	}

	public String isHidenStr() {
		if (isHide()){
			return "Hidden";
		}else {
			return "";
		}
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public void setPrix(String prix) {
		this.prix = prix;
	}

	public void setXmppAdress(String xmppAdress) {
		this.xmppAdress = xmppAdress;
	}

	public String getIdAndroid() {
		return idAndroid;
	}

	public void setIdAndroid(String idAndroid) {
		this.idAndroid = idAndroid;
	}

	public boolean isVisibleOnMap() {
		return Preferences.getInstance().isInsideMapSpan(this.latitudeE6, this.longitudeE6);
	}
}
