package example.com.myping.http;

public abstract class AsyncCallBack {

    public void onPreExecute() {
    }

    public void onProgressUpdate(Integer... values) {

    }

    public abstract void onPostExecute(String result);
}

