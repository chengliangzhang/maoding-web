package com.maoding.notice.controller;

import com.maoding.core.base.controller.BaseController;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.constant.RoleConst;
import com.maoding.notice.dto.NoticeDTO;
import com.maoding.notice.entity.NoticeEntity;
import com.maoding.notice.service.NoticeService;
import com.maoding.system.service.SystemService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/iWork/notice")
public class NoticeController extends BaseController {

    @Autowired
    private NoticeService noticeService;
    @Autowired
    public SystemService systemService;

    @ModelAttribute
    public void before() {
        this.currentUserId = this.getFromSession("userId", String.class);
        this.currentCompanyId = this.getFromSession("companyId", String.class);
    }

    /**
     * 通知公告跳转界面
     *
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/announcement")
    public String center(ModelMap model) throws Exception {
        systemService.getCurrUserInfoOfWork(model, this.getSession());
        return "views/notice/announcement";
    }

    /**
     * 方法描述：保存通知公告
     * 作者：MaoSF
     * 日期：2016/11/30
     *
     * @param:
     * @return:
     */
    @RequestMapping(value = {"/notice", "/notice/{id}"}, method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = {RoleConst.ADMIN_NOTICE}, logical = Logical.OR)
    public AjaxMessage saveNotice(@RequestBody NoticeDTO dto) throws Exception {
        dto.setCompanyId(this.currentCompanyId);
        dto.setCurrentCompanyId(this.currentCompanyId);
        dto.setAccountId(this.currentUserId);
        dto.setNoticeType(1);//标识为手工创建发送的通告
        return this.noticeService.saveNotice(dto);
    }

    /**
     * 方法描述：保存通知公告
     * 作者：MaoSF
     * 日期：2016/11/30
     *
     * @param:
     * @return:
     */
    @RequestMapping(value = {"/saveNotice"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage saveNoticeStatus(@RequestBody List<NoticeEntity> noticeEntityList) throws Exception {
        return this.noticeService.updateNoticeStatus(noticeEntityList);
    }

    /**
     * 方法描述：保存通知公告
     * 作者：MaoSF
     * 日期：2016/11/30
     *
     * @param:
     * @return:
     */
    @RequestMapping(value = "/notice/{id}", method = RequestMethod.GET)
    @ResponseBody
    public AjaxMessage getNoticeById(@PathVariable("id") String id) throws Exception {
        NoticeDTO dto = this.noticeService.getNoticeById(id);
        return this.ajaxResponseSuccess().setData(dto);
    }

    /**
     * 方法描述：删除通知公告
     * 作者：MaoSF
     * 日期：2016/11/30
     *
     * @param:
     * @return:
     */
    @RequestMapping(value = "/notice/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public AjaxMessage deleteNotice(@PathVariable("id") String id) throws Exception {
        return this.noticeService.deleteNotice(id);
    }

    /**
     * 方法描述：批量删除通知公告
     * 作者：MaoSF
     * 日期：2016/11/30
     *
     * @param:
     * @return:
     */
    @RequestMapping(value = "/deleteNoticeForBatch", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage deleteNoticeForBatch(@RequestBody Map<String, List<String>> idList) throws Exception {
        return this.noticeService.deleteNoticeForBatch(idList.get("idList"));
    }


    /**
     * 方法描述：获取通知公告
     * 作者：MaoSF
     * 日期：2016/11/30
     *
     * @param:
     * @return:
     */
    @RequestMapping(value = "/getNotice", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getNotice(@RequestBody Map<String, Object> param) throws Exception {
        param.put("companyId", this.currentCompanyId);
        param.put("userId", this.currentUserId);
        List<NoticeDTO> data = this.noticeService.getNoticeByParamNew(param);
        int totalNumber = this.noticeService.getNoticeCountByParam(param);

        param.clear();
        param.put("data", data);
        param.put("total", totalNumber);
        return this.ajaxResponseSuccess().setData(param);
    }

    /**
     * 方法描述：获取未读通知公告数
     * 作者：MaoSF
     * 日期：2016/11/30
     *
     * @param:
     * @return:
     */
    @RequestMapping(value = "/getNotReadNotice", method = RequestMethod.GET)
    @ResponseBody
    public AjaxMessage getNotReadNotice() throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("companyId", this.currentCompanyId);
        param.put("userId", this.currentUserId);
        int totalNumber = this.noticeService.getNotReadNoticeCount(param);
        param.clear();
        param.put("total", totalNumber);
        return this.ajaxResponseSuccess().setData(totalNumber);
    }

    /**
     * 方法描述：根据公司id通知公告（用于公告维护界面）
     * 作者：MaoSF
     * 日期：2016/11/30
     *
     * @param:
     * @return:
     */
    @RequestMapping(value = "/noticeByCompany", method = RequestMethod.GET)
    @ResponseBody
    public AjaxMessage getNoticeByCompanyId(int pageSize, int pageIndex) throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("companyId", this.currentCompanyId);
        param.put("pageSize", pageSize);
        param.put("pageNumber", pageIndex);
        List<NoticeDTO> data = this.noticeService.getNoticeByCompanyId(param);
        int totalNumber = this.noticeService.getNoticeCountByCompanyId(param);
        param.clear();
        param.put("data", data);
        param.put("total", totalNumber);
        return this.ajaxResponseSuccess().setData(param);
    }


    /**
     * 方法描述：根据公司id通知公告（用于公告维护界面）
     * 作者：MaoSF
     * 日期：2016/11/30
     *
     * @param:
     * @return:
     */
    @RequestMapping(value = "/getNoticeByNoticeid/{id}", method = RequestMethod.GET)
    @ResponseBody
    public AjaxMessage getNoticeByNoticeid(@PathVariable("id") String id) throws Exception {
        return this.ajaxResponseSuccess().setData(noticeService.selectById(id));
    }

}
