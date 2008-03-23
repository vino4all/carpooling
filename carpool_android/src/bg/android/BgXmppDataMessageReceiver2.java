package bg.android;

import android.content.Context;
import android.content.Intent;
import android.content.IntentReceiver;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import bg.android.coVoiturage.Car;
import bg.android.coVoiturage.CarsFactory;
import bg.android.messages.Message;
import bg.android.messages.MessagesFactory;

public class BgXmppDataMessageReceiver2 extends IntentReceiver {

	/* package */
	// static final String ACTION =
	// "com.google.android.samples.app.XmppDataMessageReceiver.ACTION_DATA_MESSAGE";
	public static final String ACTION = "bg.android.BgXmppDataMessageReceiver2.ACTION_DATA_MESSAGE";

	public BgXmppDataMessageReceiver2() {
		super();
	}

	public void onReceiveIntent(Context context, Intent intent) {
		Log.i("bg", "xmpp.onReceiveIntent:  ");
		String comment = "";
		if (intent.getAction().equals(ACTION)) {
			comment = "Got data message";
			Bundle bundle = intent.getExtras();

			if (bundle == null) {
				Log.i("bg", "bundle is null!");
				comment += "bundle is null!";
			} else {
				try {
					String idAndroid = bundle.getString("idAndroid");
					String text = bundle.getString("t");
					String prix = bundle.getString("prix");
					String destination = bundle.getString("destination");
					String name = bundle.getString("name");
					String fromXmpp = bundle.getString("xmpp");
					String latitudeE6Str = "";
					String longitudeE6Str = "";
					latitudeE6Str = bundle.getString("latitudeE6");
					longitudeE6Str = bundle.getString("longitudeE6");

					comment += "idAndroid:" + idAndroid + " text:" + text + " prix:" + prix + " destination:" + destination + " name:" + name;
					Car car = CarsFactory.getInstance().getCarById("" + idAndroid);
					if (car == null) {
						car = CarsFactory.getInstance().getNewCar(name, fromXmpp, destination, latitudeE6Str, longitudeE6Str, idAndroid);
					}

					if (car == null) {
						Log.e("bg", "xmpp. ! Car is null! Should never happen");
					} else {
						Message message = MessagesFactory.getInstance().createMessage(car, false, text, destination, prix);
						car.addHistoriqueMessages(message);
					}
				} catch (Exception e) {
					Log.e("bg", "xmpp. !Exception!", e);
				}
			}

			Toast.makeText(context, comment, Toast.LENGTH_LONG).show();
			Log.i("bg", "onReceiveIntent xmpp comment: " + comment);
		}
	}

}
