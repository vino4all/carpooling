package bg.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ApplicationContext;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyProperties;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Menu.Item;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import bg.android.coVoiturage.CarsFactory;
import bg.android.messages.MessagesFactory;

import com.google.android.maps.Point;

public class ActivityPreferences extends Activity implements RadioGroup.OnCheckedChangeListener, OnClickListener, OnCheckedChangeListener {

	private RadioGroup radioGroup_type;

	private RadioGroup radioGroup_hidden;

	private RadioGroup radioGroup_localizator;

	private CheckBox checkBoxPhoneNumberVisible;
	private EditText editText_myName;

	private EditText editText_myDestination;

	private EditText editText_myPrix;
	
	private EditText editText_phoneNumber;

	// private CheckBox checkBox_updateGPSPosition ;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		try {
			// setContentView(R.layout.main);
			setContentView(R.layout.activity_preferences);
			this.startService(new Intent(this, ServiceLocalisationBg.class), null);
			this.startService(new Intent(this, ServiceXmppSender.class), null);
			Preferences p = Preferences.getInstance();
			p.init(this);
			Button buttonMap = (Button) findViewById(R.id.appButtonMap);
			buttonMap.setOnClickListener(this);
			Button buttonMap2 = (Button) findViewById(R.id.appButtonMap2);
			buttonMap2.setOnClickListener(this);
			Button buttonMessages = (Button) findViewById(R.id.appButtonMessages);
			buttonMessages.setOnClickListener(this);
			Button buttonMessages2 = (Button) findViewById(R.id.appButtonMessages2);
			buttonMessages2.setOnClickListener(this);
			Button buttonSetPositionGPS = (Button) findViewById(R.id.appButtonSetPositionGps);
			buttonSetPositionGPS.setOnClickListener(this);
			Button buttonSetPositionManuel = (Button) findViewById(R.id.appButtonSetPositionManuel);
			buttonSetPositionManuel.setOnClickListener(this);
			Button buttonAbout = (Button) findViewById(R.id.appButtonAbout);
			buttonAbout.setOnClickListener(this);
			Button buttonAbout2 = (Button) findViewById(R.id.appButtonAbout2);
			buttonAbout2.setOnClickListener(this);
			this.checkBoxPhoneNumberVisible  = (CheckBox) findViewById(R.id.appCheckBoxPhoneNumberVisible);
			this.checkBoxPhoneNumberVisible.setOnCheckedChangeListener(this);
			editText_myName = (EditText) findViewById(R.id.app_myName);
			editText_myDestination = (EditText) findViewById(R.id.app_destination);
			editText_myPrix = (EditText) findViewById(R.id.app_prix);
			editText_phoneNumber = (EditText) findViewById(R.id.app_phone_number)  ; 
			editText_myName.setText(p.getName());
			editText_myDestination.setText(p.getDestination());
			editText_myPrix.setText(p.getPrix());
			editText_phoneNumber.setText(p.getPhoneNumber());
			this.radioGroup_type = (RadioGroup) findViewById(R.id.menu_preferences);
			this.radioGroup_type.setOnCheckedChangeListener(this);
			this.radioGroup_hidden = (RadioGroup) findViewById(R.id.menu_hidden);
			this.radioGroup_hidden.setOnCheckedChangeListener(this);
			this.radioGroup_localizator = (RadioGroup) findViewById(R.id.menu_preferences_location);
			this.radioGroup_localizator.setOnCheckedChangeListener(this);
			CarsFactory.getInstance();
			MessagesFactory.getInstance().initMessages(this);
			this.setType_and_hidden_and_localizator();

