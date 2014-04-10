package lila.raw.skills.asuka;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.view.SurfaceView;

@SuppressLint("WrongCall")
public class AIThread extends Thread{
	
	static final long FPS = 10;
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
	          startTime = System.currentTimeMillis();
	          try {
//	                 c = tg.getHolder().lockCanvas();
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
