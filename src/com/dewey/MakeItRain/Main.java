package com.dewey.MakeItRain;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

public class Main extends Activity {
    Context thisContext = this;
    ImageView movingImage;
	class GraphicObject{
		private Bitmap _image;
		private Coordinates _coor;
		private Speed _spd;
		public GraphicObject(Bitmap bitmap) {
			 _image = bitmap;
			 _coor = new Coordinates();
			 _spd = new Speed();
			 
		}
		 
		public Speed getSpeed()
		{
			return _spd;
		}
	    public Bitmap getGraphic() {
	        return _image;
	    }
	 
	    public Coordinates getCoordinates() {
	        return _coor;
	    }
	 
		//inner class
		 /**
	     * Contains the coordinates of the graphic.
	     */
	    public class Coordinates {
	        private int _x = 100;
	        private int _y = 0;
	 
	        public int getX() {
	            return _x + _image.getWidth() / 2;
	        }
	 
	        public void setX(int value) {
	            _x = value - _image.getWidth() / 2;
	        }
	 
	        public int getY() {
	            return _y + _image.getHeight() / 2;
	        }
	 
	        public void setY(int value) {
	            _y = value - _image.getHeight() / 2;
	        }
	 
	        public String toString() {
	            return "Coordinates: (" + _x + "/" + _y + ")";
	        }
	    }
	    public class Speed{
			 public static final int X_DIRECTION_RIGHT = 1;
			 public static final int X_DIRECTION_LEFT = -1;
			 public static final int Y_DIRECTION_DOWN = 1;
			 public static final int Y_DIRECTION_UP = -1;
			 
			 private int _x = 1;
			 private int _y = 1;	
			 
		    private int _xDirection = X_DIRECTION_RIGHT;
		    private int _yDirection = Y_DIRECTION_DOWN;
		    
		    /**
		     * @return the _xDirection
		     */
		    public int getXDirection() {
		        return _xDirection;
		    }
		 
		    /**
		     * @param direction the _xDirection to set
		     */
		    public void setXDirection(int direction) {
		        _xDirection = direction;
		    }
		    /**
		     * @return the _yDirection
		     */
		    public int getYDirection() {
		        return _yDirection;
		    }
		 
		    /**
		     * @param direction the _yyDirection to set
		     */
		    public void setYDirection(int direction) {
		        _yDirection = direction;
		    }
		    
		    public void toggleXDirection() {
		        if (_xDirection == X_DIRECTION_RIGHT) {
		            _xDirection = X_DIRECTION_LEFT;
		        } else {
		            _xDirection = X_DIRECTION_RIGHT;
		        }
		    }
		    public void toggleYDirection()
		    {
		    	if (_yDirection == Y_DIRECTION_DOWN){
		    		_yDirection = Y_DIRECTION_UP;
		    	}
		    	else
		    	{
		    		_yDirection = Y_DIRECTION_DOWN;
		    	}
		    }
		    /**
		     * @return the _x
		     */
		    public int getX() {
		        return _x;
		    }
		 
		    /**
		     * @param speed the _x to set
		     */
		    public void setX(int speed) {
		        _x = speed;
		    }

		    
		    /**
		     * @return the _y
		     */
		    public int getY() {
		        return _y;
		    }
		 
		    /**
		     * @param speed the _y to set
		     */
		    public void setY(int speed) {
		        _y = speed;
		    }
		 
		    public String toString() {
		        String xDirection;
		        if (_xDirection == X_DIRECTION_RIGHT) {
		            xDirection = "right";
		        } else {
		            xDirection = "left";
		        }
		        return "Speed: x: " + _x + " | y: " + _y + " | xDirection: " + xDirection;
		    }
		}
	}
	class Panel extends SurfaceView implements SurfaceHolder.Callback
	{
		private myThread _thread;
		private ArrayList<GraphicObject> _graphics = new ArrayList<GraphicObject>();
		private GraphicObject _currentGraphic = null;
		private GraphicObject _blackedGraphic = null;
		
