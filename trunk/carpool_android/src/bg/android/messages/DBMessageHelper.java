package bg.android.messages;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBMessageHelper {
	
	private static final String TABLE_NAME="messagesBg";

	private static final String DATABASE_CREATE = "create table "+TABLE_NAME+" (idMessage integer primary key autoincrement, " + "t text not null, prix text not null, destination text not null, idCar text not null, xmpp text not null);";

	private static final String DATABASE_NAME = "data";

	
	private static final int DATABASE_VERSION = 1;

	private SQLiteDatabase ddataBase;

	
	public DBMessageHelper() {

	}

	protected void initDBMessageHelper(Context ctx) {
		try {
			try {
				Log.i("bg", "initDBMessageHelper");
				if (ddataBase == null) {				
					ddataBase = ctx.openDatabase(DATABASE_NAME, null);
					Log.i("bg", "initDBMessageHelper openDataBAse done");
				}
			} catch (FileNotFoundException e) {
				Log.i("bg", "initDBMessageHelper Create DataBase " + DATABASE_NAME + "  " + DATABASE_VERSION);
				ddataBase = ctx.createDatabase(DATABASE_NAME, DATABASE_VERSION, 0, null);
				Log.i("bg", "initDBMessageHelper  " + DATABASE_CREATE);
				ddataBase.execSQL(DATABASE_CREATE);
			}
			
		} catch (Exception e1) {
			Log.i("bg", "initDB fail to initDB", e1);
			ddataBase = null;
		}
	}

	public void close() {
		ddataBase.close();
	}

	public void insertMessages(Message message) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("t", message.getText());
		contentValues.put("prix", message.getPrix());
		contentValues.put("destination", message.getDestination());
		contentValues.put("idCar", message.getIdCar());
		contentValues.put("xmpp", message.getXmppAdress());
		ddataBase.insert(TABLE_NAME, null, contentValues);
	}

	public void deleteMessage(long idMessage) { 
		ddataBase.delete(TABLE_NAME, "idMessage=" + idMessage, null);
	}

	private static String[] COLUMNS = new String[] { "idMessage", "t", "prix", "destination", "idCar","xmpp" };

	public List<Message> fetchAllMessages() {
		
		ArrayList<Message> listMessages = new ArrayList<Message>();
		try {
			Cursor c = ddataBase.query(TABLE_NAME, COLUMNS, null, null, null, null, null);
			int numRows = c.count();
			c.first();
			for (int i = 0; i < numRows; ++i) {
				Message message = new Message(c);
				listMessages.add(message);
				c.next();
			}
		} catch (Exception e) {
			Log.e("bg", "SQL fetchAllMessages ", e);
		}
		return listMessages;
	}

	public Message fetchMessage(long idMessage) {

		Cursor c = ddataBase.query(true, TABLE_NAME, COLUMNS, "idMessage=" + idMessage, null, null, null, null);
		if (c.count() > 0) {
			c.first();
			Message message = new Message(c);
			return message;
		} else {
			return null;
		}
	}

	public void updateMessage__(Message message) {
		ContentValues contentValue = new ContentValues();
		contentValue.put("t", message.getText());
		contentValue.put("prix", message.getPrix());
		contentValue.put("destination", message.getDestination());
		contentValue.put("idCar", message.getIdCar());
		contentValue.put("xmpp", message.getIdCar());
		ddataBase.update(TABLE_NAME, contentValue, "idMessage=" + message.getIdMessage(), null);
	}

	public SQLiteDatabase getDdataBase() {
		return ddataBase;
	}

	
}
