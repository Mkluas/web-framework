package cn.mklaus.framework.support.web;

import cn.mklaus.framework.bean.ServiceResult;
import cn.mklaus.framework.support.sms.SendCloudProperties;
import cn.mklaus.framework.support.sms.Sms;
import cn.mklaus.framework.util.Langs;
import cn.mklaus.framework.web.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @author klausxie
 * @date 2020-07-20
 */
@RestController
@RequestMapping("api/sms")
public class SmsController {

    public static final String MOBILE = "session_mobile";
    public static final String CAPTCHA = "session_captcha";

    private final Sms sms;
    private SendCloudProperties properties;

    public SmsController(Sms sms, SendCloudProperties properties) {
        this.sms = sms;
        this.properties = properties;
    }

    @PostMapping("send")
    public Response send(@RequestParam String mobile, HttpSession session) {
        String captcha = Langs.numberStr(properties.getCaptchaLength());
        ServiceResult result = sms.sendSms(mobile, captcha);
        if (result.isOk()) {
            session.setAttribute(MOBILE, mobile);
            session.setAttribute(CAPTCHA, captcha);
        }
        return Response.with(result);
    }

    @PostMapping("send/fake")
    public Response fakeSend(@RequestParam String mobile, @RequestParam(defaultValue = "6666") String captcha,
                             HttpSession session) {
        session.setAttribute(MOBILE, mobile);
        session.setAttribute(CAPTCHA, captcha);
        return Response.ok();
    }

    @PostMapping("check")
    public Response valid(@RequestParam String mobile, @RequestParam String captcha, HttpSession session) {
        boolean check = checkCaptcha(session, mobile, captcha);
        return check ? Response.ok() : Response.error("验证码不正确");
    }

    public static boolean checkCaptcha(HttpSession session, String mobile, String captcha) {
        Object mobileAttr = session.getAttribute(SmsController.MOBILE);
        Object captchaAttr = session.getAttribute(SmsController.CAPTCHA);
        return !(mobileAttr == null
                || captchaAttr == null
                || !mobile.equals(mobileAttr.toString())
                || !captcha.equals(captchaAttr.toString()));
    }

}
