package com.utesocial.android.feature_login.data.network.dto;

import com.utesocial.android.core.data.network.dto.TokensBody;
import com.utesocial.android.core.domain.model.User;

public class LoginBody {
    public User user;
    public TokensBody tokens;

    public LoginBody(User user, TokensBody tokens) {
        this.user = user;
        this.tokens = tokens;
    }
}
