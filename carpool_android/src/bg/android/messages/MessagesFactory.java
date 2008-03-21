package bg.android.messages;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import bg.android.IUpdatable;
import bg.android.coVoiturage.Car;

public class MessagesFactory {

	private DBMessageHelper dbMessages;

	
	private static MessagesFactory instance = new MessagesFactory();

	private boolean isDataBaseActived = false;

	private List<Message> listMessages = new ArrayList<Message>();

	private MessagesFactory() {
	}

	public static MessagesFactory getInstance() {
		return instance;
	}

	public void initMessages(Context ctx) {
		if (this.isDataBaseActived) {
			return;
		}
		this.dbMessages = new DBMessageHelper();
		this.dbMessages.initDBMessageHelper(ctx);
		if (this.dbMessages.getDdataBase() == null) {
			this.isDataBaseActived = false;
		} else {
			this.isDataBaseActived = true;
		}
		if (isDataBaseActived) {
			this.listMessages = this.dbMessages.fetchAllMessages();
			Log.i("bg","lisMessages: "+this.listMessages.size());
		}
	}

	public static MessagesFactory getInstance(IUpdatable updatable) {
		return instance;

	}

	public Message createMessage(Car car, boolean isSend, String text, String destination, String prix) {
		Message message = new Message(car, isSend, text, destination, prix);
		if (isDataBaseActived) {
			this.dbMessages.insertMessages(message);
		}
		this.listMessages.add(message);
		car.addHistoriqueMessages(message);
		return message;
	}

	public List<Message> getListMessages() {
		return listMessages;
	}

	public  void onDestroy() {
		if (this.isDataBaseActived){
			this.dbMessages.close();
		}
	}

	public void remove(Message message) {
		try {
			this.listMessages.remove(message);
			if (isDataBaseActived){
				this.dbMessages.deleteMessage(message.getIdMessage());
			}
		} catch (Exception e) {
			Log.i("bg","Exception Remove Message "+message,e);
		}		
	}

}
