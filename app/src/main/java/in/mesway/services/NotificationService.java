package in.mesway.services;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import in.mesway.R;
import in.mesway.Rusable.Constant;
import in.mesway.Rusable.EncryptedSharedPreferencesInstance;
import in.mesway.Rusable.Reusable;
import in.mesway.activity.SplashActivity;


public class NotificationService extends FirebaseMessagingService {
    private static final int NOTIFICATION_ID =220 ;
    //private final SharedPreferences sharedPreferences= EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES,this);

    @SuppressLint("UnspecifiedImmutableFlag")
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        Intent intent= new Intent(this, SplashActivity.class);
        intent.putExtra("handle_key",NOTIFICATION_ID);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
        if (message.getData().size()>0) {
            if (message.getData().get("where") != null) {
                intent.putExtra("where",message.getData().get("where"));
            }
        }
        if(message.getNotification()!=null){
            String title  = message.getNotification().getTitle();
            String body  = message.getNotification().getBody();
            // NotificationCompat.BigPictureStyle bigPictureStyle =new NotificationCompat.BigPictureStyle();

            PendingIntent pendingIntent;
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.S) {
                 pendingIntent = PendingIntent.getActivity(this, 100, intent, PendingIntent.FLAG_CANCEL_CURRENT|PendingIntent.FLAG_IMMUTABLE);
            }else {
                 pendingIntent = PendingIntent.getActivity(this, 100, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            }
            Notification notification = new NotificationCompat.Builder(this, Constant.REQUIRED_NOTIFICATION_CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.mipmap.ic_launcher_foreground)
                    .build();

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(NOTIFICATION_ID,notification);


        }


    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
       // sharedPreferences.edit().putBoolean(Constant.HAVE_TO_UPDATE_NOTIFICATION_TOKEN,true).apply();

        Reusable.updateNotificationToken(this,token);

    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }
}