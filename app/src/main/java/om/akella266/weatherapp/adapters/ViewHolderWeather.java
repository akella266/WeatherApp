package om.akella266.weatherapp.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import om.akella266.weatherapp.R;

public class ViewHolderWeather extends RecyclerView.ViewHolder {

    ImageView conditionImageView;
    TextView nameTextView;
    TextView dayTextView;
    TextView lowTextView;
    TextView highTextView;
    TextView humidityTextView;

    public ViewHolderWeather(View itemView) {
        super(itemView);

        nameTextView = (TextView)itemView.findViewById(R.id.nameTextView);
        conditionImageView = (ImageView)itemView.findViewById(R.id.conditionImageView);
        dayTextView = (TextView)itemView.findViewById(R.id.dayTextView);
        lowTextView = (TextView)itemView.findViewById(R.id.lowTextView);
        highTextView = (TextView)itemView.findViewById(R.id.highTextView);
        humidityTextView = (TextView)itemView.findViewById(R.id.humidityTextView);
    }
}
