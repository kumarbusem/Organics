package com.tekkr.organics.features.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tekkr.data.roomDatabase.BigItem
import com.tekkr.data.roomDatabase.CartItem
import com.tekkr.organics.databinding.ItemBinding

class ItemsListAdapter(private val callback: ItemCallback) : ListAdapter<BigItem, ItemsListAdapter.ItemsViewHolder>(DIFF_UTILS) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        return ItemsViewHolder(ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, pos: Int) {
        holder.bind(getItem(pos))
    }

    inner class ItemsViewHolder(private val item: ItemBinding) : RecyclerView.ViewHolder(item.root) {

        init {
            item.tvMangoPlus1.setOnClickListener {
                var i: Int = item.tvMangoCount1.text.toString().toInt()
                i++
                item.tvMangoCount1.text = i.toString()
                callback.onItemChanged(CartItem(getItem(adapterPosition).id, i), true)
            }
            item.tvMangoMinus1.setOnClickListener {
                var i: Int = item.tvMangoCount1.text.toString().toInt()
                if(i > 0){
                    i--
                    item.tvMangoCount1.text = i.toString()
                    callback.onItemChanged(CartItem(getItem(adapterPosition).id, i), false)
                }
            }
        }

        fun bind(data: BigItem) {
            item.item = data
        }
    }

    interface ItemCallback {
        fun onItemChanged(Items: CartItem, type: Boolean)
    }

    companion object {

        val TAG: String = ItemsListAdapter::class.java.name

        val DIFF_UTILS: DiffUtil.ItemCallback<BigItem> = object : DiffUtil.ItemCallback<BigItem>() {
            override fun areItemsTheSame(old: BigItem, new: BigItem): Boolean {
                return old == new
            }

            override fun areContentsTheSame(old: BigItem, new: BigItem): Boolean {
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