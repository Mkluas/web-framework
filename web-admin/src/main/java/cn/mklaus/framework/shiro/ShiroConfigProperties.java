package cn.mklaus.framework.shiro;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author klaus
 * Created on 2019-09-11 17:45
 */
@Data
@ConfigurationProperties(prefix = "cn.mklaus.shiro")
public class ShiroConfigProperties {

    private static Map<String, String> DEFAULT_PREFIX_FILTER_MAP;
    private static Map<String, String> DEFAULT_SUFFIX_FILTER_MAP;

    private boolean usePrefixFilter = true;
    private boolean useSuffixFilter = true;
    private boolean useAdminRolesFilter = true;
    private Map<String, String> filters;


    private String authPrefix = "/api/auth";
    private String loginUrl = authPrefix+ "/error";
    private String unauthorizedUrl = authPrefix + "/403";
    private String adminPrefix = "/api/backend/admin";


    private String superRole = "root";
    private String defaultAdminPassword = "12345678";
    private String hashAlgorithmName = "md5";
    private int hashIterations = 2;


    public Map<String, String> getFilterMap() {
        Map<String, String> map = new LinkedHashMap<>();

        if (usePrefixFilter) {
            map.putAll(DEFAULT_PREFIX_FILTER_MAP);
        }

        if (filters != null) {
            map.putAll(reverseKeyValue(filters));
        }

        if (useAdminRolesFilter) {
            map.putAll(getAdminRolesFilter());
        }

        if (useSuffixFilter) {
            map.putAll(DEFAULT_SUFFIX_FILTER_MAP);
        }

        return map;
    }

    private static Map reverseKeyValue(Map map) {
        Map reverseMap = new LinkedHashMap();
        for(Object key : map.keySet()) {
            reverseMap.put(map.get(key), key);
        }
        return reverseMap;
    }

    private Map<String, String> getAdminRolesFilter() {
        Map<String, String> admin = new LinkedHashMap<>();
        String permission = String.format("authc,roles[%s]", this.getSuperRole());
        admin.put(adminPrefix + "/save", permission);
        admin.put(adminPrefix + "/update", permission);
        admin.put(adminPrefix + "/remove", permission);
        admin.put(adminPrefix + "/forbid", permission);
        admin.put(adminPrefix + "/roles", permission);
        admin.put(adminPrefix + "/resetpassword", permission);
        admin.put(adminPrefix + "/**", "user");
        return admin;
    }

    static {
        Map<String, String> prefix = new LinkedHashMap<>();
        
        // static sources
        prefix.put("/", "anon");
        prefix.put("/favicon.ico", "anon");
        prefix.put("/public/**", "anon");
        prefix.put("/static/**", "anon");
        prefix.put("/resources/**", "anon");
        prefix.put("/**.css", "anon");
        prefix.put("/**.js", "anon");
        // swagger
        prefix.put("/webjars/**", "anon");
        prefix.put("/swagger-ui.html", "anon");
        prefix.put("/v2/api-docs", "anon");
        prefix.put("/configuration/security", "anon");
        prefix.put("/configuration/ui", "anon");
        prefix.put("/csrf", "anon");
        prefix.put("/swagger-resources/**", "anon");
        // error
        prefix.put("/error", "anon");
        prefix.put("/**/error", "anon");
        // login
        prefix.put("/api/auth/login", "anon");
        // test
        prefix.put("/api/auth/test/authc", "authc");
        
        DEFAULT_PREFIX_FILTER_MAP = prefix;



        Map<String, String> suffix = new LinkedHashMap<>();
        suffix.put("/api/auth/me", "user");
        suffix.put("/api/auth/logout", "user");
        suffix.put("/api/backend/**", "user");

        DEFAULT_SUFFIX_FILTER_MAP = suffix;
    }

}
