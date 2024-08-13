import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myandroidapplication.repository.UserRepositoryImpl
import com.example.myandroidapplication.viewmodel.UserProfileViewModel

class UserProfileViewModelFactory(
    private val userRepository: UserRepositoryImpl
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserProfileViewModel::class.java)) {
            return UserProfileViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
