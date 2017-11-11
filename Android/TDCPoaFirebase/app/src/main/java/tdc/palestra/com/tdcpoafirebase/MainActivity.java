package tdc.palestra.com.tdcpoafirebase;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity {

    private boolean status = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MediaPlayer player = MediaPlayer.create(this, R.raw.musica);
        player.start();


        final AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        Log.e("AUDIO", "" + audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        //audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 2, 0);//maximo é 15

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean touch = dataSnapshot.child("touch").child("valor").getValue(Boolean.class);
                int potenciometro = dataSnapshot.child("potenciometro").child("valor").getValue(Integer.class);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, potenciometro, 0);//maximo é 15

                if (touch && status){
                            status = false;
                    player.pause();
                } else if (!touch && !status){
                    status = true;
                    player.start();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        Log.e("TOKEN", FirebaseInstanceId.getInstance().getToken());
    }
}
