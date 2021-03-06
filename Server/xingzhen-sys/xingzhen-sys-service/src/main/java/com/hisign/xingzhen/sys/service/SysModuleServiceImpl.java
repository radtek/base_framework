package com.hisign.xingzhen.sys.service;

import com.hisign.xingzhen.sys.api.model.SysModule;
import com.hisign.xingzhen.sys.api.service.SysModuleService;
import com.hisign.xingzhen.sys.mapper.SysModuleMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author liangqifu
 *
 * 2017年3月29日
 */
@Service("sysModuleService")
public class SysModuleServiceImpl implements SysModuleService {

    /**
     * 系统模块管理Mapper
     */
    @Resource
    protected SysModuleMapper sysModuleMapper;

    /**
     * 获取全部模块
     * @return
     * @throws Exception
     */
    @Override
    public List<SysModule> findModuleList() throws Exception{
        List<SysModule> list = new ArrayList<SysModule>();
        list = sysModuleMapper.findAllModuleList();
        return list;
    };

    /**
     * 根据模块id获取模块信息
     * @param moduelId 模块id
     * @return
     * @throws Exception
     */
    @Override
    public List<SysModule> findSysModuleInfoById(String moduelId) throws Exception{
        List<SysModule> list = new ArrayList<SysModule>();
        list = sysModuleMapper.findSysModuleInfoById(moduelId);
        return list;
    }

    /**
     * 删除资源表
     * @param moduelId 模块id
     * @throws Exception
     */
    @Override
    public void deleteResource(String moduelId) throws Exception{
        sysModuleMapper.deleteResource(moduelId);
    }

    /**
     * 删除映射关系表
     * @param moduelId 模块id
     * @throws Exception
     */
    @Override
    public void deletePermisRes(String moduelId) throws Exception{
        sysModuleMapper.deletePermisRes(moduelId);
    }

    /**
     * 删除权限表
     * @param moduelId 模块id
     * @throws Exception
     */
    @Override
    public void deletePermission(String moduelId) throws Exception{
        sysModuleMapper.deletePermission(moduelId);
    }

    /**
     * 删除模块表
     * @param moduelId 模块id
     * @throws Exception
     */
    @Override
    public void deleteModule(String moduelId) throws Exception{
        sysModuleMapper.deleteModule(moduelId);
    }

    /**
     * 更新模块信息
     * @param sysModule 模块model
     * @throws Exception
     */
    @Override
    public void upDateModuleInfo(SysModule sysModule) throws Exception{
        //设置进入模块权限名称
        sysModule.setPermissionDescription("进入"+sysModule.getDescription());
        sysModule.setModuleId(sysModule.getParentId());
        sysModule.setParentId(null);
        sysModuleMapper.upDateModuleInfo(sysModule);//更新模块表
        sysModuleMapper.updatePermission(sysModule);//更新权限资源表
        sysModuleMapper.updatePermisRes(sysModule);//更新权限资源表
        sysModuleMapper.updateResource(sysModule);//更新资源表

        String id = sysModule.getModuleId();
        //删除单纯的权限
        sysModuleMapper.deleteResourceForPer(id);//首先删除资源表
        sysModuleMapper.deletePermisResForPer(id);//其次删除映射关系表
        sysModuleMapper.deletePermissionForPer(id);//再次删除权限表

        SysModule sysModuleTemp = sysModuleMapper.findSysModuleInfoByModuleId(id);

        sysModule.setCreateUser(sysModuleTemp.getCreateUser());
        sysModule.setCreateDatetime(sysModuleTemp.getCreateDatetime());
        sysModule.setResourceType("FUNC");

        //添加单纯的权限
        toolForUpdeatSysModuelConfig(sysModule);
    }

    /**
     * 新增模块
     * @param sysModule 模块model
     * @throws Exception
     */
    @Override
    public void addModuleInfo(SysModule sysModule) throws Exception{
        sysModule.setResourceType("FUNC");
        sysModule.setPermissionDescription("进入"+sysModule.getDescription());
        String moduleId ;
        String permissionId ;
        String resourceId ;
        String permisResId ;
        sysModule.setPermissionFlag(0);
        moduleId = sysModuleMapper.getGuid();
        sysModule.setModuleId(moduleId);
        sysModuleMapper.insertModule(sysModule); //增加一个module

        permissionId =  sysModuleMapper.getGuid();
        sysModule.setId(permissionId);
        sysModuleMapper.insertPermission(sysModule); //增加一个权限

        resourceId = sysModuleMapper.getGuid();
        sysModule.setResourceId(resourceId);
        sysModuleMapper.insertResource(sysModule); //增加一个资源

        permisResId = sysModuleMapper.getGuid();
        sysModule.setPermisResId(permisResId);
        sysModuleMapper.insertPermisRes(sysModule); //增加一个对应关系

        //添加单纯的权限
        toolForUpdeatSysModuelConfig(sysModule);
    }

