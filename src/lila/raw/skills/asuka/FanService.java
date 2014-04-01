package lila.raw.skills.asuka;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.Toast;

@SuppressLint("NewApi")
public class FanService extends Service implements OnTouchListener, OnClickListener {
    
    private View topLeftView;
 
    private Button overlayedButton;
    private float offsetX;
    private float offsetY;
    private int originalXPos;
    private int originalYPos;
    private boolean moving;
    private WindowManager wm;
    
    private Animator asuka;
 
    @Override
    public IBinder onBind(Intent intent) {
	return null;
    }
 
    @Override
    public void onCreate() {
	super.onCreate();
 
	wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
 
	overlayedButton = new Button(this);
	overlayedButton.setText("Overlay button");
	overlayedButton.setOnTouchListener(this);
	overlayedButton.setAlpha(0.0f);
	overlayedButton.setBackgroundColor(0x55fe4444);
	overlayedButton.setOnClickListener(this);
	
	asuka = new Animator(this);
	asuka.setBackgroundColor(0x55fe4444);
	asuka.setAlpha(0.0f);
	asuka.setOnTouchListener(this);
	//WindowManager.LayoutParams.WRAP_CONTENT,500
	WindowManager.LayoutParams params = new WindowManager.
					LayoutParams(198, 
							326, 
										WindowManager.LayoutParams.TYPE_SYSTEM_ALERT, 
											WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, 
											PixelFormat.TRANSLUCENT);
	params.gravity = Gravity.LEFT | Gravity.TOP;
	params.x = 0;
	params.y = 0;
	wm.addView(asuka, params);
 
	topLeftView = new View(this);
	WindowManager.LayoutParams topLeftParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);
	topLeftParams.gravity = Gravity.LEFT | Gravity.TOP;
	topLeftParams.x = 0;
	topLeftParams.y = 0;
	topLeftParams.width = 0;
	topLeftParams.height = 0;
	wm.addView(topLeftView, topLeftParams);
 
    }
 
    @Override
    public void onDestroy() {
	super.onDestroy();
	if (overlayedButton != null) {
	    wm.removeView(overlayedButton);
	    wm.removeView(topLeftView);
	    overlayedButton = null;
	    topLeftView = null;
	}
	
	if (asuka != null) {
	    wm.removeView(asuka);
	    wm.removeView(topLeftView);
	    asuka = null;
	    topLeftView = null;
	}
    }
 
    @Override
    public boolean onTouch(View v, MotionEvent event) {
 
	if (event.getAction() == MotionEvent.ACTION_DOWN) {
	    float x = event.getRawX();
	    float y = event.getRawY();
 
	    moving = false;
 
	    int[] location = new int[2];
	    asuka.getLocationOnScreen(location);
 
	    originalXPos = location[0];
	    originalYPos = location[1];
 
	    offsetX = originalXPos - x;
	    offsetY = originalYPos - y;
 
	} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
		asuka.asuka.setState(ATField.SPRITE_WALK);
	    int[] topLeftLocationOnScreen = new int[2];
	    topLeftView.getLocationOnScreen(topLeftLocationOnScreen);
 
	    System.out.println("topLeftY="+topLeftLocationOnScreen[1]);
	    System.out.println("originalY="+originalYPos);
	    
	    float x = event.getRawX();
	    float y = event.getRawY();
 
	    WindowManager.LayoutParams params = (LayoutParams) asuka.getLayoutParams();
 
	    int newX = (int) (offsetX + x);
	    int newY = (int) (offsetY + y);
 
	    if (Math.abs(newX - originalXPos) < 1 && Math.abs(newY - originalYPos) < 1 && !moving) {
		return false;
	    }
 
	    params.x = newX - (topLeftLocationOnScreen[0]);
	    params.y = newY - (topLeftLocationOnScreen[1]);
 
	    wm.updateViewLayout(asuka, params);
	    moving = true;
	} else if (event.getAction() == MotionEvent.ACTION_UP) {
		asuka.asuka.setState(ATField.SPRITE_STAND);
	    if (moving) {
		return true;
	    }
	}
 
	return false;
    }
 
    @Override
    public void onClick(View v) {
	Toast.makeText(this, "Overlay button click event", Toast.LENGTH_SHORT).show();
    }
 
}
