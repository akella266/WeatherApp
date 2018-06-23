package om.akella266.weatherapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import om.akella266.weatherapp.R;
import om.akella266.weatherapp.adapters.listeners.ItemClickListener;
import om.akella266.weatherapp.api.Models.WeatherOld;

public class WeatherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<WeatherOld> list;
    ItemClickListener listener;

    public WeatherAdapter(List<WeatherOld> list) {
        this.list = list;
    }

    public void setWeather(List<WeatherOld> _list){
        list = _list;
    }

    public WeatherAdapter(Context context,List<WeatherOld> list, ItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_weather, parent, false);


        final ViewHolderWeather viewHolder = new ViewHolderWeather(view);
        view.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, viewHolder.getAdapterPosition());
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolderWeather viewHolderWeather = (ViewHolderWeather) holder;
        WeatherOld w = list.get(position);
        viewHolderWeather.nameTextView.setText(w.cityName);
        viewHolderWeather.dayTextView.setText(context.getString(R.string.day_description,w.dayOfWeek, w.description));
        viewHolderWeather.highTextView.setText(context.getString(R.string.high_temp,w.maxTemp));
        viewHolderWeather.lowTextView.setText(context.getString(R.string.low_temp, w.minTemp));
        viewHolderWeather.humidityTextView.setText(context.getString(R.string.humidity, w.humidity));

        Picasso.with(viewHolderWeather.conditionImageView.getContext())
                .load(w.iconURL).into(viewHolderWeather.conditionImageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
