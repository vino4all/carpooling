package bg.android.web;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bg.util2.logger.LoggerFactoryBg;



import bg.android.positions.Mobile;
import bg.android.positions.MobilesFactories;

//bg.android.web.ServletService
public class ServletAdmin extends HttpServlet {

	Logger logger = LoggerFactoryBg.getLogger("ServletAdmin");

	public ServletAdmin() {
		int i = MobilesFactories.getInstance().simuElements();
		logger.info("start init simu " + i);
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doPost(req, resp);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BeanService bean = new BeanService(request);
		String action = bean.getAction();
		request.setAttribute("bean", bean);
		String r = "";
		if (action.equals(BeanService.ACTION_reInitBuffer)) {
			MobilesFactories.getInstance().reInitBuffer();
		} else if (action.equals(BeanService.ACTION_checkSimple)) {
			this.doCheckSimple(bean);
		} else if (action.equals(BeanService.ACTION_check)) {
			this.doCheck(bean);
		} else if (action.equals(BeanService.ACTION_welcome)) {
			bean.doWelcome();
		} else if (action.equals(BeanService.ACTION_simulements)) {
			this.doSimu(bean);
			this.doCheck(bean);
		} else {

		}
		// response.setContentType("application/xml");
		//response.setContentType("application/html");
		//response.getWriter().print(bean.getComment());
		ServletContext servletContext = this.getServletContext();
		RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher("/admin.jsp");
		requestDispatcher.forward(request, response);

	}

	private void doSimu(BeanService bean) {
		int i = MobilesFactories.getInstance().simuElements();
		String comment = "nb Simu " + i;
		bean.setComment(comment);
	}

	private void doCheckSimple(BeanService bean) {
		String comment = "<hr/>";
		comment += "<br/>nb Mobiles" + MobilesFactories.getInstance().getHMobiles().size();
		bean.addComment(comment);
	}

	private void doCheck(BeanService bean) {
		String comment = "<hr/>";
		comment += "<br/>nb Mobiles" + MobilesFactories.getInstance().getHMobiles().size();
		Iterator<Mobile> ite = MobilesFactories.getInstance().getHMobiles().values().iterator();
		comment += "<table border='1'>";
		comment += Mobile.toStringHtmlTrTittle("<td> </td>");
		int i = 0;
		while (ite.hasNext()) {
			Mobile m = ite.next();
			comment += "\n" + m.toStringHtmlTr("<td>" + (i++) + "</td>") + "";
		}
		comment += "</table>";
		bean.addComment(comment);
	}

}
