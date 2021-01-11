package com.alipay.config;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {

//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2021000116690540";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCZ3PStGZurEJaav6Qhocb+amzfYDefWpcFRyJDFpwelfng0cv17a36LigG74ln8ixkwYVLiZyVl9i/BZ+ktgLTqmOYVjn1lQO1PzD2mWc/RfXY7c5t4w6ii0dwJzz6S0W1Uv7W6YPVwtWioVhKZKQYBYVs4Toumzf+4/ODlMwO9ur6dqsT/rKR/uHQvpmCAoE+XZJDlmou5Hs9vqeYpSqLsq0aSamWwxQmDPmDCIoWGAYT6z3ZvKPOdhVSTRN2ksE1GMwYbVXXMbK9y+VPNYFbqLsu1xPbFsppYdapA+vmECpwokRKZe8ovM7oRmJBpbObx3Kd5hVb/RRQEnnfsL8HAgMBAAECggEBAIBgTEH1gN3dLFuXlIF/MsJp18cUx7QM6L69mEezoLUgW2oKggwCsMtyIXGQAOyashGrKeVZvy1NkcnoDkhmmLs0in67Swatu3dJbzgBzJSNcc/6sucasMZMQWHBS25myZW91uVH1vjO5GMaDcmcaRGl4apzzZC/b5wbY72gtBWzAi74pZbtrA4TZcic4eM/e1TjKZlfYAXTzgK/ZGypvQs7SaZZ4EuI8mZ5SKQpRmrVrk6mqKU8HpOfH0nuWSYtX8YGDrbDRbKH4WSP6bdp0ubv/Yw/k0SHGK+s6lVLbfEj8wOwf60wp7ORFxrken7rxklU9i93KjltM+wCGpvt0VECgYEAyBxykC6O+3PgjbWehP6IKn8e/oDRl0KXOXX0+ErDqiC6dgtbngU3+A2FJu+iX6ICPf/r1hW4LfHVu2itxwZAkf9NeKBOzjTQwD8wrYUpEZf0aYBXHJhttRSB3DwB67clXbsNkCvHgbeOz81DifcNmulPW+1vv6SnXjZkuwgtSJkCgYEAxNXdsZtXs/Q5eUO6ALDPHfYDaYseG1RK4SD7T1tymlhhtom6dsENWamyjnupy4IRoNM/+7oDg/zbSiAqbwK5hOSKGhKKXa2F60JewdUSSrqJwWZgscKU3xX0ae78qGThA6CYWCAr7+MjTORb4S9LOzqWnZX2UeSjOrHErG226J8CgYEAtEhEwjbf5BRScvj0FndF75tVLg6aRL7ehQsLJwIcY1JXCnBTGvsyoEx18E0mKShJAthk/RvWMbZVSyEUpmCwnZe3kPxD4yzcAMctMLbt+AbBUKcqU/3ToLcYA3Uged8nT9lsb72xYOEhTX9s4Ift/ulA8N3ptms9yfgRKhtEyMkCgYAmys9qVWcvfA5nq8VYpX/sM7PwdQ517GLlg46D0U4AxTJcu0AYb2umJB+gnRaVmQjIXVnazooq1QaS0UwLnMnbOv9MrCLA25XtC6OJJ+j2+VR03SxpXCPi43zL8+GvflIqWNgtZgnCuU0Xtlm1Q5DcF2vfmtJ0yU6yHp7YLLdDmQKBgHfP0pxDATSQISFk6ChDo/ymUzf4QwYiUGUrHiD6bHVtMj162Lvnw7IwwfUEHzpcd4p7+qc1cvj7tBPEPElg8tXuQalzZQvuDYn12TqAsry/9LIXt/VwuwLWDODcFzAO+P5JTpqDFY35W18lohAzRc8JJNm9NQTIRSaVq+arjmdm";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAk1YdOG2qe5B26bWnPBbeWOvAn0tC47WxZdxlq8YG44Y+a2F5rPXmO4q6x8r4BiNQQCitPDPOJOe5yZDvUPj7Q2Xj6qfRyOBO03F+nO9kveUk+vGwmTFh8UDe9XqW3ZTUjJXEmY7XiW+bxrHyZdkwN5zgH4vwqtxmiNxODb68khKjwIZ4+cw7I4oh36cBvPRAJaZ2Q8/7at/C4ODkSethOPHIfnky0NCXOMdE2Kz3/lyW9RTMAG2JSaF9dxpgJT0gyPyYAR+2weMYmuKbMpGK5yrCJeV91EjX4liCBBRSHQ/GGJV0bEIdcViaYJZ0kDe/LkG6ZnM0l2Cz7X5/OhYGmQIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://localhost:8084/alipaydemo/notify_url.jsp";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://localhost:8084/alipaydemo/return_url.jsp";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    // 支付宝网关
    public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

