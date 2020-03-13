package com.example.RESTful.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import java.util.Collection;


public class SmsCodeAuthenticationToken extends AbstractAuthenticationToken {
    private static final Long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private Object principal;

    public SmsCodeAuthenticationToken(String mobile){
        super(null);
        principal = mobile;
        setAuthenticated(false);
    }

    public SmsCodeAuthenticationToken(Object principal,Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

}
