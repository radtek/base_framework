package com.hisign.xingzhen.xz.service.impl;

import com.hisign.bfun.benum.BaseEnum;
import com.hisign.bfun.bexception.BusinessException;
import com.hisign.bfun.bif.BaseMapper;
import com.hisign.bfun.bif.BaseServiceImpl;
import com.hisign.bfun.bmodel.Conditions;
import com.hisign.bfun.bmodel.JsonResult;
import com.hisign.bfun.bmodel.UpdateParams;
import com.hisign.bfun.butils.JsonResultUtil;
import com.hisign.xingzhen.common.constant.Constants;
import com.hisign.xingzhen.common.util.StringUtils;
import com.hisign.xingzhen.xz.api.entity.Cb;
import com.hisign.xingzhen.xz.api.entity.Task;
import com.hisign.xingzhen.xz.api.entity.TaskFk;
import com.hisign.xingzhen.xz.api.model.GroupModel;
import com.hisign.xingzhen.xz.api.model.TaskFkModel;
import com.hisign.xingzhen.xz.api.model.TaskModel;
import com.hisign.xingzhen.xz.api.service.TaskService;
import com.hisign.xingzhen.xz.mapper.CbMapper;
import com.hisign.xingzhen.xz.mapper.GroupMapper;
import com.hisign.xingzhen.xz.mapper.TaskFkMapper;
import com.hisign.xingzhen.xz.mapper.TaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * 《任务》 业务逻辑服务类
 *
 * @author 何建辉
 */
@Service("taskService")
public class TaskServiceImpl extends BaseServiceImpl<Task,TaskModel, String> implements TaskService {

    @Autowired
    protected TaskMapper taskMapper;

    @Autowired
    protected TaskFkMapper taskFkMapper;

    @Autowired
    protected GroupMapper groupMapper;

    @Autowired
    protected CbMapper cbMapper;

    @Override
    protected BaseMapper<Task,TaskModel, String> initMapper() {
        return taskMapper;
    }

    @Override
    @Transactional
    public JsonResult add(List<Task> list) throws BusinessException {
        try {
            taskMapper.batchInsert(list);
        } catch (Exception e) {
            throw new BusinessException(BaseEnum.BusinessExceptionEnum.INSERT, e);
        }
        return JsonResultUtil.success();
    }

    @Override
    @Transactional
    public JsonResult update(UpdateParams params) throws BusinessException {
        try {
            taskMapper.updateCustom(params);
        } catch (Exception e) {
            throw new BusinessException(BaseEnum.BusinessExceptionEnum.UPDATE, e);
        }
        return JsonResultUtil.success();
    }

    @Override
    @Transactional
    public JsonResult delByIds(List<String> ids) throws BusinessException {
        try {
            taskMapper.deleteByIds(ids);
        } catch (Exception e) {
            throw new BusinessException(BaseEnum.BusinessExceptionEnum.DELETE, e);
        }
        return JsonResultUtil.success();
    }

    @Override
    @Transactional
    public JsonResult delBy(Conditions conditions) throws BusinessException {
        try {
            taskMapper.deleteCustom(conditions);
        } catch (Exception e) {
            throw new BusinessException(BaseEnum.BusinessExceptionEnum.DELETE, e);
        }
        return JsonResultUtil.success();
    }

