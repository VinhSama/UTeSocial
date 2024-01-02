package com.utesocial.android.feature_post.domain.use_case

data class PostUseCase (
    val getFeedPostsUseCase: GetFeedPostsUseCase,
    val uploadPostResourcesUseCase: UploadPostResourcesUseCase,
    val createPostUseCase: CreatePostUseCase,
    val deletePostUseCase: DeletePostUseCase
    val likePostUseCase: LikePostUseCase,
    val unlikePostUseCase: UnlikePostUseCase
)