package me.hwang.multitypechatboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btnShowBoard;
    private View board;
    private boolean isShown;
    private TranslateAnimation mShowAction,mHiddenAction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        EmotionKeyboard emotionKeyboard = EmotionKeyboard.with(this) //初始化配置
//                .setExtendView(extendView) //设置扩展视图（自定义功能：拍照上传、位置等）
//                .setEmotionView(emotionView) //设置表情视图（自定义添加表情）
//                .bindToContent(contentView) //绑定内容视图
//                .bindToEditText(edittext) //绑定输入框
//                .bindToExtendbutton(extendButton) //绑定扩展视图按钮
//                .bindToEmotionButton(emotionButton) //绑定表情视图按钮
//                .build(); //创建


        btnShowBoard = findViewById(R.id.btn_show_board);
        board = findViewById(R.id.layout_suppressible);
        btnShowBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isShown){
                    board.setVisibility(View.VISIBLE);

                    mShowAction= new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                            1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    mShowAction.setDuration(500);

                    board.startAnimation(mShowAction);
                    isShown = true;
                }else{
                    board.setVisibility(View.GONE);
                    mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                            0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
                    mHiddenAction.setDuration(500);
                    board.startAnimation(mHiddenAction);
                    isShown = false;
                }
            }
        });
    }
}
