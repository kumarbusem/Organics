package com.tekkr.organics.features.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tekkr.data.models.Order
import com.tekkr.data.roomDatabase.BigItem
import com.tekkr.data.roomDatabase.CartItem
import com.tekkr.organics.R
import com.tekkr.organics.databinding.CartItemBinding
import com.tekkr.organics.databinding.ItemBinding
import com.tekkr.organics.databinding.ItemOrderBinding

class OrderAdapter(private val callback: ItemCallback) : ListAdapter<Order, OrderAdapter.ItemsViewHolder>(DIFF_UTILS) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        return ItemsViewHolder(ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, pos: Int) {
        holder.bind(getItem(pos))
    }

    inner class ItemsViewHolder(private val item: ItemOrderBinding) : RecyclerView.ViewHolder(item.root) {

        init {

            item.cvParent.setOnClickListener {
                callback.onOrderSelected(getItem(adapterPosition))
            }

        }

        fun bind(data: Order) {
            item.order = data
        }
    }

    interface ItemCallback {
        fun onOrderSelected(order: Order)
    }

    companion object {

        val TAG: String = OrderAdapter::class.java.name

        val DIFF_UTILS: DiffUtil.ItemCallback<Order> = object : DiffUtil.ItemCallback<Order>() {
            override fun areItemsTheSame(old: Order, new: Order): Boolean {
                return old == new
            }

            override fun areContentsTheSame(old: Order, new: Order): Boolean {
                return old.id == new.id
            }
        }
    }
}

@BindingAdapter(value = ["setItemsText"], requireAll = true)
fun TextView.setItemsText(data: Order?) {
    if (data == null) return
    var itemString = ""
    data.order_items.forEach {
        itemString = itemString + it.item_details.name + " x " + it.quantity + ", "
    }
    this.setText(itemString)

}
