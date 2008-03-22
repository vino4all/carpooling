package bg.android.map;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.util.Log;
import bg.android.ActivityMap;
import bg.android.Common;
import bg.android.Preferences;
import bg.android.R;
import bg.android.coVoiturage.Car;
import bg.android.coVoiturage.CarsFactory;

import com.google.android.maps.Overlay;
import com.google.android.maps.Point;

public class Overlay_Map extends Overlay {

	private Bitmap bitmap_auto;

	private Bitmap bitmap_pingouin;

	private Bitmap bitmap_unknown;

	private Bitmap bitmap_isActif_NO;
	
	public static final int R_MOBILE=7;

	private ActivityMap map;

	Paint paint1 = new Paint();
	
	
	public Overlay_Map(ActivityMap map) {
		super();
		this.map = map;
		paint1.setColor(Color.RED);
		try {
			bitmap_auto = BitmapFactory.decodeResource(map.getResources(), R.drawable.type_auto);
			// bitmap_mark_auto =
			// BitmapFactory.decodeResource(map.getResources(),
			// R.drawable.mark_auto);
			bitmap_pingouin = BitmapFactory.decodeResource(map.getResources(), R.drawable.type_pingouin);
			bitmap_unknown = BitmapFactory.decodeResource(map.getResources(), R.drawable.type_unknown);
			bitmap_isActif_NO = BitmapFactory.decodeResource(map.getResources(), R.drawable.suspendre);
		} catch (Exception e) {
			Log.e("bg", "BitMAp map.getResources: " + map.getResources());
			Log.e("bg", "BitMAp Exception", e);
		}
		// paint1.setColor(Color.RED);
	}

	@Override
	public void draw(Canvas canvas, PixelCalculator pixelCalculator, boolean shadow) {
		super.draw(canvas, pixelCalculator, shadow);
		int mode = Common.getInstance().getMode();
		if (mode == Common.MODE_DEFAULT) {
			draw_MyPosition(canvas, pixelCalculator, shadow);
		} else if (mode == Common.MODE_SET_MY_POSITION) {
			draw_SetMyPosition(canvas, pixelCalculator, shadow);
		} else if (mode == Common.MODE_SHOW_MOBILES) {
			draw_PositionMobiles(canvas, pixelCalculator, shadow);
			draw_MyPosition(canvas, pixelCalculator, shadow);
			draw_isHidden(canvas);
			draw_comment(canvas);
			// draw_test(canvas, pixelCalculator, shadow, 20);
			// draw_test(canvas, pixelCalculator, shadow, 30);
			// draw_test(canvas, pixelCalculator, shadow, 40);
			// draw_test(canvas, pixelCalculator, shadow, 50);
		}
	}

	public void draw_PositionMobiles(Canvas canvas, PixelCalculator pixelCalculator, boolean shadow) {
		List<Car> listCArs = CarsFactory.getInstance(this.map).getListCarsClone();
		Iterator<Car> iteCar = listCArs.iterator();
		Car carSelected = null;
		int x_CarSelected = 0;
		int y_CArSelected = 0;
		int show = Common.getInstance().getShow();
		paint1.setStyle(Style.FILL);
		List<Car> listCarDisplayed = new ArrayList<Car>();
		
		while (iteCar.hasNext()) {
			Car car_ = iteCar.next();
			int latitude = car_.getLatitude();
			int longitude = car_.getLongitude();
			// Log.i("bg","draw_PositionMobiles latitude:"+latitude+"
			// longitude:"+longitude+" car:"+car_);
			if ((latitude == 0) && (longitude == 0)) {
			} else if (car_.isHide()) {
				Log.i("bg", "isHide car: " + car_);
			} else if (show == Common.SHOW_NOTHING) {
			} else if ((show == Common.SHOW_CAR_ONLY) && (car_.getType() != Car.TYPE_VOITURE)) {
			} else if ((show == Common.SHOW_TRAVELLERS_ONLY) && (car_.getType() != Car.TYPE_AUTO_STOPPEUR)) {
			} else {

				int color = getColor(car_);
				int[] screenCoords = new int[2];
				Point point = new Point(latitude, longitude);
				pixelCalculator.getPointXY(point, screenCoords);
				if (car_.isSelected()) {
					paint1.setColor(Color.BLACK);
					canvas.drawCircle(screenCoords[0], screenCoords[1], 9, paint1);
					x_CarSelected = screenCoords[0];
					y_CArSelected = screenCoords[1];
					carSelected = car_;
				}
				paint1.setColor(color);
				if (carSelected == null) {

				} else {
				}
				int x = screenCoords[0];
				int y = screenCoords[1];
				car_.setX_screen(x);
				car_.setY_screen(y);
				if ((x >= 0) && (x < this.map.getMapView().getWidth()) && (y >= 0) && (y < this.map.getMapView().getHeight())) {
					listCarDisplayed.add(car_);
					canvas.drawCircle(x, y, R_MOBILE, paint1);
				}
			}
		}
		paintCarSelected(carSelected, canvas, x_CarSelected, y_CArSelected);
		this.map.setListCarDisplayed(listCarDisplayed);
	}

