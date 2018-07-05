package com.maoding.message.controller;

import com.maoding.core.base.controller.BaseController;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.message.dto.SendMessageDTO;
import com.maoding.message.entity.MessageEntity;
import com.maoding.message.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by Idccapp22 on 2017/3/17.
 */
@Controller
@RequestMapping("/iWork/message")
public class MessageController extends BaseController {

    @Autowired
    private MessageService messageService;

    @ModelAttribute
    public void before() {
        this.currentUserId = this.getFromSession("userId", String.class);
        this.currentCompanyId = this.getFromSession("companyId", String.class);
        this.currentCompanyUserId = this.getFromSession("companyUserId", String.class);
    }


    /**
     * 方法描述：组织动态
     * 作者：MaoSF
     * 日期：2016/11/30
     * @param:
     * @return:
     */
    @RequestMapping("/getMessage")
    @ResponseBody
    public AjaxMessage getMessage(@RequestBody Map<String,Object> map) throws Exception{
        map.put("userId",this.currentUserId);
        map.put("companyId",this.currentCompanyId);
        return messageService.getMessage(map);
    }

    /**
     * 方法描述：为读数量
     * 作者：MaoSF
     * 日期：2016/11/30
     * @param:
     * @return:
     */
    @RequestMapping("/getMessageUnRead")
    @ResponseBody
    public AjaxMessage getMessageUnRead(@RequestBody Map<String,Object> map) throws Exception{
        map.put("userId",this.currentUserId);
        map.put("param2","0");
        return this.ajaxResponseSuccess("查询成功").setData(messageService.getMessageCount(map));
    }

    @RequestMapping("/testMessage1")
    @ResponseBody
    public AjaxMessage testMessage1(@RequestBody MessageEntity entity) throws Exception{
        messageService.sendMessage(entity);
        return AjaxMessage.succeed("发送成功");
    }
    @RequestMapping("/testMessage2")
    @ResponseBody
    public AjaxMessage testMessage2(@RequestBody List<MessageEntity> entityList) throws Exception{
        messageService.sendMessage(entityList);
        return AjaxMessage.succeed("发送成功");
    }
    @RequestMapping("/testMessage3")
    @ResponseBody
    public AjaxMessage testMessage3(@RequestBody SendMessageDTO dto) throws Exception{
        messageService.sendMessage(dto);
        return AjaxMessage.succeed("发送成功");
    }
    @RequestMapping("/testMessage4")
    @ResponseBody
    public AjaxMessage testMessage4(@RequestBody List<SendMessageDTO> dtoList) throws Exception{
        messageService.sendMessage(dtoList,SendMessageDTO.class);
        return AjaxMessage.succeed("发送成功");
    }
}