		public Panel(Context context) {
			super(context);
			getHolder().addCallback(this);
			_thread = new myThread(getHolder(), this);
			setFocusable(true);
		}
		public void onDraw(Canvas canvas){
			Bitmap _image = BitmapFactory.decodeResource(getResources(), R.drawable.dollar);
			canvas.drawColor(Color.GREEN);
			if (_blackedGraphic != null)
			{
				canvas.drawBitmap(_blackedGraphic.getGraphic(), _blackedGraphic.getCoordinates().getX(), _blackedGraphic.getCoordinates().getY(), null);
			}
			for ( GraphicObject gr : _graphics){
				canvas.drawBitmap(gr.getGraphic(), gr.getCoordinates().getX(), gr.getCoordinates().getY(), null);
			}
			
			
		}
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			// TODO Auto-generated method stub
			
		}
		
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			if (!_thread.isRunning())
			{
				_thread.setRunning(true);
				_thread.start();
			}
			
		}
		
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			boolean retry = true;
			_thread.setRunning(false);
			while (retry)
			{
				try {
					_thread.join();
					retry = false;
				}catch (InterruptedException e){
					
				}
			}
		}
		
		//varibles for speed
		 int downX = 0 ;
		 int downY = 0 ;
		 int upX = 0 ;
		 int upY = 0 ; 
		@Override
		public boolean onTouchEvent(MotionEvent e) {
			 synchronized (_thread.getSurfaceHolder()) {
				 (new Toast(thisContext).makeText(thisContext, "Clicked!", Toast.LENGTH_LONG)).show();
				 if (e.getAction() == MotionEvent.ACTION_DOWN)
				 {
					 GraphicObject gr = new GraphicObject(BitmapFactory.decodeResource(getResources(),R.drawable.dollar));
					 
					 gr.getCoordinates().setX((int) e.getX() - gr.getGraphic().getWidth()/2);
					 gr.getCoordinates().setY((int) e.getY() - gr.getGraphic().getHeight()/2);
					 downX = (int)e.getX();
					 downY = (int)e.getY();
					 _graphics.add(gr);
					 _currentGraphic = gr;
				     _blackedGraphic = new GraphicObject(BitmapFactory.decodeResource(getResources(), R.drawable.dollar));
				     _blackedGraphic.getCoordinates().setX((int) e.getX() - gr.getGraphic().getWidth()/2);
				     _blackedGraphic.getCoordinates().setY((int) e.getY() - gr.getGraphic().getHeight()/2);
				     _blackedGraphic.getSpeed().setX(0);
				     _blackedGraphic.getSpeed().setY(0);
				     _currentGraphic.getSpeed().setX(0);
					 _currentGraphic.getSpeed().setY(0);
				 }
				 else if (e.getAction() == MotionEvent.ACTION_MOVE)
				 {					 
					  _currentGraphic.getCoordinates().setX((int) e.getX() - _currentGraphic.getGraphic().getWidth() / 2);
			          _currentGraphic.getCoordinates().setY((int) e.getY() - _currentGraphic.getGraphic().getHeight() / 2);
				 }
				 else if (e.getAction() == MotionEvent.ACTION_UP)
				 {
					 upX = (int)e.getX();
					 upY = (int)e.getY();
					 //get speed of swipe action
					 int deltaX = downX - upX;
					 int deltaY = downY - upY; 
					 if (deltaX >= 0)
					 {
						 _currentGraphic.getSpeed().setXDirection(GraphicObject.Speed.X_DIRECTION_LEFT);
					 }
					 else{
						 _currentGraphic.getSpeed().setXDirection(GraphicObject.Speed.X_DIRECTION_RIGHT);
					 }
					 if (deltaY >= 0)
					 {
						 _currentGraphic.getSpeed().setYDirection(GraphicObject.Speed.Y_DIRECTION_UP);
					 }
					 else{
						 _currentGraphic.getSpeed().setYDirection(GraphicObject.Speed.Y_DIRECTION_DOWN);
					 }
					 
						 
					 _currentGraphic.getSpeed().setX((int)Math.sqrt(Math.abs((double)deltaX)));
					 _currentGraphic.getSpeed().setY((int)Math.sqrt(Math.abs((double)deltaY)));
					 
					 _graphics.add(_currentGraphic);
					 _blackedGraphic = null;
					 
				 }
				 return true;
				 }
		}
		public void updatePhysics(){
			GraphicObject.Coordinates coord;
			GraphicObject.Speed spd;
			List<GraphicObject> grsToRemove = new ArrayList<GraphicObject>();
			for (GraphicObject gr : _graphics)
			{
				coord = gr.getCoordinates();
				spd = gr.getSpeed();
				
				//gives kangaroo direction
				if(spd.getXDirection() == GraphicObject.Speed.X_DIRECTION_RIGHT){
					coord.setX(coord.getX() + spd.getX());
				}
				else{
					coord.setX(coord.getX() - spd.getX());
				}
			    if (spd.getYDirection() == GraphicObject.Speed.Y_DIRECTION_DOWN) {
		            coord.setY(coord.getY() + spd.getY());                
		        } else {
		            coord.setY(coord.getY() - spd.getY());                
		        }
			    //border checking x
			    if (coord.getX() < 0 - gr.getGraphic().getWidth()) {
		            grsToRemove.add(gr);
		        } else if (coord.getX() - gr.getGraphic().getWidth() > getWidth()) {
		        	grsToRemove.add(gr);
		        }
			    //border checking y
			    if (coord.getY() < 0 - gr.getGraphic().getHeight()) {
			    	grsToRemove.add(gr);
		        } else if (coord.getY() - gr.getGraphic().getHeight() > getHeight()) {
		        	grsToRemove.add(gr);
		        }
			}
			//clean up off screen things
			for( GraphicObject gr : grsToRemove)
			{
				_graphics.remove(gr);
			}
						
		}
	}
    
	class myThread extends Thread
	{
		private SurfaceHolder _sh;
		private Panel _panel;
		private boolean _run= false;
		
		public myThread(SurfaceHolder sh, Panel panel){
			_sh = sh;
			_panel = panel;
		}
		public SurfaceHolder getSurfaceHolder()
		{
			return _sh;
		}
		public boolean isRunning()
		{
			return _run;
		}
		public void setRunning(boolean run)
		{
			_run = run;
		}
		@SuppressLint("WrongCall")
		@Override
		public void run()
		{Canvas c;
	    while (_run) {
	        c = null;
	        try {
	            c = _sh.lockCanvas(null);
	            synchronized (_sh) {
	                _panel.updatePhysics();
	            	_panel.onDraw(c);
	                
	            }
	        } finally {
	            // do this in a finally so that if an exception is thrown
	            // during the above, we don't leave the Surface in an
	            // inconsistent state
	            if (c != null) {
	            	_sh.unlockCanvasAndPost(c);
	            }
	        }
	    }}
		
	}
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	 super.onCreate(savedInstanceState);
         requestWindowFeature(Window.FEATURE_NO_TITLE);
         setContentView(new Panel(this));
    }
}