    /**
     * 获取登录用户的权限
     * @param userName 用户名
     * @return
     * @throws Exception
     */
    @Override
    public List<SysModule> findLogUserPower(String userName) throws Exception{
        List<SysModule> list = new ArrayList<SysModule>();
        list = sysModuleMapper.findLogUserPower(userName);
        return list;
    }

    /**
     * 获取登录用户的子权限
     * @param userName 用户名
     * @return
     * @throws Exception
     */
    @Override
    public List<SysModule> findLogUserPowerLimt(String userName) throws Exception{
        List<SysModule> list = new ArrayList<SysModule>();
        list = sysModuleMapper.findLogUserPowerLimt(userName);
        return list;
    }

    /**
     * 拼装页面权限list工具方法
     * @param parentList 页面权限
     * @param childList   二级模块权限
     * @param childNodeList 三级模块权限
     * @return
     */
    @Override
    public List<SysModule> toolsForList(Map<String,List<SysModule>> lists) throws Exception{
        List<SysModule> parentList = lists.get("parentList");
        List<SysModule> childList = lists.get("childList");
        List<SysModule> childNodeList = lists.get("childNodeList");
        List<SysModule> result = new ArrayList<SysModule>();
        List<SysModule> resultParent = new ArrayList<SysModule>();
        resultParent = toolsForResultForEach(childList,childNodeList);
        result = toolsForResultForEach(parentList,resultParent);
        return result;
    }

    /**
     * 工具类遍历对象
     * @param list 模块list
     * @param nodesList 节点list
     * @return
     */
    public List<SysModule> toolsForResultForEach(List<SysModule> list,List<SysModule> nodesList) throws Exception{
        List<SysModule> resultTemp;
        List<SysModule> result = new ArrayList<SysModule>();
        List<SysModule> resultList = new ArrayList<SysModule>();
        for(int i=0;i<list.size();i++){
            resultTemp = new ArrayList<SysModule>();
            result.add(list.get(i));
            for(SysModule childSysModule:nodesList){
                if(list.get(i).getModuleId().equals(childSysModule.getParentId())){
                    resultTemp.add(childSysModule);
                }
            }
            if(resultTemp.size()!=0){
                result.get(i).setItems(resultTemp);
            }
        }
        for(SysModule sysModule : result){
            resultTemp = new ArrayList<SysModule>();
            if(null!=sysModule.getItems()){
                for(int j=0;j<sysModule.getItems().size();j++){
                    if(sysModule.getItems().get(j).getModuleId().equals(sysModule.getParentId())) {
                        resultTemp.add(sysModule);
                    }
                }
                if(0!=resultTemp.size()){
                    sysModule.setItems(resultTemp);
                }
            }
            resultList.add(sysModule);
        }
        return resultList;
    }

    /**
     * 获取登录用户角色
     * @param userName 用户名
     * @return
     */
    @Override
    public List<Map<String,String>> findRoleList(String userName) throws Exception{
        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
        list = sysModuleMapper.findRoleList(userName);
        return list;
    }
    /**
     * 工具方法更新权限信息
     * @param sysModule  模块model
     * @throws Exception
     */
    public void toolForUpdeatSysModuelConfig(SysModule sysModule) throws Exception{
        String permissionId;
        String resourceId;
        String permisResId;
        if(null!=sysModule.getDescriptionArray()&&null!=sysModule.getResourceArray()) {
            String[] descriptionArray = sysModule.getDescriptionArray().split(",");
            String[] resourceArray = sysModule.getResourceArray().split(",");
            for (int i = 0; i < descriptionArray.length; i++) {
                if (null != descriptionArray[i] && !"".equals(descriptionArray[i]) && null != resourceArray[i] && !"".equals(resourceArray[i])) {
                    sysModule.setPermissionDescription(descriptionArray[i]);
                    sysModule.setResourceStr(resourceArray[i]);
                    sysModule.setPermissionFlag(1);

                    permissionId = sysModuleMapper.getGuid();
                    sysModule.setId(permissionId);
                    sysModuleMapper.insertPermission(sysModule); //增加一个权限

                    resourceId = sysModuleMapper.getGuid();
                    sysModule.setResourceId(resourceId);
                    sysModuleMapper.insertResource(sysModule); //增加一个资源

                    permisResId = sysModuleMapper.getGuid();
                    sysModule.setPermisResId(permisResId);
                    sysModuleMapper.insertPermisRes(sysModule); //增加一个对应关系
                }
            }
        }
    }


}
