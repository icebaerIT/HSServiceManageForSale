package com.cqut.controller.ServiceMessage;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cqut.service.ServiceMessage.IServiceMessageService;

@Controller
@RequestMapping("/serviceMessageController")
public class ServiceMessageController {

	@Resource(name = "serviceMessageService")
	IServiceMessageService service;

	// 测试是否登录,获取用户ID
	@RequestMapping("/test")
	@ResponseBody
	public String test(HttpSession httpSession) throws ParseException {
		Object UserID = httpSession.getAttribute("ID");
		return (String) UserID;

	}

	// wxl显示所有的项目名
	@RequestMapping("/getAllProjectName")
	@ResponseBody
	public String getAllProjectName() throws ParseException {
		List<Map<String, Object>> list = service.showAllProject();
		if (list.size() == 0) {
			return "false";
		} else {
			return JSONArray.fromObject(list).toString();
		}
	}

	// wxl显示所有的项目名
	@RequestMapping("/getLinkmanByProjectID")
	@ResponseBody
	public String getLinkmanByProjectID(String ProjectID) {
		List<Map<String, Object>> list = service
				.getLinkmanByProjectID(ProjectID);
		return JSONArray.fromObject(list).toString();
	}

	// wxl 提交新增服务信息
	@RequestMapping("/addNewServiceMessage")
	@ResponseBody
	public String addNewServiceMessage(double TheTotalOffer,
			String pictureID, String ProjectName, String ReceiverID,
			String TemplateName, String Title, HttpSession httpSession, String contactsName) throws ParseException {
//		Object OfferorsID = httpSession.getAttribute("ID");
		String OfferorsID="20151128004";
		return service.addNewServiceMessage(TheTotalOffer, pictureID,
				ProjectName, ReceiverID, TemplateName, Title, (String) OfferorsID, contactsName) == 1 ? "true"
				: "false";
	}

	// wxl 显示我发送的服务消息
	@RequestMapping("/ISendServiceMessage")
	@ResponseBody
	public String ISendServiceMessage(HttpSession httpSession,int row, int page) {
//		Object OfferorsID = httpSession.getAttribute("ID");
		String OfferorsID = "1";
		List<Map<String, Object>> list = service
				.ISendServiceMessage((String)OfferorsID, row, page);
		return JSONArray.fromObject(list).toString();
	}

	// wxl 显示我收到的服务消息
	@RequestMapping("/IReceiveServiceMessage")
	@ResponseBody
	public String IReceiveServiceMessage(HttpSession httpSession,int row, int page) {
//		Object ReceiverID = httpSession.getAttribute("ID");
		String ReceiverID="1";
		List<Map<String, Object>> list = service
				.IReceiveServiceMessage((String)ReceiverID, row, page);
		return JSONArray.fromObject(list).toString();
	}

	// wxl 显示接收人
	@RequestMapping("/chooseReceiver")
	@ResponseBody
	public String chooseReceiver() {
		List<Map<String, Object>> list = service.chooseReceiver();
		return JSONArray.fromObject(list).toString();
	}
}
