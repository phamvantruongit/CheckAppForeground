package vn.com.phamvantruongit.checkappforeground;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {
    BroadcastReceiverManager broadcastReceiverManager;
    IntentFilter intentFilter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent=new Intent(this,ServiceTest.class);
        startService(intent);



    }

    @Override
    protected void onResume() {
        super.onResume();
        broadcastReceiverManager=new BroadcastReceiverManager();
        intentFilter=new IntentFilter();
        intentFilter.addAction("checkappforeground");
        registerReceiver(broadcastReceiverManager,intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiverManager);
    }

    public   class BroadcastReceiverManager extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle=intent.getExtras();
            Toast.makeText(MainActivity.this, bundle.getString("title") + "\t\t" +bundle.getString("message") , Toast.LENGTH_SHORT).show();
        }
    }



}