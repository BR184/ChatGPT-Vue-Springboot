package com.klbr184.req;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;

/**
 * @author KL
 * @version 1.0
 * @since 2023-04-12 19:57:06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleUpdateReq {
    private Integer id;
    private String roleKey;
    private String roleName;
    private ArrayList<Integer> rolePermissions;
    private Integer state;
}