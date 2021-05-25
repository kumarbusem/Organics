package com.tekkr.organics.features.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tekkr.data.roomDatabase.BigItem
import com.tekkr.data.roomDatabase.CartItem
import com.tekkr.organics.R
import com.tekkr.organics.databinding.CartItemBinding
import com.tekkr.organics.databinding.ItemBinding

class CartListAdapter(private val callback: ItemCallback) : ListAdapter<BigItem, CartListAdapter.ItemsViewHolder>(DIFF_UTILS) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        return ItemsViewHolder(CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, pos: Int) {
        holder.bind(getItem(pos))
    }

    inner class ItemsViewHolder(private val item: CartItemBinding) : RecyclerView.ViewHolder(item.root) {

        init {
            item.tvMangoPlus1.setOnClickListener {
                val tempItem = getItem(adapterPosition)
                var i: Int = item.tvMangoCount1.text.toString().toInt()
                i++
                item.tvMangoCount1.text = i.toString()
                callback.onItemChanged(CartItem(tempItem.id, i), tempItem.item_price, true)
            }
            item.tvMangoMinus1.setOnClickListener {
                val tempItem = getItem(adapterPosition)
                var i: Int = item.tvMangoCount1.text.toString().toInt()
                if(i > 0){
                    i--
                    item.tvMangoCount1.text = i.toString()
                    callback.onItemChanged(CartItem(tempItem.id, i), tempItem.item_price, false)
                }
            }
        }

        fun bind(data: BigItem) {
            item.item = data
        }
    }

    interface ItemCallback {
        fun onItemChanged(Items: CartItem, price: Int, type: Boolean)
    }

    companion object {

        val TAG: String = CartListAdapter::class.java.name

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
