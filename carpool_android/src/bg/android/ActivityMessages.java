package bg.android;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Menu.Item;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import bg.android.coVoiturage.Car;
import bg.android.coVoiturage.CarsFactory;
import bg.android.messages.Message;
import bg.android.messages.MessagesFactory;

public class ActivityMessages extends ListActivity implements IUpdatable {

	private List<Message> messagesList;

	private TextView textViewHeader;

	public ActivityMessages() {
		super();
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		Log.i("bg", "ActivityMessages oncreate");
		try {
			setContentView(R.layout.activity_messages);
			this.textViewHeader = (TextView) findViewById(R.id.header);
			updateMessageList();
		} catch (Exception e) {
			Log.e("bg", "", e);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, Common.ACTION_DELETE_MESSAGE, R.string.menu_delete_message);
		menu.add(0, Common.ACTION_DISPLAY_MAP, R.string.menu_map);
		menu.add(0, Common.ACTION_DISPLAY_PREFERENCES, R.string.menu_preferences);
		menu.add(0, Common.ACTION_DISPLAY_DETAIL, R.string.menu_detail);
		menu.add(0, Common.ACTION_ABOUT, R.string.menu_about);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, Item item) {
		super.onMenuItemSelected(featureId, item);
		switch (item.getId()) {
			case Common.ACTION_DELETE_MESSAGE:
				this.deleteMessageSelected();
				return true;
		}
		Common.getInstance().onMenuItemSelected(featureId, item, this);
		return true;
	}

	private void updateMessageList() {
		try {
			this.messagesList = MessagesFactory.getInstance(this).getListMessages();
			this.textViewHeader.setText("Nb De Messages:" + messagesList.size() + "\n");
			List<String> listMessagesStr = new ArrayList<String>();
			Iterator<Message> iterator = messagesList.iterator();
			while (iterator.hasNext()) {
				Message message = iterator.next();
				listMessagesStr.add("" + message.toStringDisplay());
				// textViewResultat.append("\ncar: " + car + "\n");
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.message_row, listMessagesStr);

			setListAdapter(adapter);
		} catch (Exception e) {
			Log.e("bg", "updateMessageList", e);
		}
	}

	@Override
	public void update(Object o, Object o2) {
		updateMessageList();
	}

	public void onListItemClick(ListView l, View v, int position, long id) {
		Log.i("bg", "onListItemClick  position: " + position);
		super.onListItemClick(l, v, position, id);
		showMessageSelected();
	}

	private void deleteMessageSelected() {
		Message message = this.getMessageSelected();
		MessagesFactory.getInstance().remove(message);
		this.updateMessageList();
	}

	private Message getMessageSelected() {
		int i = this.getSelectedItemPosition();
		Message message = this.messagesList.get(i);
		return message;
	}

	private void showMessageSelected() {
		// int i = this.getSelection();
		Message message = this.getMessageSelected();
		Car car = message.getCar();
		if (car == null) {
			Log.i("bg", "Activity Message No Car");
		} else {
			CarsFactory.getInstance().selectCar(car);
			Common.getInstance().showDetailCarSelected(this);
		}

	}

}
