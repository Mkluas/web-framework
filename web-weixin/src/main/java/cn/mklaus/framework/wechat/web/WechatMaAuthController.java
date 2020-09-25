package cn.mklaus.framework.wechat.web;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.mklaus.framework.base.BaseEntity;
import cn.mklaus.framework.web.Response;
import cn.mklaus.framework.wechat.authorize.WxUserHolder;
import cn.mklaus.framework.wechat.authorize.ma.WechatMaJwt;
import cn.mklaus.framework.wechat.service.WxMaUserHandler;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.web.bind.annotation.*;

/**
 * @author klausxie
 * @date 2020-09-25
 */
@RestController
@RequestMapping("/api/ma/auth")
public class WechatMaAuthController {

    private final WxMaService wxMaService;
    private final WxMaUserHandler handler;
    private final WechatMaJwt wechatMaJwt;

    public WechatMaAuthController(WxMaService wxMaService, WxMaUserHandler handler, WechatMaJwt wechatMaJwt) {
        this.wxMaService = wxMaService;
        this.handler = handler;
        this.wechatMaJwt = wechatMaJwt;
    }

    @GetMapping("check")
    public Response check() {
        String token = wechatMaJwt.createToken(WxUserHolder.getOpenid(), WxUserHolder.getUserId());
        return Response.ok().put("token", token);
    }

    @GetMapping("me")
    public Response me() {
        Object user = handler.me(WxUserHolder.getUserId(), WxUserHolder.getOpenid());
        String token = wechatMaJwt.createToken(WxUserHolder.getOpenid(),WxUserHolder.getUserId());
        return Response.ok().put("user", user).put("token", token);
    }

    @PostMapping("login")
    public Response login(@RequestParam String code) {
        try {
            WxMaJscode2SessionResult sessionInfo = wxMaService.getUserService().getSessionInfo(code);
            BaseEntity user = handler.handleWxMaUser(sessionInfo);
            String token = wechatMaJwt.createToken(sessionInfo.getOpenid(), user.getId());
            return Response.ok()
                    .put("token", token)
                    .put("user", user);
        } catch (WxErrorException e) {
            return Response.error(e.getError().toString());
        }
    }
}
