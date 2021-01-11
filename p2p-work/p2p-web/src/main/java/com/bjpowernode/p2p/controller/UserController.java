package com.bjpowernode.p2p.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bjpowernode.common.Constants;
import com.bjpowernode.common.ErrorEnum;
import com.bjpowernode.p2p.model.loan.RecentInvestment;
import com.bjpowernode.p2p.model.user.FinanceAccount;
import com.bjpowernode.p2p.model.user.User;
import com.bjpowernode.p2p.service.FinanceAccountService;
import com.bjpowernode.p2p.service.LoanInfoService;
import com.bjpowernode.p2p.service.UserService;
import com.bjpowernode.p2p.service.WebService;
import com.bjpowernode.util.CommonUtil;
import com.bjpowernode.vo.CommonResult;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * 奕凡
 * <p>
 * <p>
 * 2020/12/28
 **/
@Controller
public class UserController {
    @Reference(interfaceClass = UserService.class,version = "1.0")
    private UserService userService;
    @Resource
    private WebService webService;
    @Reference(interfaceClass = FinanceAccountService.class,version = "1.0",check = false)
    private FinanceAccountService financeAccountService;
    @Reference(interfaceClass = LoanInfoService.class,version = "1.0",check = false)
    private LoanInfoService loanInfoService;
    /**
     * 跳转到注册页面
     */
    @RequestMapping("/loan/page/register")
    public String index(){
        return "register";
    }
    /**
     * 查询手机号是否存在
     */
    @GetMapping("/loan/checkPhone")
    @ResponseBody
    public CommonResult checkPhone(@RequestParam("phone") String phone) {
        CommonResult commonResult = null;
        if (phone == null) {
            commonResult = new CommonResult();
            commonResult.setCode(400);
            commonResult.setError(ErrorEnum.PHONE_IS_NULL.getCode());
            commonResult.setMessage(ErrorEnum.PHONE_IS_NULL.getMsg());
            commonResult.setData("");
        } else if (!CommonUtil.checkPhoneFormat(phone)) {
            commonResult = new CommonResult();
            commonResult.setCode(400);
            commonResult.setError(ErrorEnum.PHONE_FORMART_ERROR.getCode());
            commonResult.setMessage(ErrorEnum.PHONE_FORMART_ERROR.getMsg());
            commonResult.setData("");
        } else {
            User user = userService.selectPhoneWhetherNull(phone);
            if (user == null) {
                //没有这个手机号
                commonResult = new CommonResult().success("可以注册", "");
            } else {
                commonResult = new CommonResult();
                commonResult.setCode(500);
                commonResult.setData("");
                commonResult.setMessage(ErrorEnum.PHONE_EXISTS.getMsg());
                commonResult.setError(ErrorEnum.PHONE_EXISTS.getCode());
            }
        }
        return commonResult;
    }

