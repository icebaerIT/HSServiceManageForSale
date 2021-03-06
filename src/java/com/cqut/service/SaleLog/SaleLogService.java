package com.cqut.service.SaleLog;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cqut.dao.SaleLog.SaleLogDao;
import com.cqut.entity.SaleLog.SaleLog;
import com.cqut.entity.SaleLogType.SaleLogType;
import com.cqut.entity.Templet.Templet;
import com.cqut.service.search.SearchService;
import com.cqut.service.type.TypeService;
import com.cqut.tool.util.BeanUtil;
import com.cqut.tool.util.EntityIDFactory;

@Service
public class SaleLogService extends SearchService implements ISaleLogService{
	
	@Resource(name="saleLogDao")
	SaleLogDao dao ;
	@Override
	public String getBaseEntityName() {
		return "saleLog";
	}
	@Override
	public String getBasePrimaryKey() {
		return "saleLog.ID";
	}
	//后台获取
	@Override
	public List<Map<String, Object>> getsalelog(int rows, int page, String order,
			String sort, String condition,String startime,String stoptime){
		if(condition==null){
			if(startime!=null&&stoptime!=null)
				condition="salelog.CreateTime BETWEEN '"+startime+"'AND '"+stoptime+"'";
				else condition="1=1";
				
			}
			String[] properties={
					" parent.`NAME` AS groundfather ",
					" children.`NAME` AS childrens ",
				    " IF(salelog.State =0,'禁用','启用') AS state ",
				    " DATE_FORMAT(salelog.CreateTime,'%Y-%m-%d %H:%m:%s') AS CreateTime ",
					" salelog.Assignments ",
					" salelog.ContactID",
					" salelog.Contents",
					" saleLog.ContractVolume",
					" salelog.CustomerName ",
					" salelog.Email ",
					" salelog.ID ",
					" salelog.ImageID ",
					" salelog.List ",
					" salelog.OfferorsID ",
					" salelog.Phone ",
					" salelog.progress ",
					" salelog.Purpose ",
					" salelog.Receivableamount ",
					" saleLog.Results ",
					" saleLog.Results as operate",
					" salelog.SaleLogName ",
					" salelog.SaleLogTypeCode ",
					" salelog.State ",
					" salelog.TempletID ",
					" saleLog.Visit "
							 };
		 
		 String baseEntity=" salelog ";
		 String joinEntity="LEFT JOIN templet AS child ON child.ID = salelog.TempletID "+
					"LEFT JOIN type AS children ON child.str0 = children.ID "+
					"LEFT JOIN type AS parent ON children.PARENTID = parent.ID";
		return this.originalSearchWithpaging(properties, baseEntity, joinEntity, null, condition, false, null,order, sort,rows, page );
	}
	//保存
	@Override
	public int savaSaleLogbehind(SaleLog data){
		return dao.save(data);
	}
	//改变状态
	@Override
	public SaleLog getState(String ID){
		return dao.getByID(ID);
	}
	@Override
	public int updatastate(SaleLog data){
		return dao.updateByID(data, data.getID());
	}
	//修改信息
	@Override
	public int updataSaleLog(Map<String, Object> map, String ID){
		
		return dao.updatePropByID(map.entrySet(), ID);
	}
    //删除
	 public int deleteSaleLog(String[] ids) throws Exception{
		 for(String id:ids){
			 if(dao.deleteByID(id)<1){
				 throw new Exception();
			 }
		 }
		 return 1;
	 }
	@Override
	public int getSaleLog(String condition){
		
		return dao.getCountByCondition(condition);
	}
	//获取销售资料前台
		@Override
		public List<Map<String, Object>>getSalelog(String ID){
			//String condition =" salelog.OfferorsID = '"+ID+"' ";
			String condition =" 1=1 ";
			String [] properties ={
					" salelog.SaleLogTypeCode ",
					" salelog.SaleLogName ",
					" DATE_FORMAT(salelog.CreateTime,'%Y-%m-%d %H:%m:%s') AS CreateTime" ,
					" salelog.TempletID " ,
					" parent.`NAME` AS groundfather ",
					" children.`NAME` AS childrens ",
					" salelog.ID "
	};
			String baseEntity = " salelog ";
			String joinEntity="LEFT JOIN templet AS child ON child.ID = salelog.TempletID "+
					"LEFT JOIN type AS children ON child.str0 = children.ID "+
					"LEFT JOIN type AS parent ON children.PARENTID = parent.ID";
			List<Map<String, Object>> list = this.originalSearchForeign(properties, baseEntity, joinEntity, null, condition, false);
			return list;
		}
	//获取销售详细信息
		@Override
		public List<Map<String, Object>> getdetail(String ID){
			String condition =" salelog.ID = '"+ID+"' ";
			String [] properties ={
					" DATE_FORMAT(salelog.CreateTime,'%Y-%m-%d %H:%m:%s') AS CreateTime ",
					" salelog.Assignments ",
					" salelog.ContactID",
					" salelog.Contents",
					" saleLog.ContractVolume",
					" salelog.CreateTime ",
					" salelog.CustomerName ",
					" salelog.Email ",
					" salelog.ID ",
					" salelog.ImageID ",
					" salelog.List ",
					" salelog.OfferorsID ",
					" salelog.Phone ",
					" salelog.progress ",
					" salelog.Purpose ",
					" salelog.Receivableamount ",
					" saleLog.Results ",
					" salelog.SaleLogName ",
					" salelog.SaleLogTypeCode ",
					" salelog.State ",
					" salelog.TempletID ",
					" saleLog.Visit "
					};
			String baseEntity = " salelog ";
			String joinEntity="";
			List<Map<String, Object>> list = this.originalSearchForeign(properties, baseEntity, joinEntity, null, condition, false);
			return list;
		}
/*	//实现获取OfferorsID
		@Override
		public List<Map<String, Object>> getOfferorsID(String ID){
			//String condition =" employee.ID = = '"+ID+"' ";
			String condition =" 1=1 ";
			String [] properties ={
					" employee.ID "
	};
			String baseEntity = " employee ";
			String joinEntity="";
			List<Map<String, Object>> list = this.originalSearchForeign(properties, baseEntity, joinEntity, null, condition, false);
			return list;
		}*/
		
	
	
		
//保存用户数据
	@Override
	public int savaSaleLog(SaleLog data) {
//		String id =EntityIDFactory.createId();
		String id=BeanUtil.createId();
		data.setID(id);
		return dao.save(data);
	}
//删除事件
	@Override
public int delete(String ID) {
	// TODO Auto-generated method stub
	return dao.deleteByID(ID);
}
	/** 
	 * 方法简述：传入申请人ID，取出申请信息(销售的)
	 * @author 常景胜
	 * @param offerorsID
	 * @param condition
	 * @param row
	 * @param page
	 * @return 
	 * 
	 */
	@Override
	public List<Map<String, Object>> getSaleLogByOfferorsIDWithpaging(
			String offerorsID, String condition, int row, int page) {
		if (condition != null && condition != "") {
			condition += " AND ";
		} else {
			condition = " ";
		}
		return originalSearchWithpaging(
				new String[] {
						"sl.ID AS ID,sl.`saleLogName` AS NAME",
						"e.employeeName AS EMPLOYEENAME",
						"sl.receivableamount AS PRICE",
						"DATE_FORMAT(sl.createTime, '%Y-%m-%e') AS JOINTIME",
//						"pj.projectName AS PROJECTNAME",
						"IF(sl.state='-1','草稿',IF(sl.state='0','待审核',IF(sl.state='1','审核中',IF(sl.state='2','已审核',IF(sl.state='3','已支付',IF(sl.state='4','驳回',-2)))))) AS STATE" },
				"saleLog AS sl,",
				"employee AS e,project AS pj",
				null,
				condition
						+ " sl.OfferorsID = '"
						+ offerorsID
//						+ "' AND e.ID = sl.OfferorsID AND pj.ID = sl.ProjectID ",
						+ "' AND e.ID = sl.OfferorsID ",
				false, null, "sl.createTime", "DESC", row, page);
	}

}


