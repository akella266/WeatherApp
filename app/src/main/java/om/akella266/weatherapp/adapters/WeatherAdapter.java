package om.akella266.weatherapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import om.akella266.weatherapp.R;
import om.akella266.weatherapp.adapters.listeners.ItemClickListener;
import om.akella266.weatherapp.api.Models.WeatherData;

public class WeatherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<WeatherData> list;
    private ItemClickListener listener;

    public void setWeather(List<WeatherData> _list){
        list = _list;
    }

    public WeatherAdapter(Context context, List<WeatherData> list, ItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_weather, parent, false);


        final ViewHolderWeather viewHolder = new ViewHolderWeather(view);
        if (listener != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v, viewHolder.getAdapterPosition());
                }
            });
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolderWeather viewHolderWeather = (ViewHolderWeather) holder;
        WeatherData w = list.get(position);
        viewHolderWeather.nameTextView.setText(context.getString(R.string.day_description,
                                                    w.getCityName(),
                                                    w.getDayOfWeek(),
                                                    w.getDescription()));
        viewHolderWeather.tempTextView.setText(context.getString(R.string.temperature, w.getTemp()));
        Picasso.with(viewHolderWeather.conditionImageView.getContext())
                .load(w.getIconURL())
                .error(R.drawable.ic_no_content_black_24dp)
                .into(viewHolderWeather.conditionImageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
