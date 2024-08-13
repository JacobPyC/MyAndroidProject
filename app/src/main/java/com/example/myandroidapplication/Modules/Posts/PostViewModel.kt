import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myandroidapplication.Model.FirebasePostModel
import com.example.myandroidapplication.Model.Post
import com.google.firebase.auth.FirebaseAuth

class PostViewModel : ViewModel() {
    private val postRepository = FirebasePostModel()
    private val userId: String = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    fun getAllPosts(): LiveData<List<Post>> {
        return postRepository.getAllPosts()
    }

    fun getUserPosts(): LiveData<List<Post>> {
        return postRepository.getPostsByUser(userId)
    }

    fun addPost(post: Post, callback: (Boolean) -> Unit) {
        postRepository.addPost(post) { success ->
            callback(success)
        }
    }

    fun updatePost(post: Post, callback: (Boolean) -> Unit) {
        postRepository.updatePost(post) { success ->
            callback(success)
        }
    }

    fun deletePost(post: Post, callback: (Boolean) -> Unit) {
        postRepository.deletePost(post) { success ->
            callback(success)
        }
    }

    fun getPostById(id: String): LiveData<Post> {
        return postRepository.getPostById(id)
    }
}
