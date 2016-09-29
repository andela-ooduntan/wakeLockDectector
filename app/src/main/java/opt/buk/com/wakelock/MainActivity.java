package opt.buk.com.wakelock;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;

public class MainActivity extends AppCompatActivity {

    final String app = "samplewakelock";
	final String lock = "SampleWakeLock";
	TextView appsList = null;
	TextView currentList = null;
    private XSharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appsList = (TextView) findViewById(R.id.appsList);
		currentList = (TextView) findViewById(R.id.currentList);
//		getInstalledAppPermissions();
		getCurrentWakeLocks();
//        getLockfromApps();
        prefs = new XSharedPreferences("PackageName", "WLTSettings");
    }

	public String getLockFromApp(String appName)
	{
        Log.d("WakeLockDetector", "Apmnknknknp: " + appName);

		return (appName.contains(app)) ? lock : null;
	}


	public void getInstalledAppPermissions()
	{
		PackageManager pm = getPackageManager();
		List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo applicationInfo : packages) {
//			Log.d("WakeLockDetector", "App: " + applicationInfo + " Package: " + applicationInfo.packageName);

			try {
				PackageInfo packageInfo = pm.getPackageInfo(applicationInfo.packageName, PackageManager.GET_PERMISSIONS);

				//Get Permissions
				String[] requestedPermissions = packageInfo.requestedPermissions;

				if(requestedPermissions != null) {
					for (int i = 0; i < requestedPermissions.length; i++) {
						Log.d("WakeLockDetector", requestedPermissions[i]);
						if(requestedPermissions[i].equals("android.permission.WAKE_LOCK")) {
							appsList.append(applicationInfo.packageName + "\n");
						}

					}
				}
			} catch (PackageManager.NameNotFoundException e) {
				e.printStackTrace();
			}
		}
	}


	public void getCurrentWakeLocks() {

		final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		final List<ActivityManager.RunningTaskInfo> recentTasks = activityManager.getRunningTasks(Integer.MAX_VALUE);


        for (int i = 0; i < recentTasks.size(); i++)
		{
//
            String appShortName = recentTasks.get(i).baseActivity.toShortString();
            Log.d("Executed app", "Application executed : " + "\t\t ID: "+ appShortName );
            String lockAppName = getLockfromApps(appShortName);
//			currentList.append((lockAppName!=null)?lockAppName:"");

//            Log.d("Executed app", "Application executed : " + "\t\t ID: "+recentTasks.get(i).baseActivity.toShortString()+"");
		}
	}

	public String getLockfromApps(final String appName)
	{

        Log.d("The real test", "Application executed : " + appName); //.append("/preventWakeLock").toString());
//		if (prefs.getBoolean((new StringBuilder(String.valueOf(appName))).append("/preventWakeLock").toString(), false))
//        {
//            Class class1 = XposedHelpers.findClass("android.os.PowerManager.WakeLock", this.getClassLoader());
//            Object aobj[] = new Object[1];
//            aobj[0] = new XC_MethodHook() {
//
//                //final MainXposedHook this$0;
//                private final de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam val$lpparam = null;
//
////                protected void beforeHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodhookparam)
////                    throws Throwable
////                {
////                    if (prefs.getBoolean((new StringBuilder(String.valueOf(appName))).append("/preventWakeLock").toString(), false))
////                    {
////                        String s = (String)XposedHelpers.getObjectField((android.os.PowerManager.WakeLock)methodhookparam.thisObject, "mTag");
////                        String s1 = prefs.getString((new StringBuilder(String.valueOf(appName))).append("/filterWakeLockTags").toString(), "");
////                        if (s1.equals(""))
////                        {
////                            s1 = "-1";
////                        }
////                        if (s1.equals("-1") || s1.contains(s))
////                        {
////                            methodhookparam.setResult(null);
////                        }
////                    }
////                }
//
//
//            };
//
//            XposedHelpers.findAndHookMethod(class1, "acquire", aobj);
//            Object aobj1[] = new Object[1];
//            aobj1[0] = new XC_MethodHook() {
//
//                //final MainXposedHook this$0;
//                private final de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam val$lpparam = null;
//
//                protected void beforeHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodhookparam)
//                    throws Throwable
//                {
//                    if (prefs.getBoolean((new StringBuilder(String.valueOf(appName))).append("/preventWakeLock").toString(), false))
//                    {
//                        String s = (String)XposedHelpers.getObjectField((android.os.PowerManager.WakeLock)methodhookparam.thisObject, "mTag");
//                        String s1 = prefs.getString((new StringBuilder(String.valueOf(appName))).append("/filterWakeLockTags").toString(), "-1");
//                        if (s1.equals(""))
//                        {
//                            s1 = "-1";
//                        }
//                        if (s1.equals("-1") || s1.contains(s))
//                        {
//                            methodhookparam.setResult(null);
//                        }
//                    }
//                }
//
//            };
//            XposedHelpers.findAndHookMethod(class1, "release", aobj1);
//        }
		return lock;
	}
}
