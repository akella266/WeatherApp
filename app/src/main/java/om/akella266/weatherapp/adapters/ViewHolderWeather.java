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
    TextView tempTextView;

    public ViewHolderWeather(View itemView) {
        super(itemView);

        nameTextView = (TextView)itemView.findViewById(R.id.nameTextView);
        tempTextView = (TextView)itemView.findViewById(R.id.tempTextView);
        conditionImageView = (ImageView)itemView.findViewById(R.id.conditionImageView);
    }
}
