
package com.hisign.xingzhen.xz.mapper;

import com.hisign.bfun.bif.BaseMapper;
import com.hisign.xingzhen.xz.api.entity.Group;
import com.hisign.xingzhen.xz.api.model.GroupModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 《专案组》 数据访问接口
 *
 * @author 何建辉
 */
@Repository
public interface GroupMapper extends BaseMapper<Group,GroupModel, String> {

    /**
     * 获取专案组编号
     * @return 专案组编号
     */
    String findMaxNo();

    List<GroupModel> findGroupByCondition(Group group);

    long findCountGroupByCondition(Group group);

    Map<String,Long> findGroupCaseInfo(@Param("backupStatus") String backupStatus);
}