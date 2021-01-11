package com.bjpowernode.p2p.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.bjpowernode.common.Constants;
import com.bjpowernode.excep.user.CreateFinanceAccountException;
import com.bjpowernode.excep.user.CreateUserException;
import com.bjpowernode.p2p.mapper.user.FinanceAccountMapper;
import com.bjpowernode.p2p.mapper.user.UserMapper;
import com.bjpowernode.p2p.model.user.FinanceAccount;
import com.bjpowernode.p2p.model.user.User;
import com.bjpowernode.p2p.service.UserService;
import com.bjpowernode.util.CommonUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 奕凡
 * <p>
 * <p>
 * 2020/12/27
 **/
@Component
@Service(interfaceClass = UserService.class,version = "1.0",timeout = 3500)
public class UserServiceImpl implements UserService{
    @Resource
    private UserMapper userMapper;
    @Resource
    private FinanceAccountMapper accountMapper;
    @Resource
    private RedisTemplate redisTemplate;
    /**计算主页用户总数
     * @return  int
     */
    @Override
    public BigDecimal queryUserCountForIndex() {
        BigDecimal usersCount = (BigDecimal)redisTemplate.opsForValue().get(Constants.USERS_COUNT);
        if(usersCount==null) {
            synchronized (this){
                usersCount = (BigDecimal)redisTemplate.opsForValue().get(Constants.USERS_COUNT);
                if (usersCount==null) {
                    usersCount = userMapper.selectUserCountForIndex();
                }
                if(usersCount!=null){
                    redisTemplate.opsForValue().set("usersCount", usersCount,20, TimeUnit.MINUTES);
                }
            }
        }
        return usersCount;
    }

    @Override
    public User selectPhoneWhetherNull(String phone) {
        User user = userMapper.selectPhoneWhetherNull(phone);
        return user;
    }

    @Override
    public User addUser(String phone, String pwd) {
        if(phone==null){
            return null;
        }else if(!CommonUtil.checkPhoneFormat(phone)){
            return null;
        }
        User insertUser = new User();
        insertUser.setPhone(phone);
        insertUser.setAddTime(new Date());
        insertUser.setLoginPassword(pwd);
        int row = userMapper.insertUserReturnId(insertUser);
        if (row>0){
            //User user = userMapper.selectPhoneWhetherNull(phone);
            FinanceAccount account = new FinanceAccount();
            account.setAvailableMoney(888.0);
            account.setUid(insertUser.getId());
            row = accountMapper.insertSelective(account);
            if (row<1){
                //没成功 抛出异常  回滚事务
                throw new CreateFinanceAccountException("注册用户时，创建资金账号失败");
            }
        }else {
            //没成功 抛出异常  回滚事务
            throw new CreateUserException("注册用户失败");
        }

        return insertUser;
    }

    @Override
    public int updateUserForIDCard(String idCard, String name, String phone) {
        return userMapper.updateUserForIDCard(idCard, name, phone);
    }
    /** 登录验证用户名密码
     * @param phone 手机号
     * @param pwd 密码
     * @return user实体类
     */
    @Override
    public User queryUserByPhonePwdForLogin(String phone, String pwd) {
        if(ObjectUtils.isEmpty(phone)||ObjectUtils.isEmpty(pwd)){
            return null;
        }else {
            User user = userMapper.selectUserByPhonePwdForLogin(phone, pwd);
            return user;
        }
    }
    /** 更新最近登录时间
     * @param user 实体类
     * @return
     */
    @Override
    public int updateUserForLastLoginTime(User user) {
        return userMapper.updateByPrimaryKeySelective(user);
    }

    /** 给登录完成后下拉页面查询用户剩余金额
     * @param uid 用户表主键
     * @return user实体类
     */

}
