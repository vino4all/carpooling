package bg.android;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.Menu.Item;
import android.view.WindowManager.LayoutParams;
import bg.android.coVoiturage.Car;
import bg.android.coVoiturage.CarsFactory;
import bg.android.map.MapViewBg;
import bg.android.map.Overlay_Map;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayController;
import com.google.android.maps.Point;
import static bg.android.map.Overlay_Map.R_MOBILE;

public class ActivityMap extends MapActivity {
	private MapView mMapView;

	private OverlayController overlayController;

	private Overlay_Map overlayMyPosition;

	private List<Car> listCarDisplayed = new ArrayList<Car>();

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		this.overlayMyPosition = new Overlay_Map(this);
		this.refreshListCar();
		this.mMapView = new MapViewBg(this);
		MapController mmapControler = mMapView.getController();
		mmapControler.centerMapTo(Preferences.getInstance().getMyLocation(), true);
		// mmapControlerc.
		int zoomLevel = Preferences.getInstance().getZoomLevel();

		mmapControler.zoomTo(zoomLevel);
		overlayController = mMapView.createOverlayController();
		overlayController.add(this.overlayMyPosition, false);

		setContentView(mMapView);

		// this.setMode(Common.MODE_SHOW_MOBILES);
		mmapControler.animateTo(Preferences.getInstance().getMyLocation());
		this.mMapView.requestFocus();
		Common.getInstance().setJustCreated(true);
		this.setTittle();

	}

	private void setTittle() {
		if (Common.getInstance().getMode() == Common.MODE_SHOW_MOBILES) {
			this.setTitle(R.string.map_title);
		} else {
			this.setTitle(R.string.map_title_set_my_position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, Common.ACTION_REFRESH, R.string.menu_refresh_map);
		menu.add(0, Common.ACTION_DISPLAY_MESSAGES, R.string.menu_messages);
		menu.add(0, Common.ACTION_DISPLAY_PREFERENCES, R.string.menu_preferences);

		menu.add(0, Common.ACTION_MOVE_TO_MY_LOCATION, R.string.menu_move_to_my_location);
		// menu.add(0, Common.ACTION_SET_MY_POSITION, "Set My Position");
		SubMenu sousMenuShow = menu.addSubMenu(0, Common.ACTION_SHOW_MOBILES, R.string.menu_show);
		sousMenuShow.add(0, Common.ACTION_SHOW_ALL, R.string.menu_show_all);
		sousMenuShow.add(0, Common.ACTION_SHOW_CAR_ONLY, R.string.menu_show_car_only);
		sousMenuShow.add(0, Common.ACTION_SHOW_TRAVELLERS_ONLY, R.string.menu_show_pedestrian_only);
		sousMenuShow.add(0, Common.ACTION_SELECT_NEXT, R.string.menu_select_next);
		sousMenuShow.add(0, Common.ACTION_SELECT_PREVIOUS, R.string.menu_select_previous);
		SubMenu sousMenuZoom = menu.addSubMenu(0, Common.ACTION_ZOOM_PLUS, R.string.menu_zoom);
		sousMenuZoom.add(0, Common.ACTION_ZOOM_PLUS, R.string.menu_zoom_plus);
		sousMenuZoom.add(0, Common.ACTION_ZOOM_MOINS, R.string.menu_zoom_moins);
		menu.add(0, Common.ACTION_ABOUT, R.string.menu_about);

		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, Item item) {
		super.onMenuItemSelected(featureId, item);
		Preferences.getInstance().setCentreEcran(this.mMapView.getMapCenter(), this.mMapView.getLatitudeSpan(), this.mMapView.getLongitudeSpan());

		switch (item.getId()) {
			case Common.ACTION_HIDE_ALL:
				this.show(Common.SHOW_NOTHING);
				return true;
			case Common.ACTION_SET_MY_POSITION:
				setMyPosition();
				return true;
			case Common.ACTION_SHOW_MOBILES:
				showMobiles();
				return true;
			case Common.ACTION_REFRESH:
				refreshListCar();
				return true;
			case Common.ACTION_ZOOM_MOINS:
				zoom_moins();
				return true;
			case Common.ACTION_ZOOM_PLUS:
				zoom_plus();
				return true;
			case Common.ACTION_SELECT_PREVIOUS:
				this.selectPrevious();
				return true;
			case Common.ACTION_SELECT_NEXT:
				this.selectNext();
				return true;
			case Common.ACTION_MOVE_TO_MY_LOCATION:
				this.moveToMyLocation();
				return true;
			case Common.ACTION_SHOW_ALL:
				showMobiles();
				CarsFactory.getInstance(this).showAll();
				this.selectNext();
				return true;
			case Common.ACTION_SHOW_CAR_ONLY:
				this.show(Common.SHOW_CAR_ONLY);
				break;
			case Common.ACTION_SHOW_TRAVELLERS_ONLY:
				this.show(Common.SHOW_TRAVELLERS_ONLY);
				break;
			case Common.ACTION_HIDDEN:
				Preferences.getInstance().setHidden_flipflop(this);
				this.refreshListCar();
				this.repaintMap_();
				break;

		}
		Common.getInstance().onMenuItemSelected(featureId, item, this);
		return true;
	}

	private void show(int show) {
		Common.getInstance().setShow(show);
		this.repaintMap_();
	}

	private void moveToMyLocation() {
		MapController mmapControler = mMapView.getController();
		mmapControler.animateTo(Preferences.getInstance().getMyLocation());
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Common.getInstance().setJustCreated(false);
		super.onKeyDown(keyCode, event);

		this.mMapView.requestFocus();
		this.mMapView.bringToFront();
		// this.mMapView.forceLayout();
		Preferences.getInstance().setCentreEcran(this.mMapView.getMapCenter(), this.mMapView.getLatitudeSpan(), this.mMapView.getLongitudeSpan());

		boolean r = false;
		switch (keyCode) {
			case KeyEvent.KEYCODE_CAP:
			case KeyEvent.KEYCODE_B:
				selectPrevious();
				r = true;
				break;
			case KeyEvent.KEYCODE_N:
				selectNext();
				r = true;
				break;
			case KeyEvent.KEYCODE_I:
			case KeyEvent.KEYCODE_MINUS:
				// Zoom In
				this.zoom_moins();
				r = true;
				break;
			case KeyEvent.KEYCODE_O:
			case KeyEvent.KEYCODE_PLUS:
				// Zoom Out
				this.zoom_plus();
				r = true;
				break;
			case KeyEvent.KEYCODE_DPAD_CENTER:
				if (Common.getInstance().getMode() == Common.MODE_SET_MY_POSITION) {
					super.setTitle("set ");
					this.setMode(Common.MODE_SHOW_MOBILES);
					this.setMyLocationManuel();
					this.refreshListCar();
					Common.getInstance().goPreferences(this);
					return true;
				} else if (Common.getInstance().getMode() == Common.MODE_SHOW_MOBILES) {
					Common.getInstance().showDetailCarSelected(this);
					return true;
				}
				break;
			case KeyEvent.KEYCODE_DPAD_DOWN:
			case KeyEvent.KEYCODE_DPAD_UP:
			case KeyEvent.KEYCODE_DPAD_LEFT:
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				this.mMapView.getController().onKey(this.mMapView, keyCode, event);
				break;
		}
		Preferences.getInstance().setCentreEcran(this.mMapView.getMapCenter(), this.mMapView.getLatitudeSpan(), this.mMapView.getLongitudeSpan());
		return r;
	}

	private void selectPrevious() {
		CarsFactory.getInstance(this).selectPrevious2();
		this.repaintMap_();
	}

	private void selectNext() {
		CarsFactory.getInstance(this).selectNext2();
		this.repaintMap_();
	}
	
	public void selectCar(Car carSelected){
		CarsFactory.getInstance(this).selectCar(carSelected);
		this.repaintMap_();
	}

	private void zoom_moins() {
		int level = mMapView.getZoomLevel();
		level--;
		mMapView.getController().zoomTo(level);
		this.setZoomLevelAndSpan();
		this.refreshListCar();
	}

	private void zoom_plus() {
		int level = mMapView.getZoomLevel();
		level++;
		mMapView.getController().zoomTo(level);
		this.setZoomLevelAndSpan();
		this.refreshListCar();
	}

	private void setZoomLevelAndSpan() {
		int level = mMapView.getZoomLevel();
		int latSpan = mMapView.getLatitudeSpan();
		int longSpan = mMapView.getLongitudeSpan();
		Preferences.getInstance().setZoomLevelAndSpan(this, latSpan, longSpan, level);
	}

	private void repaintMap_() {
		Point p = this.mMapView.getMapCenter();
		MapController mmapControler = mMapView.getController();
		mmapControler.animateTo(p);
		this.mMapView.requestFocus();
	}

	private void setMyPosition() {
		this.setMode(Common.MODE_SET_MY_POSITION);

	}

	private void showMobiles() {
		this.setMode(Common.MODE_SHOW_MOBILES);
		Common.getInstance().setShow(Common.SHOW_ALL);
		this.repaintMap_();
	}

	private void refreshListCar() {
		CarsFactory.getInstance(this).wsRequestGetCars();
	}

	private void setMyLocationManuel() {
		Point p = this.mMapView.getMapCenter();
		Preferences.getInstance().setMyLocation(this, p);
		CarsFactory.getInstance(this).wsRequestSetLocalisation();
	}

	private void setMode(int mode) {
		Common.getInstance().setMode(mode);
		this.repaintMap_();
		if (mode == Common.MODE_SET_MY_POSITION) {
			this.setTitle("Set My Position");
		} else if (mode == Common.MODE_SHOW_MOBILES) {
			this.setTitle("Show Mobiles");
		} else {
			this.setTitle("Map");
		}
	}


	public MapView getMapView() {
		return mMapView;
	}

	public void updateCarList(SortedSet<Car> l) {
		this.repaintMap_();
	}

	public List<Car> getListCarDisplayed() {
		return listCarDisplayed;
	}

	public void setListCarDisplayed(List<Car> listCarDisplayed) {
		this.listCarDisplayed = listCarDisplayed;
	}


	public Car getCarClicked(int x, int y) {
		MapView m =this.getMapView();
		for (Car car : this.listCarDisplayed) {
			int xCar = car.getX_screen();
			int yCar = car.getY_screen();
			int xx = x ;
			int yy = y ;
			int dX = Math.abs(xx - xCar);
			int dY = Math.abs(yy - yCar);
			Log.i("bg"," x :"+x+"  xx:"+xx+" xCar:"+xCar+" dX:"+dX+" :: y:"+y+" yy:"+yy+" yCar:"+yCar+" dY:"+dY);
			if ((dX <=(2*R_MOBILE)) && (dY <=(2*R_MOBILE))){
				return car;
			}
		}
		return null;
	}

}
