package com.cqut.service.permission;

import java.util.List;
import java.util.Map;

import com.cqut.entity.permission.Permission;
import com.cqut.tool.treeNode.NodeList;


public interface IPermissionService {

	NodeList getPermissionTree();

	List<Permission> getPermissionByCondition(String condition);

	int savePermission(Permission p);

	int updataPermission(Permission p);

	int deletePermission(String ID);

	List<Map<String, Object>> getPermissionWithPageByCondition(
			String condition, int rows, int page);

	int getPermissionCountByCondition(String condition);
	
}
