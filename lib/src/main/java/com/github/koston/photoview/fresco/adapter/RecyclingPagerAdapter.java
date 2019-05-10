/*
 * Copyright 2015 "Henry Tao <hi@henrytao.me>"
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.koston.photoview.fresco.adapter;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import java.util.ArrayList;
import java.util.List;

public abstract class RecyclingPagerAdapter<VH extends RecyclingPagerViewHolder>
    extends PagerAdapter {

    private static final String STATE = RecyclingPagerAdapter.class.getSimpleName();
    private static final String TAG = RecyclingPagerAdapter.class.getSimpleName();
    public static boolean DEBUG = false;
    private final SparseArray<RecycleCache> mRecycleTypeCaches = new SparseArray<>();
    private SparseArray<Parcelable> mSavedStates = new SparseArray<>();

    protected RecyclingPagerAdapter() {
    }

    protected abstract int getItemCount();

    protected abstract void onBindViewHolder(VH holder, int position);

    protected abstract VH onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public void destroyItem(@NonNull ViewGroup parent, int position, @NonNull Object object) {
        if (object instanceof RecyclingPagerViewHolder) {
            ((RecyclingPagerViewHolder) object).detach(parent);
        }
    }

    @Override
    public int getCount() {
        return getItemCount();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    @Override
    public Object instantiateItem(@NonNull ViewGroup parent, int position) {
        int viewType = getItemViewType(position);
        if (mRecycleTypeCaches.get(viewType) == null) {
            mRecycleTypeCaches.put(viewType, new RecycleCache(this));
        }
        RecyclingPagerViewHolder viewHolder =
            mRecycleTypeCaches.get(viewType).getFreeViewHolder(parent, viewType);
        viewHolder.attach(parent, position);
        onBindViewHolder((VH) viewHolder, position);
        viewHolder.onRestoreInstanceState(mSavedStates.get(getItemId(position)));
        return viewHolder;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return object instanceof RecyclingPagerViewHolder
            && ((RecyclingPagerViewHolder) object).itemView == view;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        for (RecyclingPagerViewHolder viewHolder : getAttachedViewHolders()) {
            onNotifyItemChanged(viewHolder);
        }
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            bundle.setClassLoader(loader);
            SparseArray<Parcelable> ss =
                bundle.containsKey(STATE) ? bundle.getSparseParcelableArray(STATE) : null;
            mSavedStates = ss != null ? ss : new SparseArray<>();
        }
        super.restoreState(state, loader);
    }

    @Override
    public Parcelable saveState() {
        Bundle bundle = new Bundle();
        for (RecyclingPagerViewHolder viewHolder : getAttachedViewHolders()) {
            mSavedStates.put(getItemId(viewHolder.mPosition), viewHolder.onSaveInstanceState());
        }
        bundle.putSparseParcelableArray(STATE, mSavedStates);
        return bundle;
    }

    private int getItemId(int position) {
        return position;
    }

    private int getItemViewType(int position) {
        return 0;
    }

    private void onNotifyItemChanged(RecyclingPagerViewHolder viewHolder) {
    }

    private List<RecyclingPagerViewHolder> getAttachedViewHolders() {
        List<RecyclingPagerViewHolder> attachedViewHolders = new ArrayList<>();
        int n = mRecycleTypeCaches.size();
        for (int i = 0; i < n; i++) {
            for (RecyclingPagerViewHolder viewHolder :
                mRecycleTypeCaches.get(mRecycleTypeCaches.keyAt(i)).mCaches) {
                if (viewHolder.mIsAttached) {
                    attachedViewHolders.add(viewHolder);
                }
            }
        }
        return attachedViewHolders;
    }

    private static class RecycleCache {

        private final RecyclingPagerAdapter mAdapter;

        private final List<RecyclingPagerViewHolder> mCaches;

        RecycleCache(RecyclingPagerAdapter adapter) {
            mAdapter = adapter;
            mCaches = new ArrayList<>();
        }

        RecyclingPagerViewHolder getFreeViewHolder(ViewGroup parent, int viewType) {
            int i = 0;
            RecyclingPagerViewHolder viewHolder;
            for (int n = mCaches.size(); i < n; i++) {
                viewHolder = mCaches.get(i);
                if (!viewHolder.mIsAttached) {
                    return viewHolder;
                }
            }
            viewHolder = mAdapter.onCreateViewHolder(parent, viewType);
            mCaches.add(viewHolder);
            return viewHolder;
        }
    }
}
