package bg.android;




import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IServiceManager;
import android.os.ServiceManagerNative;
import android.telephony.IPhone;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Menu.Item;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import bg.android.coVoiturage.Car;
import bg.android.coVoiturage.CarsFactory;
import bg.android.messages.MessagesFactory;


public class ActivityDetail extends Activity implements OnClickListener {

	private Car car;

	private EditText editText;

	private TextView textView_HistoriqueMessages;

	private TextView textView_detailName;

	private TextView textView_detailHidden;

	private TextView textView_detailPrix;

	private TextView textView_detailTittle;

	private TextView textView_detailTelephone;

	private TextView textView_detailDestination;

	private TextView textView_detailXmpp;

	//private ImageView imageViewAndroid;

	private ImageView imageViewType;

	private Bitmap bitMapTypeAuto;

	private Bitmap bitMapTypePassager;

	private Bitmap bitMapTypeUnknow;

	
	private String prixLabel="Price";
	
	private String labelHistoriqueMessages;

	// private IXmppSession mXmppSession = null;

	public ActivityDetail() {
		super();
	}

	// private boolean xmppAvailable = false;


	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		this.car = CarsFactory.getInstance(this).getCarSelected();
		// setContentView(R.layout.main);

		if (car == null) {
			this.car = CarsFactory.getInstance(this).selectNext2();
		}
		setContentView(R.layout.activity_detail);
		//bindService((new Intent()).setComponent(com.google.android.xmppService.XmppConstants.XMPP_SERVICE_COMPONENT), null, mConnection, 0);
		((Button) findViewById(R.id.detailMap)).setOnClickListener(this);
		((Button) findViewById(R.id.detailNext)).setOnClickListener(this);
		((Button) findViewById(R.id.detailPrevious)).setOnClickListener(this);
		((Button) findViewById(R.id.detailHide)).setOnClickListener(this);
		((Button) findViewById(R.id.detailShowAll)).setOnClickListener(this);
		((Button) findViewById(R.id.detailPhoneCall)).setOnClickListener(this);
		((Button) findViewById(R.id.detailSendXMPP)).setOnClickListener(this);

		((Button) findViewById(R.id.detailMap2)).setOnClickListener(this);
		((Button) findViewById(R.id.detailNext2)).setOnClickListener(this);
		((Button) findViewById(R.id.detailPrevious2)).setOnClickListener(this);
		((Button) findViewById(R.id.detailHide2)).setOnClickListener(this);
		((Button) findViewById(R.id.detailShowAll2)).setOnClickListener(this);

		textView_HistoriqueMessages = (TextView) findViewById(R.id.detailHistoriqueMessages);
		textView_detailName = (TextView) findViewById(R.id.detailName);
		textView_detailHidden = (TextView) findViewById(R.id.detailHidden);
		textView_detailPrix = (TextView) findViewById(R.id.detailPrix);
		textView_detailTittle = (TextView) findViewById(R.id.detailTittle);
		textView_detailTelephone = (TextView) findViewById(R.id.detailTelephone);
		textView_detailDestination = (TextView) findViewById(R.id.detailDestination);
		textView_detailXmpp = (TextView) findViewById(R.id.detailXmpp);
		editText = (EditText) findViewById(R.id.detailMessageToSend);
		this.imageViewType = (ImageView) findViewById(R.id.detailImageType);

		this.bitMapTypeAuto = BitmapFactory.decodeResource(this.getResources(), R.drawable.type_auto);
		this.bitMapTypePassager = BitmapFactory.decodeResource(this.getResources(), R.drawable.type_pingouin);
		this.bitMapTypeUnknow = BitmapFactory.decodeResource(this.getResources(), R.drawable.type_unknown);

		//this.bitMapAndroid = BitmapFactory.decodeResource(this.getResources(), R.drawable.ua_android);
		//this.bitMapAndroidNo = BitmapFactory.decodeResource(this.getResources(), R.drawable.ua_no_android);

