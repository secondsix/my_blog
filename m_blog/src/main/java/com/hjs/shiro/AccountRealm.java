package com.hjs.shiro;

import cn.hutool.core.bean.BeanUtil;
import com.hjs.entity.User;
import com.hjs.service.UserService;
import com.hjs.util.JwtUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountRealm extends AuthorizingRealm {
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    UserService userService;

    /**
     * 为了让realm支持jwt的凭证校验
     *
     * @param token 令牌
     * @return boolean
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 权限校验
     *
     * @param principalCollection 主要收集
     * @return {@link AuthorizationInfo}
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        JwtToken jwtToken = (JwtToken) token;
        //获取userId
        String userId = jwtUtils.getClaimByToken((String) jwtToken.getPrincipal()).getSubject();
        //获取用户内容
        User user = userService.getById(Long.valueOf(userId));
        if (user == null ){
            throw new UnknownAccountException("账户不存在");
        }
        if (user.getStatus() == -1){
            throw new LockedAccountException("账户不存在");
        }
        AccountProfile profile = new AccountProfile();
        //将user数据转移到profile
        BeanUtil.copyProperties(user,profile);
        //用户信息 密钥token 用户名字
        return new SimpleAuthenticationInfo(profile,jwtToken.getCredentials(),getName());
    }
}
