package example.com.myping.http;

import android.text.TextUtils;

public class AsyncTaskUtil {

    public static MyAsyncTask doAsync(String url, AsyncCallBack asyncCallBack) {

        if (!TextUtils.isEmpty(url) && asyncCallBack != null) {
            MyAsyncTask myTask = new MyAsyncTask(asyncCallBack);
            myTask.execute(url);
            return myTask;
        }
        return null;
    }

}

