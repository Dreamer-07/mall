package pers.prover.mall.cart.interceptor;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import pers.prover.mall.cart.bo.UserKeyBo;
import pers.prover.mall.cart.constant.CartConstant;
import pers.prover.mall.common.constant.AuthConstant;
import pers.prover.mall.common.utils.Constant;
import pers.prover.mall.common.vo.MemberLoginInfoVo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

/**
 * 为(离线/在线)购物车数据添加用户标识
 *
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/11 16:13
 */
public class UserKeyInterceptor implements HandlerInterceptor {

    /**
     * 使用 ThreadLocal 让同一线程内的业务可以访问购物车数据的所属者(用户)
     */
    public static ThreadLocal<UserKeyBo> userKeyBoThreadLocal = new ThreadLocal<>();

    /**
     * 在目标方法执行之前
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断用户是否登录
        MemberLoginInfoVo memberLoginInfoVo = (MemberLoginInfoVo) request.getSession().getAttribute(AuthConstant.SESSION_LOGIN_INFO);
        UserKeyBo userKeyBo = new UserKeyBo();
        if (memberLoginInfoVo != null) {
            // 用户已经登录
            userKeyBo.setUserId(memberLoginInfoVo.getMemberId().toString());
        }
        // 不管用户是否登录，都需要记录它的 user-key 信息
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (CartConstant.USER_KEY.equals(cookie.getName())) {
                    // 保存 user-key 信息
                    userKeyBo.setUserKey(cookie.getValue());
                    break;
                }
            }
        }
        // 如果用户没有 user-key，就需要先为它创建一个
        if (StringUtils.isBlank(userKeyBo.getUserKey())) {
            String userKey = UUID.randomUUID().toString().replaceAll("-", "");
            userKeyBo.setUserKey(userKey);
            // 刚刚新建的 userKey，也就标识该用户是临时用户(未登录 + 未持有 userkey)
            userKeyBo.setIsTempUser(true);
        }
        userKeyBoThreadLocal.set(userKeyBo);
        return true;
    }

    /**
     * 在目标方法执行之后，负责为用户添加 user-key(临时凭据)
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserKeyBo userKeyBo = userKeyBoThreadLocal.get();
        // 通过 cookie 为临时用户(未登录 + 未持有 userkey)添加用户标识
        if (userKeyBo.getIsTempUser()) {
            Cookie userKeyCookie = new Cookie(CartConstant.USER_KEY, userKeyBo.getUserKey());
            userKeyCookie.setDomain("mall.com");
            userKeyCookie.setMaxAge(CartConstant.USER_KEY_EXPIRE);
            response.addCookie(userKeyCookie);
        }
    }
}
