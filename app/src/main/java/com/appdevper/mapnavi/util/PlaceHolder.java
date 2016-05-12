package com.appdevper.mapnavi.util;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appdevper.mapnavi.R;
import com.appdevper.mapnavi.activity.MapsActivity;
import com.appdevper.mapnavi.model.Place;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by worawit on 2/18/16.
 */
public class PlaceHolder extends RecyclerView.Adapter<PlaceHolder.ViewHolder> {
    private final List<Place> list;
    private final Activity context;
    private List<Place> filteredData;
    private ItemFilter mFilter = new ItemFilter();

    public PlaceHolder(Activity act, List<Place> placeList) {
        context = act;
        list = placeList;
        filteredData = placeList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Place place = filteredData.get(position);
        holder.tvName.setText(place.name);
        if (place.type.equals("S")) {
            holder.imgView.setImageResource(R.drawable.pin_shop);
        } else if (place.type.equals("B")) {
            holder.imgView.setImageResource(R.drawable.pin_bud);
        } else if (place.type.equals("H")) {
            holder.imgView.setImageResource(R.drawable.pin_hotel);
        } else if (place.type.equals("F")) {
            holder.imgView.setImageResource(R.drawable.pin_food);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MapsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("name", place.name);
                intent.putExtra("type", place.type);
                intent.putExtra("detail", place.detail);
                intent.putExtra("location", place.getLocation());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return filteredData.size();
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

            Place filterableString;

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

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView imgView;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            imgView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
}
