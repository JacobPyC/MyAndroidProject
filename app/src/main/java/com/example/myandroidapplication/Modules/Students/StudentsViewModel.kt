import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myandroidapplication.Model.Student

class StudentsViewModel:ViewModel(){

    var students: LiveData<List<Student>>? = null
}