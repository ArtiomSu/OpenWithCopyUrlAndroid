package terminal_heat_sink.openwithcopyurl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private boolean isAppInstalled(String uri, Context context) {
        PackageManager pm = getApplicationContext().getPackageManager();
        try {
            pm.getPackageInfo(uri, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private TextView youtubedetectedtext;
    private Button youtubeButton;
    private Button youtubeButtonAlways;
    private Button vancedButton;
    private Button vancedButtonAlways;
    private Button neverButton;
    private Button okButton;
    private LinearLayout llYoutube;
    private LinearLayout llVanced;
    private LinearLayout llok;

    private LinearLayout llSettings;
    private Button clearButton;

    private ClipboardManager clipboard;
    private String new_url;
    final Handler handler = new Handler();
    final Runnable r = new Runnable() {
        public void run() {
            clipboard = (ClipboardManager) getSystemService(getApplicationContext().CLIPBOARD_SERVICE);
            if(clipboard.hasPrimaryClip()){
                ClipData abc = clipboard.getPrimaryClip();
                ClipData.Item item = abc.getItemAt(0);
                String text = item.getText().toString();
            }else{
                Log.i("clip", "no text");
            }
        }
    };

    public static final String SHARED_PREFERENCES = "terminal_heat_sink.openwithcopyurl" ;
    public static final String SHARED_PREF_OPTION = "terminal_heat_sink.openwithcopyurl.defaultOption";
    SharedPreferences sharedpreferences;

    private final String[] youtubeUrls = {
            "youtu.be",
            "youtube.com"
    };

    private enum Settings {
        DEFAULT (-1),
        ALWAYS_COPY (0),
        ALWAYS_YOUTUBE (1),
        ALWAYS_VANCED(2);

        private final int setting;
        Settings(int i) {
            this.setting = i;
        }
        public int getSetting() {
            return setting;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedpreferences = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);

        int selectedOption = sharedpreferences.getInt(SHARED_PREF_OPTION, Settings.DEFAULT.getSetting());

        youtubedetectedtext = findViewById(R.id.youtubedetectedtext);
        youtubeButton = findViewById(R.id.openbutton);
        youtubeButtonAlways = findViewById(R.id.openbuttonalways);
        vancedButton = findViewById(R.id.openvancedbutton);
        vancedButtonAlways = findViewById(R.id.openvancedbuttonalways);
        okButton = findViewById(R.id.okbutton);
        neverButton = findViewById(R.id.neverbutton);
        llYoutube = findViewById(R.id.llyoutube);
        llVanced = findViewById(R.id.llvanced);
        llok = findViewById(R.id.llok);

        llSettings = findViewById(R.id.llsettings);
        clearButton = findViewById(R.id.cleardefaults);

        clipboard = (ClipboardManager) getSystemService(getApplicationContext().CLIPBOARD_SERVICE);
        Intent intent = getIntent();



        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri = intent.getData();
            new_url = uri.toString();
            ClipData clip = ClipData.newPlainText("url", new_url);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getApplicationContext(),"Copied "+new_url,Toast.LENGTH_SHORT).show();

            boolean youtube_found = false;
            for (String url: youtubeUrls){
                if(new_url.contains(url)){
                    youtube_found = true;
                    break;
                }
            }
            Log.d("TEST", " youtube url found? " + String.valueOf(youtube_found));
            //String valueOne = uri.getQueryParameter("keyOne");
            //String valueTwo = uri.getQueryParameter("keyTwo");
            if(youtube_found){
                boolean found_apps = false;
                if(isAppInstalled("com.google.android.youtube", getApplicationContext())) {
                    youtubeButton.setVisibility(View.VISIBLE);
                    llYoutube.setVisibility(View.VISIBLE);
                    found_apps = true;
                    if(selectedOption == Settings.ALWAYS_YOUTUBE.getSetting()){
                        launchApp("com.google.android.youtube");
                    }
                }

                if(isAppInstalled("com.vanced.android.youtube", getApplicationContext())) {
                    vancedButton.setVisibility(View.VISIBLE);
                    llVanced.setVisibility(View.VISIBLE);
                    found_apps = true;
                    if(selectedOption == Settings.ALWAYS_VANCED.getSetting()){
                        launchApp("com.vanced.android.youtube");
                    }
                }

                if(!found_apps){
                    finish();
                }else{
                    llok.setVisibility(View.VISIBLE);
                    youtubedetectedtext.setVisibility(View.VISIBLE);

                    if(selectedOption == Settings.ALWAYS_COPY.getSetting()){
                        finish();
                    }
                }
            }else {
                finish();
            }
        }else{
            llSettings.setVisibility(View.VISIBLE);
            //handler.postDelayed(r, 200);
        }


        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        neverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedpreferences.edit().putInt(SHARED_PREF_OPTION, Settings.ALWAYS_COPY.getSetting()).apply();
                finish();
            }
        });

        youtubeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchApp("com.google.android.youtube");
            }
        });

        vancedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchApp("com.vanced.android.youtube");
            }
        });

        youtubeButtonAlways.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedpreferences.edit().putInt(SHARED_PREF_OPTION, Settings.ALWAYS_YOUTUBE.getSetting()).apply();
                launchApp("com.google.android.youtube");
            }
        });

        vancedButtonAlways.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedpreferences.edit().putInt(SHARED_PREF_OPTION, Settings.ALWAYS_VANCED.getSetting()).apply();
                launchApp("com.vanced.android.youtube");
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedpreferences.edit().putInt(SHARED_PREF_OPTION, Settings.DEFAULT.getSetting()).apply();
                finish();
            }
        });

    }

    private void launchApp(String packageName){
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(new_url));
        intent.setPackage(packageName);
        startActivity(intent);
        finish();
    }
}