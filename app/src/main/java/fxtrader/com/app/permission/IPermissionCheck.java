package fxtrader.com.app.permission;

import java.util.List;

public interface IPermissionCheck {
	/**
	 * 
	 * @Title: granted   
	 * @Description: TODO(权限检测结果)   
	 * @param: deniedPermissions  被禁止的权限
	 * @return: void      
	 */
	public void result(List<String> deniedPermissions);
}
