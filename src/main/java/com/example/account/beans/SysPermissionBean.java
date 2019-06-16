package com.example.account.beans;

import com.example.common.base.BaseTree;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;


/**
* AccountSysPermission 实体类
* Created by auto generator on Fri May 31 19:34:58 CST 2019.
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SysPermissionBean extends BaseTree<SysPermissionBean> {

        /**
        * 名称
        */
        @NotBlank(message = "权限名称不能为空")
        private String name;
        /**
        * 类型 1菜单 2功能
        */
        private Integer type;

        /**
        * 菜单地址
        */
        private String url;
        /**
        * 菜单图标标识
        */
        private String icon;
        /**
        * 授权标识，多个以,分隔
        */
        private String permission;
        /**
        * 权限状态 1正常 2禁用
        */
        private Integer status;
        /**
        * 备注
        */
        private String memo;
        /**
        * 排序
        */
        private Integer sort;

        /**
         * 类型名称
         */
        private String typeName;

        public SysPermissionBean(String name, String url) {
                this.name = name;
                this.url = url;
        }
}
