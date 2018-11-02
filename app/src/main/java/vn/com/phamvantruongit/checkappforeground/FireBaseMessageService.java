package vn.com.phamvantruongit.checkappforeground;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;
import java.util.Map;

public class FireBaseMessageService extends FirebaseMessagingService {
    private static int count = 0;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String title=remoteMessage.getNotification().getTitle();
        String body=remoteMessage.getNotification().getBody();
        Toast.makeText(this, "---", Toast.LENGTH_SHORT).show();
        Log.d("body",title + body + "test");
        if(isRunningForeground()) {
            sendNotification(title, body, remoteMessage.getData());
        }else {
            Intent intent=new Intent();
            intent.setAction("checkappforeground");
            intent.putExtra("title",title);
            intent.putExtra("body",body);
            this.sendBroadcast(intent);
        }


    }

    private void sendNotification(String messageTitle, String messageBody, Map<String, String> row) {
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
        notificationManager.notify(count, notificationBuilder.build());
        count++;
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
}
