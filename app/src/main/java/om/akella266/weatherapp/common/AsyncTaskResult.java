package om.akella266.weatherapp.common;

public class AsyncTaskResult<T> {
    private T result;
    private Exception e;

    public T getResult() {
        return result;
    }

    public Exception getError() {
        return e;
    }

    public AsyncTaskResult(T result) {
        this.result = result;
    }

    public AsyncTaskResult(Exception e) {
        this.e = e;
    }
}
