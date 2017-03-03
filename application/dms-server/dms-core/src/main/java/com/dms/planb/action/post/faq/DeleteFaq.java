package com.dms.planb.action.post.faq;

import java.sql.SQLException;

import org.boxfox.dms.utilities.actions.ActionRegistration;
import org.boxfox.dms.utilities.actions.Actionable;
import org.boxfox.dms.utilities.actions.support.Sender;
import org.boxfox.dms.utilities.json.EasyJsonObject;

import com.dms.planb.support.Commands;

@ActionRegistration(command = Commands.DELETE_FAQ)
public class DeleteFaq implements Handler<RoutingContext> {
	@Override
	public EasyJsonObject action(Sender sender, int command, EasyJsonObject requestObject) throws SQLException {
		int no = requestObject.getInt("no");
		
		int status = database.executeUpdate("DELETE FROM faq WHERE no=", no);
		
		responseObject.put("status", status);
		
		return responseObject;
	}
}
