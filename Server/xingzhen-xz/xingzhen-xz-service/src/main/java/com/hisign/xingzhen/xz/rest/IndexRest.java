package com.hisign.xingzhen.xz.rest;

import com.hisign.bfun.benum.BaseEnum;
import com.hisign.bfun.bmodel.Conditions;
import com.hisign.bfun.bmodel.JsonResult;
import com.hisign.xingzhen.common.constant.Constants;
import com.hisign.xingzhen.common.controller.BaseController;
import com.hisign.xingzhen.xz.api.entity.Task;
import com.hisign.xingzhen.xz.api.entity.TaskFk;
import com.hisign.xingzhen.xz.api.service.IndexService;
import com.hisign.xingzhen.xz.api.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hisign on 2017/10/16.
 */
@Api(description = "首页")
@RestController
@RequestMapping("index")
public class IndexRest extends BaseController {

    @Autowired
    TaskService taskService;

    @Autowired
    IndexService indexService;

    @ApiOperation(value = "待办任务",notes = "首页待办工作",httpMethod = "GET", response = Map.class)
    @ApiImplicitParam(name = "userId",value = "当前用户id",required = true,dataType = "String")
    @RequestMapping(value = "/getTaskCountInfo", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public JsonResult getTaskCountInfo(@ApiParam @RequestParam(value="userId") String userId) {

        //未确认
        Long count1 = indexService.getNotConfirmCountByCreator(userId);

        //未反馈 未签收 未过期 接收人
        Conditions conditions2 = new Conditions(Task.class);

        Conditions.Criteria criteria2 = conditions2.createCriteria();


        criteria2.add(Task.TaskEnum.fkzt.get(), BaseEnum.ConditionEnum.EQ, Constants.NO)
                .add(Task.TaskEnum.qszt.get(), BaseEnum.ConditionEnum.EQ, Constants.NO)
                .add(Task.TaskEnum.fkjzTime.get(), BaseEnum.ConditionEnum.GT, new Date())
                .add(Task.TaskEnum.jsr.get(), BaseEnum.ConditionEnum.EQ, userId);

        Long count2 = taskService.getCount(conditions2);

        //未反馈 已过期 接收人
        Conditions conditions3 = new Conditions(Task.class);

        Conditions.Criteria criteria3 = conditions3.createCriteria();

        criteria3.add(Task.TaskEnum.fkzt.get(), BaseEnum.ConditionEnum.EQ, Constants.NO)
                .add(Task.TaskEnum.fkjzTime.get(), BaseEnum.ConditionEnum.LTE, new Date())
                .add(Task.TaskEnum.jsr.get(), BaseEnum.ConditionEnum.EQ, userId);

        Long count3 = taskService.getCount(conditions3);

        Map<String, Long> map = new HashMap<String,Long>();
        map.put("feedback",count1);
        map.put("receive",count2);
        map.put("pastdue",count3);

        return success(map);
    }

    @ApiOperation(value = "平台成果展示",notes = "平台成果展示",httpMethod = "GET", response = Map.class)
    @RequestMapping(value = "/getAchievement", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public JsonResult getAchievement() {
        return indexService.getAchievementList();
    }

    @ApiOperation(value = "专案组破案情况",notes = "专案组破案情况",httpMethod = "GET", response = Map.class)
    @RequestMapping(value = "/getSolveCaseInfo", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public JsonResult getSolveCaseInfo() {
        return indexService.getGroupCaseInfo(Constants.YES);
    }

    @ApiOperation(value = "专案组创建情况",notes = "专案组创建情况",httpMethod = "GET", response = Map.class)
    @RequestMapping(value = "/getCreateInfo", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public JsonResult getCreateInfo() {
        String a = "";
        return indexService.getGroupCaseInfo(null);
    }


}