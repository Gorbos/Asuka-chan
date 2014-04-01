package lila.raw.skills.asuka;

import java.util.ArrayList;
import java.util.List;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.Toast;

public class AsukaChan {
	
	private String TAG = "Allouete";
	
	private List<Coordinates> walkSprite = new ArrayList<Coordinates>();
	private List<Coordinates> runSprite = new ArrayList<Coordinates>();
	private List<Coordinates> standbySprite = new ArrayList<Coordinates>();
	private List<Coordinates> jumpSprite = new ArrayList<Coordinates>();
	private Coordinates coords;
	
	public  int STAND = ATField.SPRITE_STAND;
	public  int WALK = ATField.SPRITE_WALK;
	public  int RUN = ATField.SPRITE_RUN;
	public  int JUMP = ATField.SPRITE_JUMP;
	public  int DEAD = ATField.SPRITE_DEAD;
	private int state = STAND;
	
	private Rect spriteImg;
	private Rect actualImg;
	private int x;
	private int y;
	public int width = 66;
	public int height = 130;
	private int spriteX = 0;
	
	public int ySpeed = 0;
	public int bottom = 0;
	
	public boolean gravity = false;
	
	private SurfaceView sv;
	
	private int direction = ATField.SPRITE_DIRECTION_RIGHT;
	
	public Bitmap asuka;
	private Bitmap asukanormal;
	private Bitmap asukamirror;
	Matrix matrix = new Matrix();
	
	public AsukaChan(SurfaceView sv){
		this.sv = sv;
		bottom = height;
		x = 10;
		createasuka();
	}
	
	public void onDraw(Canvas canvas) {
		 
		 move();
		 canvas.drawBitmap(asuka, spriteImg, actualImg, null);
	}
	
	public void move(){
		if(asuka == null)
			asuka = asukanormal;
		switch(state){
			case ATField.SPRITE_STAND:
				spriteX = 0;
				coords = standbySprite.get(spriteX);
				spriteImg = new Rect(coords.left,coords.top,coords.right,coords.bottom);
				actualImg = new Rect(x, y, x + width, y + height);
				break;
			
		
			case ATField.SPRITE_WALK:
				spriteX++;
				if(spriteX >1){
					spriteX = 0;
				}
				
				coords = walkSprite.get(spriteX);
				spriteImg = new Rect(coords.left,coords.top,coords.right,coords.bottom);
				actualImg = new Rect(x, y, x + width, y + height);
			break;
			
			case ATField.SPRITE_RUN:
				spriteX++;
				if(spriteX >walkSprite.size()){
					spriteX = 3;
				}
				coords = standbySprite.get(spriteX);
				spriteImg = new Rect(coords.left,coords.top,coords.right,coords.bottom);
				actualImg = new Rect(x, y, x + width, y + height);
				break;
			
		}
	}
	
	private void createasuka(){
		asukamirror = BitmapFactory.decodeResource(Main.context.getResources(), R.drawable.asukachan);
		asukanormal = BitmapFactory.decodeResource(Main.context.getResources(), R.drawable.asukachan);
		setStand();
		setWalk();
//		asuka = Bitmap.createBitmap(BitmapFactory.decodeResource(MainActivity.context.getResources(), R.drawable.asuka),0,0,251,153);
//		Log.i(TAG,me.getWidth()+"");
//		me = Bitmap.createBitmap(me, 0,0,340,180);
//		Log.i(TAG,me.getWidth()+"");
	}
	
	private void setStand(){
		standbySprite.add(new Coordinates(0,0,66,130));
	}
	
	private void setWalk(){
		walkSprite.add(new Coordinates(66,0,132,130));
		walkSprite.add(new Coordinates(132,0,198,130));
	}
	
	private void setRun(){
		runSprite.add(new Coordinates(198,0,264,130));
		runSprite.add(new Coordinates(264,0,330,130));
	}
	
	private void setJump(){
		jumpSprite.add(new Coordinates(330,0,396,130));;
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
//		state = st;
		direction = st;
//		switch(direction){
//			case ATField.USER_SWIPE_E:
//
//
//			break;
//			
//			case ATField.USER_SWIPE_W:
//
//
//			break;
//	
//		}
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
	