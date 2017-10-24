package com.hisign.xingzhen.xz.rest;

import com.hisign.bfun.bif.BaseRest;
import com.hisign.bfun.bmodel.JsonResult;
import com.hisign.xingzhen.xz.api.entity.Task;
import com.hisign.xingzhen.xz.api.model.TaskModel;
import com.hisign.xingzhen.xz.api.param.TaskAddParam;
import com.hisign.xingzhen.xz.api.param.TaskMoveParam;
import com.hisign.xingzhen.xz.api.param.TaskSelectParam;
import com.hisign.xingzhen.xz.api.service.TaskService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * 《任务》 rest服务类
 *
 * @author 何建辉
 */
@Api(description = "任务")
@RestController
@RequestMapping("task")
public class TaskRest extends BaseRest<Task, TaskModel, String, TaskService> implements TaskService {

    @Override
    @Autowired
    @Resource(name = "taskService")
    public void setBaseService(TaskService baseService) {
        super.setBaseService(baseService);
    }

    /**
     * 查询分页
     * @param taskSelectParam
     * @return
     */
    @Override
    @ApiOperation(value = "任务管理查询分页",httpMethod ="GET",response = TaskModel.class)
    @RequestMapping(value = "/getTaskPage", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public JsonResult getTaskPage(@ApiParam @RequestBody TaskSelectParam taskSelectParam) {
        return baseService.getTaskPage(taskSelectParam);
    }

    /**
     * 查询任务详情
     * @param id  userId
     * @return
     */
    @Override
    @ApiOperation(value = "任务详情",httpMethod ="GET",response = String.class)
    @RequestMapping(value = "/taskDetail", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public JsonResult taskDetail(@RequestParam String id,@RequestParam String userId) {
        return baseService.taskDetail(id, userId);
    }

    /**
     * 新增任务
     * @param taskAddParam
     * @return
     */
    @Override
    @ApiOperation(value = "新增任务",httpMethod ="POST",response = String.class)
    @RequestMapping(value = "/addTask", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public JsonResult addTask(@ApiParam @RequestBody TaskAddParam taskAddParam) {
        return baseService.addTask(taskAddParam);
    }

    /**
     * 删除任务
     * @param  id userId
     * @return
     */
    @Override
    @ApiOperation(value = "删除任务",httpMethod ="DELETE",response = String.class)
    @ApiImplicitParams({ @ApiImplicitParam(name="id",value = "任务id",required = true,dataType = "String"),@ApiImplicitParam(name="userId",value = "当前用户id",required = true,dataType = "String")})
    @RequestMapping(value = "/deleteTaskById", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public JsonResult deleteTaskById(@RequestParam String id,@RequestParam String userId) {
        return baseService.deleteTaskById(id,userId);
    }

    /**
     * 移交任务
     * @param taskMoveParam
     * @return
     */
    @Override
    @ApiOperation(value = "移交任务",httpMethod ="POST",response = String.class)
    @RequestMapping(value = "/moveTask", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public JsonResult moveTask(@ApiParam @RequestBody TaskMoveParam taskMoveParam) {
        return baseService.moveTask(taskMoveParam);
    }
}