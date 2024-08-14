package com.soocompany.wodify.common.config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextUtil {

    public static void setTemporaryAuthentication(String username) {
        // 권한 설정이 필요하다면 GrantedAuthority 리스트를 설정할 수 있습니다.
        // 예를 들어, Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(username, null, null);

        // SecurityContextHolder에 Authentication 객체 설정
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
