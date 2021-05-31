package com.tekkr.organics.features.shop

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
@BindingAdapter(value = ["setItemImage"], requireAll = true)
fun ImageView.setItemImage(data: BigItem?) {
    if (data == null) return

    if(data.name.contains("orange", true)) this.setImageResource(R.drawable.orange)
    else if(data.name.contains("alphonso", true) && data.quantity == 5) this.setImageResource(R.drawable.mango2)
    else if(data.name.contains("alphonso", true) && data.quantity == 12) this.setImageResource(R.drawable.mangos)
    else if(data.name.contains("KIwi", true)) this.setImageResource(R.drawable.kiwi)
    else if(data.name.contains("Broccoli", true)) this.setImageResource(R.drawable.braccoli     )
    else if(data.name.contains("Tomato", true)) this.setImageResource(R.drawable.tomato)
    else if(data.name.contains("Capsicum", true)) this.setImageResource(R.drawable.capsicum)
    else if(data.name.contains("Brinjal", true)) this.setImageResource(R.drawable.brinjal)
    else if(data.name.contains("Cauliflower", true)) this.setImageResource(R.drawable.cauliflower)
    else if(data.name.contains("Cabbage", true)) this.setImageResource(R.drawable.cabbage)
    else if(data.name.contains("Onions", true)) this.setImageResource(R.drawable.onions)
    else if(data.name.contains("Carrot", true)) this.setImageResource(R.drawable.carrot)
    else if(data.name.contains("Banginapalli", true) && data.name.contains("mini", true)) this.setImageResource(R.drawable.alphanso3)
    else if(data.name.contains("Banginapalli", true) && data.name.contains("jumbo", true)) this.setImageResource(R.drawable.alphanso5)
    else if(data.name.contains("Grapes", true)) this.setImageResource(R.drawable.grapes)
    else if(data.name.contains("Apple", false)) this.setImageResource(R.drawable.apple)
    else if(data.name.contains("Pineapple", true)) this.setImageResource(R.drawable.pineapple)
    else if(data.name.contains("chicken", true)) this.setImageResource(R.drawable.chicken)
    else if(data.name.contains("mutton", true)) this.setImageResource(R.drawable.mutton)
    else if(data.name.contains("fish", true)) this.setImageResource(R.drawable.fish)
    else  this.setImageResource(R.drawable.logo_fresh)

}