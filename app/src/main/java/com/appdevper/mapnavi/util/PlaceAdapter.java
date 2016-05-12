package com.appdevper.mapnavi.util;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appdevper.mapnavi.R;
import com.appdevper.mapnavi.model.Place;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by worawit on 2/18/16.
 */
public class PlaceAdapter extends BaseAdapter {
    private final List<Place> list;
    private final Activity context;
    private List<Place> filteredData;
    private ItemFilter mFilter = new ItemFilter();

    public PlaceAdapter(Activity act, List<Place> placeList) {
        context = act;
        list = placeList;
        filteredData = placeList;
    }

    @Override
    public int getCount() {
        return filteredData.size();
    }

    @Override
    public Object getItem(int i) {
        return filteredData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = mInflater.inflate(R.layout.item_place, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Place place = filteredData.get(i);
        viewHolder.tvName.setText(place.name);
        if (place.type.equals("S")) {
            viewHolder.imgView.setImageResource(R.drawable.pin_shop);
        } else if (place.type.equals("B")) {
            viewHolder.imgView.setImageResource(R.drawable.pin_bud);
        } else if (place.type.equals("H")) {
            viewHolder.imgView.setImageResource(R.drawable.pin_hotel);
        } else if (place.type.equals("F")) {
            viewHolder.imgView.setImageResource(R.drawable.pin_food);
        }

        return view;
    }

    static class ViewHolder {
        public TextView tvName;
        public ImageView imgView;

        public ViewHolder(View v) {
            tvName = (TextView) v.findViewById(R.id.tvName);
            imgView = (ImageView) v.findViewById(R.id.imageView);
        }
    }

    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Place> listFilter = list;

            int count = listFilter.size();
            final ArrayList<Place> nlist = new ArrayList<>(count);

            Place filterableString ;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i);
                if (filterableString.name.toLowerCase().contains(filterString)) {
                    nlist.add(filterableString);
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<Place>) results.values;
            notifyDataSetChanged();
        }
    }
}
