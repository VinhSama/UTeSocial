package com.utesocial.android.feature_post.data.datasource.factory

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.utesocial.android.core.domain.model.User
import com.utesocial.android.feature_post.data.datasource.paging.PostPageKeyedDataSource
import com.utesocial.android.feature_post.domain.use_case.PostUseCase
import io.reactivex.rxjava3.disposables.CompositeDisposable

class PostDataSourceFactory (
    private val postUseCase: PostUseCase,
    private val disposable: CompositeDisposable
) {

    fun getFeedPosts(limit: Int, userType: User.UserType) = Pager(
        config = PagingConfig(
            pageSize = limit,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { PostPageKeyedDataSource(postUseCase, userType, disposable)}
    ).liveData
}