		this.prixLabel= ""+this.getText(R.string.detail_prixLabel);
        this.labelHistoriqueMessages=""+this.getText(R.string.detail_labelHistoriqueMessages);
		updateCar();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, Common.ACTION_DISPLAY_MESSAGES, R.string.menu_messages);
		menu.add(0, Common.ACTION_DISPLAY_MAP, R.string.menu_map);
		menu.add(0, Common.ACTION_DISPLAY_PREFERENCES, R.string.menu_preferences);
		menu.add(0, Common.ACTION_ABOUT, R.string.menu_about);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, Item item) {
		super.onMenuItemSelected(featureId, item);
		Common.getInstance().onMenuItemSelected(featureId, item, this);
		return true;
	}

	private void updateCar() {
		if (car == null) {
			textView_detailName.setText(" No Car ");
			textView_detailHidden.setText(" ");
			textView_detailPrix.setText("--");
			textView_detailTittle.setText(" No Car ");
			textView_detailTelephone.setText("--");
			textView_detailDestination.setText("Destination : ");
			textView_HistoriqueMessages.setText("");
			textView_detailXmpp.setText("");
			this.imageViewType.setImageBitmap(this.bitMapTypeUnknow);
			return;
		}
		
		textView_detailTittle.setText(this.getDetailTitleStr_(car));
		textView_detailName.setText(car.getName() );
		textView_detailHidden.setText(" " + car.isHidenStr());
		textView_detailPrix.setText(prixLabel +" : "+ car.getPrix());
		textView_detailTelephone.setText(car.getTel());
		textView_detailDestination.setText("Destination : " + car.getDestination());
		textView_HistoriqueMessages.setText(this.getHistoriquesMessages());
		textView_detailXmpp.setText("adress : " + car.getXmppAdress());
		this.setTitle("" + this.car.getMapTittleStr());
		if (this.car.isTypeCar()) {
			this.imageViewType.setImageBitmap(this.bitMapTypeAuto);
		} else if (this.car.isTypeAutostoppeur()) {
			this.imageViewType.setImageBitmap(this.bitMapTypePassager);
		} else {
			this.imageViewType.setImageBitmap(this.bitMapTypeUnknow);
		}

		
	}
	
	public int getDetailTitleStr_(Car car) {
		if (car.isTypeUnknow()){
			return R.string.detail_t_unknow;
		}
		if (car.isTypeCar()){
			return R.string.detail_t_voiture_cherche_passager;
		}
		if (car.isTypeAutostoppeur()){
			return R.string.detail_t_passager_cherche_vehicule;
		}
		return  R.string.detail_t_strange;
	}

	private void telephone() {
		try {
			this.setTitle("ring");
			IServiceManager sm = ServiceManagerNative.getDefault();
			IPhone iPhone = IPhone.Stub.asInterface(sm.getService("phone"));
			iPhone.call(this.car.getTel());
		} catch (Exception e) {
			this.setTitle("" + e.getMessage());
			Log.w("bg", "telephone", e);
		}

	}

	private void sendXmppMessage() {
		this.setTitle("xmpp");
		try {
			String text = "" + this.editText.getText();
			//com.google.android.gtalkservice.IGTalkSession
			com.google.android.gtalkservice.IGTalkSession xmppSession = ServiceXmppSender.getInstance().getXmppSession();
			
			if (xmppSession == null) {
				logPopupMessage(" No xmpp session connected ");
			} else if (text.trim().length() == 0) {
				logPopupMessage(" Pas de messages! ");
			} else {
				String xmppAdress = this.car.getXmppAdress();
				String destination = Preferences.getInstance().getDestination();
				String prix = Preferences.getInstance().getPrix();
				if (this.car.isAndroid()) {
					Log.i("bg", "sendDAtaMessage");
					xmppSession.sendDataMessage(xmppAdress, getIntentToSend(text, destination, prix));
				} else {
					Log.i("bg", "sendDAtaMessage (No Android !!)");
					xmppSession.sendDataMessage(xmppAdress, getIntentToSend(text, destination, prix));
				}
				// String messageArchive = Preferences.getInstance().getName() +
				// " : " + dateFormat.format(new Date()) + " : " + text;
				MessagesFactory.getInstance().createMessage(car, true, text, destination, prix);
				this.textView_HistoriqueMessages.setText(this.getHistoriquesMessages());
				this.editText.setText("");
				Log.i("bg", "Message sent to " + this.car.getXmppAdress());
				logPopupMessage("Message sent to:"+xmppAdress+"  text: "+text);
			}
		} catch (Exception e) {
			Log.i("bg", "sendXmpp", e);
			logPopupMessage("ERROR xmpp ");
		}
	}
	
	private String getHistoriquesMessages() {
		return this.car.getHistoriqueMessages(this.labelHistoriqueMessages);
	}

	/**
	 * Let the user know there was an issue
	 * 
	 * @param msg
	 */
	private void logPopupMessage(CharSequence msg) {
		try {
			Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			Log.e("bg","logPOpup Excption",e);
		}
	}
	
	

	private Intent getIntentToSend(String text, String destination, String prix) {
		Intent intent = new Intent(BgXmppDataMessageReceiver2.ACTION);
		intent.putExtra("t", text);
		intent.putExtra("name", Preferences.getInstance().getName());
		intent.putExtra("idAndroid", CarsFactory.getInstance().getIdAndroid());
		intent.putExtra("destination", destination);
		intent.putExtra("prix", prix);
		intent.putExtra("xmpp", "" + Preferences.getInstance().getXmppAdress());
		intent.putExtra("latitudeE6", "" + Preferences.getInstance().getMyLocation().getLatitudeE6());
		intent.putExtra("longitudeE6", "" + Preferences.getInstance().getMyLocation().getLongitudeE6());
		return intent;
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
			case R.id.detailMap:
			case R.id.detailMap2:
				Common.getInstance().displayMap(this);
				break;
			case R.id.detailShowAll:
			case R.id.detailShowAll2:
				CarsFactory.getInstance(this).showAll();
				Common.getInstance().displayMap(this);
				break;
			case R.id.detailHide:
			case R.id.detailHide2:
				this.car.setHiden(true);
				this.updateCar();
				break;
			case R.id.detailNext:
			case R.id.detailNext2:
				this.car = CarsFactory.getInstance(this).selectNext2();
				this.updateCar();
				break;
			case R.id.detailPrevious:
			case R.id.detailPrevious2:
				this.car = CarsFactory.getInstance(this).selectPrevious2();
				this.updateCar();
				break;
			case R.id.detailSendXMPP:
				this.sendXmppMessage();
				break;
			case R.id.detailPhoneCall:
				this.telephone();
				break;
		}

	}

}
