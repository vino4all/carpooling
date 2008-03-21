package bg.android.messages;

import android.database.Cursor;
import bg.android.coVoiturage.Car;

public class Message {

	private Car car = null;

	private String text = "";

	private String destination = "";

	private String prix = "";

	private boolean isSentMessage = false;

	private String idCar = "";

	private String xmppAdress = "";

	private long idMessage = System.currentTimeMillis();

	public Message() {
	}

	protected Message(Car car, boolean isSend, String text2, String destination, String prix) {
		this.car = car;
		this.text = text2;
		this.destination = destination;
		this.prix = prix;
		this.isSentMessage = isSend;
		this.xmppAdress = car.getXmppAdress();
		this.idCar = car.getIdAndroid();
	}

	protected Message(boolean isSentMessage_, String text2, String destination, String prix) {
		isSentMessage = isSentMessage_;
		this.text = text2;
		this.destination = destination;
		this.prix = prix;
	}

	public Message(Cursor c) {
		this.idMessage = c.getLong(0);
		this.text = c.getString(1);
		this.prix = c.getString(2);
		this.destination = c.getString(3);
		this.idCar = c.getString(4);
		this.xmppAdress = c.getString(5);
	}

	public String toString() {
		return "destination: " + this.destination + " prix: " + prix + " Message: " + text + " xmpp:" + xmppAdress;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public String toStringDisplay() {
		String s = "";
		if (this.isSentMessage) {
			s += "to: ";
		} else {
			s += "from: ";
		}
		if (this.car == null) {
			s += "  ";
		} else {
			s += this.car.getName();
		}
		s += " dest: " + this.destination;
		s += " prix: " + this.prix;
		s += "  " + this.text;
		return s;
	}

	public long getIdMessage() {
		return idMessage;
	}

	public void setIdMessage(long idMessage) {
		this.idMessage = idMessage;
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

	public String getIdCar() {
		if (this.car == null) {
			return this.idCar;
		}
		return this.car.getIdAndroid();
	}

	public String getXmppAdress() {
		return xmppAdress;
	}

	public void setXmppAdress(String xmppAdress) {
		this.xmppAdress = xmppAdress;
	}

}
