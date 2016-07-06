package com.mimeintent;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;

import com.facebook.react.bridge.JSApplicationIllegalArgumentException;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class MimeIntentManager extends ReactContextBaseJavaModule {
    public MimeIntentManager(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    /**
     * Starts a corresponding external activity for the given URL.
     *
     * For example, if the URL is "https://www.facebook.com", the system browser will be opened,
     * or the "choose application" dialog will be shown.
     *
     * @param url the URL to open
     * @param mime the mimetype of the content
     */
    @ReactMethod
    public void openURLWithMime(String url, String mime, Promise promise) {
        if (url == null || url.isEmpty()) {
            promise.reject(new JSApplicationIllegalArgumentException("Invalid URL: " + url));
            return;
        }

        try {
            Activity currentActivity = getCurrentActivity();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(url), mime);

            String selfPackageName = getReactApplicationContext().getPackageName();
            ComponentName componentName = intent.resolveActivity(
                    getReactApplicationContext().getPackageManager());
            String otherPackageName = (componentName != null ? componentName.getPackageName() : "");

            // If there is no currentActivity or we are launching to a different package we need to set
            // the FLAG_ACTIVITY_NEW_TASK flag
            if (currentActivity == null || !selfPackageName.equals(otherPackageName)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }

            if (currentActivity != null) {
                currentActivity.startActivity(intent);
            } else {
                getReactApplicationContext().startActivity(intent);
            }

            promise.resolve(true);
        } catch (Exception e) {
            promise.reject(new JSApplicationIllegalArgumentException(
                    "Could not open URL '" + url + "': " + e.getMessage()));
        }
    }

    /**
     * Determine whether or not an installed app can handle a given URL.
     *
     * @param url the URL to open
     * @param promise a promise that is always resolved with a boolean argument
     */
    @ReactMethod
    public void canOpenURLWithMime(String url, String mime, Promise promise) {
        if (url == null || url.isEmpty()) {
            promise.reject(new JSApplicationIllegalArgumentException("Invalid URL: " + url));
            return;
        }

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(url), mime);
            // We need Intent.FLAG_ACTIVITY_NEW_TASK since getReactApplicationContext() returns
            // the ApplicationContext instead of the Activity context.
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            boolean canOpen =
                    intent.resolveActivity(getReactApplicationContext().getPackageManager()) != null;
            promise.resolve(canOpen);
        } catch (Exception e) {
            promise.reject(new JSApplicationIllegalArgumentException(
                    "Could not check if URL '" + url + "' can be opened: " + e.getMessage()));
        }
    }
}