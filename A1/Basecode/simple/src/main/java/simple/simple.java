package simple;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CyclicBarrier;

import javax.swing.JFrame;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import jrtr.GLRenderPanel;
import jrtr.Material;
import jrtr.RenderContext;
import jrtr.RenderPanel;
import jrtr.SWRenderPanel;
import jrtr.Shader;
import jrtr.Shape;
import jrtr.SimpleSceneManager;
import jrtr.VertexData;

/**
 * Implements a simple application that opens a 3D rendering window and shows a
 * rotating cube.
 */
public class simple {
	static boolean renderCylinder = false;
	static boolean renderTorus = false;
	static RenderPanel renderPanel;
	static RenderContext renderContext;
	static Shader normalShader;
	static Shader diffuseShader;
	static Material material;
	static SimpleSceneManager sceneManager;
	
	static Shape cylinder; 
	private static Matrix4f initialCylinderMatrix;
	static Shape torus; 
	private static Matrix4f initialTorusMatrix;
	
	static Shape	body,
					tail,
					rotorConnection,
					rotor,
					blade1,
					blade2,
					backwing;
	static float currentstep, basicstep;
	private static float phi = 0.01f;
	private static Matrix4f initialBodyMatrix,
							initialTailMatrix,
							initialRotorConnectionMatrix,
							initialRotorMatrix,
							initialBlade1Matrix,
							initialBlade2Matrix,
							initialBackwingMatrix;

	/**
	 * An extension of {@link GLRenderPanel} or {@link SWRenderPanel} to provide
	 * a call-back function for initialization. Here we construct a simple 3D
	 * scene and start a timer task to generate an animation.
	 */
	public final static class SimpleRenderPanel extends GLRenderPanel {


		/**
		 * Initialization call-back. We initialize our renderer here.
		 * 
		 * @param r
		 *            the render context that is associated with this render
		 *            panel
		 */
		public void init(RenderContext r) {
			renderContext = r;
			phi = 0.01f;

			// Make a scene manager and add the object
			sceneManager = new SimpleSceneManager();
			
			if (renderCylinder) {
				Cylinder c = new Cylinder(1, 1, 50);
				VertexData cylinderData = c.getVertexData(renderContext);
				cylinder = new Shape(cylinderData);
				initialCylinderMatrix = cylinder.getTransformation();
				sceneManager.addShape(cylinder);
			} else if (renderTorus) {
				Torus t = new Torus(2, 1, 44);
				VertexData torusData = t.getVertexData(renderContext);
				torus = new Shape(torusData);
				initialTorusMatrix = torus.getTransformation();
				sceneManager.addShape(torus);
			} else {
				
				Chopper chopper = new Chopper();
				VertexData bodyData = chopper.getBodyVertexData(renderContext);
				VertexData tailData = chopper.getTailVertexData(renderContext);
				VertexData rotorConnectionData = chopper.getRotorConnectionVertexData(renderContext);
				VertexData rotorData = chopper.getRotorVertexData(renderContext);
				VertexData bladeData = chopper.getRotorBladeVertexData(renderContext);
				VertexData backwingData = chopper.getBackwingVertexData(renderContext);
				
				body = new Shape(bodyData);
				tail = new Shape(tailData);
				rotorConnection = new Shape(rotorConnectionData);
				rotor = new Shape(rotorData);
				blade1 = new Shape(bladeData);
				blade2 = new Shape(bladeData);
				backwing = new Shape(backwingData);
				
				initialBodyMatrix = body.getTransformation();
				initialTailMatrix = tail.getTransformation();
				initialRotorConnectionMatrix = rotorConnection.getTransformation();
				initialRotorMatrix = rotor.getTransformation();
				initialBlade1Matrix = blade1.getTransformation();
				initialBlade2Matrix = blade2.getTransformation();
				initialBackwingMatrix = backwing.getTransformation();
				
				sceneManager.addShape(body);
				sceneManager.addShape(tail);
				sceneManager.addShape(rotorConnection);
				sceneManager.addShape(rotor);
				sceneManager.addShape(blade1);
				sceneManager.addShape(blade2);
				sceneManager.addShape(backwing);

			}
			// Add the scene to the renderer
			renderContext.setSceneManager(sceneManager);

			// Load some more shaders
			normalShader = renderContext.makeShader();
			try {
				normalShader.load("../jrtr/shaders/normal.vert",
						"../jrtr/shaders/normal.frag");
			} catch (Exception e) {
				System.out.print("Problem with shader:\n");
				System.out.print(e.getMessage());
			}

			diffuseShader = renderContext.makeShader();
			try {
				diffuseShader.load("../jrtr/shaders/diffuse.vert",
						"../jrtr/shaders/diffuse.frag");
			} catch (Exception e) {
				System.out.print("Problem with shader:\n");
				System.out.print(e.getMessage());
			}

			// Make a material that can be used for shading
			material = new Material();
			material.shader = diffuseShader;
			material.texture = renderContext.makeTexture();
			try {
				material.texture.load("../textures/plant.jpg");
			} catch (Exception e) {
				System.out.print("Could not load texture.\n");
				System.out.print(e.getMessage());
			}

			// Register a timer task
			Timer timer = new Timer();
			basicstep = 0.01f;
			currentstep = basicstep;
			timer.scheduleAtFixedRate(new AnimationTask(), 0, 10);
		}
	}