    /** 调用京东万象发送验证码
     * @param phone 用户输入的手机号
     * @return
     */
    @RequestMapping("/loan/sendCode")
    @ResponseBody
    public CommonResult sendCode(@RequestParam("phone") String phone){
        CommonResult commonResult = new CommonResult();
        if(null==phone){
                commonResult.setCode(400);
                commonResult.setError(ErrorEnum.PHONE_IS_NULL.getCode());
                commonResult.setMessage(ErrorEnum.PHONE_IS_NULL.getMsg());
                commonResult.setData("");
            }else if(!CommonUtil.checkPhoneFormat(phone)){
                commonResult.setCode(400);
                commonResult.setError(ErrorEnum.PHONE_FORMART_ERROR.getCode());
                commonResult.setMessage(ErrorEnum.PHONE_FORMART_ERROR.getMsg());
                commonResult.setData("");
            }else {
            String code = webService.sendCodeSMS(phone);
            if(code!=null){
                commonResult=new CommonResult().success("发送成功", code);
            }else {
                //发送失败
                commonResult=new CommonResult().faled("发送失败", "");
            }
        }

        return commonResult;
    }
    @RequestMapping("/loan/register")
    @ResponseBody
    @Transactional
    public CommonResult Register(String phone, String pwd, String code, HttpServletRequest request){
        CommonResult commonResult =new CommonResult();
        if (null==phone){
            commonResult.setCode(500);
            commonResult.setData("");
            commonResult.setMessage("注册失败");
            commonResult.setError(1003);
        }
        //查数据库确认手机号不存在
        User queryUser = userService.selectPhoneWhetherNull(phone);
        if(queryUser==null){
            if(!CommonUtil.checkPhoneFormat(phone)){
                commonResult=new CommonResult(500, ErrorEnum.PHONE_FORMART_ERROR.getCode(), ErrorEnum.PHONE_FORMART_ERROR.getMsg(), "");
            }else if(!webService.checkRedisVerificationCode(phone, code)){
                commonResult=new CommonResult(500, ErrorEnum.PEGISTER_SMS_CODE_ERROR.getCode(), ErrorEnum.PEGISTER_SMS_CODE_ERROR.getMsg(), "");
            }else {
                User user = userService.addUser(phone, pwd);
                commonResult=new CommonResult().success("注册成功", user);
                //比对成功把redis中的key删除
                webService.delRedisKey(phone);
                //把用户对象存在session中，实名认证可以用到
                request.getSession().setAttribute(Constants.SESSION_USER, user);
            }
        }
        return commonResult;
    }
    //跳转实名认证
    @GetMapping("/loan/realName")
    public String realName(Model model,HttpSession session){
        User user =(User) session.getAttribute(Constants.SESSION_USER);
        model.addAttribute(Constants.SESSION_USER,user);
        return "realName";
    }
    //实名认证
    @PostMapping("/loan/reRealName")
    @ResponseBody
    public CommonResult registerRealName(String Name,String idCard,HttpSession session){
        //验证是否为空
        User user =(User) session.getAttribute(Constants.SESSION_USER);
        String phone = user.getPhone();
        CommonResult result =new CommonResult().faled("起始为失败", "");
        if(Name==null||idCard==null){
            result=null;
        }else if(!CommonUtil.checkPhoneFormat(phone)){
            result.setData("");
            result.setCode(ErrorEnum.PHONE_FORMART_ERROR.getCode());
            result.setMessage(ErrorEnum.PHONE_FORMART_ERROR.getMsg());
            result.setError(500);
        }else if(webService.registerRealName(Name, idCard)){
            //更新数据库
            int row = userService.updateUserForIDCard(idCard, Name, phone);
            if(row>0){
                result.success("认证成功", "");
            }
        }
            return result;
    }
    //账户页面
    @RequestMapping("/loan/myCenter")
    public String myCenter(Model model,HttpSession session){
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        FinanceAccount account = financeAccountService.queryMoneyForLogin(user.getId());
        //查出最近投资记录
        List<RecentInvestment> InvestmentList = loanInfoService.queryRecentInvestment(user.getId());
        model.addAttribute("InvestmentList",InvestmentList);
        model.addAttribute("account",account);
        return "myCenter";
    }

    //跳转登录页面
    @GetMapping("/loan/login")
    public String login(@RequestParam(value = "returnStr",required = false) String returnStr,Model model){
        model.addAttribute("returnStr",returnStr);
        return "login";
    }

    /** 登录验证，更新登录时间
     * @return
     */
    @PostMapping("/loan/loginVerification")
    @ResponseBody
    public CommonResult loginVerification(String phone,@RequestParam("loginPassword") String pwd,HttpSession session){
        CommonResult result = new CommonResult();
        if (ObjectUtils.isEmpty(phone) || ObjectUtils.isEmpty(pwd)) {
            result.setCode(500);
            result.setData("");
            result.setError(1004);
            result.setMessage("请稍后重试");
        } else {
            User user = userService.queryUserByPhonePwdForLogin(phone, pwd);
            if (phone.equals(user.getPhone()) && pwd.equals(user.getLoginPassword())) {
                /*phone.equals(user.getPhone()) && pwd.equals(user.getLoginPassword())*/
                result.setCode(200);
                result.setError(1000);
                result.setData("");
                result.setMessage("登录成功");
                user.setLastLoginTime(new Date());
                userService.updateUserForLastLoginTime(user);
                session.setAttribute(Constants.SESSION_USER, user);
            }
        }
        return result;
    }

    //查询用户的金额
    @RequestMapping("/loan/queryAccount")
    @ResponseBody
    public CommonResult queryFinanceAccount(HttpSession session) {
        CommonResult result = new CommonResult().faled("查询失败", "-");
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        if (user != null) {
            FinanceAccount account = financeAccountService.queryMoneyForLogin(user.getId());
            if (account != null) {
                result = new CommonResult().success("cg", account.getAvailableMoney());
            }
        }
        return result;
    }

    //退出
    @GetMapping("/loan/logout")
    public String logout(HttpSession session) {
        session.removeAttribute(Constants.SESSION_USER);
        session.invalidate();
        return "redirect:/index";
    }

}
