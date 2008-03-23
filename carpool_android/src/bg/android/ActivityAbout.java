package bg.android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Menu.Item;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import bg.android.coVoiturage.CarsFactory;

public class ActivityAbout extends Activity implements OnClickListener {

	public static String tHtml = "<html><body style=\"font-size:12px;border: 2px solid #ff0000;padding-left:4px;\">" + "<br/><a href=\"http://carpool-android.blogspot.com/\">Blog carpool Android</a>" + "<br/><a href=\"http://carpool-android-help.blogspot.com/\">Blog Help</a>" +"<br/> <a href=\"http://www.gnu.org/licenses/gpl-3.0-standalone.html\">Licence gpl</a>"+ "</body></html>";

	/**
	 * Setup the XMPP Session using a service connection
	 */

	private EditText editText;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		try {

			setContentView(R.layout.activity_about);
			((Button) findViewById(R.id.aboutButtonSendXMPP)).setOnClickListener(this);
			this.editText = (EditText) findViewById(R.id.aboutEditTextMessageToSend);

			WebView webView = (WebView) findViewById(R.id.aboutWebView);
			webView.loadData(ActivityAbout.tHtml, "text/html", "utf-8");
			webView.refreshDrawableState();
			webView.bringToFront();
			webView.requestLayout();
			// webView.reload();
			webView.setVisibility(View.VISIBLE);
			// this.
			// webView.loadUrl("http://carpool-android.blogspot.com/");
		} catch (Exception e) {
			Log.w("bg", "ACtivityAbout onCreate Execption", e);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, Common.ACTION_DISPLAY_MESSAGES, R.string.menu_messages);
		menu.add(0, Common.ACTION_DISPLAY_MAP, R.string.menu_map);
		menu.add(0, Common.ACTION_DISPLAY_DETAIL, R.string.menu_detail);
		menu.add(0, Common.ACTION_DISPLAY_PREFERENCES, R.string.menu_preferences);
		menu.add(0, Common.ACTION_EXIT, R.string.menu_exit);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, Item item) {
		super.onMenuItemSelected(featureId, item);
		Common.getInstance().onMenuItemSelected(featureId, item, this);
		return true;
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
			case R.id.aboutButtonSendXMPP:
				this.sendMessage();
				break;
		}
	}

	/**
	 * Let the user know there was an issue
	 * 
	 * @param msg
	 */
	private void logPopupMessage(CharSequence msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	private void sendMessage() {
		try {
			String message = "" + this.editText.getText();
			if (message.length() == 0) {
				logPopupMessage("No text! ");
				return;
			}
			CarsFactory.getInstance().sendMessage(message);
			this.editText.setText("");
			logPopupMessage(this.getText(R.string.about_message_sent));
		} catch (Exception e) {
			Log.i("bg", "sendXmpp", e);
			logPopupMessage("ERROR xmpp ");
		}
	}

}