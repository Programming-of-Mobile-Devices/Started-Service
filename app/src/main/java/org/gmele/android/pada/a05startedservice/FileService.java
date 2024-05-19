package org.gmele.android.pada.a05startedservice;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class FileService extends Service
{
    ArrayList <String> Filenames;
    FileObserver Observer;

    public FileService ()
    {
        File PhotDir = Environment.getExternalStoragePublicDirectory (Environment.DIRECTORY_DCIM);
        PhotDir = new File (PhotDir.getAbsolutePath () + "/Camera");
        System.out.println ("*** Photo Dir " + PhotDir.getAbsolutePath ());

        Observer = new FileObserver (PhotDir)
        {
            @Override
            public void onEvent(int event, String file)
            {
                if(event == FileObserver.CREATE && !file.equals (".probe"))
                {
                    Filenames.add (file);
                    //ShowMessage ("New Photo : " + file );    Γιατί όχι;;;;
                    Message msg = new Message ();
                    Bundle bun = new Bundle ();
                    bun.putString ("Message", "New Photo : " + file);
                    msg.setData (bun);
                    MyHandler.sendMessage (msg);

                }
            }
        };
        System.out.println ("*** Service Constructor....");
    }

    public void onCreate ()
    {
        ShowMessage ("Creating...");
        Filenames = new ArrayList ();
        Observer.startWatching();
    }

    @Override
    public int onStartCommand (Intent intent, int flags, int startId)
    {
        ShowMessage ("Starting... : " + startId );
        if (intent.getBooleanExtra ("Stop", false))    //Για το παράδειγμα μόνο....
            stopSelf ();
        return START_STICKY;
    }

    public void onDestroy ()
    {
        Observer.stopWatching ();
        Observer = null;
        ShowMessage ("Destroying...");
    }


    private void ShowMessage (String Mess)
    {
        Toast Tst = Toast.makeText (this, "Service : " + Mess, Toast.LENGTH_LONG );
        Tst.show ();
    }

    Handler MyHandler = new Handler ()
    {
        @Override
        public void handleMessage (Message Mess)
        {
            Bundle b = Mess.getData ();
            String tbp = b.getString ("Message");
            ShowMessage (tbp);
        }
    };


    /*
    ======================================================
    Binding......
    ======================================================
    */
    @Override
    public IBinder onBind (Intent intent)
    {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException ("Not yet implemented");
    }


}