import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myandroidapplication.databinding.ItemGifBinding

class GifAdapter(
    private var gifUrls: List<String>,
    private val onGifSelected: (String) -> Unit
) : RecyclerView.Adapter<GifAdapter.GifViewHolder>() {

    // ViewHolder class for GIF items
    class GifViewHolder(private val binding: ItemGifBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(gifUrl: String, onGifSelected: (String) -> Unit) {
            Glide.with(binding.imageViewGif.context)
                .asGif()
                .load(gifUrl)
                .into(binding.imageViewGif)

            // Set click listener for selecting a GIF
            binding.root.setOnClickListener {
                onGifSelected(gifUrl)
            }
        }
    }

    // Method to update the list of GIF URLs
        fun updateGifList(newGifUrls: List<String>) {
        gifUrls = newGifUrls
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifViewHolder {
        val binding = ItemGifBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GifViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GifViewHolder, position: Int) {
        holder.bind(gifUrls[position], onGifSelected)
    }

    override fun getItemCount(): Int {
        return gifUrls.size
    }
}
