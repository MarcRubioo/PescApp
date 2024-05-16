import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marcr.pescapp.data.Post
import com.marcr.pescapp.data.User
import com.marcr.pescapp.data.repository

class ViewModelPerfilSearch : ViewModel() {

    private val _postProfile = MutableLiveData<List<Post>>()
    val postProfile: LiveData<List<Post>> = _postProfile

    private val _followResult = MutableLiveData<Boolean>()
    val followResult: LiveData<Boolean> = _followResult

    private val _unfollowResult = MutableLiveData<Boolean>()
    val unfollowResult: LiveData<Boolean> = _unfollowResult

    private val _isFollower = MutableLiveData<Boolean>()
    val isFollower: LiveData<Boolean> = _isFollower

    fun getUserProfile(email: String, context: Context, callback: (User?) -> Unit) {
        repository.getUserData(email, context) { user ->
            callback(user)
        }
    }

    fun getUserPosts(email: String) {
        repository.getPostProfileSearch(email) { postProfileList ->
            _postProfile.value = postProfileList
        }
    }

    fun followUser(emailToFollow: String, emailUserLogged: String) {
        repository.addFollowerAndFollowing(emailToFollow, emailUserLogged) { success ->
            _followResult.value = success
        }
    }

    fun unfollowUser(emailToFollow: String, emailUserLogged: String) {
        repository.removeFollowerAndFollowing(emailToFollow, emailUserLogged) { success ->
            _unfollowResult.value = success
        }
    }

    fun checkIfUserIsFollower(emailToCheck: String, emailUserLogged: String) {
        repository.checkIfUserIsFollower(emailToCheck, emailUserLogged) { isFollower ->
            _isFollower.value = isFollower
        }
    }
}
