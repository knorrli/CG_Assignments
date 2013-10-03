package simple;

import jrtr.RenderContext;
import jrtr.VertexData;

public class Cube {
	private VertexData vertexData;
	private float length;
	private float width;
	private float height;
	private float[] vertices;
	
	// The vertex colors
	private float colors[] = {
						0.5f,0.5f,0.5f, 0.5f,0.5f,0.5f, 0.5f,0.5f,0.5f, 0.5f,0.5f,0.5f, // front face
						0.5f,0.5f,0.5f, 0.5f,0.5f,0.5f, 0.5f,0.5f,0.5f, 0.5f,0.5f,0.5f, // left face
						0.5f,0.5f,0.5f, 0.5f,0.5f,0.5f, 0.5f,0.5f,0.5f, 0.5f,0.5f,0.5f, // back face
						0.5f,0.5f,0.5f, 0.5f,0.5f,0.5f, 0.5f,0.5f,0.5f, 0.5f,0.5f,0.5f, // right face
						0,0,0.5f, 0,0,0.5f, 0,0,0.5f, 0,0,0.5f, // top face
						0.5f,0.5f,0.5f, 0.5f,0.5f,0.5f, 0.5f,0.5f,0.5f, 0.5f,0.5f,0.5f // bottom face
						};
	
	int indices[] = {
					0,2,3, 0,1,2,			// front face
					4,6,7, 4,5,6,			// left face
					8,10,11, 8,9,10,		// back face
					12,14,15, 12,13,14,		// right face
					16,18,19, 16,17,18,		// top face
					20,22,23, 20,21,22		// bottom face
					};
	
	
	/**
	 * Constructor: build a cube shape
	 */
	public Cube(float length, float width, float height) {
		this.length = length;
		this.width = width;
		this.height = height;
		initVertices();
	}
	
	private void initVertices() {
		
		float[] vertices = {
			-length,-width,height, length,-width,height, length,width,height, -length,width,height,		// front face
			-length,-width,-height, -length,-width,height, -length,width,height, -length,width,-height,	// left face
			length,-width,-height,-length,-width,-height, -length,width,-height, length,width,-height,	// back face
			length,-width,height, length,-width,-height, length,width,-height, length,width,height,		// right face
			length,width,height, length,width,-height, -length,width,-height, -length,width,height,		// top face
			-length,-width,height, -length,-width,-height, length,-width,-height, length,-width,height 	// bottom face
			};
		
		this.vertices = new float[vertices.length];
		for (int i = 0; i < vertices.length; i++) {
			this.vertices[i] = vertices[i];
		}
	}

	public VertexData getVertexData(RenderContext renderContext){
		this.vertexData = renderContext.makeVertexData(24);
		
		this.vertexData.addElement(this.colors, VertexData.Semantic.COLOR, 3);
		this.vertexData.addElement(this.vertices, VertexData.Semantic.POSITION, 3);
		this.vertexData.addIndices(this.indices);
		return this.vertexData;
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
