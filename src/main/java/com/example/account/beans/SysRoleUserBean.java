package com.example.account.beans;

import com.example.common.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
* AccountSysRoleUser 实体类
* Created by auto generator on Thu May 30 20:50:32 CST 2019.
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SysRoleUserBean extends BaseEntity<Integer> {
        /**
        * 用户ID
        */
        private Integer userId;
        /**
        * 角色ID
        */
        private Integer roleId;
}