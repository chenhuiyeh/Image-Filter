package com.chenhuiyeh.imagefilter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chenhuiyeh.imagefilter.Adapter.ThumbnailAdapter;
import com.chenhuiyeh.imagefilter.Interfaces.FilterListFragmentListener;
import com.chenhuiyeh.imagefilter.Utils.BitmapUtils;
import com.chenhuiyeh.imagefilter.Utils.SpacesItemDecoration;
import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;
import com.zomato.photofilters.utils.ThumbnailsManager;

import java.util.ArrayList;
import java.util.List;

public class FilterListFragment extends BottomSheetDialogFragment implements FilterListFragmentListener{

    RecyclerView recyclerView;
    ThumbnailAdapter adapter;
    List<ThumbnailItem> thumbnailItems;

    FilterListFragmentListener listener;
    static FilterListFragment instance;


    public static  FilterListFragment getInstance() {
        if (instance == null)
            instance = new FilterListFragment();
        return instance;
    }
    public void setListener(FilterListFragmentListener listener) {
        this.listener = listener;
    }

    public FilterListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_filter_list, container, false);

        thumbnailItems = new ArrayList<>();
        adapter = new ThumbnailAdapter(thumbnailItems, (FilterListFragmentListener) this, getActivity());

        recyclerView = itemView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        int space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics());
        recyclerView.addItemDecoration(new SpacesItemDecoration(space));
        recyclerView.setAdapter(adapter);

        displayThumbnail(null);
        return itemView;
    }

    private void displayThumbnail(final Bitmap bmp) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Bitmap thumbImg;
                if (bmp == null) { // default image
                    thumbImg = BitmapUtils
                            .getBitmapFromAssets(
                                    getActivity(),MainActivity.PICTURE_NAME,
                                    100, 100 );
                } else {
                    thumbImg = Bitmap.createScaledBitmap(bmp, 100, 100, false);
                }

                if (thumbImg == null) {
                    return;
                }
                ThumbnailsManager.clearThumbs();
                thumbnailItems.clear();

                // add normal bitmap first
                ThumbnailItem thumbnailItem = new ThumbnailItem();
                thumbnailItem.image = thumbImg;
                thumbnailItem.filterName = "Normal";
                ThumbnailsManager.addThumb(thumbnailItem);

                List<Filter> filters = FilterPack.getFilterPack(getActivity());

                for(Filter filter:filters) {
                    ThumbnailItem item = new ThumbnailItem();
                    item.image = thumbImg;
                    item.filter = filter;
                    item.filterName = filter.getName();
                    ThumbnailsManager.addThumb(item);
                }

                thumbnailItems.addAll(ThumbnailsManager.processThumbs(getActivity()));

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });

            }
        };
        new Thread((r)).start();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onFilterSelected(Filter filter) {
        if (listener!=null) {
            listener.onFilterSelected(filter);
        }

    }
}