			// this.setTitle(""+this.hashCode());
		} catch (Exception e) {
			Log.e("bg", "Execption Init ", e);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, Common.ACTION_DISPLAY_MESSAGES, R.string.menu_messages);
		menu.add(0, Common.ACTION_DISPLAY_MAP, R.string.menu_map);
		// menu.add(0, Common.ACTION_DISPLAY_DETAIL, "detail");
		menu.add(0, Common.ACTION_ABOUT, R.string.menu_about);
		menu.add(0, Common.ACTION_EXIT, R.string.menu_exit);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, Item item) {
		super.onMenuItemSelected(featureId, item);
		Common.getInstance().onMenuItemSelected(featureId, item, this);
		return true;
	}

	private void setType_and_hidden_and_localizator() {
		int type = Preferences.getInstance().getType();
		if (type == Preferences.TYPE_AUTO_STOPPEUR) {
			RadioButton rb = (RadioButton) findViewById(R.id.radio_I_am_passager);
			rb.setChecked(true);
		} else if (type == Preferences.TYPE_VOITURE) {
			RadioButton rb = (RadioButton) findViewById(R.id.radio_I_am_vehicule);
			rb.setChecked(true);
		} else {
			((RadioButton) findViewById(R.id.radio_I_am_vehicule)).setChecked(false);
			((RadioButton) findViewById(R.id.radio_I_am_passager)).setChecked(false);
		}
		boolean isHidden = Preferences.getInstance().isHidden();
		if (isHidden) {
			RadioButton rb = (RadioButton) findViewById(R.id.radio_group_2_hidden);
			rb.setChecked(true);
		} else {
			RadioButton rb = (RadioButton) findViewById(R.id.radio_group_2_hidden_false);
			rb.setChecked(true);
		}

		boolean isPhoneNumberVisible = Preferences.getInstance().isPhoneNumberVisible();
		EditText editTextPhoneNumber = (EditText) findViewById(R.id.app_phone_number);
		editTextPhoneNumber.setEnabled(isPhoneNumberVisible);

		boolean isLocateByGPS = Preferences.getInstance().isLocateByGPS();
		if (isLocateByGPS) {
			RadioButton rb = (RadioButton) findViewById(R.id.radio_preferences_location_gps);
			rb.setChecked(true);
		} else {
			RadioButton rb = (RadioButton) findViewById(R.id.radio_preferences_location_map);
			rb.setChecked(true);
		}
	}

	@Override
	public void onClick(View view) {
		this.saveParams();
		if (!this.mandatoryFieldAreOK()) {
			this.dialogShow();
			return;
		}
		int id = view.getId();
		switch (id) {
			case R.id.appButtonMap:
			case R.id.appButtonMap2:
				this.displayMap_SHOW_MOBILES();
				break;
			case R.id.appButtonMessages:
			case R.id.appButtonMessages2:
				Common.getInstance().displayMessages(this);
				break;
			case R.id.appButtonSetPositionGps:
				this.setPositionGPs();
				break;
			case R.id.appButtonSetPositionManuel:
				this.setPositionManuel();
				break;
			case R.id.appButtonAbout:
			case R.id.appButtonAbout2:
				Common.getInstance().displayAbout(this);
				break;

		}
	}

	private void displayMap_SHOW_MOBILES() {
		Common.getInstance().setMode(Common.MODE_SHOW_MOBILES);
		Common.getInstance().displayMap(this);
	}

	private void setPositionManuel() {
		Common.getInstance().setMode(Common.MODE_SET_MY_POSITION);
		Common.getInstance().displayMap(this);
	}

	private void setPositionGPs() {
		try {
			Point p = ServiceLocalisationBg.getInstance().processGPS();
			this.update_("Location done", p);
		} catch (Exception e) {
			this.logPopupMessage("Exception " + e.getMessage());
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
		Log.i("bg", "onCheckedChanged  checkedId:" + checkedId);
		int idChecked_type = this.radioGroup_type.getCheckedRadioButtonId();
		if (idChecked_type == R.id.radio_I_am_passager) {
			Preferences.getInstance().setType(this, Preferences.TYPE_AUTO_STOPPEUR);
		} else if (idChecked_type == R.id.radio_I_am_vehicule) {
			Preferences.getInstance().setType((ApplicationContext) this, Preferences.TYPE_VOITURE);
		} else {
			Preferences.getInstance().setType((ApplicationContext) this, Preferences.TYPE_UNKNOW);
		}
		int idChecked_hidden = this.radioGroup_hidden.getCheckedRadioButtonId();
		if (idChecked_hidden == R.id.radio_group_2_hidden) {
			Preferences.getInstance().setHidden(this, true);
		} else if (idChecked_hidden == R.id.radio_group_2_hidden_false) {
			Preferences.getInstance().setHidden(this, false);
		}

		int idChecked_localizator = this.radioGroup_localizator.getCheckedRadioButtonId();
		if (idChecked_localizator == R.id.radio_preferences_location_gps) {
			Preferences.getInstance().setLocalizator(this, Preferences.LOCALIZATOR_GPS);
		} else if (idChecked_localizator == R.id.radio_preferences_location_map) {
			Preferences.getInstance().setLocalizator(this, Preferences.LOCALIZATOR_MAP);
		}

	}

	private void saveParams() {
		Preferences p = Preferences.getInstance();
		p.setDestination(this, "" + editText_myDestination.getText());
		p.setName(this, "" + editText_myName.getText());
		p.setPrix(this, "" + editText_myPrix.getText());
		p.setPhoneNumber(this, ""+editText_phoneNumber.getText());

	}

	private void logPopupMessage(CharSequence msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
		Log.i("bg", "Activity Preference popup :" + msg);
	}

	public void update_(Object message, Point p) {
		if (p == null) {
			return;
		}
		if (p == null) {
			return;
		}
		if ((p.getLatitudeE6() == 0) && (p.getLongitudeE6() == 0)) {
			Log.e("bg", " !ActivityPreferences latitude and longitude 0!");
			this.logPopupMessage("!Location no available !");
		} else {
			Preferences.getInstance().setMyLocation(this, p);
		}
		this.logPopupMessage("Location done " + message);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		MessagesFactory.getInstance().onDestroy();
	}

	private boolean mandatoryFieldAreOK() {
		if (this.editText_myName.getText().length() <= 0) {
			return false;
		}
		if (this.editText_myDestination.getText().length() <= 0) {
			return false;
		}
		return true;
	}

	private void dialogShow() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setIcon(R.drawable.auto_icone);
		alertDialog.setTitle(R.string.alert_dialog_warning);
		alertDialog.setMessage(R.string.alert_dialog_fillField);
		alertDialog.setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

			}
		});

		alertDialog.show();
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		boolean isPhoneNumberVisible = this.checkBoxPhoneNumberVisible.isChecked();
		Preferences.getInstance().setPhoneNumberVisible(this, isPhoneNumberVisible);
		this.setType_and_hidden_and_localizator();
	}

	
	
}