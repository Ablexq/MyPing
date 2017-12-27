package example.com.myping;

import android.app.Dialog;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import example.com.myping.util.ThreadPoolManager;
import example.com.myping.util.ToastUtil;

/**
 * 1、输入IP少于三位点击小数点跳转下一个输入框
 * 2、输入IP等于三位自动跳转下一个输入框
 * 3、输入IP大于255清空并提示错误
 * 4、输入IP大于四位数清空并提示
 * <p>
 * 使用PING命令获取网络连通状态
 */
public class MainActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener, View.OnFocusChangeListener, View.OnKeyListener {

    private EditText mEditText1, mEditText2, mEditText3, mEditText4;
    private EditText mEditText5, mEditText6, mEditText7, mEditText8;
    private EditText mCurrentEditView;
    private TextView mTv;
    private Button mBtn, mBtnClear;

    private String msg;
    private Process mProcess;
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        setListener();
    }

    private void findViews() {
        mTv = ((TextView) findViewById(R.id.tv));
        mEditText1 = (EditText) findViewById(R.id.EditText1);
        mEditText2 = (EditText) findViewById(R.id.EditText2);
        mEditText3 = (EditText) findViewById(R.id.EditText3);
        mEditText4 = (EditText) findViewById(R.id.EditText4);
        mEditText5 = (EditText) findViewById(R.id.EditText5);
        mEditText6 = (EditText) findViewById(R.id.EditText6);
        mEditText7 = (EditText) findViewById(R.id.EditText7);
        mEditText8 = (EditText) findViewById(R.id.EditText8);
        mBtn = ((Button) findViewById(R.id.btn));
        mBtnClear = ((Button) findViewById(R.id.btn_clear));
    }

    private void setListener() {
        mBtn.setOnClickListener(this);
        mBtnClear.setOnClickListener(this);

        mEditText1.setOnKeyListener(this);
        mEditText2.setOnKeyListener(this);
        mEditText3.setOnKeyListener(this);
        mEditText4.setOnKeyListener(this);
        mEditText5.setOnKeyListener(this);
        mEditText6.setOnKeyListener(this);
        mEditText7.setOnKeyListener(this);
        mEditText8.setOnKeyListener(this);

        mEditText1.addTextChangedListener(this);
        mEditText2.addTextChangedListener(this);
        mEditText3.addTextChangedListener(this);
        mEditText4.addTextChangedListener(this);
        mEditText5.addTextChangedListener(this);
        mEditText6.addTextChangedListener(this);
        mEditText7.addTextChangedListener(this);
        mEditText8.addTextChangedListener(this);

        mEditText1.setOnFocusChangeListener(this);
        mEditText2.setOnFocusChangeListener(this);
        mEditText3.setOnFocusChangeListener(this);
        mEditText4.setOnFocusChangeListener(this);
        mEditText5.setOnFocusChangeListener(this);
        mEditText6.setOnFocusChangeListener(this);
        mEditText7.setOnFocusChangeListener(this);
        mEditText8.setOnFocusChangeListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String string = s.toString();

        if (string.length() == 1 && string.equals(".")) {//直接小数点：.
            mCurrentEditView.setText("");
            ToastUtil.showToast("小数点不能开头");
        } else if ((string.length() == 2 || string.length() == 3) && string.contains(".") && string.endsWith(".")) {//1. 或者12. ：数字加小数点
            String replace = string.replace(".", "");
            mCurrentEditView.setText(replace);

            if (mEditText1.isFocused()) {
                mEditText2.requestFocus();
                mEditText2.setSelection(mEditText2.getText().length());
            } else if (mEditText2.isFocused()) {
                mEditText3.requestFocus();
                mEditText3.setSelection(mEditText3.getText().length());
            } else if (mEditText3.isFocused()) {
                mEditText4.requestFocus();
                mEditText4.setSelection(mEditText4.getText().length());
            } else if (mEditText4.isFocused()) {
                mEditText5.requestFocus();
                mEditText5.setSelection(mEditText5.getText().length());
            } else if (mEditText5.isFocused()) {
                mEditText6.requestFocus();
                mEditText6.setSelection(mEditText6.getText().length());
            } else if (mEditText6.isFocused()) {
                mEditText7.requestFocus();
                mEditText7.setSelection(mEditText7.getText().length());
            } else if (mEditText7.isFocused()) {
                mEditText8.requestFocus();
                mEditText8.setSelection(mEditText8.getText().length());
            }
        } else if (string.length() == 3 && !string.contains(".")) {//三个无小数点，如：123
            int value = Integer.valueOf(s.toString());
            if (value < 0) {
                mCurrentEditView.setText(String.valueOf(0));
            } else if (value > 255) {
                ToastUtil.showToast("ip不能大于255");
                mCurrentEditView.setText(String.valueOf(""));
            } else {
                if (mEditText1.isFocused()) {
                    mEditText2.requestFocus();
                    mEditText2.setSelection(mEditText2.getText().length());
                } else if (mEditText2.isFocused()) {
                    mEditText3.requestFocus();
                    mEditText3.setSelection(mEditText3.getText().length());
                } else if (mEditText3.isFocused()) {
                    mEditText4.requestFocus();
                    mEditText4.setSelection(mEditText4.getText().length());
                } else if (mEditText4.isFocused()) {
                    mEditText5.requestFocus();
                    mEditText5.setSelection(mEditText5.getText().length());
                } else if (mEditText5.isFocused()) {
                    mEditText6.requestFocus();
                    mEditText6.setSelection(mEditText6.getText().length());
                } else if (mEditText6.isFocused()) {
                    mEditText7.requestFocus();
                    mEditText7.setSelection(mEditText7.getText().length());
                } else if (mEditText7.isFocused()) {
                    mEditText8.requestFocus();
                    mEditText8.setSelection(mEditText8.getText().length());
                }
            }
        } else if (string.length() == 4) {//没有小数点如：1234
            if (string.contains(".") && string.endsWith(".")) {
                String replace = string.replace(".", "");
                mCurrentEditView.setText(replace);
            } else {
                mCurrentEditView.setText("");
                ToastUtil.showToast("IP只能三位数");
            }

        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                req();
                break;

            case R.id.btn_clear:
                mEditText1.setText("");
                mEditText2.setText("");
                mEditText3.setText("");
                mEditText4.setText("");
                mEditText5.setText("");
                mEditText6.setText("");
                mEditText7.setText("");
                mEditText8.setText("");
                mTv.setText("");
                break;
        }
    }

    private void req() {

        String trim1 = mEditText1.getText().toString().trim();
        String trim2 = mEditText2.getText().toString().trim();
        String trim3 = mEditText3.getText().toString().trim();
        String trim4 = mEditText4.getText().toString().trim();
        String trim5 = mEditText5.getText().toString().trim();
        String trim6 = mEditText6.getText().toString().trim();
        String trim7 = mEditText7.getText().toString().trim();
        String trim8 = mEditText8.getText().toString().trim();

        if (TextUtils.isEmpty(trim1) || TextUtils.isEmpty(trim2) || TextUtils.isEmpty(trim3) || TextUtils.isEmpty(trim4)) {
            ToastUtil.showToast("不能为空");
            return;
        }

        final String startIpAddress = trim1 + "." + trim2 + "." + trim3 + "." + trim4;
        String endIpAddress = trim5 + "." + trim6 + "." + trim7 + "." + trim8;
        Log.e("111", "startIpAddress==" + startIpAddress);
        Log.e("111", "endIpAddress==" + endIpAddress);

        showLoading();

        final String url = "ping -c 3 -w 100 " + startIpAddress;
        ThreadPoolManager.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                BufferedReader in = null;
                try {
                    //参数-c 1是指ping的次数为1次，-w 5是指超时时间单位为5s。
                    mProcess = Runtime.getRuntime().exec(url);

                    /*--------------信息------------------------------------*/
                    InputStream input = mProcess.getInputStream();
                    in = new BufferedReader(new InputStreamReader(input));
                    StringBuilder buffer = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        buffer.append(line);
                    }
                    msg = buffer.toString();
                    Log.e("111", "msg====" + msg);

                    /*--------------状态-----------------------------------*/
                    //status 等于0的时候表示网络可用，status等于2时表示当前网络不可用。
                    int status = mProcess.waitFor();
                    final String result;
                    if (status == 0) {
                        result = "success";
                    } else {
                        result = "failed";
                    }

                    Log.e("111", "result=======" + result);

                    /*--------------更新UI------------------------------------*/
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTv.setText("msg===" + msg + "\n" + "result===" + result);

                            dismissLoading();
                        }
                    });
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (mProcess != null) {
                        mProcess.destroy();
                    }

                    try {
                        if (in != null) {
                            in.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            mCurrentEditView = (EditText) v;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (mEditText8.isFocused() && mEditText8.getText().length() == 0) {
                mEditText7.requestFocus();
                mEditText7.setSelection(mEditText7.getText().length());
            } else if (mEditText7.isFocused() && mEditText7.getText().length() == 0) {
                mEditText6.requestFocus();
                mEditText6.setSelection(mEditText6.getText().length());
            } else if (mEditText6.isFocused() && mEditText6.getText().length() == 0) {
                mEditText5.requestFocus();
                mEditText5.setSelection(mEditText5.getText().length());
            } else if (mEditText5.isFocused() && mEditText5.getText().length() == 0) {
                mEditText4.requestFocus();
                mEditText4.setSelection(mEditText4.getText().length());
            } else if (mEditText4.isFocused() && mEditText4.getText().length() == 0) {
                mEditText3.requestFocus();
                mEditText3.setSelection(mEditText3.getText().length());
            } else if (mEditText3.isFocused() && mEditText3.getText().length() == 0) {
                mEditText2.requestFocus();
                mEditText2.setSelection(mEditText2.getText().length());
            } else if (mEditText2.isFocused() && mEditText2.getText().length() == 0) {
                mEditText1.requestFocus();
                mEditText1.setSelection(mEditText1.getText().length());
            }
        }
        return false;
    }

    protected void showLoading() {
        progressDialog = new Dialog(this, R.style.progress_dialog);
        progressDialog.setContentView(R.layout.dialog_loading);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        Window window = progressDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }
        progressDialog.show();
    }

    protected void dismissLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
