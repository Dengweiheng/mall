package com.server.server.entity.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 修改密码 DTO
 */
@Data
public class PasswordDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    
    private String oldPassword;
    
    private String newPassword;
}
