package om.akella266.weatherapp.common;

public interface AsyncTaskCompleteListener<T> {
    void onTaskComplete(T result);
}
