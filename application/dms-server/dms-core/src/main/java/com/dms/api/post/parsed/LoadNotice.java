package com.dms.api.post.parsed;

import org.boxfox.dms.utilities.actions.RouteRegistration;

import com.dms.parser.dataio.post.PostModel;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

@RouteRegistration(path="/post/school/notice", method={HttpMethod.GET})
public class LoadNotice implements Handler<RoutingContext> {
	@Override
	public void handle(RoutingContext ctx) {

		int no = Integer.parseInt(ctx.request().getParam("no"));
		
		ctx.response().setStatusCode(200);
		ctx.response().end(PostModel.getPost(0, no).toString());
		ctx.response().close();
	}
}