package bg.android;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.provider.Im;
import android.util.Log;
import bg.android.coVoiturage.Car;

import com.google.android.gtalkservice.IGTalkService;
import com.google.android.gtalkservice.IGTalkSession;
import com.google.android.gtalkservice.Presence;


public class ServiceXmppSender extends Service {

	private IGTalkSession xmppSession = null;

	private static ServiceXmppSender instance;

	/**
	 * Setup the XMPP Session using a service connection
	 */
	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			IGTalkService xmppService = IGTalkService.Stub.asInterface(service);
			try {
				xmppSession = xmppService.getDefaultSession();
				//setXmppPresence();
				setXmppAdress();
			} catch (Exception ex) {
				Log.e("bg", "ActivityDetail caught ", ex);
			}
		}

		public void onServiceDisconnected(ComponentName componentName) {
			xmppSession = null;
			Log.i("bg", "ServiceConnection. disconnected---------------------------------");

		}
	};

	public ServiceXmppSender() {
		super();
		instance = this;
		Log.w("bg4", "ServicesXmpp.constructeur "+instance);
	}

	@Override
	protected void onCreate() {
		super.onCreate();
		bindService((new Intent()).setComponent(com.google.android.gtalkservice.GTalkServiceConstants.GTALK_SERVICE_COMPONENT),  mConnection, 0);

		Log.w("bg4", "ServicesXmpp.oncreate  " + xmppSession);
	}

	public IBinder getBinder() {
		return mBinder;
	}

	// This is the object that receives interactions from clients. See
	// RemoteService for a more complete example.
	private final IBinder mBinder = new LocalBinder_bg();

	public void setXmppPresence_() {
		try {
			Preferences p = Preferences.getInstance();
			String text = null;
			if (p.getType() == Car.TYPE_VOITURE) {
				text = "I have a car , i go to " + p.getDestination() + " and look for passengers";
			} else if (p.getType() == Car.TYPE_AUTO_STOPPEUR) {
				text = "I am looking for a car  to " + p.getDestination() + " ";
			} else {

			}
			if (text == null) {
				Log.e("bg4", "No set xmpp presence !");
			} else if (xmppSession == null) {
			} else {
				
				xmppSession.setPresence(new Presence(Im.PresenceColumns.AVAILABLE, "Am here now!"));
			}
		} catch (Exception e) {
			Log.w("bg4", "Exception setPresence", e);
		}
	}

	private void setXmppAdress() {
		try {
			if (xmppSession == null) {
				Log.e("bg4", "xmpp session is null! ");
			} else {
				Log.e("bg4", "xmpp Param: Jid : " + xmppSession.getJid() + "  " + xmppSession.getUsername() + "  ");
				Preferences.getInstance().setXmppAdress(this, xmppSession.getUsername());
			}
		} catch (Exception e) {
			Log.e("bg4", "setXmppADress", e);
		}
	}

	public static ServiceXmppSender getInstance() {
		if (instance == null){
			Log.i("bg","ServiceXmppSender instance is null !");
		}
		return instance;
	}

	public IGTalkSession getXmppSession() {
		return xmppSession;
	}
	
	public String getUserName() {
		
		try {
			if (this.xmppSession==null){
				return Preferences.getInstance().getXmppAdress();
			}
			Log.e("bg4","getUserName xmppSession.isConnected() : "+xmppSession.isConnected());
			return this.xmppSession.getUsername();
		} catch (Exception e) {
			Log.e("bg4","EXception",e);
			return Preferences.getInstance().getXmppAdress();
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return this.mBinder;
	}
	
	/**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder_bg extends Binder {
    	ServiceXmppSender getService() {
            return ServiceXmppSender.this;
        }
    }

}
