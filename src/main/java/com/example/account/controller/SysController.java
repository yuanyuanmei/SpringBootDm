package com.example.account.controller;

import com.example.account.beans.SysPermissionBean;
import com.example.account.beans.SysRoleBean;
import com.example.account.beans.UserBaseBean;
import com.example.account.service.SysPermissionService;
import com.example.account.service.SysRoleService;
import com.example.account.service.UserBaseService;
import com.example.common.annotations.CrudCustom;
import com.example.common.annotations.ValidateCustom;
import com.example.common.constants.ApiFuncConsts;
import com.example.common.constants.ApiModuleConsts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * 权限列表管理
 */
@RestController
@RequestMapping(ApiModuleConsts.SYS)
public class SysController {

    @Autowired
    private UserBaseService baseService;

    @Autowired
    private SysRoleService roleService;

    @Autowired
    private SysPermissionService permsService;

    //用户列表
    @CrudCustom(UserBaseService.class)
    @GetMapping(ApiFuncConsts.USER)
    public Object getUser(HttpServletRequest request){
        return request.getAttribute("jsonObject");
    }

    //角色列表
    @CrudCustom(UserBaseService.class)
    @GetMapping(ApiFuncConsts.ROLE)
    public Object getRole(HttpServletRequest request){
        return request.getAttribute("jsonObject");
    }

    //权限列表
    @CrudCustom(UserBaseService.class)
    @GetMapping(ApiFuncConsts.MENU)
    public Object getMenu(HttpServletRequest request){
        return request.getAttribute("jsonObject");
    }

    //用户新增或修改
    @ValidateCustom(UserBaseBean.class)
    @CrudCustom(UserBaseService.class)
    @PostMapping(ApiFuncConsts.USER)
    public Object saveUser(UserBaseBean paramBean, HttpServletRequest request){
        return request.getAttribute("jsonObject");
    }

    //权限修改或新增
    @ValidateCustom(SysRoleBean.class)
    @CrudCustom(SysRoleService.class)
    @PostMapping(ApiFuncConsts.ROLE)
    public Object saveRole(SysRoleBean paramBean, HttpServletRequest request){
        return request.getAttribute("jsonObject");
    }

    //菜单修改或新增
    @ValidateCustom(SysPermissionBean.class)
    @CrudCustom(SysPermissionService.class)
    @PostMapping(ApiFuncConsts.MENU)
    public Object saveMenu(SysPermissionBean paramBean, HttpServletRequest request){
        return request.getAttribute("jsonObject");
    }
}
