package com.hisign.xingzhen.xz.controller;

import com.hisign.bfun.benum.BaseEnum;
import com.hisign.bfun.bmodel.Conditions;
import com.hisign.bfun.bmodel.JsonResult;
import com.hisign.xingzhen.common.controller.BaseController;
import com.hisign.xingzhen.common.util.StringUtils;
import com.hisign.xingzhen.xz.api.entity.AsjAj;
import com.hisign.xingzhen.xz.api.entity.AsjAj.AsjAjEnum;
import com.hisign.xingzhen.xz.api.entity.Group;
import com.hisign.xingzhen.xz.api.model.AsjAjModel;
import com.hisign.xingzhen.xz.api.param.AsjAjParam;
import com.hisign.xingzhen.xz.api.service.AsjAjService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 《案件》 rest服务类
 *
 * @author 何建辉
 */
@Api(description="案件")
@RestController
@RequestMapping("/xz/asjAj")
public class AsjAjController extends BaseController{

    @Autowired
    AsjAjService asjAjService;

    /**
     *@Author: 何建辉
     *@Description: 所有案件查询分页
     *@Date: 2017/11/1 17:07
     *@Email: hejianhui@hisign.com.cn
     */
    @ApiOperation(value = "所有案件查询分页",httpMethod ="POST",response = AsjAjModel.class)
    @RequestMapping(value = "/getAjPage", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public JsonResult getAjPage(@RequestBody AsjAjParam param) {
        AsjAj aj = new AsjAj();
        BeanUtils.copyProperties(param,aj);

        Conditions conditions = new Conditions(AsjAj.class);
        Conditions.Criteria criteria = conditions.createCriteria();
        //案件编号
        if (StringUtils.isNotBlank(aj.getAjbh())) {
            criteria.add(AsjAjEnum.ajbh.get(), BaseEnum.ConditionEnum.LIKE, "%" + aj.getAjbh() + "%");
        }
        //案别
        if (StringUtils.isNotBlank(aj.getAb())) {
            criteria.add(AsjAjEnum.ab.get(), BaseEnum.ConditionEnum.EQ, aj.getAb());
        }
        //案件名称
        if (StringUtils.isNotBlank(aj.getAjmc())) {
            criteria.add(AsjAjEnum.ajmc.get(), BaseEnum.ConditionEnum.LIKE, "%" + aj.getAjmc() + "%");
        }
        //案发时间
        if (aj.getStartTime() != null && aj.getEndTime() != null) {
            criteria.add(AsjAjEnum.fasjcz.get(), BaseEnum.IsBTEnum.BT, aj.getStartTime(), aj.getEndTime());
        }
        //案件状态
        if (StringUtils.isNotBlank(aj.getAjstate())) {
            criteria.add(AsjAjEnum.ajstate.get(), BaseEnum.ConditionEnum.EQ, aj.getAjstate());
        }

        criteria.add("1", BaseEnum.ConditionEnum.EQ,"1");

        //返回字段
        conditions.setReturnFields(new String[]{AsjAjEnum.id.get(),AsjAjEnum.ajbh.get(), AsjAjEnum.ajmc.get(), AsjAjEnum.ajlx.get(), AsjAjEnum.ajstate.get(), AsjAjEnum.ab.get(), AsjAjEnum.zyaq.get(), AsjAjEnum.fadd.get(), AsjAjEnum.ajzbry.get()});
        conditions.setLimit(param.getBegin(),param.getEnd());

        if (StringUtils.isNotBlank(param.getOrderBy())){
            AsjAj.AsjAjEnum asjAjEnum = AsjAj.AsjAjEnum.valueOf(param.getOrderBy());
            if (asjAjEnum!=null){
                if (param.isDesc()){
                    conditions.setOrderByClause(asjAjEnum.get(), BaseEnum.DESCEnum.DESC);
                }else {
                    conditions.setOrderByClause(asjAjEnum.get(), BaseEnum.DESCEnum.ASC);
                }
            }
        }

        return asjAjService.getPage(conditions);
    }


    /**
     *@Author: 何建辉
     *@Description: 组内涉及案件分页
     *@Date: 2017/11/1 17:07
     *@Email: hejianhui@hisign.com.cn
     */
    @ApiOperation(value = "组内涉及/组外案件分页",httpMethod ="POST",response = AsjAjModel.class)
    @RequestMapping(value = "/getAjGroupPage", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public JsonResult getAjGroupPage(@RequestBody AsjAjParam param) {
        if (StringUtils.isNotBlank(param.getOrderBy())){
            AsjAj.AsjAjEnum asjAjEnum = AsjAj.AsjAjEnum.valueOf(param.getOrderBy());
            if (asjAjEnum!=null){
                param.setOrderBy(asjAjEnum.get());
            }
        }else {
            param.setDesc(false);
        }
        return asjAjService.getAjGroupPage(param);
    }

    @ApiOperation(value = "根据案件id获取案件详情",httpMethod ="POST",response = AsjAjModel.class)
    @RequestMapping(value = "/getById", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public JsonResult getById(@RequestParam String id){
        AsjAjModel ajModel = asjAjService.getById(id);
        return success(ajModel);
    }

    @ApiOperation(value = "根据案件id获取案件详情",httpMethod ="POST",response = AsjAjModel.class)
    @RequestMapping(value = "/getCaseById", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public JsonResult getCaseById(@RequestParam String id){
        AsjAjModel ajModel = asjAjService.getCaseById(id);
        return success(ajModel);
    }

    /**
     *@Author: 何建辉
     *@Description: 根据专案组id获取最早关联案件
     *@Date: 2017/11/1 17:07
     *@Email: hejianhui@hisign.com.cn
     */
    @ApiOperation(value = "根据专案组id获取最早关联案件",httpMethod ="POST",response = AsjAjModel.class)
    @RequestMapping(value = "/getFirstCaseByGroupId", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public AsjAjModel getFirstCaseByGroupId(@RequestParam String id) {
        return asjAjService.getFirstCaseByGroupId(id);
    }
}