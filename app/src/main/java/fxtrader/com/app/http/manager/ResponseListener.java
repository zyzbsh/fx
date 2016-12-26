package fxtrader.com.app.http.manager;

/**
 * Created by pc on 2016/12/26.
 */
public interface ResponseListener<T> {
    public void success(T object);
    public void error(String error);
}
