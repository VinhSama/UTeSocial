package com.utesocial.android.feature_login.data.network.dto;

import com.utesocial.android.core.data.network.dto.TokensBody;
import com.utesocial.android.core.domain.model.UserModel;

public class LoginBody {
    public UserModel user;
    public TokensBody tokens;

    public LoginBody(UserModel user, TokensBody tokens) {
        this.user = user;
        this.tokens = tokens;
    }
}
