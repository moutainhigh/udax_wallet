package com.github.wxiaoqi.security.admin.rest.casino;

import com.github.wxiaoqi.security.admin.biz.casino.CasinoRebateConfigBiz;
import com.github.wxiaoqi.security.admin.biz.casino.CasinoRoleBiz;
import com.github.wxiaoqi.security.common.constant.AdminCommonConstant;
import com.github.wxiaoqi.security.common.context.BaseContextHandler;
import com.github.wxiaoqi.security.common.entity.casino.CasinoRebateConfig;
import com.github.wxiaoqi.security.common.entity.casino.CasinoRole;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 推荐返利配置表
 */
@RestController
@RequestMapping("/casinoRole")
public class CasinoRoleController extends BaseController<CasinoRoleBiz, CasinoRole> {

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public TableResultResponse<CasinoRole> list(@RequestParam Map<String, Object> param) {
        if (!BaseContextHandler.getExId().equals(AdminCommonConstant.ROOT)) {
            param.put("exchangeId", BaseContextHandler.getExId());
        }
        return super.queryListByCustomPage(param);
    }
}