	private Bitmap getBitMap(Car car_) {
		if (car_.isTypeCar()) {
			return bitmap_auto;
		} else if (car_.isTypeAutostoppeur()) {
			return bitmap_pingouin;
		} else {
			return bitmap_unknown;
		}
	}

	private int getColor(Car car_) {
		int color = Color.BLACK;
		if (car_.isTypeUnknow()) {
			color = Color.MAGENTA;
		} else if (car_.isTypeCar()) {
			color = Color.BLUE;
		} else if (car_.isTypeAutostoppeur()) {
			color = Color.RED;
		}
		return color;
	}

	private void paintCarSelected(Car carSelected, Canvas canvas, int x, int y) {
		if (carSelected == null) {
			return;
		}
		int h = 30;
		// working canvas.drawArc(rf, 10.0f, 200.0f,paint1);
		int yb = y + 20;
		int bb = 10;
		paint1.setColor(Color.WHITE);
		for (int i = 0; i < bb; i++) {
			canvas.drawLine(x, y, x + h, yb + i, paint1);
		}
		paint1.setColor(Color.BLACK);

		canvas.drawLine(x, y, x + h, yb, paint1);
		canvas.drawLine(x, y, x + h, yb + bb, paint1);
		paintCarSelectedFrame(carSelected, canvas, x + h, y);
	}

	private void paintCarSelectedFrame(Car carSelected, Canvas canvas, int x, int y) {

		Bitmap bitmap = getBitMap(carSelected);
		String text = carSelected.getDestination();
		float mText = paint1.measureText(text);
		float mImage = bitmap.getWidth();
		float m = Math.max(mText, mImage);

		// paint1.setColor(this.getColor(carSelected));
		paint1.setColor(Color.WHITE);
		// Rect rect = new Rect(x + 8, y, x + 12 + (int) m, y +
		// 12+bitmap.getHeight());
		Rect rect_ = new Rect(x, y, x + 12 + (int) m, y + 12 + bitmap.getHeight());
		RectF rect = new RectF(rect_);
		float rx = 5.0f;
		float ry = 5.0f;
		paint1.setStyle(Style.FILL);
		canvas.drawRoundRect(rect, rx, ry, paint1);

		paint1.setColor(Color.BLACK);
		paint1.setStyle(Style.STROKE);
		canvas.drawRoundRect(rect, rx, ry, paint1);
		paint1.setStyle(Style.FILL);

		canvas.drawText(text, x + 10, y + 10, paint1);
		this.map.setTitle(carSelected.getMapTittleStr());
		canvas.drawBitmap(bitmap, x, y + 12, paint1);
	}

