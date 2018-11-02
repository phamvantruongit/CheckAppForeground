package vn.com.phamvantruongit.checkappforeground;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.security.Provider;
import java.util.List;
import java.util.Map;

public class ServiceTest extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String title="Hello";
        String message="Message";
        if(isRunningForeground()){
            Intent intentBroadcast=new Intent();
            intentBroadcast.setAction("checkappforeground");
            intentBroadcast.putExtra("title",title);
            intentBroadcast.putExtra("message",message);
            this.sendBroadcast(intentBroadcast);
        }else {
            sendNotification(title,message);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public boolean isRunningForeground(){
        String topActivityClassName=getTopActivityName(this);
        if (topActivityClassName!=null&&topActivityClassName.startsWith("vn.com.phamvantruongit.checkappforeground")) {
            return true;
        } else {

            return false;
        }
    }

    public  String getTopActivityName(Context context){
        String topActivityClassName=null;
        ActivityManager activityManager =
                (ActivityManager)(context.getSystemService(Context.ACTIVITY_SERVICE )) ;
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1) ;
        if(runningTaskInfos != null){
            ComponentName f=runningTaskInfos.get(0).topActivity;
            topActivityClassName=f.getClassName();
        }
        return topActivityClassName;
    }

    private void sendNotification(String messageTitle, String messageBody) {
        PendingIntent contentIntent = null;
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(contentIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notificationBuilder.build());

    }
}
