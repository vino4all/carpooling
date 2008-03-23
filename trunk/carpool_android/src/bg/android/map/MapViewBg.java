package bg.android.map;

import android.util.Log;
import android.view.MotionEvent;
import bg.android.ActivityMap;
import bg.android.coVoiturage.Car;

import com.google.android.maps.MapView;

public class MapViewBg extends MapView{

	ActivityMap activityMap;
	public MapViewBg(ActivityMap aMap) {
		super(aMap);
		this.activityMap=aMap;
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
			case MotionEvent.ACTION_UP:
				int x = (int) ev.getX();
				int y = (int) ev.getY();
				Car car = this.activityMap.getCarClicked(x, y);
				Log.i("bg", "dispatchTouchEvent up x:" + x + "  y:" + y+"  size:"+this.activityMap.getListCarDisplayed().size()+"   carClicked:"+car);
				this.activityMap.selectCar(car);
				break;

		}
		return super.dispatchTouchEvent(ev);
	}


}
