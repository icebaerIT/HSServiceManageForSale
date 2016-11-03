package com.cqut.dao.SaleLog;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import  com.cqut.entity.SaleLog.SaleLog;

public interface SaleLogDao {
	public int save(SaleLog saleLog);
	public int saveEntities(@Param("entities")SaleLog[] saleLogs);
	
	public int updateByID(@Param("entity")SaleLog saleLog, @Param("id")String ID);
	public int updateByCondition(@Param("entity")SaleLog saleLog, @Param("condition")String condition);
	public int updatePropByID(@Param("prop")Set<Entry<String, Object>> prop,  @Param("id")String ID);
	public int updatePropByCondition(@Param("prop")Set<Entry<String, Object>> prop,  @Param("condition")String condition);
	
	public int deleteByID(@Param("ID")String ID);
	public int deleteByCondition(@Param("condition")String condition);
	public int deleteEntities(@Param("ids")String[] IDs);
	
	public SaleLog getByID(@Param("ID")String ID);
	public List<SaleLog> getByCondition(@Param("condition")String condition);
	public Map<String,Object> findByID(@Param("properties")String[] properties, @Param("id")String id);
	public List<Map<String,Object>> findByCondition(@Param("properties")String[] properties, @Param("condition")String condition);
	
	public int getCountByCondition(@Param("condition")String condition);
	
	
}
