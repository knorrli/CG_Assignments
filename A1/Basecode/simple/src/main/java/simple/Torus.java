package simple;

import java.util.ArrayList;

import jrtr.RenderContext;
import jrtr.VertexData;

/**
 * Implements a simple application that opens a 3D rendering window and 
 * shows a rotating cube.
 */
public class Torus
{	
	public float torusRadius;
	public float ringRadius;
	public int segments;
	public float[] vertices, colors;
	public int[] indices;
	
	public Torus(float torusRadius, float ringRadius, int segments) {
		this.torusRadius = torusRadius;
		this.ringRadius = ringRadius;
		this.segments = segments;
		initVertexData();
	}
	
	public VertexData getVertexData(RenderContext renderContext) {
		VertexData vertexData = renderContext.makeVertexData(segments*segments); // set size to indices.length
		vertexData.addElement(this.vertices, VertexData.Semantic.POSITION, 3);
		vertexData.addElement(this.colors, VertexData.Semantic.COLOR, 3);
		vertexData.addIndices(this.indices);
		return vertexData;
		
	}
	
	public void initVertexData() {
		this.vertices = initVertices();
		this.indices = initIndices(vertices);
		this.colors = initColors(vertices);
	}

	public float[] initVertices() {
		double angle = 2*Math.PI / (segments);
		
		// Iterator which stores temporarily our vertices
		ArrayList<Float> vertexPositions = new ArrayList<Float>();
		
		// since we have two angels phi and teta - see wikipedia - torus coordinates -
		// we need 2 for loops in order to calculates our torus verices.
		for(double phi = angle; phi <= 2.0f*Math.PI; phi += angle){
			for(double theta = angle; theta <= 2.0f*Math.PI; theta += angle){
				float x = (float) ((torusRadius + ringRadius * Math.cos(phi)) * Math.cos(theta)); 
				float y = (float) ((torusRadius + ringRadius * Math.cos(phi)) * Math.sin(theta));
				float z = (float) (ringRadius*Math.sin(phi));
				vertexPositions.add(x);
				vertexPositions.add(y);
				vertexPositions.add(z);
			}
		}
		
		// fill our vertices array by using an iterator for our list.
		float[] vertices = new float[vertexPositions.size()];
		int i = 0;
		for(Float value : vertexPositions){
			vertices[i] = value.floatValue();
			i++;
		}
		return vertices;
	}
	
	public int[] initIndices(float[] vertices) {
		
		ArrayList<Integer> indicesValues = new ArrayList<Integer>();
		
		for(int k=0; k < segments; k++){
			
			for(int p=0; p < segments; p++){
				int val1 = (k*segments+p); // x
				
				int val2 = (k*segments+p+1); // y
				
				if(p==segments-1) {
					val2 = k*segments+p-segments+1;
				}
				int val3 = ((k+1)*segments+p)%(segments*segments); //modulo
				indicesValues.add(val1);
				indicesValues.add(val2);
				indicesValues.add(val3);
			}
		}
		
		for(int k=0; k < segments; k++){
			for(int p=0; p < segments; p++){
				int val1 = ((k+1)*segments+p)%(segments*segments); 
				int val2 = (p==segments-1)? (val1+1)-segments : val1+1; 
				int val3 = (p==segments-1)? (k*segments+p)+1-segments : (k*segments+p)+1; //modulo
				
				indicesValues.add(val1);
				indicesValues.add(val2);
				indicesValues.add(val3);
			}
		}
		
		// fill our indices array by using an iterator for our list.
		int[] indices = new int[indicesValues.size()];
		int i = 0;
		for(Integer value : indicesValues){
			indices[i] = value.intValue();
			i++;
		}
		return indices;
	}
	
	public float[] initColors(float[] vertices) {
		
		ArrayList<Float> colors = new ArrayList<Float>();
		for (int segment = 0; segment < vertices.length/3; segment++) {
			if (segment % 2 == 0) {
				colors.add(0.5f);
				colors.add(1f);
				colors.add(0.5f);
			} else {
				colors.add(0f);
				colors.add(0.5f);
				colors.add(0.5f);
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

	public float[] getVertices() {
		return this.vertices;
	}

	public float[] getColors() {
		return this.colors;
	}

	public int[] getIndices() {
		return this.indices;
	}
}
