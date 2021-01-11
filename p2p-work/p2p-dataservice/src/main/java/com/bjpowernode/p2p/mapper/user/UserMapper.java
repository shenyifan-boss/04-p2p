package com.bjpowernode.p2p.mapper.user;

import com.bjpowernode.p2p.model.user.User;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**查询主页用户总数
     * @return BigDecimal
     */
    BigDecimal selectUserCountForIndex();

    /**
     * 查询手机号是否存在
     * @return user实体类
     * @param phone 手机号
     */
    User selectPhoneWhetherNull(@Param("phone") String phone);

    /** 注册用户，同时给u_finance_account 用户资金表添加888 同时查出最后一个添加数据的主键值
     *                                                      但主键值必须是自动增长的
     *
     * @return
     */
    int insertUserReturnId(User record);

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
    User selectUserByPhonePwdForLogin(String phone,String pwd);


}