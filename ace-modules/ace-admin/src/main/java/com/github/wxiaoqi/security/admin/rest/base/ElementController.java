package com.github.wxiaoqi.security.admin.rest.base;

import com.github.wxiaoqi.security.admin.biz.base.ElementBiz;
import com.github.wxiaoqi.security.admin.biz.base.UserBiz;
import com.github.wxiaoqi.security.common.constant.AdminCommonConstant;
import com.github.wxiaoqi.security.common.context.BaseContextHandler;
import com.github.wxiaoqi.security.common.entity.admin.Element;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author wanghaobin
 * @create 2017-06-23 20:30
 */
@Controller
@RequestMapping("element")
public class ElementController extends BaseController<ElementBiz, Element> {
    @Autowired
    private UserBiz userBiz;

    @Autowired
    private ElementBiz elementBiz;


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public TableResultResponse<Element> page(@RequestParam(defaultValue = "10") int limit,
                                             @RequestParam(defaultValue = "1") int offset, String name, @RequestParam(defaultValue = "0") Long menuId) {
        Example example = new Example(Element.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("menuId", menuId);
        if (StringUtils.isNotBlank(name)) {
            criteria.andLike("name", "%" + name + "%");
        }
        List<Element> elements;
        if (BaseContextHandler.getExId().equals(AdminCommonConstant.ROOT)) {
            elements = baseBiz.selectByExample(example);
        } else {
            elements = elementBiz.selectElementByMenuIdAndUser(menuId);
        }
        return new TableResultResponse<Element>(elements.size(), elements);
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<List<Element>> getAuthorityElement(Long menuId) {
        Long userId = userBiz.getUserByUsername(getCurrentUserName()).getId();
        List<Element> elements = baseBiz.getAuthorityElementByUserId(userId, menuId);
        return new ObjectRestResponse<List<Element>>().data(elements);
    }

    @RequestMapping(value = "/user/menu", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<List<Element>> getAuthorityElement() {
        Long userId = userBiz.getUserByUsername(getCurrentUserName()).getId();
        List<Element> elements = baseBiz.getAuthorityElementByUserId(userId);
        return new ObjectRestResponse<List<Element>>().data(elements);
    }

}
