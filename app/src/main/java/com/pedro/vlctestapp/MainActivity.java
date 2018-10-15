package com.pedro.vlctestapp;

import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.pedro.vlc.VlcListener;
import com.pedro.vlc.VlcVideoLibrary;

/**
 * Created by pedro on 25/06/17.
 */
public class MainActivity extends AppCompatActivity implements VlcListener, View.OnClickListener, SurfaceHolder.Callback {

  private VlcVideoLibrary vlcVideoLibrary;
  private Button bStartStop;
  private EditText etEndpoint;
  private SurfaceView surfaceView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    setContentView(R.layout.activity_main);
    surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
    surfaceView.getHolder().addCallback(new SHCallback());
    bStartStop = (Button) findViewById(R.id.b_start_stop);
    bStartStop.setOnClickListener(this);
    etEndpoint = (EditText) findViewById(R.id.et_endpoint);
    vlcVideoLibrary = new VlcVideoLibrary(this, this, surfaceView);

    WifiManager manager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
    DhcpInfo dhcpInfo = manager.getDhcpInfo();
    String address = Formatter.formatIpAddress(dhcpInfo.gateway);
    etEndpoint.setText(String.format("rtsp://%s:1234", address));
  }

  @Override
  public void onComplete() {
    Toast.makeText(this, "Playing", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onError() {
    Toast.makeText(this, "Error, make sure your endpoint is correct", Toast.LENGTH_SHORT).show();
    vlcVideoLibrary.stop();
    bStartStop.setText(getString(R.string.start_player));
  }

  @Override
  public void onClick(View view) {
    if (!vlcVideoLibrary.isPlaying()) {
      vlcVideoLibrary.play(etEndpoint.getText().toString());
//      bStartStop.setText(getString(R.string.stop_player));
      bStartStop.setVisibility(View.INVISIBLE);
      etEndpoint.setVisibility(View.INVISIBLE);
    } else {
      vlcVideoLibrary.stop();
      bStartStop.setText(getString(R.string.start_player));
    }
  }

  @Override
  public void surfaceCreated(SurfaceHolder holder) {
//    vlcVideoLibrary
  }

  @Override
  public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

  }

  @Override
  public void surfaceDestroyed(SurfaceHolder holder) {

  }

  protected class SHCallback implements SurfaceHolder.Callback {

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
      //Get the SurfaceView layout parameters
      android.view.ViewGroup.LayoutParams lp = surfaceView.getLayoutParams();

      //Set the width of the SurfaceView to the width of the screen
      lp.width = 1080;

      //Set the height of the SurfaceView to match the aspect ratio of the video
      //be sure to cast these as floats otherwise the calculation will likely be 0
      lp.height = 1794;

      //Commit the layout parameters
      surfaceView.setLayoutParams(lp);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
      Log.e("T", "Yo");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
  }
}
