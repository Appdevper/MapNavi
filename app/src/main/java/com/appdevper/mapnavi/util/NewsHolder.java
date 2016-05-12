package com.appdevper.mapnavi.util;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appdevper.mapnavi.R;
import com.appdevper.mapnavi.activity.MapsActivity;
import com.appdevper.mapnavi.model.News;
import com.appdevper.mapnavi.model.Place;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by worawit on 2/18/16.
 */
public class NewsHolder extends RecyclerView.Adapter<NewsHolder.ViewHolder> {
    private final List<News> list;
    private final Activity context;
    private List<News> filteredData;
    private ItemFilter mFilter = new ItemFilter();

    public NewsHolder(Activity act, List<News> placeList) {
        context = act;
        list = placeList;
        filteredData = placeList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        News news = filteredData.get(position);
        holder.tvDate.setText(news.news_date);
        holder.tvDetail.setText(news.news_detail);
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

            final List<News> listFilter = list;

            int count = listFilter.size();
            final ArrayList<News> nlist = new ArrayList<>(count);

            News filterableString;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i);
                if (filterableString.news_date.toLowerCase().contains(filterString)) {
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
            filteredData = (ArrayList<News>) results.values;
            notifyDataSetChanged();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate;
        TextView tvDetail;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvDetail = (TextView) itemView.findViewById(R.id.tvDetail);
        }
    }
}
