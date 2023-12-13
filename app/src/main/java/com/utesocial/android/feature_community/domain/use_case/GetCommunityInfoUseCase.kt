package com.utesocial.android.feature_community.domain.use_case

import com.utesocial.android.core.domain.util.Resource
import com.utesocial.android.feature_community.domain.model.Community
import com.utesocial.android.feature_community.domain.repository.CommunityRepository
import com.utesocial.android.feature_group.domain.model.Group
import com.utesocial.android.feature_post.domain.model.Post
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class GetCommunityInfoUseCase(private val communityRepository: CommunityRepository) {

    operator fun invoke(): Flow<Resource<Community>> = flow {
        try {
            emit(Resource.Loading())

            val group = Group("", "Android Programmer")
            val post = Post(
                "Snackbars inform users of a process that an app has performed or will perform. They appear temporarily, towards the bottom of the screen. They shouldn’t interrupt the user experience, and they don’t require user input to disappear.\n" +
                        "\n" +
                        "Frequency\n" +
                        "Only one snackbar may be displayed at a time.\n" +
                        "\n" +
                        "Actions\n" +
                        "A snackbar can contain a single action. \"Dismiss\" or \"cancel\" actions are optional.",
                "24 Th07",
                "",
                "",
                listOf(
                    "https://static-cse.canva.com/blob/825910/ComposeStunningImages6.jpg",
                    "https://phlearn.com/wp-content/uploads/2019/03/Aspect-Ratios-Explained-no-text.jpg?fit=1400%2C628&quality=99&strip=all",
                    "https://phlearn.com/wp-content/uploads/2019/03/fixed-ratio.png",
                    "https://i.pinimg.com/originals/40/a4/92/40a492b0a148eeafeded44e958edc958.jpg",
                    "https://solution-e-learning.com/wp-content/uploads/2017/09/aspectratio.jpg"
                ),
                true,
                234,
                90,
                "",
                "My Name"
            )
            val community = Community(
                groups = listOf(group, group, group, group, group, group),
                posts = listOf(post, post, post, post, post, post, post)
            )

            delay(2500)

            emit(Resource.Success(community))
        } catch (error: HttpException) {
            emit(Resource.Error(message = error.localizedMessage ?: "An unexpected error occurred"))
        } catch (error: IOException) {
            emit(Resource.Error(message = "Couldn't reach server. Check your internet connect"))
        }
    }
}