package com.dementiev.testwork.ui.itemsCollection;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dementiev.testwork.R;
import com.dementiev.testwork.model.entity.Item;

import java.util.AbstractList;

/**
 * Created by dron on 03.03.17.
 */

public class ItemsListAdapter extends RecyclerView.Adapter<ItemsListAdapter.ItemViewHolder> {

    public interface ItemClickListener {
        void onClick(Item item, int selectedIndex);
    }


    private ItemClickListener itemClickListener;
    private AbstractList<Item> items;

    private ItemsListAdapter(ItemClickListener itemClickListener, AbstractList<Item> items) {
        //setHasStableIds(true);
        this.itemClickListener = itemClickListener;
        this.items = items;
    }


    public static ItemsListAdapter create(AbstractList<Item> items, ItemClickListener listener) {
        return new ItemsListAdapter(listener, items);
    }

    private Item getItem(int position) {
        return items.get(position);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_row, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(root);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.label.setText(getItem(position).getTitle());
        holder.layout.setTag(position);
        holder.layout.setOnClickListener(v -> {
            Log.d("ITEMS_ADAPTER", "onClick");
            if (itemClickListener != null) {
                itemClickListener.onClick(getItem((int) v.getTag()), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public View layout;
        public TextView label;

        public ItemViewHolder(View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout);
            label = (TextView) itemView.findViewById(R.id.label);
        }
    }
}