	/**
	 * A timer task that generates an animation. This task triggers the
	 * redrawing of the 3D scene every time it is executed.
	 */
	public static class AnimationTask extends TimerTask {
		
		public void run() {
			
			if (renderCylinder) {
				animateCylinder();	
			} else if (renderTorus) {
				animateTorus();
			} else {
				animateChopper();	
			}
			
			// Trigger redrawing of the render window
			renderPanel.getCanvas().repaint();
		}

		private void animateTorus() {
			Matrix4f torusMatrix = new Matrix4f(initialTorusMatrix);
			
			Matrix4f rotX45 = new Matrix4f();
			Matrix4f rotY45 = new Matrix4f();
			rotX45.rotX(phi*(float) (Math.PI/4));
			rotY45.rotY(phi*(float) (Math.PI/4));
			
			torusMatrix.mul(rotX45);
			torusMatrix.mul(rotY45);
			
			torus.setTransformation(torusMatrix);
			phi += 0.01f;
			
		}

		private void animateCylinder() {
			Matrix4f cylinderMatrix = new Matrix4f(initialCylinderMatrix);
			
			Matrix4f rotX45 = new Matrix4f();
			Matrix4f rotY45 = new Matrix4f();
			rotX45.rotX(phi*(float) (Math.PI/4));
			rotY45.rotY(phi*(float) (Math.PI/4));
			
			cylinderMatrix.mul(rotX45);
			cylinderMatrix.mul(rotY45);
			
			cylinder.setTransformation(cylinderMatrix);
			phi += 0.01f;
		}

