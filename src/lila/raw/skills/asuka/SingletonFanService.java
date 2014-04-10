package lila.raw.skills.asuka;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;



import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

@SuppressLint("NewApi")
public class SingletonFanService extends Service implements OnTouchListener, OnClickListener {
    
	private String TAG = "SingletonFanService";
	
    private View topLeftView;
 
    private Button overlayedButton;
    private float offsetX;
    private float offsetY;
    private int originalXPos;
    private int originalYPos;
    private boolean moving;
    public WindowManager wm;
    DisplayMetrics dm;
    
    private AnimateME asukaChibi;
    
    public WindowManager.LayoutParams asukaParams;
    
    public FanService fs;
    Handler handler;
    RelativeLayout rl;
    public int asukaSpriteWidth = 66;
    public int asukaSpriteHeight = 130;
    public int asukaWidth = asukaSpriteWidth * 2;
    public int asukaHeight = asukaSpriteHeight * 2;
    @Override
    public IBinder onBind(Intent intent) {
    	return null;
    }
 
    @Override
    public void onCreate() {
	super.onCreate();
	handler = new Handler(Looper.getMainLooper());
	wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

	dm = new DisplayMetrics();
	wm.getDefaultDisplay().getMetrics(dm);

//	rl = (RelativeLayo
	
	overlayedButton = new Button(this);
	overlayedButton.setText("Overlay button");
	overlayedButton.setOnTouchListener(this);
	overlayedButton.setAlpha(0.0f);
	overlayedButton.setBackgroundColor(0x55fe4444);
	overlayedButton.setOnClickListener(this);
	
	asukaChibi = new AnimateME(this);
//	asuka.setBackgroundColor(0x55fe4444);
//	asuka.setAlpha(0.0f);
	asukaChibi.setOnTouchListener(this);
	//WindowManager.LayoutParams.WRAP_CONTENT,500
	WindowManager.LayoutParams params = new WindowManager.
					LayoutParams(asukaWidth, 
									asukaHeight, 
										WindowManager.LayoutParams.TYPE_SYSTEM_ALERT, 
											WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, 
											PixelFormat.TRANSLUCENT);
	params.gravity = Gravity.LEFT | Gravity.TOP;
	params.x = 0;
	params.y = 0;
	wm.addView(asukaChibi, params);
	asukaParams = (LayoutParams) asukaChibi.getLayoutParams();
	
	topLeftView = new View(this);
	WindowManager.LayoutParams topLeftParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);
	topLeftParams.gravity = Gravity.LEFT | Gravity.TOP;
	topLeftParams.x = 0;
	topLeftParams.y = 0;
	topLeftParams.width = 0;
	topLeftParams.height = 0;
	wm.addView(topLeftView, topLeftParams);
//	AIThread ai = new AIThread();
//	ai.setRunning(true);
//	ai.run();
//	moveAsuka();
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
	
