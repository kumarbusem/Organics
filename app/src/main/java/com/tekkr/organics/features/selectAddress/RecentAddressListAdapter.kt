package com.tekkr.organics.features.selectAddress

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tekkr.data.roomDatabase.Address
import com.tekkr.organics.databinding.ItemRecentAddressBinding

class RecentAddressListAdapter(private val callback: ItemSelectionCallback) : ListAdapter<Address, RecentAddressListAdapter.RecentAddressViewHolder>(DIFF_UTILS) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentAddressViewHolder {
        return RecentAddressViewHolder(ItemRecentAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecentAddressViewHolder, pos: Int) {
        holder.bind(getItem(pos))
    }

    inner class RecentAddressViewHolder(private val item: ItemRecentAddressBinding) : RecyclerView.ViewHolder(item.root) {
        init {
            item.layoutRecentAddress.setOnClickListener {
                callback.onAddressSelected(getItem(adapterPosition))
            }
        }

        fun bind(data: Address) {
            item.data = data
        }
    }

    interface ItemSelectionCallback {
        fun onAddressSelected(recentAddress: Address)
    }
    companion object {

        val TAG: String = RecentAddressListAdapter::class.java.name

        val DIFF_UTILS: DiffUtil.ItemCallback<Address> = object : DiffUtil.ItemCallback<Address>() {
            override fun areItemsTheSame(old: Address, new: Address): Boolean {
                return old.line2 == new.line2
            }

            override fun areContentsTheSame(old: Address, new: Address): Boolean {
                return old == new
            }
        }
    }
}