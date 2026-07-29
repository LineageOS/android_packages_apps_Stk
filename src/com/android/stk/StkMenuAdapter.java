/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.stk;

import com.android.internal.telephony.cat.Item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Icon list view adapter to show the list of STK items.
 */
public class StkMenuAdapter extends RecyclerView.Adapter<StkMenuAdapter.ViewHolder> {
    interface OnItemClickListener {
        void onItemClick(int position);
    }

    interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    private final LayoutInflater mInflater;
    private final List<Item> mItems;
    private boolean mIcosSelfExplanatory = false;
    private final OnItemClickListener mClickListener;
    private final OnItemLongClickListener mLongClickListener;
    private final View.OnCreateContextMenuListener mContextMenuListener;

    public StkMenuAdapter(Context context, List<Item> items,
            boolean icosSelfExplanatory, OnItemClickListener clickListener,
            OnItemLongClickListener longClickListener,
            View.OnCreateContextMenuListener contextMenuListener) {
        mInflater = LayoutInflater.from(context);
        mItems = items;
        mIcosSelfExplanatory = icosSelfExplanatory;
        mClickListener = clickListener;
        mLongClickListener = longClickListener;
        mContextMenuListener = contextMenuListener;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.stk_menu_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Item item = mItems.get(position);
        final View itemView = holder.itemView;
        TextView textView = itemView.findViewById(R.id.text);
        textView.setText(!mIcosSelfExplanatory || item.icon == null ? item.text : null);

        ImageView imageView = itemView.findViewById(R.id.icon);
        if (item.icon == null) {
            imageView.setVisibility(View.GONE);
        } else {
            imageView.setImageBitmap(item.icon);
            imageView.setVisibility(View.VISIBLE);
        }

        final int count = getItemCount();
        final int background;
        if (count == 1) {
            background = R.drawable.settingslib_round_background;
        } else if (position == 0) {
            background = R.drawable.settingslib_round_background_top;
        } else if (position == count - 1) {
            background = R.drawable.settingslib_round_background_bottom;
        } else {
            background = R.drawable.settingslib_round_background_center;
        }
        itemView.setBackgroundResource(background);
        itemView.setClipToOutline(true);
        itemView.setOnCreateContextMenuListener(mContextMenuListener);
        itemView.setOnClickListener(view -> {
            int adapterPosition = holder.getBindingAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                mClickListener.onItemClick(adapterPosition);
            }
        });
        itemView.setOnLongClickListener(view -> {
            int adapterPosition = holder.getBindingAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION && mLongClickListener != null) {
                mLongClickListener.onItemLongClick(adapterPosition);
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
