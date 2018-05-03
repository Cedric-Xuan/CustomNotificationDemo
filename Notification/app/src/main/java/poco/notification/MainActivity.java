package poco.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private int i=0;
    private int j=0;
    private int k=0;
    private Button btn;
    private Button clear_btn;
    private Button show_btn;
    private Button print;
    private TextView tx1;
    private TextView tx2;
    private TextView showTv;
    private ButtonBroadcastReceiver bReceiver;
    public static final String ACTION_BUTTON = "intent_action";
    static NotificationManager manager;
    static NotificationCompat.Builder builder;
    private static RemoteViews mRemoteViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tx1=(TextView) findViewById(R.id.tx1);
        tx2=(TextView) findViewById(R.id.tx2);
        clear_btn=(Button)findViewById(R.id.clear_btn);
        print=(Button)findViewById(R.id.print);
        show_btn=(Button)findViewById(R.id.show_btn);




                manager = (NotificationManager) getBaseContext().getSystemService(getBaseContext().NOTIFICATION_SERVICE);

                initBttonReceiver();//注册按键广播接收者

                showNotifictionIcon(getBaseContext());//显示通知

                //清除消息通知
                clear_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        manager.cancel(0);
            }
        });


        //显示消息通知按钮
        show_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.notify(0,builder.build());
            }
        });

        //每点击一个，消息通知中的数字便会+1
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                k++;
                mRemoteViews.setTextViewText(R.id.showTv,""+k);
                manager.notify(0,builder.build());
            }
        });






    }

    //消息通知设置和显示
    public static void showNotifictionIcon(Context context) {
        builder = new NotificationCompat.Builder(context);
        mRemoteViews = new RemoteViews(context.getPackageName(), R.layout.notification);
        mRemoteViews.setViewVisibility(R.id.btn, View.VISIBLE);
        Intent intent = new Intent(context, MainActivity.class);//将要跳转的界面
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//activity启动模式为：singleTop


        builder.setAutoCancel(false);//点击是否后消失
        builder.setSmallIcon(R.mipmap.ic_launcher);//设置通知栏消息标题的头像
//        builder.setDefaults(NotificationCompat.DEFAULT_SOUND);//设置通知铃声
        builder.setTicker("通知");
        builder.setContentTitle("通知");
        builder.setOngoing(true);


        Intent buttonIntent =new Intent(ACTION_BUTTON);
        buttonIntent.putExtra("ButtonId", 1);
        //假如在同一个requestCode下，使用FLAG_UPDATE_CURRENT，最新接收的广播消息中的Intent的extra会替换掉旧的广播消息Intent的extra
        PendingIntent intent_btn=PendingIntent.getBroadcast(context,1,buttonIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.btn,intent_btn);

        buttonIntent.putExtra("ButtonId",2);
        intent_btn=PendingIntent.getBroadcast(context,2,buttonIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.btn2,intent_btn);


        //利用PendingIntent来包装我们的intent对象,使其延迟跳转
        builder.setContent(mRemoteViews);
        //假如在同一个requestCode下，使用FLAG_CANCEL_CURRENT，会把旧的广播消息Intent 中的extra清除掉
        PendingIntent intentPend = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(intentPend);

        //刷新通知
        manager.notify(0, builder.build());
    }

    //初始化广播接收器
    public void initBttonReceiver() {
        bReceiver=new ButtonBroadcastReceiver();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(ACTION_BUTTON);
        registerReceiver(bReceiver,intentFilter);

    }


    //广播接收类
    public  class ButtonBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if(action.equals(ACTION_BUTTON)){
                int buttonId=intent.getIntExtra("ButtonId",0);
                switch (buttonId){
                    case 1:
                        i++;
                        tx1.setText("点击了←"+i+"次");
                        break;
                    case 2:
                        j++;
                        tx2.setText("点击了→"+j+"次");
                    default:
                        break;
                }
            }
        }
    }




}
