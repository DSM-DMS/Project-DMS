package com.dms.templates.report;

import java.io.IOException;

import com.dms.account_manager.UserManager;
import com.dms.templates.DmsTemplate;
import com.dms.utilities.log.Log;
import com.dms.utilities.routing.Route;

import freemarker.template.TemplateException;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

@Route(path="/post/report/write", method={HttpMethod.GET})
public class ReportWriteRouter implements Handler<RoutingContext> {
	private UserManager userManager;
	
	public ReportWriteRouter() {
		userManager = new UserManager();
	}
	
	public void handle(RoutingContext context) {

		boolean isLogin = userManager.isLogined(context);
		if(isLogin) {
			DmsTemplate templates = new DmsTemplate("editor");
			try {
				templates.put("category", "report");
				templates.put("type", "write");
				
				context.response().setStatusCode(200);
				context.response().end(templates.process());
				context.response().close();
			} catch(IOException e) {
				Log.l("IOException");
			} catch(TemplateException e) {
				Log.l("TemplateException");
			}
		} else {
			context.response().setStatusCode(200);
            context.response().putHeader("Content-type", "text/html; utf-8");
            context.response().end("<script>window.location.href='/'</script>");
            context.response().close();
		}
	}
}
