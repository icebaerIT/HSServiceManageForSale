package com.cqut.service.type;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.cqut.service.search.SearchService;
import  com.cqut.dao.type.TypeDao;
import  com.cqut.entity.type.Type;

@Service
public class TypeService extends SearchService implements ITypeService{
	
	@Resource(name="typeDao")
	TypeDao dao ;

	@Override
	public String getBaseEntityName() {
		return "type";
	}

	@Override
	public String getBasePrimaryKey() {
		return "type.ID";
	}
	@Override
	public List<Type> getSalelogByCondition(String condition) {
		return dao.getByCondition(condition);
	}
//获取分类信息
	public List<Map<String, Object>> getType(String ID){
		String condition =" type.ID = '"+ID+"' ";
		String [] properties ={
				   " type.`CODE` as CODE",
				   " type.DESCRIPTION ",
				   " type.ID ",
				   " type.`LEVEL` as LEVEL",
				   " type.`NAME` as NAME ",
				   " type.PARENTID ",
				   " type.TYPE",
				   " IF(type.STATE =0,'禁用','启用') AS STATE "
		};
		String baseEntity = " type ";
		String joinEntity="";
		List<Map<String, Object>> list = this.originalSearchForeign(properties, baseEntity, joinEntity, null, condition, false);
		return list;
	}
	//换级
	@Override
	public List<Map<String, Object>> getnext(int rows, int page, String order,
			String sort, String condition){
		if(condition==null){
			System.out.println("cuo");
			condition="LEVEL=1";}
//		condition="level=2";
		String[] properties={
				               " type.`CODE` as CODE",
							   " type.DESCRIPTION ",
							   " type.ID ",
							   " type.`LEVEL` as LEVEL",
							   " type.`NAME` as NAME ",
							   " type.PARENTID ",
							   " type.TYPE",
							   " IF(type.STATE =0,'禁用','启用') AS STATE ",
							   " type.TYPE as operate"
				};
		 String baseEntity=" type ";
		 String joinEntity="";
		return this.originalSearchWithpaging(properties, baseEntity, joinEntity, null, condition, false, null,order, sort,rows, page );
	}
	public int gettype(String condition){
		return dao.getCountByCondition(condition);
	}
	//改变状态
		@Override
		public Type getState(String ID){
			return dao.getByID(ID);
		}
	@Override
	public int updatastate(Type data){
		return dao.updateByID(data, data.getID());
	}
	//修改信息
		@Override
		public int updataType(Map<String, Object> map, String ID){
			return dao.updatePropByID(map.entrySet(), ID);
		}
    //删除
		 public int deleteType(String[] ids) throws Exception{
			 for(String id:ids){
				 if(dao.deleteByID(id)<1){
					 throw new Exception();
				 }
			 }
			 return 1;
		 }
}
