package example.com.myping;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;


/**
 * 监听软键盘，在软键盘关闭时显示其他
 */
public class MainActivityII extends AppCompatActivity {

    private EditText mEt1;
    private Button mBtn1;
    private Button mBtn2;
    private Button mBtn3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainii);

        mEt1 = (EditText) findViewById(R.id.et1);
        mBtn1 = ((Button) findViewById(R.id.btn1));
        mBtn2 = ((Button) findViewById(R.id.btn2));
        mBtn3 = ((Button) findViewById(R.id.btn3));

        mBtn1.setOnClickListener(new View.OnClickListener() {//切换
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });

        mBtn2.setOnClickListener(new View.OnClickListener() {//开
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    mEt1.requestFocus();
                    imm.showSoftInput(mEt1, 0);//可toggle关闭

//                    imm.showSoftInput(mEt1, InputMethodManager.SHOW_FORCED);//强制开启只能mBtn3关，不能mBtn1的toggle关闭
                }
            }
        });

        mBtn3.setOnClickListener(new View.OnClickListener() {//关
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(mEt1.getWindowToken(), 0);
                }
            }
        });
    }
}
