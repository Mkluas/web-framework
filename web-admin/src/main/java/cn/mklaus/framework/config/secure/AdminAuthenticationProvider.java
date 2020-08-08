package cn.mklaus.framework.config.secure;

import cn.mklaus.framework.entity.Admin;
import cn.mklaus.framework.service.AdminService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author klausxie
 * @date 2020-08-08
 */
public class AdminAuthenticationProvider implements AuthenticationProvider {

    private final AdminService adminService;

    public AdminAuthenticationProvider(AdminService adminService) {
        this.adminService = adminService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Object principal = authentication.getPrincipal();
        Object credentials = authentication.getCredentials();
        String account = principal == null ? "" : principal.toString();
        String password = credentials == null ? "" : credentials.toString();

        if (account.isEmpty()) {
            throw new UsernameNotFoundException("account 不能为空");
        }
        if (password.isEmpty()) {
            throw new UsernameNotFoundException("password 不能为空");
        }

        Admin admin = adminService.getByAccount(account);
        if (admin == null) {
            throw new UsernameNotFoundException("账号或密码不正确");
        }

        String encrypt = PasswordEncipher.encrypt(password, admin.getSalt());
        if (!encrypt.equals(admin.getPassword())) {
            throw new BadCredentialsException("账号或密码不正确");
        }

        List<SimpleGrantedAuthority> grantedAuthorities = admin.getRoles().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(account, password, grantedAuthorities);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass == UsernamePasswordAuthenticationToken.class;
    }

}
