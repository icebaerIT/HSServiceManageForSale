package com.cqut.controller.QuotedPrice;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cqut.entity.CheckProcess.CheckProcess;
import com.cqut.entity.ProcessDescription.ProcessDescription;
import com.cqut.entity.Process.Process;
import com.cqut.service.CheckProcess.ICheckProcessService;
import com.cqut.service.ProcessDescription.IProcessDescriptionService;
import com.cqut.entity.QuotedPrice.QuotedPrice;
import com.cqut.entity.QuotedPriceDtaile.QuotedPriceDtaile;
import com.cqut.service.QuotedPrice.IQuotedPriceService;
import com.cqut.service.SaleLog.ISaleLogService;
import com.cqut.tool.util.BeanUtil;

@Controller
@RequestMapping("/quotedPriceController")
public class QuotedPriceController {

	/**
	 * @Fields service : TODO
	 */
	@Resource(name = "quotedPriceService")  
	IQuotedPriceService service;

	/**
	 * @Fields checkProcessservice : TODO
	 */
	@Resource(name = "checkProcessService")
	ICheckProcessService checkProcessservice;

	/**
	 * @Fields processDescriptionService : TODO
	 */
	@Resource(name = "processDescriptionService")
	IProcessDescriptionService processDescriptionService;
	
	@Resource(name = "saleLogService")
	ISaleLogService saleLogService;
	/**
	 * 方法简述：取用户报价数据
	 * 
	 * @author 常景胜
	 * @param session
	 * @param page
	 * @param row
	 * @return
	 * 
	 */
	@RequestMapping("/getQuotedPriceInfo")
	@ResponseBody
	public String getQuotedPriceInfo(HttpSession session, int page, int row) {
		String ID = (String) session.getAttribute("ID");
		 ID = "111";
		List<Map<String, Object>> list = service.getQuotedPriceInfo(ID, page,
				row);
		int total = service.getQuotedPriceInfoCount(ID);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("total", total);
		return JSONArray.fromObject(map).toString();
	}

	/**
	 * 方法简述：新建菜单数据
	 * 
	 * @author 常景胜
	 * @param session
	 * @return
	 * 
	 */
	@RequestMapping("/getNewQuotedPriceInfo")
	@ResponseBody
	public String getNewQuotedPriceInfo(HttpSession session) {
		String ID = (String) session.getAttribute("ID");
		 ID = "111";
		List<Map<String, Object>> list = service.getNewQuotedPriceInfo(ID);
		List<Map<String, Object>> templet = service.getTemplet("1");
		List<Map<String, Object>> project = service.getProject();
		List<Map<String, Object>> employee = service.getEmployee(ID);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("templet", templet);
		map.put("list", list);
		map.put("project", project);
		map.put("employee", employee);
		return JSONArray.fromObject(map).toString();
	}
	

	/** 
	 * 方法简述 ：通过用户ID选择非用户的ID
	 * @author 常景胜
	 * @param session
	 * @return 
	 * 
	 */
	@RequestMapping("/getEmployeeNotMe")
	@ResponseBody
	public String getEmployeeNotMe(HttpSession session) {
		String ID = (String) session.getAttribute("ID");
		 ID = "111";
		List<Map<String, Object>> employee = service.getEmployee(ID);;
		return JSONArray.fromObject(employee).toString();
	}

