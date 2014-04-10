package lila.raw.skills.asuka;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff.Mode;
import android.view.SurfaceHolder;

@SuppressLint({ "WrongCall", "DrawAllocation" })
public class Animator extends MySurfaceView {
	private MySurfaceView me;
	private SurfaceHolder holder;	
	private Paint paint = new Paint();
	private AnimatorThread animatorThread;
	public AsukaChan asuka;
	
	public Animator(Context context) {
		super(context);
		me = this;
		animatorThread = new AnimatorThread(this);
		holder = this.getHolder();
		holder.addCallback(leCallback());
		this.setZOrderOnTop(true);
		this.getHolder().setFormat(PixelFormat.TRANSPARENT);
	}
	
	@SuppressLint("DrawAllocation")
	@Override
	public void onDraw(Canvas canvas) {
		 
		   //The platform
		   paint = new Paint();
		   paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
		   canvas.drawPaint(paint);
		   paint.setXfermode(new PorterDuffXfermode(Mode.SRC));
		   
		   asuka.onDraw(canvas);
	 }
	
	private SurfaceHolder.Callback leCallback(){
		
		SurfaceHolder.Callback holdy = new SurfaceHolder.Callback() {

			@Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            	boolean retry = true;
            	animatorThread.setRunning(false);
                while (retry) {
                   try {
                	   animatorThread.join();
                         retry = false;
                   } catch (InterruptedException e) {
                   }
                }
            }	

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
            	
//            	createSprite();
            	createAsuka();
            	animatorThread.setRunning(true);
            	animatorThread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                          int width, int height) {
            	
            }
            
		};
		
		
		return holdy;
		
	}
	
	private void createAsuka(){
		asuka = new AsukaChan(me);
		asuka.gravity = true;
	}
	
}
