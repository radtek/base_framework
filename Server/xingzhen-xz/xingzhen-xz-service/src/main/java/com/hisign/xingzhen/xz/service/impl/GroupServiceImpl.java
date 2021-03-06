package com.hisign.xingzhen.xz.service.impl;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jmessage.api.JMessageClient;
import cn.jmessage.api.group.CreateGroupResult;
import com.hisign.bfun.benum.BaseEnum;
import com.hisign.bfun.bexception.BusinessException;
import com.hisign.bfun.bif.BaseMapper;
import com.hisign.bfun.bif.BaseRest;
import com.hisign.bfun.bif.BaseServiceImpl;
import com.hisign.bfun.bmodel.Conditions;
import com.hisign.bfun.bmodel.JsonResult;
import com.hisign.bfun.bmodel.UpdateParams;
import com.hisign.bfun.butils.JsonResultUtil;
import com.hisign.xingzhen.common.constant.Constants;
import com.hisign.xingzhen.common.util.IpUtil;
import com.hisign.xingzhen.common.util.StringUtils;
import com.hisign.xingzhen.nt.api.model.MsgBean;
import com.hisign.xingzhen.nt.api.service.NtService;
import com.hisign.xingzhen.sys.api.model.SysUserInfo;
import com.hisign.xingzhen.sys.api.param.SysUserInfoParam;
import com.hisign.xingzhen.sys.api.service.SysUserService;
import com.hisign.xingzhen.xz.api.entity.Group;
import com.hisign.xingzhen.xz.api.entity.Usergroup;
import com.hisign.xingzhen.xz.api.entity.XzLog;
import com.hisign.xingzhen.xz.api.model.GroupModel;
import com.hisign.xingzhen.xz.api.param.GroupParam;
import com.hisign.xingzhen.xz.api.service.GroupService;
import com.hisign.xingzhen.xz.mapper.GroupMapper;
import com.hisign.xingzhen.xz.mapper.UsergroupMapper;
import com.hisign.xingzhen.xz.mapper.XzLogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * 《专案组》 业务逻辑服务类
 *
 * @author 何建辉
 */
@Service("groupService")
public class GroupServiceImpl extends BaseServiceImpl<Group, GroupModel, String> implements GroupService {

    @Autowired
    protected GroupMapper groupMapper;

    @Autowired
    private XzLogMapper xzLogMapper;

    @Autowired
    private UsergroupMapper usergroupMapper;

    @Autowired
    private JMessageClient jMessageClient;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private NtService ntService;

    Logger log = LoggerFactory.getLogger(GroupServiceImpl.class);

    @Override
    protected BaseMapper<Group, GroupModel, String> initMapper() {
        return groupMapper;
    }


