package cylinder;

import jrtr.*;

import javax.swing.*;

import java.awt.event.*;

import javax.vecmath.*;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Implements a simple application that opens a 3D rendering window and 
 * shows a rotating cube.
 */
public class cylinder
{	
	static RenderPanel renderPanel;
	static RenderContext renderContext;
	static Shader normalShader;
	static Shader diffuseShader;
	static Material material;
	static SimpleSceneManager sceneManager;
	static Shape shape;
	static float currentstep, basicstep;
	static int segments;

	/**
	 * An extension of {@link GLRenderPanel} or {@link SWRenderPanel} to 
	 * provide a call-back function for initialization. Here we construct
	 * a simple 3D scene and start a timer task to generate an animation.
	 */ 
	public final static class SimpleRenderPanel extends GLRenderPanel
	{
		/**
		 * Initialization call-back. We initialize our renderer here.
		 * 
		 * @param r	the render context that is associated with this render panel
		 */
		public void init(RenderContext r)
		{
			renderContext = r;
			segments = 50;
			VertexData vertexData = initVertexData(); 
			
								
			// Make a scene manager and add the object
			sceneManager = new SimpleSceneManager();
			shape = new Shape(vertexData);
			sceneManager.addShape(shape);

			// Add the scene to the renderer
			renderContext.setSceneManager(sceneManager);
			
			// Load some more shaders
		    normalShader = renderContext.makeShader();
		    try {
		    	normalShader.load("../jrtr/shaders/normal.vert", "../jrtr/shaders/normal.frag");
		    } catch(Exception e) {
		    	System.out.print("Problem with shader:\n");
		    	System.out.print(e.getMessage());
		    }
	
		    diffuseShader = renderContext.makeShader();
		    try {
		    	diffuseShader.load("../jrtr/shaders/diffuse.vert", "../jrtr/shaders/diffuse.frag");
		    } catch(Exception e) {
		    	System.out.print("Problem with shader:\n");
		    	System.out.print(e.getMessage());
		    }

		    // Make a material that can be used for shading
			material = new Material();
			material.shader = diffuseShader;
			material.texture = renderContext.makeTexture();
			try {
				material.texture.load("../textures/plant.jpg");
			} catch(Exception e) {				
				System.out.print("Could not load texture.\n");
				System.out.print(e.getMessage());
			}

			// Register a timer task
		    Timer timer = new Timer();
		    basicstep = 0.01f;
		    currentstep = basicstep;
		    timer.scheduleAtFixedRate(new AnimationTask(), 0, 10);
		}
		
		public VertexData initVertexData() {

			float[] vertices = initVertices(segments);
			int[] indices = initIndices(vertices);
			float[] colors = initColors(segments);
			
			VertexData vertexData = renderContext.makeVertexData(indices.length); // set size to indices.length
			vertexData.addIndices(indices);
			vertexData.addElement(vertices, VertexData.Semantic.POSITION, 3);
			vertexData.addElement(colors, VertexData.Semantic.COLOR, 3);

			return vertexData;
		}

		public float[] initVertices(int segments) {
			
			ArrayList<Float> vertices = new ArrayList<Float>();
			
			float angle = (float)360/segments;
			
			
			for (float a = 0; a < 359.9;) {
				float x0 = 0f;
				float z0 = 0f;
				
				float x1 = (float) Math.cos(Math.toRadians(a));
				float z1 = (float) Math.sin(Math.toRadians(a));
				
				a += angle;
				
				float x2 = (float)Math.cos(Math.toRadians(a));
				float z2 = (float)Math.sin(Math.toRadians(a));
				
				float yBottom = 0f;
				float yTop = 3f;
				
				// add bottom triangle
				// center point
				vertices.add(x0);
				vertices.add(yBottom);
				vertices.add(z0);
				
				// right from center
				vertices.add(x1);
				vertices.add(yBottom);
				vertices.add(z1);
				
				// left from center
				vertices.add(x2);
				vertices.add(yBottom);
				vertices.add(z2);
				
				
				// add top triangle
				// center point
				vertices.add(x0);
				vertices.add(yTop);
				vertices.add(z0);
				
				// right from center
				vertices.add(x1);
				vertices.add(yTop);
				vertices.add(z1);
				
				// left from center
				vertices.add(x2);
				vertices.add(yTop);
				vertices.add(z2);
				
				
				// add side triangle 1
				// bottom right from center
				vertices.add(x1);
				vertices.add(yBottom);
				vertices.add(z1);
				
				// bottom left from center
				vertices.add(x2);
				vertices.add(yBottom);
				vertices.add(z2);
				
				// top right from center
				vertices.add(x1);
				vertices.add(yTop);
				vertices.add(z1);
				
				// add side triangle 2
				// bottom left from center
				vertices.add(x2);
				vertices.add(yBottom);
				vertices.add(z2);
				
				// top right from center
				vertices.add(x1);
				vertices.add(yTop);
				vertices.add(z1);
				
				// top left from center
				vertices.add(x2);
				vertices.add(yTop);
				vertices.add(z2);
			}
			
			
			float[] vertice_array = new float[vertices.size()];
			
			// convert ArrayList to Array
			for (int j = 0; j < vertices.size(); j++) {
			    Float f = vertices.get(j);
			    vertice_array[j] = (f != null ? f : Float.NaN);
			}
					 			 
			return vertice_array;
		}
		