	private void draw_SetMyPosition(Canvas canvas, PixelCalculator pixelCalculator, boolean shadow) {
		// TODO Auto-generated method stub
		// super.draw(canvas, pixelCalculator, shadow);
		paint1.setColor(Color.BLACK);
		int[] screenCoords = new int[2];
		Point point = map.getMapView().getMapCenter();
		pixelCalculator.getPointXY(point, screenCoords);
		// canvas.drawCircle(screenCoords[0], screenCoords[1], 9, paint1);
		int h = 25;
		int hh = 10;

		canvas.drawCircle(screenCoords[0], screenCoords[1], 3, paint1);
		// canvas.drawCircle(screenCoords[0], screenCoords[1], 18, paint1);

		canvas.drawLine(screenCoords[0], screenCoords[1] + hh, screenCoords[0], screenCoords[1] + h, paint1);
		canvas.drawLine(screenCoords[0], screenCoords[1] - hh, screenCoords[0], screenCoords[1] - h, paint1);
		canvas.drawLine(screenCoords[0] + hh, screenCoords[1], screenCoords[0] + h, screenCoords[1], paint1);
		canvas.drawLine(screenCoords[0] - hh, screenCoords[1], screenCoords[0] - h, screenCoords[1], paint1);

	}

	private void draw_MyPosition(Canvas canvas, PixelCalculator pixelCalculator, boolean shadow) {
		// super.draw(canvas, pixelCalculator, shadow);
		// TODO Auto-generated method stub
		paint1.setColor(Color.RED);
		int[] screenCoords = new int[2];
		Point point = Preferences.getInstance().getMyLocation();
		pixelCalculator.getPointXY(point, screenCoords);
		// canvas.drawCircle(screenCoords[0], screenCoords[1], 9, paint1);
		int h = 9;
		int hh = 4;

		canvas.drawCircle(screenCoords[0], screenCoords[1], 2, paint1);
		canvas.drawLine(screenCoords[0], screenCoords[1] + hh, screenCoords[0], screenCoords[1] + h, paint1);
		canvas.drawLine(screenCoords[0], screenCoords[1] - hh, screenCoords[0], screenCoords[1] - h, paint1);
		canvas.drawLine(screenCoords[0] + hh, screenCoords[1], screenCoords[0] + h, screenCoords[1], paint1);
		canvas.drawLine(screenCoords[0] - hh, screenCoords[1], screenCoords[0] - h, screenCoords[1], paint1);
	}

	private void draw_isHidden(Canvas canvas) {
		if (Preferences.getInstance().isHidden()) {
			int x = bitmap_isActif_NO.width() - 1;
			int y = 10;
			paint1.setColor(Color.WHITE);
			String text = "Warning: Hidden!";
			float m = paint1.measureText(text);
			Rect rect = new Rect(x, y, x + 12 + (int) m, y + 14);
			canvas.drawRect(rect, paint1);

			paint1.setColor(Color.RED);
			canvas.drawText(text, x + 5, y + 12, paint1);
			canvas.drawBitmap(bitmap_isActif_NO, 2, 2, paint1);
		}
	}

	private void draw_comment(Canvas canvas) {
		if (Common.getInstance().isConnectingServer() && Common.getInstance().isJustCreated()) {
			int x = 40;
			int y = 60;
			String text_connecting = "" + this.map.getText(R.string.map_message_connectingServer);
			draw_comment(canvas, x, y, text_connecting);
			y = y + 40;
			String text_zoom = "" + this.map.getText(R.string.map_message_zoom_plus);
			draw_comment(canvas, x, y, text_zoom);
		}
	}

	private void draw_comment(Canvas canvas, int x, int y, String text_1) {
		paint1.setColor(R.color.solid_yellow);
		float fSize = paint1.getTextSize();
		paint1.setTextSize(2 * fSize);
		float m_1 = paint1.measureText(text_1);
		int h = 0 + (int) paint1.getTextSize();
		Rect rect_1 = new Rect(x, y - 6, x + 12 + (int) m_1, y + h);
		paint1.setColor(R.color.solid_yellow);
		canvas.drawRect(rect_1, paint1);
		paint1.setColor(Color.RED);
		canvas.drawText(text_1, x + 5, y + 12, paint1);
		paint1.setTextSize(fSize);
	}

	
}
