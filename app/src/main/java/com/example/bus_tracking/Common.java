package com.example.bus_tracking;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import com.example.bus_tracking.Model.DriveInfoModel;
import com.example.bus_tracking.Service.MyFirebaseMassagingService;

import androidx.core.app.NotificationCompat;

public class Common {
    public static final String DRIVER_INFO_REFERENCE ="DriverInfo";
    public static final String DRIVERS_LOCATION_REFERENCES ="DriversLocation" ;
    public static final String TOKEN_REFERENCE ="Token" ;
    public static final String NOTI_TITLE ="Title" ;
    public static final String NOTI_CONTENT = "Body";
    public static DriveInfoModel currentUser;

    public static StringBuilder buildWelcomeMessage() {

       if(Common.currentUser !=null)
       {
           return  new StringBuilder("welcom ")
                   .append(Common.currentUser.getFirstname())
                   .append(" ")
                   .append(Common.currentUser.getLastname().toString()) ;

       }else

        return null;
    }

    public static void showNotification(Context context, int Id, String Title, String body, Intent intent) {

        PendingIntent pendingIntent = null;
        if(intent != null )
        pendingIntent = pendingIntent.getActivity(context,Id,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        String NOTIFICATION_CHANNEL_ID = "channel ID";
        NotificationManager notificationmanager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notificationchannel = new NotificationChannel (NOTIFICATION_CHANNEL_ID, "Sky EYE", NotificationManager.IMPORTANCE_HIGH);
            notificationchannel.setDescription("Ske Eye ");
            notificationchannel.enableLights(true);
            notificationchannel.setLightColor(android.R.color.holo_red_dark);
            notificationchannel.setVibrationPattern(new long[]{0,1000,500,1000});
            notificationchannel.enableVibration(true);

            notificationmanager.createNotificationChannel (notificationchannel);
        }
        NotificationCompat.Builder builder =new NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID);
        builder.setContentTitle(Title)
                .setContentText(body)
                .setAutoCancel(false)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSmallIcon(R.drawable.bus_no_bg)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.bus_no_bg));


        if(pendingIntent != null)
        {builder.setContentIntent(pendingIntent);}
        Notification notification = builder.build();
        notificationmanager.notify(Id,notification);

    }




}

