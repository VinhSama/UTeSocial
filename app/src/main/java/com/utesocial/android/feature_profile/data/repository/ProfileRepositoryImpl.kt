package com.utesocial.android.feature_profile.data.repository

import com.utesocial.android.feature_profile.data.network.ProfileApi
import com.utesocial.android.feature_profile.domain.repository.ProfileRepository

class ProfileRepositoryImpl(private val profileApi: ProfileApi) : ProfileRepository {
}