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

import com.bm.library.Info;
import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.administrator.myapplication.JSON.PhotoBean;
import com.example.administrator.myapplication.Activity.MovieActicity;
import com.example.administrator.myapplication.R;

import java.util.List;

public class PhotoFragment extends Fragment {
    private RecyclerView fragment_photo_recycler;
    private PhotoAdapter adapter;
    private LinearLayoutManager manager;
    private PhotoView activity_movie_enlarge;
    private MovieActicity movieActicity;
    private Info photoInfo;

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_photo, container, false);
        fragment_photo_recycler = view.findViewById(R.id.fragment_photo_recycler);
        return view;
    }

    public void prepareRecycler (List<PhotoBean> photoBeanList) {
        movieActicity = (MovieActicity) getActivity();
        activity_movie_enlarge = movieActicity.findViewById(R.id.activity_movie_enlarge);
        adapter = new PhotoAdapter(photoBeanList);
        manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        fragment_photo_recycler.setAdapter(adapter);
        fragment_photo_recycler.setLayoutManager(manager);
        fragment_photo_recycler.addItemDecoration(new PhotoItemDecoration());
    }

    private class PhotoItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets (@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int pos = parent.getChildAdapterPosition(view);
            if (pos == adapter.getItemCount() - 1) {
                outRect.right = 30;
            }
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
        private List<PhotoBean> urlBeanList;

        public PhotoAdapter (List<PhotoBean> urlBeanList) {
            this.urlBeanList = urlBeanList;
        }

        @NonNull
        @Override
        public PhotoViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int i) {
            View view = getLayoutInflater().inflate(R.layout.item_photo, viewGroup, false);
            PhotoViewHolder holder = new PhotoViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder (@NonNull final PhotoViewHolder holder, int i) {
            final PhotoBean photoBean = urlBeanList.get(i);
            RequestOptions options = new RequestOptions().centerCrop();
            holder.fragment_photo_recycler_imgv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View view) {
                    final PhotoView tempPhotoView = holder.fragment_photo_recycler_imgv;
                    activity_movie_enlarge.setVisibility(View.VISIBLE);
                    Glide.with(getContext()).load(photoBean.image).into(new SimpleTarget<Drawable>() {
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
            Glide.with(getContext()).load(photoBean.image).apply(options).into(holder.fragment_photo_recycler_imgv);
        }

        @Override
        public int getItemCount () {
            return urlBeanList.size();
        }

        class PhotoViewHolder extends RecyclerView.ViewHolder {
            private PhotoView fragment_photo_recycler_imgv;

            public PhotoViewHolder (@NonNull View itemView) {
                super(itemView);
                fragment_photo_recycler_imgv = itemView.findViewById(R.id.fragment_photo_recycler_imgv);
            }
        }
    }
}
