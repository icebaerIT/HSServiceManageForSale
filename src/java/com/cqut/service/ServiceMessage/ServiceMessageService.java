package com.cqut.service.ServiceMessage;

import java.util.Date;
import java.util.List;
import java.util.Map;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.cqut.service.search.SearchService;
import com.cqut.tool.util.BeanUtil;
import com.cqut.tool.util.FileTool;
import  com.cqut.dao.ServiceMessage.ServiceMessageDao;
import com.cqut.dao.systemFile.SystemFileDao;
import  com.cqut.entity.ServiceMessage.ServiceMessage;
import com.cqut.entity.systemFile.SystemFile;

@Service
public class ServiceMessageService extends SearchService implements IServiceMessageService{
	
	@Resource(name="serviceMessageDao")
	ServiceMessageDao dao ;
	
	@Resource(name="systemFileDao")
	SystemFileDao systemFileDao ;

	@Override
	public String getBaseEntityName() {
		return "serviceMessage";
	}

	@Override
	public String getBasePrimaryKey() {
		return "serviceMessage.ID";
	}


/*
 * wxl 显示所有的项目名称
 * @see com.cqut.service.ServiceMessage.IServiceMessageService#chooseProjectMessage(java.lang.String)
 */
	@Override
	public List<Map<String, Object>> showAllProject() throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String nowTime=df.format(new Date());
		String properties[]={"project.projectName AS projectName",
							"project.ID AS projectID",
							"project.contactsName AS linkman",
							 "DATE_FORMAT(project.endTime,'%Y-%m-%d %H:%i:%s') as endTime"
		};
		String baseEntity = "project";
		String condition = " project.endTime >= '"+nowTime+"' ORDER BY project.createTime ";
		List<Map<String, Object>> list = this.originalSearchForeign(properties, baseEntity, null, null, condition, false);
		return list;
	}
/*
 * wxl根据项目id找到联系人
 * @see com.cqut.service.ServiceMessage.IServiceMessageService#getLinkmanByProjectID(java.lang.String)
 */
	@Override
	public List<Map<String, Object>> getLinkmanByProjectID(String ProjectID) {
		String condition = "ID='"+ProjectID+"'";
		String properties[]={"project.contactsName AS linkman"};
		String baseEntity = "project";
		List<Map<String, Object>> list =this.originalSearchForeign(properties, baseEntity, null, null, condition, false);
		return list;
	}
/*WXL 提交新增服务信息
 * @see com.cqut.service.ServiceMessage.IServiceMessageService#addNewServiceMessage(java.lang.String, java.lang.String, int, java.lang.String)
 */
	@Override
	public int addNewServiceMessage(double TheTotalOffer,
			String pictureID, String ProjectName, String ReceiverID,
			String TemplateName, String Title, String OfferorsID, String contactsName)
			throws ParseException  {
		ServiceMessage newServiceMessage=new ServiceMessage();
		newServiceMessage.setID(BeanUtil.createId());
		newServiceMessage.setTheTotalOffer(TheTotalOffer);
		newServiceMessage.setOfferorsID(OfferorsID);
		newServiceMessage.setPictureID(pictureID);
		newServiceMessage.setProjectName(ProjectName);
		newServiceMessage.setReceiverID(ReceiverID);
		newServiceMessage.setTemplateName(TemplateName);
		newServiceMessage.setTitle(Title);
		newServiceMessage.setContactsName(contactsName);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		newServiceMessage.setOfferTime(df.parse(df.format(new Date())));
		return dao.save(newServiceMessage);
	}
	/*
	 * wxl 我发送的服务消息(non-Javadoc)
	 * @see com.cqut.service.ServiceMessage.IServiceMessageService#ISendServiceMessage()
	 */
		@Override
		public List<Map<String, Object>> ISendServiceMessage(String OfferorsID, int row, int page) {
			String properties[]={"servicemessage.ProjectName",
					             "servicemessage.TheTotalOffer",
					             "servicemessage.OfferorsID",
					             "servicemessage.pictureID",
					             "servicemessage.Title",
					             "servicemessage.contactsName",
					             "servicemessage.TemplateName",
					             "DATE_FORMAT(servicemessage.offerTime,'%Y-%m-%d %H:%i:%s') as offerTime",
					             "employee.ID",
					             "employee.employeeName"
					             };
			
			String baseEntity = "servicemessage";
			String joinEntity=" LEFT JOIN employee ON employee.ID = servicemessage.OfferorsID ";
			String condition = "servicemessage.OfferorsID='"+OfferorsID+"'";
//			List<Map<String, Object>> list = this.originalSearchForeign(properties, baseEntity, joinEntity, null, condition, false);
			List<Map<String, Object>> list = this.originalSearchWithpaging(properties, baseEntity, joinEntity, null, condition, false, null, "servicemessage.offerTime", "DESC", row, page);
			for (int i = 0; i<list.size(); i++){
				if((list.get(i).get("pictureID")!=null) && (!(list).get(i).get("pictureID").equals(""))){
					String[] photos=((String) list.get(i).get("pictureID")).split(",");
					for(int k=0;k<photos.length;k++){
						System.out.println("photos["+k+"]:"+photos[k]);
						System.out.println("------------------------");
					}
					String [] FileNames = new String [photos.length];
					String [] FileNameRoute = new String [photos.length];
					for(int j = 0;j<photos.length;j++){
						SystemFile systemfile= new SystemFile();
						System.out.println("photos["+j+"]:"+photos[j]);
						if(!systemfile.equals("")){
							systemfile= systemFileDao.getByID(photos[j]);
							FileNames[j]=systemfile.getFileName();
							FileNameRoute[j]= FileTool.getImgRoute(FileNames[j]);
						}
					}
					list.get(i).put("FileNameRoute", FileNameRoute);
				}
			}
			return list;
		}