		public int[] initIndices(float[] vertices) {
			// The triangles (three vertex indices for each triangle)
			ArrayList<Integer> indices = new ArrayList<Integer>();
			for (int i = 0; i < vertices.length/3; i++) {
				indices.add(i);
			}

			int[] indices_array = new int[indices.size()];
			// convert ArrayList to array
			for (int j = 0; j < indices.size(); j++) {
			    int i = indices.get(j);
			    indices_array[j] = i;
			}
			System.out.println("vertices length: "+vertices.length);
			System.out.println("indices length: "+indices_array.length );
					 			 
			return indices_array;
		}
		
		public float[] initColors(int segments) {
			
			ArrayList<Float> colors = new ArrayList<Float>();
			float[][] colorValues = {{1f, 0f, 0f}, {0f, 1f, 0f}}; // red, green
			for (int segment = 0; segment < segments; segment++) {
				// each segment consists of:
				// bottom, top, side1, side2 = 4 triangles
				for (int triangle = 0; triangle < 4; triangle++) {
					
					// each triangle consists of 3 vertices
					for (int vertex = 0; vertex < 3; vertex++) {
						colors.add(colorValues[segment % 2][0]); // R
						colors.add(colorValues[segment % 2][1]); // G
						colors.add(colorValues[segment % 2][2]); // B
					}
				}
			}

			// convert ArrayList to array			
			float[] colors_array = new float[colors.size()];
			for (int j = 0; j < colors.size(); j++) {
			    Float f = colors.get(j);
			    colors_array[j] = (f != null ? f : Float.NaN);
			}		 			 
			return colors_array;
		}
				
		public float[] initNormals(float[] vertices) {
			// The vertex normals 
			float normals[] = new float[vertices.length];
			
			for (int i = 0; i < vertices.length; i++) {
				if (i % 3 == 1) {
					normals[i] = -1;	
				} else {
					normals[i] = 0;
				}
				
			};
			return normals;
		}
		
		public float[] initTextureCoordinates(float[] vertices) {
			// Texture coordinates 
			float textureCoordinates[] = new float[vertices.length];
			
			for (int i = 0; i < vertices.length; i++) {
				if (i % 3 == 1) {
					textureCoordinates[i] = -1;	
				} else {
					textureCoordinates[i] = 0;
				}
				
			};
			return textureCoordinates;
		}
	}

	/**
	 * A timer task that generates an animation. This task triggers
	 * the redrawing of the 3D scene every time it is executed.
	 */
	public static class AnimationTask extends TimerTask
	{
		public void run()
		{
			// Update transformation by rotating with angle "currentstep"
    		Matrix4f t = shape.getTransformation();
    		Matrix4f rotX = new Matrix4f();
    		rotX.rotX(currentstep);
    		Matrix4f rotY = new Matrix4f();
    		rotY.rotY(currentstep);
    		t.mul(rotX);
    		t.mul(rotY);
    		shape.setTransformation(t);
    		
    		// Trigger redrawing of the render window
    		renderPanel.getCanvas().repaint(); 
		}
	}

	/**
	 * A mouse listener for the main window of this application. This can be
	 * used to process mouse events.
	 */
	public static class SimpleMouseListener implements MouseListener
	{
    	public void mousePressed(MouseEvent e) {}
    	public void mouseReleased(MouseEvent e) {}
    	public void mouseEntered(MouseEvent e) {}
    	public void mouseExited(MouseEvent e) {}
    	public void mouseClicked(MouseEvent e) {}
	}
	
	/**
	 * A key listener for the main window. Use this to process key events.
	 * Currently this provides the following controls:
	 * 's': stop animation
	 * 'p': play animation
	 * '+': accelerate rotation
	 * '-': slow down rotation
	 * 'd': default shader
	 * 'n': shader using surface normals
	 * 'm': use a material for shading
	 */
	public static class SimpleKeyListener implements KeyListener
	{
		public void keyPressed(KeyEvent e)
		{
			switch(e.getKeyChar())
			{
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
				case 'n': {
					// Remove material from shape, and set "normal" shader
					shape.setMaterial(null);
					renderContext.useShader(normalShader);
					break;
				}
				case 'd': {
					// Remove material from shape, and set "default" shader
					shape.setMaterial(null);
					renderContext.useDefaultShader();
					break;
				}
				case 'm': {
					// Set a material for more complex shading of the shape
					if(shape.getMaterial() == null) {
						shape.setMaterial(material);
					} else
					{
						shape.setMaterial(null);
						renderContext.useDefaultShader();
					}
					break;
				}
			}
			
			// Trigger redrawing
			renderPanel.getCanvas().repaint();
		}
		
		public void keyReleased(KeyEvent e)
		{
		}

		public void keyTyped(KeyEvent e)
        {
        }

	}
	
	/**
	 * The main function opens a 3D rendering window, implemented by the class
	 * {@link SimpleRenderPanel}. {@link SimpleRenderPanel} is then called backed 
	 * for initialization automatically. It then constructs a simple 3D scene, 
	 * and starts a timer task to generate an animation.
	 */
	public static void main(String[] args)
	{		
		// Make a render panel. The init function of the renderPanel
		// (see above) will be called back for initialization.
		renderPanel = new SimpleRenderPanel();
		
		// Make the main window of this application and add the renderer to it
		JFrame jframe = new JFrame("simple");
		jframe.setSize(500, 500);
		jframe.setLocationRelativeTo(null); // center of screen
		jframe.getContentPane().add(renderPanel.getCanvas());// put the canvas into a JFrame window

		// Add a mouse and key listener
	    renderPanel.getCanvas().addMouseListener(new SimpleMouseListener());
	    renderPanel.getCanvas().addKeyListener(new SimpleKeyListener());
		renderPanel.getCanvas().setFocusable(true);   	    	    
	    
	    jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    jframe.setVisible(true); // show window
	}
}
