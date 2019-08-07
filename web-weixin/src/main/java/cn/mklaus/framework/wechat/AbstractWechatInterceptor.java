package cn.mklaus.framework.wechat;

import cn.mklaus.framework.extend.AbstractTokenInterceptor;
import cn.mklaus.framework.util.Https;
import lombok.ToString;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Mklaus
 * Created on 2018-04-02 上午10:22
 */
@EnableConfigurationProperties(WechatProperties.class)
public abstract class AbstractWechatInterceptor<T> extends AbstractTokenInterceptor<AbstractWechatInterceptor.TokenBox>{

    private static Logger log = LoggerFactory.getLogger(AbstractWechatInterceptor.class);

    private final static String WECHAT_OPENID = "WECAHT_OPENID";
    private final static String WECHAT_UNIONID = "WECAHT_UNIONID";
    private final static String WECHAT_CODE = "code";
    private final static int DEFAULT_WECHAT_OPENID_COOKIE_TIME = 3600 * 24 * 30;

    private int cookieTime;

    @Resource
    private WechatProperties.Mp mp;

    public AbstractWechatInterceptor() {
        this.cookieTime = DEFAULT_WECHAT_OPENID_COOKIE_TIME;
    }

    public void setCookieTime(int cookieTime) {
        this.cookieTime = cookieTime;
    }

    @Override
    protected String getName() {
        return "AbstractWechatInterceptor";
    }

    @Override
    protected TokenBox extraToken(HttpServletRequest req) {
        return new TokenBox(Https.getCookie(req, WECHAT_OPENID), Https.getCookie(req, WECHAT_UNIONID));
    }

    @Override
    protected boolean handleToken(TokenBox tokenBox, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        log.info("Token box: {}", tokenBox.toString());
        if (tokenBox.hasToken()) {
            return getUserByTokenBox(req, resp, tokenBox);
        } else {
            return getUserByCode(req, resp);
        }
    }

    /**
     * 参考开源微信SDK: https://github.com/Wechat-Group/weixin-java-tools
     *
     * @return a instance of WxMpService
     */
    protected abstract WxMpService getWxMpService();

    /**
     * 根据用户TokenBox{openid，unionid}获取保存在数据库的用户信息
     *
     * @param tokenBox openid and unionid
     * @return  An user entity
     */
    protected abstract T getByTokenBox(TokenBox tokenBox);

    /**
     *
     * 根据WxMpUser保存并获取用户信息
     *
     * @param user  WxMpUser
     * @return  An user entity
     */
    protected abstract T getOrCreateByWxMpUser(WxMpUser user);


    /**
     * 保存当前登录用户，以备后续操作使用
     *
     * @param user  用户实例
     * @param req   HttpServletRequest
     */
    protected abstract void saveUserContext(T user, HttpServletRequest req);



    private boolean getUserByTokenBox(HttpServletRequest req, HttpServletResponse resp, TokenBox tokenBox) throws Exception {
        T user = this.getByTokenBox(tokenBox);
        if (user != null) {
            log.debug("Get user info by token box.");
            saveUserContext(user, req);
            return true;
        } else {
            log.debug("Clear cookies and request authorization for user info.");
            Https.clearCookie(resp, WECHAT_OPENID);
            Https.clearCookie(resp, WECHAT_UNIONID);
            return getUserByCode(req, resp);
        }
    }


    private boolean getUserByCode(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String code = req.getParameter(WECHAT_CODE);
        if (isBlank(code)) {
            log.debug("No auth code, send request and redirect");
            return requestAuthCode(req, resp);
        } else {
            try {
                WxMpOAuth2AccessToken accessToken = this.getWxMpService().oauth2getAccessToken(code);
                WxMpUser mpUser = this.getWxMpService().oauth2getUserInfo(accessToken, "zh_CN");
                Https.setCookie(resp, WECHAT_OPENID,  mpUser.getOpenId(), this.cookieTime);
                Https.setCookie(resp, WECHAT_UNIONID, mpUser.getUnionId(), this.cookieTime);
                T user = this.getOrCreateByWxMpUser(mpUser);
                saveUserContext(user, req);
                return true;
            } catch (WxErrorException e) {
                log.error("Request access token: {}", e.getMessage());
                return (e.getError().getErrorCode() == 40029) && requestAuthCode(req, resp);
            }
        }
    }


    private boolean requestAuthCode(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        String queryString = isBlank(req.getQueryString()) ? "" : "?" + req.getQueryString();
        String thisUrl = mp.getDomain() + req.getRequestURI() + queryString;
        String url = this.getWxMpService().oauth2buildAuthorizationUrl(thisUrl, "snsapi_userinfo", "state");
        resp.sendRedirect(url);
        return false;
    }


    private static boolean isBlank(String s) {
        return s == null || s.trim().length() == 0;
    }
    private static boolean isNotBlank(String s) {
        return !isBlank(s);
    }

    @ToString
    public static class TokenBox {
        private String openid;
        private String unionid;

        public TokenBox(String openid, String unionid) {
            this.openid = openid;
            this.unionid = unionid;
        }

        private boolean hasToken() {
            return isNotBlank(openid) || isNotBlank(unionid);
        }

        public String getOpenid() {
            return openid;
        }

        public String getUnionid() {
            return unionid;
        }
    }

}
