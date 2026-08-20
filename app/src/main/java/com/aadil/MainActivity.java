package com.aadil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {

    public String GameActivity = "com.unity3d.player.UnityPlayerActivity";
    public boolean hasLaunched = false;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        if (!this.hasLaunched) {
            try {
                this.hasLaunched = true;

                Intent intent = new Intent(
                        this,
                        Class.forName(this.GameActivity)
                );

                startActivity(intent);
                Main.Start(this);

                return;

            } catch (ClassNotFoundException e) {
                Log.e(
                        "Mod_menu",
                        "Error. Game's main activity does not exist",
                        e
                );
            }
        }

        Main.Start(this);
    }
}