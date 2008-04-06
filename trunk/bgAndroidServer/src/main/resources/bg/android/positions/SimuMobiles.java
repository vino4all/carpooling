package bg.android.positions;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Logger;

import com.bg.util2.logger.LoggerFactoryBg;


//  bg.android.positions.SimuMobiles
public class SimuMobiles implements Runnable{

	private Logger logger = LoggerFactoryBg.getLogger("SimuMobiles");
	private boolean isOn = true;
	private long timeSleep = 10L*1000L;
	private String name="";
	Thread t;
	
	public SimuMobiles() {
		super();
	}
	
	public void activate() {
		t = new Thread(this);
		t.start();
	}
	
	public void deActivate() {
		isOn = false;
		t.interrupt();
	}
	
	
	private void updateMobilesSimu(){
		try {
			int nbSimu=0;
			int nbSimuNo=0;
			Iterator<Mobile> ite = MobilesFactories.getInstance().getHMobiles().values().iterator();			
			while(ite.hasNext()){
				Mobile m =ite.next(); 
				if (m== null){						
				}else if (m.isSimu()){
					m.setDate(new Date());
					nbSimu++;
				}else {
					nbSimuNo++;
				}
			}
			logger.info("updateMobilesSimu done nbSimu : "+nbSimu+" nbSimuNo : "+nbSimuNo+"  total:"+(nbSimu+nbSimuNo));
		} catch (Exception e) {
			logger.info("collectMobilesInactif Exception"+e);
		}
	}
	
	
	public void run(){
		while(isOn){
			try {
				Thread.sleep(timeSleep);
				this.updateMobilesSimu();
			} catch (Exception e) {
				
			}
		}
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

}
