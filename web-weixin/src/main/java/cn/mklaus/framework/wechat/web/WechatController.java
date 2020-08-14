package cn.mklaus.framework.wechat.web;

import cn.mklaus.framework.bean.ServiceResult;
import cn.mklaus.framework.web.Response;
import cn.mklaus.framework.wechat.WechatConsts;
import cn.mklaus.framework.wechat.service.WechatPayService;
import cn.mklaus.framework.wechat.service.WechatService;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author klausxie
 * @date 2020-08-14
 */
@RestController
@RequestMapping("api/wechat")
public class WechatController {

    private final WxMpService wxMpService;
    private final WechatService wechatService;
    private final WechatPayService wechatPayService;

    public WechatController(WxMpService wxMpService, WechatService wechatService, WechatPayService wechatPayService) {
        this.wxMpService = wxMpService;
        this.wechatService = wechatService;
        this.wechatPayService = wechatPayService;
    }

    @GetMapping(produces = "application/text;charset=utf8")
    public String get(@RequestParam String timestamp, @RequestParam String nonce, @RequestParam String signature, @RequestParam String echostr) {
        return wxMpService.checkSignature(timestamp,nonce,signature) ? echostr : "fail";
    }

    @PostMapping(produces = "application/xml;charset=utf8")
    public String post(HttpServletRequest req) throws Exception {
        WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(req.getInputStream());
        WxMpXmlOutMessage outMessage = wechatService.route(inMessage);
        return outMessage != null ? outMessage.toXml() : "";
    }

    @GetMapping("signature")
    public Response signature(@RequestParam String url) throws WxErrorException {
        WxJsapiSignature signature = wxMpService.createJsapiSignature(url);
        return Response.ok().put("wx", signature);
    }

    @PostMapping(value = "pay/notify", produces = "application/xml;charset=utf8")
    @ResponseBody
    public String notify(HttpServletRequest req) throws IOException {
        String data = IOUtils.toString(req.getInputStream(), Charset.forName("utf-8"));
        ServiceResult result = wechatPayService.handlePayNotify(data);
        return result.isOk()
                ? WechatConsts.NOTIFY_SUCCESS_RESPONSE
                : WechatConsts.NOTIFY_FAIL_RESPONSE;
    }

}
