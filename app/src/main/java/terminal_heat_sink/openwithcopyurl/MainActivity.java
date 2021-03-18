package terminal_heat_sink.openwithcopyurl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText editTextTextMultiLine;
    private ClipboardManager clipboard;
    final Handler handler = new Handler();
    final Runnable r = new Runnable() {
        public void run() {
            clipboard = (ClipboardManager) getSystemService(getApplicationContext().CLIPBOARD_SERVICE);
            if(clipboard.hasPrimaryClip()){
                ClipData abc = clipboard.getPrimaryClip();
                ClipData.Item item = abc.getItemAt(0);
                String text = item.getText().toString();
                editTextTextMultiLine.setText(text);
            }else{
                Log.i("clip", "no text");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTextMultiLine = findViewById(R.id.editTextTextMultiLine);

        clipboard = (ClipboardManager) getSystemService(getApplicationContext().CLIPBOARD_SERVICE);
        Intent intent = getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri = intent.getData();
            ClipData clip = ClipData.newPlainText("url", uri.toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getApplicationContext(),"Copied "+uri.toString(),Toast.LENGTH_SHORT).show();
            //String valueOne = uri.getQueryParameter("keyOne");
            //String valueTwo = uri.getQueryParameter("keyTwo");
            finish();
        }else{
            handler.postDelayed(r, 200);
        }
    }
}