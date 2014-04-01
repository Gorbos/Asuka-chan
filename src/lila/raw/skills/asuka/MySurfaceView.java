package lila.raw.skills.asuka;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff.Mode;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

public class MySurfaceView extends SurfaceView{

	public MySurfaceView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onDraw(Canvas canvas) { 
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		return false;
	}
	
	
}