/*
 * wxl 我收到的消息(non-Javadoc)
 * @see com.cqut.service.ServiceMessage.IServiceMessageService#IReceiveServiceMessage(java.lang.String)
 */
		@Override
		public List<Map<String, Object>> IReceiveServiceMessage(String ReceiverID, int row, int page) {
			String properties[]={"servicemessage.ProjectName",
		             "servicemessage.TheTotalOffer",
		             "servicemessage.OfferorsID",
		             "servicemessage.pictureID",
		             "servicemessage.Title",
		             "servicemessage.contactsName",
		             "servicemessage.TemplateName",
		             "DATE_FORMAT(servicemessage.offerTime,'%Y-%m-%d %H:%i:%s') as offerTime",
		             "employee.ID",
		             "employee.employeeName"};
			String baseEntity = "servicemessage";
			String joinEntity= " LEFT JOIN employee ON employee.ID = servicemessage.OfferorsID ";
			String condition = "servicemessage.ReceiverID='"+ReceiverID+"'";
//			List<Map<String, Object>> list = this.originalSearchForeign(properties, baseEntity, joinEntity, null, condition, false);
			List<Map<String, Object>> list = this.originalSearchWithpaging(properties, baseEntity, joinEntity, null, condition, false, null, "servicemessage.offerTime", "DESC", row, page);
			for(int i=0;i<list.size();i++){
				if((list.get(i).get("pictureID")!=null)&&(!list.get(i).get("pictureID").equals(""))){
					String[] photos=((String) list.get(i).get("pictureID")).split(",");
					String [] FileNames = new String [photos.length];
					String [] FileNameRoute = new String [photos.length];
					for(int k=0;k<photos.length;k++){
						SystemFile systemfile= new SystemFile();
						System.out.println("photos["+k+"]:"+photos[k]);
						if(!systemfile.equals("")){
							systemfile= systemFileDao.getByID(photos[k]);
							FileNames[k]=systemfile.getFileName();
							FileNameRoute[k]= FileTool.getImgRoute(FileNames[k]);
					}
						list.get(i).put("FileNameRoute", FileNameRoute);
				}
			}
		}
			return list;
		}
/*
 * wxl 选择接收人(non-Javadoc)
 * @see com.cqut.service.ServiceMessage.IServiceMessageService#chooseReceiver()
 */
	@Override
	public List<Map<String, Object>> chooseReceiver() {
		String[] properties = { "department.departmentName", 
								"department.ID AS demeID ",
								"employee.departmentID", 
								"employee.employeeName",
								"employee.ID AS employeeID",
								};

		String baseEntity = "department";
		String joinEntity = "LEFT JOIN employee ON employee.departmentID = department.ID";
		String condition = "1=1";
		List<Map<String, Object>> list = this.originalSearchForeign(properties,
				baseEntity, joinEntity, null, condition, false);
		return list;
	}
}
