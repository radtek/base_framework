
package com.hisign.xingzhen.xz.api.service;

import com.hisign.bfun.bexception.BusinessException;
import com.hisign.bfun.bmodel.JsonResult;
import com.hisign.xingzhen.xz.api.entity.GroupBackup;
import com.hisign.bfun.bif.BaseService;
import com.hisign.xingzhen.xz.api.model.GroupBackupModel;
import com.hisign.xingzhen.xz.api.param.GroupBackupParam;

/**
 * 《专案组归档记录》 业务逻辑服务接口
 * @author 何建辉
 */
public interface GroupBackupService extends BaseService<GroupBackup,GroupBackupModel, String>{

    public JsonResult backup(GroupBackupParam param) throws BusinessException;

}