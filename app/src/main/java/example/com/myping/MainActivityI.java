package example.com.myping;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.TextView;


/**
 * 监听软键盘，在软键盘关闭时显示其他
 */
public class MainActivityI extends AppCompatActivity {

    private EditText mEt1;
    private boolean sLastVisiable;
    private TextView mTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maini);

        mEt1 = (EditText) findViewById(R.id.et1);
        mTv = ((TextView) findViewById(R.id.tv));

        mEt1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String trim = mEt1.getText().toString().trim();
                if (TextUtils.isEmpty(trim)) {
                    mTv.setVisibility(View.GONE);
                }
            }
        });

        addOnSoftKeyBoardVisibleListener(this, new OnSoftKeyBoardVisibleListener() {
            @Override
            public void onSoftKeyBoardVisible(boolean visible) {
                String trim = mEt1.getText().toString().trim();
                if (!visible && !TextUtils.isEmpty(trim)) {
                    mTv.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    interface OnSoftKeyBoardVisibleListener {
        void onSoftKeyBoardVisible(boolean visible);
    }

    /**
     * 监听软键盘状态
     */
    public void addOnSoftKeyBoardVisibleListener(Activity activity, final OnSoftKeyBoardVisibleListener listener) {
        final View decorView = activity.getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                //getWindowVisibleDisplayFrame()执行结果会受到系统状态栏，系统软键盘，系统虚拟按键的影响。
                decorView.getWindowVisibleDisplayFrame(rect);
                int displayHight = rect.bottom - rect.top;
                int hight = decorView.getHeight();
                Log.e("MainActivityI", "rect.bottom===" + rect.bottom);//会变
                Log.e("MainActivityI", "rect.top======" + rect.top);//72不会变
                Log.e("MainActivityI", "displayHight==" + displayHight);//1740会变
                Log.e("MainActivityI", "hight=========" + hight);//1920不会变

                //没打开软键盘时：0.90625，打开软键盘时：0.4119791666666667
                Log.e("MainActivityI", "(double) displayHight / hight=========" + ((double) displayHight / hight));
                boolean visible = (double) displayHight / hight < 0.8;

                if (visible != sLastVisiable) {
                    listener.onSoftKeyBoardVisible(visible);
                }
                sLastVisiable = visible;
            }
        });
    }
}
