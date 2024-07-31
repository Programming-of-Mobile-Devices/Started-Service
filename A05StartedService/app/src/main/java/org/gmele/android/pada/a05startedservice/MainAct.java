package org.gmele.android.pada.a05startedservice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class MainAct extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener
{
    boolean Act = false;
    Switch SwService;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.main_lay);
        SwService = findViewById (R.id.SwService);
        if (isMyServiceRunning ())
            SwService.setChecked (true);
        SwService.setOnCheckedChangeListener (this);   //Γιατί μετά από το προηγούμενο;

    }

    @Override
    public void onCheckedChanged (CompoundButton buttonView, boolean isChecked)
    {
        if (buttonView == SwService)
            if (isChecked)
            {
                Intent ServInt = new Intent (getApplicationContext (), FileService.class);
                startService (ServInt);
                ShowMessage ("Starting Service");
            }
            else
            {
                Intent ServInt = new Intent (getApplicationContext (), FileService.class);
                ServInt.putExtra ("Stop", true);
                startService (ServInt);
                ShowMessage ("Stoping Service");
            }
    }






    private void ShowMessage (String Mess)
    {
        Toast Tst = Toast.makeText (this, "Activity : " + Mess, Toast.LENGTH_LONG );
        Tst.show ();
    }

    //Καταργήθηκε. Για να την έχουμε δει...
    private boolean isMyServiceRunning ()
    {
        boolean flag = false;
        ActivityManager manager = (ActivityManager) getSystemService (Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices (Integer.MAX_VALUE))
        {
            System.out.println ("*** " + service.service.getClassName ());
            if (FileService.class.getName (). equals (service.service.getClassName ()))
            {
                flag = true;
            }
        }
        return flag;
    }
}