    @Override
    public JsonResult getTaskPage(Task task) {
        task.setYjzt("0");
        List<TaskModel> list = taskMapper.getTaskByCondition(task);
        long count = taskMapper.getCountTaskByCondition(task);
        return JsonResultUtil.success(count,list);
    }
    /**
     * 查看任务
     * @param  task
     * 刘玉兰
     * 接收人查看未反馈的信息，将任务单状态改为已签收，接收人查看已反馈的信息，反馈信息的状态改为已确认
     */
    @Override
    @Transactional
    public JsonResult taskDetail(Task task) {
        TaskModel taskModel=taskMapper.findById(task.getId());
        if(taskModel==null){
            return error("该任务不存在");
        }
        Conditions conditions = new Conditions(TaskFk.class);
        Conditions.Criteria criteria = conditions.createCriteria();
        criteria.add(TaskFk.TaskFkEnum.taskid.get());
        //已反馈的才能有反馈信息
        if("1".equals(taskModel.getFkzt())){
            List<TaskFkModel> taskFkModels=taskFkMapper.findList(conditions);
            taskModel.setTaskFkModels(taskFkModels);
            if(taskModel.getJsr().equals(task.getUserId())) {
                Date now=new Date();
                TaskFk taskFk=new TaskFk();
                taskFk.setTaskid(task.getId());
                taskFk.setQrzt("1");
                taskFk.setQrTime(now);
                taskFk.setLastupdatetime(now);
                taskFkMapper.updateTaskQrzt(taskFk);
            }
        } else{
            if(!"1".equals(taskModel.getQszt()) && taskModel.getJsr()!=null && taskModel.getJsr().equals(task.getUserId())) {
                Date now=new Date();
                Task t = new Task();
                t.setId(task.getId());
                t.setQszt("1");
                t.setQsTime(now);
                t.setLastupdatetime(now);
                taskMapper.updateNotNull(t);
            }
        }
        return JsonResultUtil.success(taskModel);
    }

    @Override
    @Transactional
    public JsonResult addTask(Task task) {
        GroupModel group=groupMapper.findById(task.getGroupid());
        if(group==null) {
            return error("添加记录失败,专案组不存在");
        }
        if(group.getDeparmentcode()==null||group.getDeparmentcode().length()!=12){
            return error("添加记录失败,该专案组所属机构有误");
        }
        Date now=new Date();
        task.setId(UUID.randomUUID().toString());
        task.setTaskNo(createTaskNo(group.getDeparmentcode()));
        task.setPgroupid(group.getPgroupid());
        task.setFqTime(now);
        task.setCreatetime(now);
        task.setLastupdatetime(now);
        task.setDeleteflag(Constants.DELETE_FALSE);
        return super.addNotNull(task);
    }

    @Override
    @Transactional
    public JsonResult deleteTaskById(String id) {
        return super.delById(id);
    }
    /**
     * 移交任务
     * @param  entity
     * 刘玉兰
     * 本方法移交是创建一条新数据用来保存历史，修改旧数据作为移交后的数据，移交后需要修改提醒记录中的taskid
     */
    @Override
    @Transactional
    public JsonResult moveTask(Task entity) {
        try {
            Task new_task  = super.getEntityById(entity.getId());
            Date now=new Date();
            new_task.setId(UUID.randomUUID().toString());
            new_task.setLastupdatetime(now);
            new_task.setYjzt("1");
            new_task.setYjTime(now);
            taskMapper.insertNotNull(new_task);
            entity.setCreatetime(now);
            entity.setLastupdatetime(now);
            entity.setDeleteflag(Constants.DELETE_FALSE);
            entity.setYjrwid(new_task.getId());
            Cb cb=new Cb();
            cb.setTaskid(new_task.getId());
            cb.setOld_taskid(entity.getId());
            cbMapper.updateCbTaskid(cb);
            return super.updateNotNull(entity);
        } catch (Exception e) {
            throw new BusinessException(BaseEnum.BusinessExceptionEnum.UPDATE,e);
        }
    }


    private String createTaskNo(String deparmentcode) {
        String maxNo = taskMapper.getMaxNo(deparmentcode);
        String nextNumber;
        if(StringUtils.isEmpty(maxNo)){
            nextNumber="000000";
        }  else{
            nextNumber=String.valueOf(Integer.parseInt(maxNo)+1);
            while (nextNumber.length()<6){
                nextNumber="0"+nextNumber;
            }
        }
        return "RW" + deparmentcode+new SimpleDateFormat("yyyy").format(new Date())+nextNumber;
    }
}