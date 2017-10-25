
package com.hisign.xingzhen.xz.mapper;

import com.hisign.bfun.bif.BaseMapper;
import com.hisign.xingzhen.xz.api.entity.AsjAj;
import com.hisign.xingzhen.xz.api.model.AsjAjModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 《案件》 数据访问接口
 *
 * @author 何建辉
 */
@Repository
public interface AsjAjMapper extends BaseMapper<AsjAj,AsjAjModel, String> {

    List<AsjAjModel> findAjGroupPage(AsjAj aj);

    long findAjGroupPageCount(AsjAj aj);

    AsjAj findFirstCaseByGroupId(String id);

}