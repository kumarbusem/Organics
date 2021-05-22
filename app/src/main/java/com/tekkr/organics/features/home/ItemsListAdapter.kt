package com.tekkr.organics.features.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tekkr.data.roomDatabase.Item
import com.tekkr.organics.databinding.ItemBinding

class ItemsListAdapter(private val callback: ItemSelectionCallback) : ListAdapter<Item, ItemsListAdapter.ItemsViewHolder>(DIFF_UTILS) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        return ItemsViewHolder(ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, pos: Int) {
        holder.bind(getItem(pos))
    }

    inner class ItemsViewHolder(private val item: ItemBinding) : RecyclerView.ViewHolder(item.root) {

        init {


        }

        fun bind(data: Item) {
            item.item = data
        }
    }

    interface ItemSelectionCallback {
        fun onItemselected(Items: Item)
    }

    companion object {

        val TAG: String = ItemsListAdapter::class.java.name

        val DIFF_UTILS: DiffUtil.ItemCallback<Item> = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(old: Item, new: Item): Boolean {
                return old == new
            }

            override fun areContentsTheSame(old: Item, new: Item): Boolean {
                return old.id == new.id
            }
        }
    }
}
//@BindingAdapter(value = ["setTickImages"], requireAll = true)
//fun ImageView.setTickImages(data: Item?) {
//    if (data == null) return
//
//    if (data.occupiedBy == Item.PLAYER_A) {
//        when (data.value) {
//            1 -> this.setImageResource(R.drawable.a1)
//            2 -> this.setImageResource(R.drawable.a2)
//            3 -> this.setImageResource(R.drawable.a3)
//        }
//    }else  if (data.occupiedBy == Item.PLAYER_B) {
//        when (data.value) {
//            1 -> this.setImageResource(R.drawable.b1)
//            2 -> this.setImageResource(R.drawable.b2)
//            3 -> this.setImageResource(R.drawable.b3)
//        }
//    }
//}