	if (asukaChibi != null) {
	    wm.removeView(asukaChibi);
	    wm.removeView(topLeftView);
	    asukaChibi = null;
	    topLeftView = null;
	}
    }
 
    @Override
    public boolean onTouch(View v, MotionEvent event) {
//    	handler.post(new Runnable() {
//			@Override
//			public void run() {
//				moveAsuka();
//			}
//		});
//    
// 
//	return true;
    	

    	if (event.getAction() == MotionEvent.ACTION_DOWN) {
    	    float x = event.getRawX();
    	    float y = event.getRawY();
     
    	    moving = false;
     
    	    int[] location = new int[2];
    	    asukaChibi.getLocationOnScreen(location);
     
    	    originalXPos = location[0];
    	    originalYPos = location[1];
     
    	    offsetX = originalXPos - x;
    	    offsetY = originalYPos - y;
     
    	} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
    		asukaChibi.asuka.setState(ATField.SPRITE_WALK);
    	    int[] topLeftLocationOnScreen = new int[2];
    	    topLeftView.getLocationOnScreen(topLeftLocationOnScreen);
     
//    	    System.out.println("topLeftY="+topLeftLocationOnScreen[1]);
//    	    System.out.println("originalY="+originalYPos);
    	    
    	    float x = event.getRawX();
    	    float y = event.getRawY();
     
    	    WindowManager.LayoutParams params = (LayoutParams) asukaChibi.getLayoutParams();
     
    	    int newX = (int) (offsetX + x);
    	    int newY = (int) (offsetY + y);
     
    	    if (Math.abs(newX - originalXPos) < 1 && Math.abs(newY - originalYPos) < 1 && !moving) {
    		return false;
    	    }
     
    	    params.x = newX - (topLeftLocationOnScreen[0]);
    	    params.y = newY - (topLeftLocationOnScreen[1]);
     
    	    wm.updateViewLayout(asukaChibi, params);
    	    moving = true;
    	} else if (event.getAction() == MotionEvent.ACTION_UP) {
    		//asuka.asuka.setState(ATField.SPRITE_RUN);
    		//AIAction();
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
    
    
    
    public void moveAsuka(){
    	WindowManager.LayoutParams params = (LayoutParams) asukaChibi.getLayoutParams();
    	int[] location = new int[2];
	    asukaChibi.getLocationOnScreen(location);
	    originalXPos = location[0];
	    originalYPos = location[1];
	    Log.i(TAG,"orgXPos & orgYPos = "+  originalXPos+":"+originalYPos+":"+dm.heightPixels);
	    offsetX = originalXPos;
	    offsetY = originalYPos;
	    int newX = (int) (offsetX);
	    int newY = (int) (offsetY);
//	    Log.i(TAG,"newX & newY = "+ newX+":"+newY);
	    params.x = originalXPos + 1;
//	    params.y = originalYPos - 10;
//	    params.x = (int)asukaChibi.getX() + 10;
//	    params.y = (int)asukaChibi.getY();
	    WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
	    wm.updateViewLayout(asukaChibi, params);
	    
    }
    
    public class AIThread extends Thread{
    	
    	static final long FPS = 1;
    	private boolean running = false;
    	private MySurfaceView sv;	
    	
    	public void setRunning(boolean run) {
            running = run;
    	}
    	
    	@Override
    	  public void run() {
    		  long ticksPS = 1000 / FPS;
    	      long startTime;
    	      long sleepTime;
    	      while (running) {
    	          Canvas c = null;
    	          
    	          //AIAction();
    	          
    	          startTime = System.currentTimeMillis();
    	          sleepTime = ticksPS-(System.currentTimeMillis() - startTime);
    	          try {
    	                 if (sleepTime > 0)
    	                        sleep(sleepTime);
    	                 else
    	                        sleep(10);
    	          } catch (Exception e) {}
    	   }
    	  }	

    }
    
    @SuppressLint("WrongCall")
	public class AnimateME extends MySurfaceView {
    	private MySurfaceView me;
    	private SurfaceHolder holder;	
    	private Paint paint = new Paint();
    	private AnimateThread animatorThread;
    	public AsukaLangley asuka;
    	
    	public AnimateME(Context context) {
    		super(context);
    		me = this;
    		animatorThread = new AnimateThread(this);
    		holder = this.getHolder();
    		holder.addCallback(leCallback());
//    		this.setZOrderOnTop(true);
//    		this.getHolder().setFormat(PixelFormat.TRANSPARENT);
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
                	
//                	createSprite();
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
    		asuka = new AsukaLangley(me);
    		asuka.gravity = true;
    	}
    	
    }
    
    @SuppressLint("WrongCall")
	public class AnimateThread extends Thread{
    	
    	static final long FPS = 10;
    	private boolean running = false;
    	private MySurfaceView sv;	
    	
    	public AnimateThread(MySurfaceView	leSV){
    		sv = leSV;
    	}
    	
    	public void setRunning(boolean run) {
            running = run;
    	}
    	
    	@Override
    	  public void run() {
    		  long ticksPS = 1000 / FPS;
    	      long startTime;
    	      long sleepTime;
    	      while (running) {
    	          Canvas c = null;
    	          startTime = System.currentTimeMillis();
    	          try {
//    	                 c = tg.getHolder().lockCanvas();
    	                 c = sv.getHolder().lockCanvas();
    	                 synchronized (sv.getHolder()) {
    	                        sv.onDraw(c);
    	                 }
    	          } finally {
    	                 if (c != null) {
    	                        sv.getHolder().unlockCanvasAndPost(c);
    	                 }
    	          }
    	          sleepTime = ticksPS-(System.currentTimeMillis() - startTime);
    	          try {
    	                 if (sleepTime > 0)
    	                        sleep(sleepTime);
    	                 else
    	                        sleep(10);
    	          } catch (Exception e) {}
    	   }
    	  }	

    }
    
    public class AsukaLangley {
    	
    	private String TAG = "AsukaChan";
    	
    	private List<Coordinates> walkSprite = new ArrayList<Coordinates>();
    	private List<Coordinates> runSprite = new ArrayList<Coordinates>();
    	private List<Coordinates> standbySprite = new ArrayList<Coordinates>();
    	private List<Coordinates> jumpSprite = new ArrayList<Coordinates>();
    	private List<Coordinates> angrySprite = new ArrayList<Coordinates>();
    	private List<Coordinates> crySprite = new ArrayList<Coordinates>();
    	private List<Coordinates> pointSprite = new ArrayList<Coordinates>();
    	private List<Coordinates> loveSprite = new ArrayList<Coordinates>();
//    	private List<Coordinates> sorrySprite = new ArrayList<Coordinates>();
    	private List<Coordinates> punchSprite = new ArrayList<Coordinates>();
    	private List<Coordinates> confuseSprite = new ArrayList<Coordinates>();
    	private List<Coordinates> happySprite = new ArrayList<Coordinates>();
    	private Coordinates coords;
    	
    	public  int STAND = ATField.SPRITE_STAND;
    	public  int WALK = ATField.SPRITE_WALK;
    	public  int RUN = ATField.SPRITE_RUN;
    	public  int JUMP = ATField.SPRITE_JUMP;
    	public  int DEAD = ATField.SPRITE_DEAD;
    	public  int ANGRY = ATField.SPRITE_ANGRY;
    	public  int CRY = ATField.SPRITE_CRY;
    	public  int POINT = ATField.SPRITE_POINT;
    	public  int ATTACK = ATField.SPRITE_ATTACK;
    	public  int HIT = ATField.SPRITE_HIT;
    	public  int HAPPY = ATField.SPRITE_HAPPY;
    	public  int LOVE = ATField.SPRITE_LOVE;
    	
    	private int state = WALK;
    	
    	private Rect spriteImg;
    	private Rect actualImg;
    	private int x;
    	private int y;
    	public int width = asukaWidth;
    	public int height = asukaHeight;
    	private int spriteX = 0;
    	
    	public int ySpeed = 0;
    	public int bottom = 0;
    	
    	public boolean gravity = false;
    	
    	private SurfaceView sv;
    	
    	private int direction = ATField.SPRITE_DIRECTION_RIGHT;
    	
    	public Bitmap asukaLangley;
    	private Bitmap asukanormal;
    	private Bitmap asukamirror;
    	Matrix matrix = new Matrix();
    	int spriteDuration = 10;
    	
    	public AsukaLangley(SurfaceView sv){
    		this.sv = sv;
    		bottom = height;
    		x = 10;
    		createasuka();
    		
    	}
    	
    	public void onDraw(Canvas canvas) {
    		 
    		 move();
    		 //Bitmap.
    		 canvas.drawBitmap(asukaLangley, spriteImg, actualImg, null);
    		 
    	}
    	
    	public void move(){
//    		if(asuka == null)
//    			asuka = asukanormal;
    		switch(state){
    			case ATField.SPRITE_STAND:
    				spriteX = 0;
    				coords = standbySprite.get(spriteX);
    				spriteImg = new Rect(coords.left,coords.top,coords.right,coords.bottom);
    				actualImg = new Rect(x, y, x + width, y + height);
    				break;
    			
    		
    			case ATField.SPRITE_WALK:
    				if(spriteX >walkSprite.size() - 1){
    					spriteX = 0;
    					AIAction();
    				}
    				
    				coords = walkSprite.get(spriteX);
    				spriteImg = new Rect(coords.left,coords.top,coords.right,coords.bottom);
    				actualImg = new Rect(x, y, x + width, y + height);
    				spriteX++;
    				//moveAsuka();
    				
    				
    			break;
    			
    			case ATField.SPRITE_RUN:
    				
    				if(spriteX >runSprite.size() - 1){
    					spriteX = 0;
    					AIAction();
    				}
    				coords = runSprite.get(spriteX);
    				spriteImg = new Rect(coords.left,coords.top,coords.right,coords.bottom);
    				actualImg = new Rect(x, y, x + width, y + height);
    				spriteX++;
    				
    				break;
    			
    			case ATField.SPRITE_JUMP:
    				if(spriteX >5){
    					AIAction();
    				}
    				coords = jumpSprite.get(0);
    				spriteImg = new Rect(coords.left,coords.top,coords.right,coords.bottom);
    				actualImg = new Rect(x, y, x + width, y + height);
    				spriteX++;
    				break;
    		}
    	}
    	
    	private void createasuka(){
    		asukaLangley = BitmapFactory.decodeResource(Main.context.getResources(), R.drawable.asukachanmirror);
    		asukamirror = BitmapFactory.decodeResource(Main.context.getResources(), R.drawable.asukachan);
    		
    		
    		setStand();
    		setWalk();
    		setJump();
    		setRun();
    		setAngry();
    		setCry();
    		setPoint();
    		setLove();
    		setPunch();
    		setConfuse();
    		setHappy();
    		
//    		asuka = Bitmap.createBitmap(BitmapFactory.decodeResource(MainActivity.context.getResources(), R.drawable.asuka),0,0,251,153);
//    		Log.i(TAG,me.getWidth()+"");
//    		me = Bitmap.createBitmap(me, 0,0,340,180);
//    		Log.i(TAG,me.getWidth()+"");
    	}
    	
    	private void setStand(){
    		standbySprite.add(new Coordinates(0,0,66,130));
    	}
    	
    	private void setWalk(){
    		walkSprite.add(new Coordinates(66,0,132,130));
    		walkSprite.add(new Coordinates(132,0,198,130));
    		//walkSprite.add(new Coordinates(198,0,264,130));
    	}
    	
    	private void setRun(){
    		runSprite.add(new Coordinates(198,0,264,130));
    		runSprite.add(new Coordinates(264,0,330,130));
    	}
    	
    	private void setJump(){
    		jumpSprite.add(new Coordinates(330,0,396,130));
    	}
    	
    	private void setAngry(){
    		angrySprite.add(new Coordinates(396,0,462,130));
    		angrySprite.add(new Coordinates(462,0,528,130));
    	}
    	
    	private void setCry(){
    		crySprite.add(new Coordinates(528,0,594,130));
    		crySprite.add(new Coordinates(594,0,660,130));
    	}
    	
    	private void setPoint(){
    		pointSprite.add(new Coordinates(660,0,726,130));
    	}
    	
    	private void setLove(){
    		loveSprite.add(new Coordinates(726,0,792,130));
    		loveSprite.add(new Coordinates(792,0,858,130));
    	}
    	
    	private void setPunch(){
    		punchSprite.add(new Coordinates(858,0,924,130));
    	}
    	
    	private void setConfuse(){
    		confuseSprite.add(new Coordinates(924,0,990,130));
    	}
    	
    	private void setHappy(){
    		happySprite.add(new Coordinates(990,0,1056,130));
    	}
    	
    	private void AIAction(){
        	int ai = new Random().nextInt(3);
        	
        	switch(ai){
        	
        	case 0:
        		break;
        	
        	case 1:
        		//WALK
        		state = WALK;
        		break;
        		
        	case 2:
        		//RUN
        		state = RUN;
        		break;
        		
        	case 3:
        		//JUMP
        		state = JUMP;
        		break;
        		
        	case 4:
        		//ANGRY
        		state = ANGRY;
        		break;	
        	
        	case 5:
        		//CRY
        		state = CRY;
        		break;	
        	
        	case 6:
        		//POINT
        		state = POINT;
        		break;
        	
        	case 7:
        		//LOVE
        		state = LOVE;
        		break;	
        	
        	case 8:
        		//JUMP
        		state = ATTACK;
        		break;	
        		
        	case 9:
        		//CONFUSE
        		state = HIT;
        		break;		
        	
        	case 10:
        		//HAPPY
        		state = HAPPY;
        		break;	
        		
        	default:
        		state = STAND;
        		break;
        	}
        	
        }
    	
    	public class Coordinates{
    		
    		public int left = 0;
    		public int top = 0;
    		public int right = 0;
    		public int bottom = 0;
    		
    		public Coordinates(int left, int top, int right, int bottom){
    			this.left = left;
    			this.top = top;
    			this.right = right;
    			this.bottom = bottom;
    		}
    		
    	}
    	
    	public int getY(){
    		return y;
    	}
    	
    	public void setY(int leY){
    		y = leY;
    	}
    	
    	public int getX(){
    		return x;
    	}
    	
    	public void setX(int leX){
    		x = leX;
    	}
    	
    	public int getState(){
    		return state;
    	}
    	
    	public void setState(int st){
    		state = st;
    	}
    	
    	public int getDirection(){
    		return direction;
    	}
    	
    	public void setDirection(int st){
//    		state = st;
    		spriteX = 0;
    		direction = st;
//    		switch(direction){
//    			case ATField.USER_SWIPE_E:
    //
    //
//    			break;
//    			
//    			case ATField.USER_SWIPE_W:
    //
    //
//    			break;
    //	
//    		}
    	}
    	
    	private Bitmap flipImage(Bitmap src, int type){
    		
    		if(type == ATField.FLIP_VERTICAL){
    			matrix.preScale(1.0f, -1.0f);
    		}
    		else if(type == ATField.FLIP_HORIZONTAL){
    			matrix.preScale(-1.0f, 1.0f);
    		}
    		else 
    			return null;
    		
    		
    		return Bitmap.createBitmap(src, 0, 0,
    				src.getWidth(), src.getHeight(),
    				matrix, true);
    	}

    }
 
}

