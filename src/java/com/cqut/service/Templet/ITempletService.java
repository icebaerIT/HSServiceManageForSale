package com.cqut.service.Templet;

import java.util.List;
import java.util.Map;

import com.cqut.entity.Templet.Templet;


public interface ITempletService {

	List<Map<String, Object>> getstr(String conditions);
	public List<Map<String, Object>> getServiceMessageTemplet(int rows, int page, String order, String sort, String condition);
	public int deleteServiceTemplet(String ID);
	public Map<String, Object> getProjectInfoTempletListWithPaging(int rows, int page, String orderFiled, String sortMode, String condition);
	public int deleteTemplets(String[]  IDs);
	//public int updatePropTempletByID(Templet templet,String ID);
}