    @Override
    public JsonResult add(Group entity) throws BusinessException {
        return addNotNull(entity);
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public JsonResult addNotNull(Group entity) throws BusinessException {
        if (entity.getDeparmentcode() == null || entity.getDeparmentcode().length() != 12) {
            return error("登陆人用户单位不正确");
        }
        if (!StringUtils.isEmpty(entity.getPgroupid())) {
            //获取父专案组
            GroupModel pgroupModel = groupMapper.findById(entity.getPgroupid());
            if (pgroupModel == null) {
                return error(BaseEnum.BusinessExceptionEnum.PARAMSEXCEPTION.Msg());
            }
        }
        Date now = new Date();
        //保存专案组
        entity.setId(StringUtils.getUUID());
        entity.setGroupnum(createGroupNum(entity.getDeparmentcode()));
        entity.setCreatetime(now);
        entity.setLastupdatetime(now);
        entity.setDeleteflag(Constants.DELETE_FALSE);
        entity.setBackupReason(null);
        entity.setBackupStatu(Constants.NO);
        entity.setBackupTime(null);

        //创建极光群组
        try {
            CreateGroupResult cgr = jMessageClient.createGroup(entity.getCreator(), entity.getGroupname(), entity.getId(), entity.getCreator());
            if (!cgr.isResultOK()){
                log.info("对不起，创建群组失败!:",cgr.getResponseCode());
                throw new BusinessException("对不起，创建群组失败!");
            }else{
                Long gid = cgr.getGid();
                entity.setJmgid(gid.toString());
            }
        } catch (APIConnectionException e) {
            log.error("Connection error. Should retry later. ", e);
            throw new BusinessException("对不起，创建群组失败!");
        } catch (APIRequestException e) {
            log.error("Error response from JPush server. Should review and fix it. ", e);
            log.info("HTTP Status: " + e.getStatus());
            log.info("Error Message: " + e.getMessage());
            throw new BusinessException("对不起，创建群组失败!");
        }

        long i = groupMapper.insertNotNull(entity);

        if (i != 1) {
            throw new BusinessException("对不起，创建专案组失败!");
        }

        //把创建人添加到关联人员
        Usergroup usergroup = new Usergroup();
        usergroup.setId(StringUtils.getUUID());
        usergroup.setCreatetime(new Date());
        usergroup.setCreator(entity.getCreator());
        usergroup.setGroupid(entity.getId());
        usergroup.setDeleteflag(Constants.DELETE_FALSE);
        usergroup.setDeparmentcode(entity.getDeparmentcode());
        usergroup.setUserid(entity.getCreator());
        usergroup.setUsername(entity.getCreatename());
        usergroup.setJh(entity.getPoliceId());

        long num = usergroupMapper.insertNotNull(usergroup);
        if (num != 1) {
            throw new BusinessException("对不起，创建专案组失败!");
        }

        try {
            String content = "专案组新增(ID:" + entity.getId() + ")";
            if (StringUtils.isNotBlank(entity.getPgroupid())) {
                content = content.replace("新增", "组内建组");
            }
            //保存操作日志
            XzLog xzLog = new XzLog(IpUtil.getRemotIpAddr(BaseRest.getRequest()), Constants.XZLogType.GROUP, content, entity.getCreator(), now, entity.getId());
            xzLogMapper.insertNotNull(xzLog);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return success(super.getById(entity.getId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult add(List<Group> list) throws BusinessException {
        try {
            groupMapper.batchInsert(list);
        } catch (Exception e) {
            throw new BusinessException(BaseEnum.BusinessExceptionEnum.INSERT, e);
        }
        return JsonResultUtil.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult update(UpdateParams params) throws BusinessException {
        try {
            groupMapper.updateCustom(params);
        } catch (Exception e) {
            throw new BusinessException(BaseEnum.BusinessExceptionEnum.UPDATE, e);
        }
        return JsonResultUtil.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult delByIds(List<String> ids) throws BusinessException {
        try {
            groupMapper.deleteByIds(ids);
        } catch (Exception e) {
            throw new BusinessException(BaseEnum.BusinessExceptionEnum.DELETE, e);
        }
        return JsonResultUtil.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult delBy(Conditions conditions) throws BusinessException {
        try {
            groupMapper.deleteCustom(conditions);
        } catch (Exception e) {
            throw new BusinessException(BaseEnum.BusinessExceptionEnum.DELETE, e);
        }
        return JsonResultUtil.success();
    }

    private synchronized String createGroupNum(String deparmentcode) {
        String maxNo = groupMapper.findMaxNo(deparmentcode);
        String nextNumber;
        if (StringUtils.isEmpty(maxNo)) {
            nextNumber = "00000";
        } else {
            nextNumber = String.valueOf(Integer.parseInt(maxNo) + 1);
            while (nextNumber.length() < 5) {
                nextNumber = "0" + nextNumber;
            }
        }
        return "ZAZ" + deparmentcode + new SimpleDateFormat("yyyy").format(new Date()) + nextNumber;
    }

    @Override
    public JsonResult getGroupPage(GroupParam groupParam) {
        //获取父专案组
        List<GroupModel> list = groupMapper.findGroupByCondition(groupParam);
        long count = groupMapper.findCountGroupByCondition(groupParam);
        return JsonResultUtil.success(count, list);
    }

    @Override
    public JsonResult getChildGroupList(String pgroupid,String memberName,String userId) {
        List<GroupModel> list = groupMapper.findChildGroupList(pgroupid,memberName,userId);
        return JsonResultUtil.success(list);
    }


    @Override
    public JsonResult getAllGroupByUserId(String userId,String groupName) {
        return JsonResultUtil.success(groupMapper.findAllGroupByUserId(userId,groupName));
    }

    @Override
    public JsonResult sendBroadcast(String userId, String groupId, String msgContent) throws Exception {
        //获取当前人对象
        SysUserInfo user = sysUserService.getUserInfoByUserId(userId);
        if (user==null){
            return error("登陆人用户不存在");
        }
        GroupModel groupModel = groupMapper.findById(groupId);
        if (groupModel==null){
            return error("改专案组不存在");
        }

        try {
            MsgBean bean = new MsgBean();
            //发送信息提醒
            String text = StringUtils.concat("广播:[",user.getUserName(),"]在专案组[", groupModel.getGroupname(), "]发了一条广播[内容：",msgContent,"]");
            bean.setMsgId(StringUtils.getUUID());
            bean.setReceiverType(String.valueOf(Constants.ReceiveMessageType.TYPE_3));
            bean.setMsgContent(text);
            bean.setPublishId(userId);
            bean.setPublishName(user.getUserName());

            //获取组内成员
            SysUserInfoParam info = new SysUserInfoParam();
            info.setGroupId(groupModel.getId());
            info.setInGroup(true);
            List<SysUserInfo> userList = usergroupMapper.findGroupUserList(info);
            if (userList!=null && userList.size()!=0){
                bean.setList(userList);
            }
            ntService.sendMsg(bean);
            //保存操作日志
            StringBuilder sb = new StringBuilder();
            sb.append("用户(ID:").append(userId).append(")在专案组(ID:").append(groupId).append(")发了一条广播");
            XzLog xzLog = new XzLog(IpUtil.getRemotIpAddr(BaseRest.getRequest()),Constants.XZLogType.GROUP, sb.toString(), userId, new Date(), groupId);
        } catch (Exception e) {
            log.error("发送广播-操作日志失败",e);
        }
        return success();
    }

    @Override
    public GroupModel getById(String id) {
        return groupMapper.findGroupById(id);
    }
}