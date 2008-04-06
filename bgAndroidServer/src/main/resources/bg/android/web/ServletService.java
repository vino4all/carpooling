package bg.android.web;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bg.util2.logger.LoggerFactoryBg;


import bg.android.positions.MobilesFactories;
//bg.android.web.ServletService
public class ServletService extends  HttpServlet {

	
	private Logger logger = LoggerFactoryBg.getLogger("ServletService");
	
	public ServletService() {
		logger.info("==============");
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doPost(req, resp);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("QueryString:"+request.getQueryString());
		BeanService bean = new BeanService(request);
		String action = bean.getAction();
		String r = "";
		if (action.equals(BeanService.ACTION_DEFAULT)){
		   bean.doDefault();
		} else if (action.equals(BeanService.ACTION_reInitBuffer)){
			MobilesFactories.getInstance().reInitBuffer();
		} else if (action.equals(BeanService.ACTION_SetPosition)){
			bean.doSetLocalization();
			bean.doDefault();
		}else if (action.equals(BeanService.ACTION_setPositionWeb)){
			bean.doSetLocalizationWeb();
			bean.doAcknowledge();
		} else if (action.equals(BeanService.ACTION_simulements)){
			MobilesFactories.getInstance().simuElements();
		}  else if (action.equals(BeanService.ACTION_setPositionSimu)){
			bean.doSetPositionSimu();
		}  else if (action.equals(BeanService.ACTION_sendMessage)){
			bean.doSetLocalization();
			bean.doSendMessage();
		}  else if (action.equals(BeanService.ACTION_getCarsFromWeb)){
			bean.doDefault();
		}  else if (action.equals(BeanService.ACTION_welcome)){
			bean.doWelcome();
		}  else if (action.equals(BeanService.ACTION_getCars)){
			bean.doSetLocalization();
			bean.doDefault();
		} else {
			bean.doSetLocalization();
			bean.doDefault();
		}
		response.setContentType("text/xml");
		response.getWriter().print(bean.getRetour());
	}
	
	

	
	

	
}
