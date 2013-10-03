package simple;

import java.util.ArrayList;

import jrtr.RenderContext;
import jrtr.VertexData;

public class Cylinder {
	
	public float height;
	public float radius;
	public int segments;
	private float[] vertices, colors;
	private int[] indices;

	public Cylinder(float height, float radius, int segments) {
		this.height = height;
		this.radius = radius;
		this.segments = segments;
		initVertexData();
	}

	private void initVertexData() {
		this.vertices = initVertices(height, segments);
		this.colors = initColors(segments);
		this.indices = initIndices(vertices);
		
	}

	public VertexData getVertexData(RenderContext renderContext) {
		VertexData vertexData = renderContext.makeVertexData(indices.length); // set size to indices.length
		vertexData.addElement(this.vertices, VertexData.Semantic.POSITION, 3);
		vertexData.addElement(this.colors, VertexData.Semantic.COLOR, 3);
		vertexData.addIndices(this.indices);
		return vertexData;
	}
	
	public float[] initVertices(float height, int segments) {
		ArrayList<Float> vertices = new ArrayList<Float>();
		float angle = (float) ((2*Math.PI)/ (segments));
		
		for (float a = 0; a < 2*Math.PI;) {
			float x0 = 0f;
			float z0 = 0f;
			
			float x1 = this.radius * (float) Math.cos(a);
			float z1 = this.radius * (float) Math.sin(a);
			
			a += angle;
			
			float x2 = this.radius * (float)Math.cos(a);
			float z2 = this.radius * (float)Math.sin(a);
			
			float yBottom = 0f;
			float yTop = height;
			
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
				 			 
		return indices_array;
	}
	
	public float[] initColors(int segments) {
		
		ArrayList<Float> colors = new ArrayList<Float>();
		for (int segment = 0; segment < vertices.length/3; segment++) {
			if (segment % 2 == 0) {
				colors.add(0.5f);
				colors.add(0.5f);
				colors.add(0.5f);
			} else {
				colors.add(1f);
				colors.add(0f);
				colors.add(0f);
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
