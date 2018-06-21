package om.akella266.weatherapp.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import om.akella266.weatherapp.R;
import om.akella266.weatherapp.api.Models.WeatherOld;

public class WeatherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Map<String, Bitmap> bitmaps = new HashMap<>();
    List<WeatherOld> list;

    public WeatherAdapter(List<WeatherOld> list) {
        this.list = list;
    }

    public void setWeather(List<WeatherOld> _list){
        list = _list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_weather, parent, false);
        return new ViewHolderWeather(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolderWeather viewHolderWeather = (ViewHolderWeather) holder;
        WeatherOld w = list.get(position);
        viewHolderWeather.nameTextView.setText(w.cityName);
        viewHolderWeather.dayTextView.setText(w.dayOfWeek);
        viewHolderWeather.highTextView.setText(w.maxTemp);
        viewHolderWeather.lowTextView.setText(w.minTemp);
        viewHolderWeather.humidityTextView.setText(w.humidity);

        if (bitmaps.containsKey(w.iconURL))
            viewHolderWeather.conditionImageView.setImageBitmap(bitmaps.get(w.iconURL));
        else
            new LoadImageTask(viewHolderWeather.conditionImageView).execute(w.iconURL);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;

        public LoadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap;
            HttpURLConnection connection = null;

            try{
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                try(InputStream inputStream = connection.getInputStream()){
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    bitmaps.put(strings[0], bitmap);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }
}