		private void animateChopper() {
			Matrix4f bodyMatrix = new Matrix4f(initialBodyMatrix);
			Matrix4f tailMatrix = new Matrix4f(initialTailMatrix);
			Matrix4f rotorConnectionMatrix = new Matrix4f(initialRotorConnectionMatrix);
			Matrix4f rotorMatrix = new Matrix4f(initialRotorMatrix);
			Matrix4f blade1Matrix = new Matrix4f(initialBlade1Matrix);
			Matrix4f blade2Matrix = new Matrix4f(initialBlade2Matrix);
			Matrix4f backwingMatrix = new Matrix4f(initialBackwingMatrix);
			
			
			// change perspective, create orbit
			Matrix4f perspective = new Matrix4f();
			perspective.rotX((float) -Math.PI/4);
			
			// rotation matrix for basic ring movement
			Matrix4f rotation = new Matrix4f();
			rotation.rotZ(phi);
			
			// create translation for moving in a ring shape
			Matrix4f translation = new Matrix4f();
			translation.setIdentity();
			translation.setTranslation(new Vector3f(3f, 0f, 0f));
			
			// shaping the body
			Matrix4f scaleBody = new Matrix4f();
			scaleBody.setRow(0, 0.5f, 0, 0, 0);
			scaleBody.setRow(1, 0, 2, -0.5f, 0);
			scaleBody.setRow(2, 0, 0, 1, 0);
			scaleBody.setRow(3, 0, 0, 0, 1);
			
			// connect tail with rear body, move up a bit
			Matrix4f tailPosition = new Matrix4f();
			tailPosition.setIdentity();
			tailPosition.setTranslation(new Vector3f(0f, -6f, 0.5f));
			
			Matrix4f scaleTail = new Matrix4f();
			scaleTail.setRow(0, 0.2f, 0, 0, 0);
			scaleTail.setRow(1, 0, 4, -0.5f, 0);
			scaleTail.setRow(2, 0, 0, 0.4f, 0);
			scaleTail.setRow(3, 0, 0, 0, 1);
			
			// rotates the cylinder upright
			Matrix4f rotorConnectionRotation = new Matrix4f();
			rotorConnectionRotation.rotX((float) (Math.PI/2));
			
			// positions the rotorConnection on top of the body 
			Matrix4f rotorConnectionPosition = new Matrix4f();
			rotorConnectionPosition.setIdentity();
			rotorConnectionPosition.setTranslation(new Vector3f(0f, 0f, 1f));
			
			// used for rotating the second rotor blade
			Matrix4f rot90Z = new Matrix4f();
			rot90Z.rotZ((float) (Math.PI/2));
			
			// rotorConnectionSpin
			Matrix4f rotorConnectionSpin = new Matrix4f();
			rotorConnectionSpin.setIdentity();
			rotorConnectionSpin.rotY(4*phi);
			
			Matrix4f scaleRotorConnection = new Matrix4f();
			scaleRotorConnection.setRow(0, 0.1f, 0, 0, 0);
			scaleRotorConnection.setRow(1, 0, 1, 0, 0);
			scaleRotorConnection.setRow(2, 0, 0, 0.1f, 0);
			scaleRotorConnection.setRow(3, 0, 0, 0, 1);
			
			// position rotor above body
			Matrix4f rotorPosition = new Matrix4f();
			rotorPosition.setIdentity();
			rotorPosition.setTranslation(new Vector3f(0f, 0f, 2f));
			
			// scale rotor outline torus
			Matrix4f scaleRotor = new Matrix4f();
			scaleRotor.setRow(0, 2, 0, 0, 0);
			scaleRotor.setRow(1, 0, 2, 0, 0);
			scaleRotor.setRow(2, 0, 0, 0.5f, 0);
			scaleRotor.setRow(3, 0, 0, 0, 5);
			
			// rotor spin
			Matrix4f rotorSpin = new Matrix4f();
			rotorSpin.rotZ(10*phi);
			
			// scale rotor blades
			Matrix4f scaleRotorBlades = new Matrix4f();
			scaleRotorBlades.setColumn(0, 3, 0, 0, 0);
			scaleRotorBlades.setColumn(1, 0, 0.2f, 0, 0);
			scaleRotorBlades.setColumn(2, 0, 0, 0.01f, 0);
			scaleRotorBlades.setColumn(3, 0, 0, 0, 1);
			
			// position the backwing at the end of the tail
			Matrix4f backwingPosition = new Matrix4f();
			backwingPosition.setIdentity();
			backwingPosition.setTranslation(new Vector3f(0, -6.75f, 0.75f));
			
			Matrix4f scaleBackwing = new Matrix4f();
			scaleBackwing.setRow(0, 0.2f, 0, 0, 0);
			scaleBackwing.setRow(1, 0, 0.5f, -0.3f, 0);
			scaleBackwing.setRow(2, 0, 0, 0.75f, 0);
			scaleBackwing.setRow(3, 0, 0, 0, 1);
			
			// multiply all shape matrices with the needed transformations etc.
			bodyMatrix.mul(perspective);
			bodyMatrix.mul(rotation);
			bodyMatrix.mul(translation);
			bodyMatrix.mul(scaleBody);
			
			tailMatrix.mul(perspective);
			tailMatrix.mul(rotation);
			tailMatrix.mul(translation);
			tailMatrix.mul(tailPosition);
			tailMatrix.mul(scaleTail);
			
			rotorConnectionMatrix.mul(perspective);
			rotorConnectionMatrix.mul(rotation);
			rotorConnectionMatrix.mul(translation);
			rotorConnectionMatrix.mul(rotorConnectionPosition);
			rotorConnectionMatrix.mul(rotorConnectionRotation);
			rotorConnectionMatrix.mul(rotorConnectionSpin);
			rotorConnectionMatrix.mul(scaleRotorConnection);
			
			rotorMatrix.mul(perspective);
			rotorMatrix.mul(rotation);
			rotorMatrix.mul(translation);
			rotorMatrix.mul(rotorPosition);
			rotorMatrix.mul(rotorSpin);
			rotorMatrix.mul(scaleRotor);
			
			blade1Matrix.mul(perspective);
			blade1Matrix.mul(rotation);
			blade1Matrix.mul(translation);
			blade1Matrix.mul(rotorPosition);
			blade1Matrix.mul(rotorSpin);
			blade1Matrix.mul(scaleRotorBlades);
			
			blade2Matrix.mul(perspective);
			blade2Matrix.mul(rotation);
			blade2Matrix.mul(translation);
			blade2Matrix.mul(rot90Z);
			blade2Matrix.mul(rotorPosition);
			blade2Matrix.mul(rotorSpin);
			blade2Matrix.mul(scaleRotorBlades);
			
			backwingMatrix.mul(perspective);
			backwingMatrix.mul(rotation);
			backwingMatrix.mul(translation);
			backwingMatrix.mul(backwingPosition);
			backwingMatrix.mul(scaleBackwing);
			
			body.setTransformation(bodyMatrix);
			tail.setTransformation(tailMatrix);
			rotorConnection.setTransformation(rotorConnectionMatrix);
			rotor.setTransformation(rotorMatrix);
			blade1.setTransformation(blade1Matrix);
			blade2.setTransformation(blade2Matrix);
			backwing.setTransformation(backwingMatrix);
			phi += 0.01f;
		}
	}

