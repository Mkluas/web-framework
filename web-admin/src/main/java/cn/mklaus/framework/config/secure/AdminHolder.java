package cn.mklaus.framework.config.secure;

import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author klausxie
 * @date 2020-08-08
 */
public class AdminHolder {

    public static String getLoginAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (RememberMeAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
            AdminUserDetailsService.AdminUserDetails userDetails = (AdminUserDetailsService.AdminUserDetails) authentication.getPrincipal();
            return userDetails.getUsername();
        }

        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }

}
