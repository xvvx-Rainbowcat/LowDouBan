package com.example.administrator.myapplication.Fragment;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bm.library.Info;
import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.administrator.myapplication.JSON.CastBean;
import com.example.administrator.myapplication.Activity.MovieActicity;
import com.example.administrator.myapplication.R;

import java.util.List;

public class CastFragment extends Fragment {
    private RecyclerView fragment_cast_recycler;
    private CastAdapter adapter;
    private LinearLayoutManager manager;
    private PhotoView activity_movie_enlarge;
    private Info photoInfo;
    private MovieActicity movieActicity;

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_casts, container, false);
        fragment_cast_recycler = view.findViewById(R.id.fragment_cast_recycler);
        return view;
    }

    public void prepareRecycler (List<CastBean> castBeanList) {
        movieActicity = (MovieActicity) getActivity();
        activity_movie_enlarge = movieActicity.findViewById(R.id.activity_movie_enlarge);
        adapter = new CastAdapter(castBeanList);
        manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        fragment_cast_recycler.setLayoutManager(manager);
        fragment_cast_recycler.setAdapter(adapter);
        fragment_cast_recycler.addItemDecoration(new CastItemDecoration());

    }

    private class CastItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets (@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int pos = parent.getChildAdapterPosition(view);
            if (pos == adapter.getItemCount() - 1) {
                outRect.right = 30;
            }
        }
    }

    private class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {
        private List<CastBean> castBeanList;

        public CastAdapter (List<CastBean> castBeanList) {
            this.castBeanList = castBeanList;
        }

        public void setCastBeanList (List<CastBean> castBeanList) {
            this.castBeanList = castBeanList;
        }

        @NonNull
        @Override
        public CastViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, final int i) {
            View view = getLayoutInflater().inflate(R.layout.item_casts, viewGroup, false);
            CastViewHolder holder = new CastViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder (@NonNull final CastViewHolder holder, final int i) {
            final CastBean castBean = castBeanList.get(i);
            holder.fragment_cast_recycler_txtv.setText(castBean.name);
            if(castBean.avatars!=null){
                holder.fragment_cast_recycler_imgv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick (View view) {
                        final PhotoView tempPhotoView = holder.fragment_cast_recycler_imgv;
                        activity_movie_enlarge.setVisibility(View.VISIBLE);
                        Glide.with(getContext()).load(castBean.avatars.large).into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady (@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                activity_movie_enlarge.setImageDrawable(resource);
                                photoInfo = tempPhotoView.getInfo();
                                movieActicity.prepareEnlarge(photoInfo, tempPhotoView);
                                activity_movie_enlarge.animaFrom(photoInfo);
                            }
                        });
                    }
                });
                Glide.with(getContext()).load(castBean.avatars.large).apply(RequestOptions.centerCropTransform()).into(holder.fragment_cast_recycler_imgv);
            }
        }

        @Override
        public int getItemCount () {
            return castBeanList.size();
        }

        class CastViewHolder extends RecyclerView.ViewHolder {
            private PhotoView fragment_cast_recycler_imgv;
            private TextView fragment_cast_recycler_txtv;

            public CastViewHolder (@NonNull View itemView) {
                super(itemView);
                fragment_cast_recycler_imgv = itemView.findViewById(R.id.item_casts_imgv);
                fragment_cast_recycler_txtv = itemView.findViewById(R.id.item_casts_txtv);
            }
        }
    }
}
