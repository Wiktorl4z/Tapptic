package com.example.tapptic.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.tapptic.databinding.SingleDummyItemBinding
import com.example.tapptic.entities.Dummy
import javax.inject.Inject

class DummiesAdapter @Inject constructor(private val glide: RequestManager) :
    ListAdapter<Dummy, DummiesAdapter.DummyViewHolder>(DiffUtilHelper) {

    class DummyViewHolder(private val itemBinding: SingleDummyItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        val name = itemBinding.tvItemNumber
        val image = itemBinding.iv
        val layout = itemBinding.constraintLayout
    }

    private var onItemClickListener: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DummyViewHolder {
        val itemBinding =
            SingleDummyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DummyViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: DummyViewHolder, position: Int) {
        val dummy = getItem(position)

        holder.itemView.apply {
            holder.name.text = dummy.name
            if (dummy.image.isNotEmpty()) {
                glide.load(dummy.image).into(holder.image)
            }
        }

        holder.layout.setOnClickListener {
            it.isSelected = true
            onItemClickListener?.let { click ->
                click(position)
            }
        }
    }

    fun setOnItemClickListener(onItemClickListener: (Int) -> Unit) {
        this.onItemClickListener = onItemClickListener
    }

}

object DiffUtilHelper : DiffUtil.ItemCallback<Dummy>() {
    override fun areContentsTheSame(oldItem: Dummy, newItem: Dummy): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: Dummy, newItem: Dummy): Boolean {
        return oldItem == newItem
    }
}