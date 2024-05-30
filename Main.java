import processing.core.PApplet;

/**
 * Main class to execute sketch
 * @author A. Razack
 *
 */
class Main {
  public static void main(String[] args) {
    
    String[] processingArgs = {"MySketch"};
	  Sketch mySketch = new Sketch();
	  
	  PApplet.runSketch(processingArgs, mySketch);

	  //Check to see if sketch is finished running
	  while (!Sketch.isFinished) {
	    try {
		  Thread.sleep(100);
		} catch (InterruptedException e) {
		  e.printStackTrace();
		}
  	  }

	  // Run Sketch 1 after sketch is done 
	  String[] sketch1Args = {"Sketch1"};
	  Sketch1 sketch1 = new Sketch1();
	  PApplet.runSketch(sketch1Args, sketch1);
  }
}
