package com.utesocial.android.feature_home.domain.use_case

import com.utesocial.android.feature_post.domain.model.Post
import com.utesocial.android.core.domain.util.Resource
import com.utesocial.android.feature_home.domain.repository.HomeRepository
import com.utesocial.android.feature_notification.domain.model.Notify
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class GetSuggestPostsUseCase(private val homeRepository: HomeRepository) {

    operator fun invoke(): Flow<Resource<List<Post>>> = flow {
        try {
            emit(Resource.Loading())

            /*val posts = homeRepository.getSuggestPosts().map { it.toPost() }
            emit(Resource.Success(posts))*/

            val posts: ArrayList<Post> = ArrayList()
            delay(2500)

            posts.add(
                Post(
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
                emptyList(),
                true,
                234,
                90,
                "",
                "My Name"
            )
            )

            posts.add(
                Post(
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
            )

            posts.add(
                Post(
                "Snackbars inform users of a process that an app has performed or will perform. They appear temporarily, towards the bottom of the screen. They shouldn’t interrupt the user experience, and they don’t require user input to disappear.\n" +
                        "\n" +
                        "Frequency\n" +
                        "Only one snackbar may be displayed at a time.\n" +
                        "\n" +
                        "Actions\n" +
                        "A snackbar can contain a single action. \"Dismiss\" or \"cancel\" actions are optional.",
                "24 Th07",
                "",
                "Android Programmatically",
                listOf(
                    "https://static-cse.canva.com/blob/825910/ComposeStunningImages6.jpg",
                    "https://phlearn.com/wp-content/uploads/2019/03/Aspect-Ratios-Explained-no-text.jpg?fit=1400%2C628&quality=99&strip=all",
                    "https://phlearn.com/wp-content/uploads/2019/03/fixed-ratio.png",
                    "https://i.pinimg.com/originals/40/a4/92/40a492b0a148eeafeded44e958edc958.jpg",
                    "https://solution-e-learning.com/wp-content/uploads/2017/09/aspectratio.jpg"
                ),
                false,
                234,
                90,
                "",
                "My Name"
            )
            )

            posts.add(
                Post(
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
                "My Namec asda asda asa d ds s sasasvsf s w w w w wveqcwdd wfsdfds fsd fsd fsdf s gerg rwwcexexw ew we w "
            )
            )

            posts.add(
                Post(
                "Snackbars inform users of a process that an app has performed or will perform. They appear temporarily, towards the bottom of the screen. They shouldn’t interrupt the user experience, and they don’t require user input to disappear.\n" +
                        "\n" +
                        "Frequency\n" +
                        "Only one snackbar may be displayed at a time.\n" +
                        "\n" +
                        "Actions\n" +
                        "A snackbar can contain a single action. \"Dismiss\" or \"cancel\" actions are optional.",
                "24 Th07",
                "",
                "Python API",
                listOf(
                    "https://static-cse.canva.com/blob/825910/ComposeStunningImages6.jpg",
                    "https://phlearn.com/wp-content/uploads/2019/03/Aspect-Ratios-Explained-no-text.jpg?fit=1400%2C628&quality=99&strip=all",
                    "https://phlearn.com/wp-content/uploads/2019/03/fixed-ratio.png",
                    "https://i.pinimg.com/originals/40/a4/92/40a492b0a148eeafeded44e958edc958.jpg",
                    "https://solution-e-learning.com/wp-content/uploads/2017/09/aspectratio.jpg"
                ),
                false,
                234,
                90,
                "",
                "My Name"
            )
            )

            emit(Resource.Success(posts))
        } catch (error: HttpException) {
            emit(Resource.Error(message = error.localizedMessage ?: "An unexpected error occurred"))
        } catch (error: IOException) {
            emit(Resource.Error(message = "Couldn't reach server. Check your internet connect"))
        }
    }
}