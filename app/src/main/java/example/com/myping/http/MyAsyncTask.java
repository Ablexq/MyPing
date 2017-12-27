package example.com.myping.http;

import android.os.AsyncTask;

public class MyAsyncTask extends AsyncTask<String, Integer, String> {

    private AsyncCallBack asyncCallBack;

    public MyAsyncTask(AsyncCallBack asyncCallBack) {
        this.asyncCallBack = asyncCallBack;
    }

    @Override
    protected void onPreExecute() {
        asyncCallBack.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        return HttpURLConnectionUtil.httpURLConectionGET(params[0]);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        asyncCallBack.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        asyncCallBack.onPostExecute(result);
    }
}