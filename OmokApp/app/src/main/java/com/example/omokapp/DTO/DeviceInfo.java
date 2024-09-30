package com.example.omokapp.DTO;

import android.content.Context;
import android.provider.Settings;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DeviceInfo {
    private String info;
    private Double random;

    public static DeviceInfo create(Context context){
        DeviceInfo info = new DeviceInfo();
        info.setInfo(Settings.Secure.getString(
                context.getContentResolver(),
                Settings.Secure.ANDROID_ID));
        return info;
    }
}
