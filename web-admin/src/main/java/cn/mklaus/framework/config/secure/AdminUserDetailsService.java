package cn.mklaus.framework.config.secure;

import cn.mklaus.framework.entity.Admin;
import cn.mklaus.framework.service.AdminService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author klausxie
 * @date 2020-08-08
 */
public class AdminUserDetailsService implements UserDetailsService {

    private final AdminService adminService;

    public AdminUserDetailsService(AdminService adminService) {
        this.adminService = adminService;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Admin admin = adminService.getByAccount(s);
        if (admin == null) {
            throw new UsernameNotFoundException("账号或密码错误");
        }
        return new AdminUserDetails(admin);
    }

    public static class AdminUserDetails implements UserDetails {

        private Admin admin;

        public AdminUserDetails(Admin admin) {
            this.admin = admin;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return admin.getRoles().stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }

        @Override
        public String getPassword() {
            return admin.getPassword();
        }

        @Override
        public String getUsername() {
            return admin.getAccount();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }

}
