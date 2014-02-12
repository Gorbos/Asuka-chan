package eva02.thebeast.asuka_chan.draw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public class Item extends View {
	
	public Item(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	private Bitmap itemBitmap;
	private int itemSpriteX;
	private int itemSpriteY;
	private int itemCanvasX;
	private int itemCanvasY;
	
	public Item item(){
		
		return null;
	}
	
	public void item(int drawableID, int itemCanvasX, int itemCanvasY){
		
		this.itemBitmap = BitmapFactory.decodeResource(getResources(), drawableID);
		this.itemCanvasX = itemCanvasX;
		this.itemCanvasY = itemCanvasY;
		
	}
	
	 @Override
	 synchronized public void onDraw(Canvas canvas) {
		 canvas.drawBitmap(itemBitmap, itemCanvasX, itemCanvasY, null);
	 }


}
