package bg.android;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.Menu.Item;

public class Common {

	public static final int MODE_DEFAULT = 0;

	public static final int MODE_SET_MY_POSITION = 1;

	public static final int MODE_SHOW_MOBILES = 2;

	public static final int ACTIVITY_CREATE_BG_ = 0;

	public static final int ACTION_MOVE_TO_MY_LOCATION = Menu.FIRST;

	public static final int ACTION_SET_MY_POSITION = Menu.FIRST + 1;

	public static final int ACTION_SHOW_MOBILES = Menu.FIRST + 2;

	public static final int ACTION_HIDE_ALL = Menu.FIRST + 3;

	public static final int ACTION_REFRESH = Menu.FIRST + 4;

	public static final int ACTION_SELECT_NEXT = Menu.FIRST + 5;

	public static final int ACTION_SELECT_PREVIOUS = Menu.FIRST + 6;

	public static final int ACTION_ZOOM_PLUS = Menu.FIRST + 7;

	public static final int ACTION_ZOOM_MOINS = Menu.FIRST + 8;

	public static final int ACTION_SHOW_ALL = Menu.FIRST + 9;

	public static final int ACTION_SHOW_CAR_ONLY = Menu.FIRST + 10;

	public static final int ACTION_SHOW_TRAVELLERS_ONLY = Menu.FIRST + 11;

	public static final int ACTION_DISPLAY_PREFERENCES = Menu.FIRST + 12;

	public static final int ACTION_DISPLAY_MESSAGES = Menu.FIRST + 13;

	public static final int ACTION_DISPLAY_MAP = Menu.FIRST + 14;

	public static final int ACTION_DISPLAY_DETAIL = Menu.FIRST + 15;

	public static final int ACTION_DELETE_MESSAGE = Menu.FIRST + 16;

	public static final int ACTION_ABOUT = Menu.FIRST + 17;

	public static final int ACTION_EXIT = Menu.FIRST + 18;

	public static final int ACTION_HIDDEN = Menu.FIRST + 19;

	public static final int SHOW_ALL = 0;

	public static final int SHOW_NOTHING = 1;

	public static final int SHOW_CAR_ONLY = 2;

	public static final int SHOW_TRAVELLERS_ONLY = 3;

	private static final int ACTIVITY_CREATE_ACTIVITY_displayMap = 1;

	private static final int ACTIVITY_CREATE_ACTIVITY_showDetail = 2;

	private static final int ACTIVITY_CREATE_ACTIVITY_displayMessages = 3;

	private static final int ACTIVITY_CREATE_ACTIVITY_displayAbout = 4;

	private static final int ACTIVITY_CREATE_ACTIVITY_goHome = 5;

	private int mode = Common.MODE_DEFAULT;

	private int show = SHOW_ALL;

	public boolean connectingServer = true;

	public boolean justCreated = true;

	private static Common instance = new Common();

	public Common() {
	}

	public static Common getInstance() {
		return instance;
	}

	public static void setInstance(Common instance) {
		Common.instance = instance;
	}

	public void goPreferences(Activity activity) {
		Log.e("bg", "goHome");
		try {
			Intent intent = new Intent(activity, ActivityPreferences.class);
			activity.startSubActivity(intent, Common.ACTIVITY_CREATE_ACTIVITY_goHome);
		} catch (Exception e) {
			Log.e("bg", "goHome Exception: " + e.getMessage(), e);
		}
	}

	public void displayMap(Activity activity) {
		Intent intent = new Intent(activity, ActivityMap.class);
		activity.startSubActivityIfNeeded(intent, Common.ACTIVITY_CREATE_ACTIVITY_displayMap);
	}

	public void showDetailCarSelected(Activity activity) {
		Log.i("bg", "showDetailCarSelected");
		Intent intent = new Intent(activity, ActivityDetail.class);
		activity.startSubActivityIfNeeded(intent, Common.ACTIVITY_CREATE_ACTIVITY_showDetail);
	}

	public void displayMessages(Activity activity) {
		Intent intent = new Intent(activity, ActivityMessages.class);
		activity.startSubActivityIfNeeded(intent, Common.ACTIVITY_CREATE_ACTIVITY_displayMessages);
	}

	public void displayAbout(Activity activity) {
		Intent intent = new Intent(activity, ActivityAbout.class);
		activity.startSubActivityIfNeeded(intent, Common.ACTIVITY_CREATE_ACTIVITY_displayAbout);
	}

	public boolean onMenuItemSelected(int featureId, Item item, Activity activity) {
		switch (item.getId()) {
			case ACTION_DISPLAY_PREFERENCES:
				this.goPreferences(activity);
				break;
			case ACTION_DISPLAY_MESSAGES:
				this.displayMessages(activity);
				break;
			case ACTION_DISPLAY_DETAIL:
				this.showDetailCarSelected(activity);
				break;
			case ACTION_DISPLAY_MAP:
				this.setMode(Common.MODE_SHOW_MOBILES);
				this.displayMap(activity);
				break;
			case ACTION_ABOUT:
				this.displayAbout(activity);
				break;
			case ACTION_EXIT:
				Log.i("bg", "!exit request from menu!");
				this.exit(activity);
				break;

		}
		return true;
	}

	private void exit(Activity activity) {
		Log.i("bg", "exit byby ");
		System.exit(0);
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public int getShow() {
		return show;
	}

	public void setShow(int show) {
		this.show = show;
	}

	public boolean isConnectingServer() {
		return connectingServer;
	}

	public void setConnectingServer(boolean connectingServer) {
		this.connectingServer = connectingServer;
	}

	public boolean isJustCreated() {
		return justCreated;
	}

	public void setJustCreated(boolean justCreated) {
		this.justCreated = justCreated;
	}

}