	/**
	 * 方法简述：保存报价单数据
	 * 
	 * @author 常景胜
	 * @param param
	 * @param session
	 * @param dtaileNameList
	 * @param dtailePriceList
	 * @param employeeMyid
	 * @param employeeOpen
	 * @param employeeName
	 * @return
	 * 
	 */
	@RequestMapping("/saveNewQuotedPriceInfo")
	@ResponseBody
	public String saveNewQuotedPriceInfo(QuotedPrice param,
			HttpSession session, String dtaileNameList, String dtailePriceList,
			String employeeMyid, String employeeOpen, String employeeName) {
		ProcessDescription theProcessDescription = new ProcessDescription();// 创建报价流程描述
		String ID = (String) session.getAttribute("ID");// 获取用户的ID
		 ID = "111";
		String chars = "QWERTYUIOPASDFGHJKLZXCVBNM";
		int result;
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 将时间转换为
																			// //
																			// yyyy-MM-dd
																			// //
																			// HH:mm:ss格式
		String dat = sdf.format(date);
		Date datee = null;

		try {
			datee = sdf.parse(dat);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// 存报价详情//
		String[] dtaileNameArry = dtaileNameList.split(",");
		String[] dtailePriceArry = dtailePriceList.split(",");
		QuotedPriceDtaile dtaile = new QuotedPriceDtaile();
		for (int i = 0; i < dtaileNameArry.length; i++) {
			Date now = new Date();
			dtaile.setModelName(dtaileNameArry[i]);
			dtaile.setModelPrice(Double.parseDouble(dtailePriceArry[i]));
			String IID = String.valueOf(now.getTime());
			dtaile.setID(IID);
			dtaile.setQuotedPriceID(param.getID());
			theProcessDescription.setProcessID(param.getProcessID());// 保存报价流程ID
			theProcessDescription.setID(param.getID());// 报价单ID与流程ID相同
			theProcessDescription.setType(2);// 风格为报价流程
			theProcessDescription.setState(1);// 状态为待审核
			theProcessDescription.setSubmitAccountID(param.getID());// 保存报价单ID
			theProcessDescription.setOrderNumber(1);// 审核顺序
			param.setQuotedPriceCode(chars.charAt((int) (Math.random() * 26))
					+ "" + chars.charAt((int) (Math.random() * 26)) + "" + IID);// 放入编码

		}
		// 判断是否为自定义审批人
		if (employeeOpen.equals("1")) {// 如果审批人自定义功能打开"1",更改流程描述的流程ID
			System.out.println("创建新流程");
			Date now = new Date();
			theProcessDescription.setProcessID(String.valueOf(now.getTime()));// 更改流程描述的流程ID
			param.setProcessID(theProcessDescription.getProcessID());
			Process newProcess = new Process();// 新建流程
			CheckProcess newCheckProcess = new CheckProcess();// 新建审批流程
			// 初始化流程和审批流程
			newProcess.setCreateTime(datee);// 新流程创建时间
			newProcess.setID(theProcessDescription.getProcessID());
			newProcess.setProcessCode(chars.charAt((int) (Math.random() * 26))
					+ "" + chars.charAt((int) (Math.random() * 26)) + ""
					+ theProcessDescription.getProcessID());
			newProcess.setProcessName("手动流程" + newProcess.getProcessCode());
			newProcess.setProcessType(2);
			newProcess.setProcessState(1);
			newProcess.setType(1);
			newCheckProcess.setID(theProcessDescription.getProcessID());
			newCheckProcess.setProcessID(newProcess.getID());
			newCheckProcess.setOrderNumber(1);
			newCheckProcess.setCheckName(employeeName);
			newCheckProcess.setCheckUserID(employeeMyid);
			// 保存流程和审批流程到数据库
			service.saveNewProcess(newProcess);
			service.saveNewCheckProcess(newCheckProcess);
		}
		param.setOfferorsID(ID);// 改报价单用户id
		param.setJoinTime(datee);// 改报价单生成时间
		result = service.saveNewQuotedPriceInfo(param);// 保存报价单
		if (result == 1) {// 判断报价单是否保存成功
			service.saveNewProcessDescription(theProcessDescription);// 保存报价流程
			service.saveNewQuotedPriceDtaileInfo(dtaile);// 保存报价细项
		}
		return result + "";
	}

	/** 
	 * 方法简述：常景胜删除报价单
	 * @author 常景胜
	 * @param condition
	 * @return 
	 * 
	 */
	@RequestMapping("/deletQuotedPriceInfo")
	@ResponseBody
	public String deletQuotedPriceInfo(String condition) {
		int list = service.deletQuotedPriceInfo(condition);
		return list + "";
	}

	/** 
	 * 方法简述：常景胜获取报价细项
	 * @author 常景胜
	 * @param condition
	 * @return 
	 * 
	 */
	@RequestMapping("/quoDetailsList")
	@ResponseBody
	public String quoDetailsList(String condition) {
		List<Map<String, Object>> list = service.quoDetailsList(condition);
		return JSONArray.fromObject(list).toString();
	}

	/** 
	 * 方法简述：获取报价次数
	 * @author 常景胜
	 * @param IDs
	 * @param betweens
	 * @param type
	 * @return 
	 * 
	 */
	@RequestMapping("/getEmployeeInfo")
	@ResponseBody
	public String getEmployeeInfo(String IDs, String betweens, int type) {

		String[] betweenArray = betweens.split(",");// 将获取的时间分成字符串
		// 根据获取的type得知模式0年1月2周3日
		Map<String, Object> map = getYearInfo(IDs, betweenArray, type);
		return JSONArray.fromObject(map).toString();

	}


	/** 
	 * 方法简述：处理时间格式
	 * @author 常景胜
	 * @param IDs
	 * @param betweenArray
	 * @param type
	 * @return 
	 * 
	 */
	@RequestMapping("/getYearInfo")
	@ResponseBody
	public Map<String, Object> getYearInfo(String IDs, String[] betweenArray,
			int type) {
		Calendar calendar = Calendar.getInstance();// 获取日历对象
		calendar.add(Calendar.WEEK_OF_YEAR, -1);
		System.out.println(calendar);
		// Date date = new Date();// 一个对象用来获取当前时间
		List<Map<String, Object>> list = null;
		Map<String, Object> map = new HashMap<String, Object>();
		String mod = "%Y-%m-%d";
		switch (type) {
		case 0:
			mod = "%Y";
			break;
		case 1:
			mod = "%Y-%m";
			break;
		case 2:
			mod = "%Y-%m-%d";
			break;
		case 3:
			mod = "%Y-%m-%d";
			break;
		}

		for (int i = 0; i < betweenArray.length; i++) {
			list = service.getEmployeeInfo(IDs, betweenArray[i], mod);
			map.put("" + i + "", list);
		}
		return map;
	}

	/**
	 * 常景胜获取报价单(后台)
	 * */
	@RequestMapping("/getBackEmployeeInfo")
	@ResponseBody
	public String getBackEmployeeInfo(int rows, int page, String order,
			String sort, String condition) {
		List<Map<String, Object>> list = service.getBackEmployeeInfo(rows,
				page, order, sort, condition);
		int count = service.countBackEmployeeInfo("1=1");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", count);
		map.put("rows", list);
		return JSONObject.fromObject(map).toString();

	}

	/**
	 * 常景胜删除报价单(后台)
	 * */
	@RequestMapping("/deleteBackEmployeeInfo")
	@ResponseBody
	public String deleteBackEmployeeInfo(String[] IDs) {
		for (int i = 0; i < IDs.length; i++) {
			service.deleteBackEmployeeInfo(IDs[i]);
		}
		return "1";
	}

	/**
	 * 常景胜 获取报价细项(后台)
	 */
	@RequestMapping("/quoDetailsListBack")
	@ResponseBody
	public String quoDetailsListBack(String ID) {
		List<Map<String, Object>> list = service.quoDetailsListBack(ID);
		return JSONArray.fromObject(list).toString();
	}

	/**
	 * 常景胜删除模板(后台)
	 * */
	@RequestMapping("/deleteTemplet")
	@ResponseBody
	public String deleteTemplet(String[] IDs) {
		for (int i = 0; i < IDs.length; i++) {
			service.deleteTemplet(IDs[i]);
		}
		return "1";
	}

	/**
	 * 常景胜获取模板(后台)
	 * */
	@RequestMapping("/getTemplet")
	@ResponseBody
	public String getTemplet(int rows, int page, String order, String sort,
			String condition) {
		List<Map<String, Object>> list = service.getTemplet(rows, page, order,
				sort, condition);
		int count = service.countTemplet("1=1");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", count);
		map.put("rows", list);
		return JSONObject.fromObject(map).toString();
	}

	/**
	 * 胡定鹏 获取传入审核人的id对应的审批信息
	 * 
	 * @param userid
	 *            ：审批人的ID
	 * @return
	 */
	@RequestMapping("/getMyQuotedPriceByCheckName")
	@ResponseBody
	public String getMyQuotedPriceByCheckName(HttpSession session, int row,
			int page) {
		String userID = (String) session.getAttribute("ID");
		userID = "111";
		List<Map<String, Object>> checkProcessList = checkProcessservice
				.getCheckProcess(" p.processState='1' AND cp.processID = p.ID AND p.processType = '2' ");
		for (int i = 0; i < checkProcessList.size(); i++) {
			if (!checkProcessList.get(i).get("CHECKUSERID").toString()
					.contains(userID)) {
				checkProcessList.remove(i);
				i--;
			}
		}
		StringBuffer condition = new StringBuffer();
		StringBuffer pdCondition = new StringBuffer();
//		StringBuffer slCondition = new StringBuffer();
		pdCondition.append(" qp.state != '-1' ");
		for (int i = 0; i < checkProcessList.size(); i++) {
//			if (checkProcessList.size()-i != 0) {
//				pdCondition.append(" AND ");
//			}
			if (i == 0) {
				pdCondition.append(" AND ");
				condition.append(" (");
			}
			if (i != 0) {
				condition.append(" || ");
			}
			condition.append("( pd.processID='"
					+ checkProcessList.get(i).get("PROCESSID")
					+ "' && pd.orderNumber='"
					+ (Integer.parseInt(checkProcessList.get(i)
							.get("ORDERNUMBER").toString())) + "')");
			if (i == (checkProcessList.size() - 1)) {
				condition.append(") ");
			}
		}
		// 取出报价审批信息
		List<Map<String, Object>> saleLogList = service
				.getQuotedPriceByConditonWithpaging(
						pdCondition.append(condition).toString(), row, page);
		return JSONArray.fromObject(saleLogList).toString();
	}

	/** 
	 * 方法简述：取出我审核的销售日志
	 * @author 常景胜
	 * @param session
	 * @param row
	 * @param page
	 * @return 
	 * 
	 */
	@RequestMapping("/getMySaleLogByCheckName")
	@ResponseBody
	public String getMySaleLogByCheckName(HttpSession session, int row,
			int page) {
		String userID = (String) session.getAttribute("ID");
		userID = "111";
		List<Map<String, Object>> checkProcessList = checkProcessservice
				.getCheckProcess(" p.processState='1' AND cp.processID = p.ID AND p.processType='1' ");
		for (int i = 0; i < checkProcessList.size(); i++) {
			if (!checkProcessList.get(i).get("CHECKUSERID").toString()
					.contains(userID)) {
				checkProcessList.remove(i);
				i--;
			}
		}
		StringBuffer condition = new StringBuffer();
		StringBuffer pdCondition = new StringBuffer();
//		StringBuffer slCondition = new StringBuffer();
		pdCondition.append(" sl.state != '-1' ");
		for (int i = 0; i < checkProcessList.size(); i++) {
//			if (checkProcessList.size()-i != 0) {
//				pdCondition.append(" AND ");
//			}
			if (i == 0) {
				pdCondition.append(" AND ");
				condition.append(" (");
			}
			if (i != 0) {
				condition.append(" || ");
			}
			condition.append("( pd.processID='"
					+ checkProcessList.get(i).get("PROCESSID")
					+ "' && pd.orderNumber='"
					+ (Integer.parseInt(checkProcessList.get(i)
							.get("ORDERNUMBER").toString())) + "')");
			if (i == (checkProcessList.size() - 1)) {
				condition.append(") ");
			}
		}
		// 取出报价审批信息
		List<Map<String, Object>> SaleLogList = service
				.getMySaleLogByConditonWithpaging(
						pdCondition.append(condition).toString(), row, page);
		return JSONArray.fromObject(SaleLogList).toString();
	}

	/**
	 * 胡定鹏 传入申请人ID，取出申请信息
	 * 
	 * @param offerorsID
	 *            申请人ID
	 * @param condition
	 *            条件
	 * @return
	 */
	@RequestMapping("/getQuotedPriceByOfferorsID")
	@ResponseBody
	public String getQuotedPriceByOfferorsID(String condition, int row,
			int page, HttpSession session) {
		String offerorsID = (String) session.getAttribute("ID");
		 offerorsID = "111";
		return JSONArray.fromObject(
				service.getQuotedPriceByOfferorsIDWithpaging(offerorsID,
						condition, row, page)).toString();
	}
	/** 
	 * 方法简述：传入申请人ID，取出申请信息(销售的)
	 * @author 常景胜
	 * @param condition
	 * @param row
	 * @param page
	 * @param session
	 * @return 
	 * 
	 */
	@RequestMapping("/getSaleLogByOfferorsID")
	@ResponseBody
	public String getSaleLogByOfferorsID(String condition, int row,
			int page, HttpSession session) {
		String offerorsID = (String) session.getAttribute("ID");
		 offerorsID = "111";
		return JSONArray.fromObject(
				saleLogService.getSaleLogByOfferorsIDWithpaging(offerorsID,
						condition, row, page)).toString();
	}
	/**
	 * 胡定鹏 审批操作
	 * 
	 * @param quotedPriceID
	 *            报价单ID
	 * @param userID
	 *            审批人ID     
	 * @param lastCheckUser     
	 *            自定义流程时是否结束流程      
	 * @param theCheckUserID
	 * 	      	  下一位审批人ID          
	 * @return
	 * 
	 */
	@RequestMapping("/operationQuotedPrice")
	@ResponseBody
	public String throughQuotedPrice(String quotedPriceID, int type,
			String reason,int lastCheckUser,String theCheckUserID,HttpSession session) {
		String userID = (String) session.getAttribute("ID");
		System.out.println("userID:" + userID);
		 userID = "111";
		 // type=1 通过，type=2 驳回，type=3 撤回  
		QuotedPrice quotedPrice = service.getQuotedPriceByID(quotedPriceID);
		List<Map<String, Object>> checkProcessList = checkProcessservice
				.getCheckProcess(" p.processState='1' AND cp.processID=p.ID AND cp.processID = '"
						+ quotedPrice.getProcessID()
						+ "' order by cp.orderNumber DESC");
		ProcessDescription pd = new ProcessDescription();
		ProcessDescription thepd = new ProcessDescription();
		CheckProcess thecp = new CheckProcess();
		String[] theCheckMan = null;
		// 取出修改当前流程描述信息
		List<ProcessDescription> pdList = processDescriptionService
				.getProcessDescriptionByCondition(" processdescription.ProcessID='"
						+ quotedPrice.getProcessID()
						+ "' AND processdescription.orderNumber='"
						+ (quotedPrice.getProgress() + 1)
						+"' AND processdescription.submitAccountID = '" + quotedPriceID
						+ "' order by processdescription.orderNumber");
		List<ProcessDescription> thepdList = processDescriptionService
				.getProcessDescriptionByCondition(" processdescription.ProcessID='"
						+ quotedPrice.getProcessID()
						+ "' AND processdescription.orderNumber='"
						+ (quotedPrice.getProgress())
						+"' AND processdescription.submitAccountID = '" + quotedPriceID
						+ "' order by processdescription.orderNumber");
		//取出此订单目前次序的审批流程
		List<CheckProcess> thecpList = checkProcessservice.getCheckProcessByCondition("checkprocess.ProcessID='"+quotedPrice.getProcessID() 
										+ "'AND checkprocess.orderNumber= '"
										+quotedPrice.getProgress()+"'");
		//取出对应报价单的流程
		List<Process> pList = service.getQuotedPriceProcessByID(" ID = " + quotedPrice.getProcessID()); 
		Process P = pList.get(0);
		
		if(thecpList.size() > 0){
			thecp = thecpList.get(0);
			theCheckMan = thecp.getCheckUserID().split(",");
		}
		if (pdList.size() > 0) {
			pd = pdList.get(0);
			System.out.println("pdList.size()==" + pdList.size());
			System.out.println(pdList.get(0));
		}

		if(thepdList.size() > 0){
			thepd = thepdList.get(0);
		}
		// 审批通过--该消息已经被同级审批、该消息已经被撤回
		// 审批驳回--该消息已经被同级审批、该消息已经被撤回
		// 审批撤回--该消息已经审批结束
		if (quotedPrice.getState() == -1) {
			return "4";// 消息已经被撤回，不可审批
		}
		if (type == 3) {
			// 该信息是否已经审批结束
			if (quotedPrice.getState() != 0 && quotedPrice.getState() != 1) {
				return "2";// 消息已经被审批，不可撤回
			}
			// 撤回审批信息
			quotedPrice.setState(-1);
			quotedPrice.setProgress(0);
		} else {
			// 该信息是否被同级人员审批
//			String aa = pd.getCheckUserID();
//			System.out.println(aa);
//			System.out.println(!aa.equals(""));
//			System.out.println(pdList.size() <= 0);
//			System.out.println(pd.getCheckUserID() != null);
//			System.out.println(!pd.getCheckUserID().equals(""));
//			System.out.println(pd.getCheckUserID().equals(userID));
			System.out.println(pd.getCheckUserID() != null);
			System.out.println(thepdList.size() > 0);
			if(thepdList.size() > 0){
				if (thepd.getCheckUserID() != null || thepd.getCheckUserID() != "" || theCheckMan!=null) {
					for(String n : theCheckMan){
						if(n.equals(userID)){
							return "3";// 该消息已经被同级人员审批，不可审批
						}

					}
				}
			}
			pd.setCheckUserID(userID);

			//判断是否为自定义流程
			if(P.getType() == 1 && type == 1){               
				System.out.println("手动流程");
				if (lastCheckUser == 1) {
					// 当自定义审批人结束流程的时候
					System.out.println("这是最后一个流程");
					quotedPrice.setState(2);
					}else if(theCheckUserID != null && theCheckUserID != "" && theCheckUserID!="0" ) {
						if (quotedPrice.getProgress() == 0) {
							// 当审批人是此次审批流程的第一级时
							quotedPrice.setState(1);
						}
						// 保存下一级流程描述的基本信息
						ProcessDescription pd1 = new ProcessDescription();
						pd1.setID(BeanUtil.createId());
						pd1.setProcessID(pd.getProcessID());
						pd1.setCheckUserID(null);
						pd1.setOrderNumber(pd.getOrderNumber() + 1);
						pd1.setState(1);
						pd1.setSubmitAccountID(quotedPriceID);
						processDescriptionService.saveProcessDescription(pd1);
						// 保存下一级审批流程的基本信息
						CheckProcess cp1 = new CheckProcess();
						cp1.setID(BeanUtil.createId());
						cp1.setProcessID(pd.getProcessID());
						cp1.setOrderNumber(pd.getOrderNumber() + 1);
						cp1.setCheckUserID(theCheckUserID);
						checkProcessservice.savacheckProcess(cp1);
					}else{
						return "11";
					}
				pd.setState(2);
				quotedPrice.setProgress(quotedPrice.getProgress() + 1);
			}else if (type == 1 && P.getType() == 0) {
				// 通过审批信息
				// System.out.println("quotedPrice.getProgress():"+quotedPrice.getProgress());
				// System.out.println("checkProcessList.size() - 1:"+(checkProcessList.size()
				// - 1));
				if (quotedPrice.getProgress() == (checkProcessList.size() - 1)) {
					// 当审批人是此次审批流程的最后一级时
					System.out.println("这是最后一个流程");
					quotedPrice.setState(2);
				} else {
					if (quotedPrice.getProgress() == 0) {
						// 当审批人是此次审批流程的第一级时
						quotedPrice.setState(1);
					}
					// 保存下一级流程描述的基本信息
					ProcessDescription pd1 = new ProcessDescription();
					pd1.setID(BeanUtil.createId());
					pd1.setProcessID(pd.getProcessID());
					pd1.setCheckUserID(null);
					pd1.setOrderNumber(pd.getOrderNumber() + 1);
					pd1.setState(1);
					pd1.setSubmitAccountID(quotedPriceID);
					processDescriptionService.saveProcessDescription(pd1);
				}
				pd.setState(2);
				quotedPrice.setProgress(quotedPrice.getProgress() + 1);
			} else if (type == 2) {
				// 驳回审批1
				quotedPrice.setState(4);
				quotedPrice.setProgress(0);
				pd.setState(0);
				if (reason != null && reason != "") {
					pd.setRemark(reason);
					// System.out.println("reason:"+reason);
				}
			}
			// 保存当前流程描述信息
			processDescriptionService.updateProcessDescription(pd);
		}
		// 保存报价单信息
		return service.updateByID(quotedPrice) == 1 ? "true" : "false";
	}
}
