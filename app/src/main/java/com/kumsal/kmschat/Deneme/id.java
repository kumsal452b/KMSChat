package com.kumsal.kmschat.Deneme;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

public class id extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        super.onMessageReceived(remoteMessage);
        Map<String ,String> senten=remoteMessage.getData();

        Map<String ,String> senten=remoteMessage.getData();
    }
}
