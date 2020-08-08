package cn.mklaus.framework.bean.dto;

import cn.mklaus.framework.entity.Admin;
import lombok.Data;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author klaus
 * Created on 2019-09-12 00:10
 */
@Data
@ToString
public class AdminDTO extends Admin{

    public static AdminDTO create(Admin admin) {
        if (admin == null) {
            return null;
        }
        AdminDTO dto = new AdminDTO();
        BeanUtils.copyProperties(admin, dto);
        dto.setPassword(null);
        dto.setSalt(null);
        return dto;
    }

    public static List<AdminDTO> createList(List<Admin> adminList) {
        return adminList.stream()
                .map(AdminDTO::create)
                .collect(Collectors.toList());
    }

}
