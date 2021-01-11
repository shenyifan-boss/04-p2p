package com.bjpowernode.p2p.service;

import com.bjpowernode.p2p.model.user.User;

import java.math.BigDecimal;

/**
 * 奕凡
 * 2020/12/27
 **/
public interface UserService {

    /** 为主页查询用户总数
     * @return BigDecimal
     */
    BigDecimal queryUserCountForIndex();

    /**
     * 查询手机号是否存在
     * @return user实体类
     * @param phone 手机号
     */
    User selectPhoneWhetherNull(String phone);

    /** 注册用户，同时给u_finance_account 用户资金表添加888
     * @param phone
     * @param pwd
     * @return
     */

    User addUser(String phone,String pwd);

    /** 实名认证更新u_user
     * @param idCard 身份证号
     * @param name 名字
     * @param phone 手机号
     * @return
     */
    int updateUserForIDCard(String idCard,String name,String phone);

    /** 登录验证用户名密码
     * @param phone 手机号
     * @param pwd 密码
     * @return user实体类
     */
    User queryUserByPhonePwdForLogin(String phone,String pwd);

    /** 更新最近登录时间
     * @param user 实体类
     * @return
     */
    int updateUserForLastLoginTime(User user);

}
