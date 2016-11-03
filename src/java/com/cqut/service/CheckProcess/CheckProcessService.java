package com.cqut.service.CheckProcess;

import java.util.List;
import java.util.Map;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.cqut.service.search.SearchService;
import com.cqut.tool.util.BeanUtil;
import com.cqut.tool.util.EntityIDFactory;

import com.cqut.dao.CheckProcess.CheckProcessDao;
import com.cqut.entity.CheckProcess.CheckProcess;
import com.cqut.entity.ProcessDescription.ProcessDescription;
import com.cqut.entity.SaleLog.SaleLog;

@Service
public class CheckProcessService extends SearchService implements
		ICheckProcessService {

	@Resource(name = "checkProcessDao")
	CheckProcessDao dao;

	@Override
	public String getBaseEntityName() {
		return "checkProcess";
	}

	@Override
	public String getBasePrimaryKey() {
		return "checkProcess.ID";
	}
	//保存自定义流程数据
		@Override
		public int savacheckProcess(CheckProcess data) {
//			String id =EntityIDFactory.createId();
//			String ID=BeanUtil.createId();
			return dao.save(data);
		}
		
	/* (non-Javadoc)
	 * 覆盖方法描述：通过condition取 CheckProcess
	 * @see com.cqut.service.CheckProcess.ICheckProcessService#getCheckProcessByCondition(java.lang.String)
	 * 常景胜
	 */
	@Override
	public List<CheckProcess> getCheckProcessByCondition(String condition){
		return dao.getByCondition(condition);
	}
	/**
	 * 胡定鹏
	 * 获取CheckProcess
	 * @condition 条件，没有为null
	 */
	@Override
	public List<Map<String, Object>> getCheckProcess(String condition) {
		return this.originalSearchForeign(new String[] {
				"cp.processID AS PROCESSID", "cp.orderNumber AS ORDERNUMBER",
				"cp.checkUserID AS CHECKUSERID" }, "checkprocess AS cp,",
				"process AS p", null, condition, false);
	}




}