	/**
	 * A mouse listener for the main window of this application. This can be
	 * used to process mouse events.
	 */
	public static class SimpleMouseListener implements MouseListener {
		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mouseClicked(MouseEvent e) {
		}
	}

	/**
	 * A key listener for the main window. Use this to process key events.
	 * Currently this provides the following controls: 's': stop animation 'p':
	 * play animation '+': accelerate rotation '-': slow down rotation 'd':
	 * default shader 'n': shader using surface normals 'm': use a material for
	 * shading
	 */
	public static class SimpleKeyListener implements KeyListener {
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyChar()) {
			case 's': {
				// Stop animation
				currentstep = 0;
				break;
			}
			case 'p': {
				// Resume animation
				currentstep = basicstep;
				break;
			}
			case '+': {
				// Accelerate roation
				currentstep += basicstep;
				break;
			}
			case '-': {
				// Slow down rotation
				currentstep -= basicstep;
				break;
			}
			}

			// Trigger redrawing
			renderPanel.getCanvas().repaint();
		}

		public void keyReleased(KeyEvent e) {
		}

		public void keyTyped(KeyEvent e) {
		}

	}

	/**
	 * The main function opens a 3D rendering window, implemented by the class
	 * {@link SimpleRenderPanel}. {@link SimpleRenderPanel} is then called
	 * backed for initialization automatically. It then constructs a simple 3D
	 * scene, and starts a timer task to generate an animation.
	 */
	public static void main(String[] args) {
		// Make a render panel. The init function of the renderPanel
		// (see above) will be called back for initialization.
		renderPanel = new SimpleRenderPanel();

		// Make the main window of this application and add the renderer to it
		JFrame jframe = new JFrame("simple");
		jframe.setSize(700, 700);
		jframe.setLocationRelativeTo(null); // center of screen
		jframe.getContentPane().add(renderPanel.getCanvas());// put the canvas
																// into a JFrame
																// window

		// Add a mouse and key listener
		renderPanel.getCanvas().addMouseListener(new SimpleMouseListener());
		 renderPanel.getCanvas().addKeyListener(new SimpleKeyListener());
		renderPanel.getCanvas().setFocusable(true);

		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setVisible(true); // show window
	}
}
