package simple;

import jrtr.RenderContext;
import jrtr.VertexData;

public class Chopper {
	
	private float[] bodyVertices, bodyColors;
	private int[] bodyIndices;
	private float[] tailVertices, tailColors;
	private int[] tailIndices;
	
	private float[] rotorVertices, rotorColors;
	private int[] rotorIndices;
	
	private float[] rotorConnectionVertices, rotorConnectionColors;
	private int[] rotorConnectionIndices;
	
	private float[] bladeVertices, bladeColors;
	private int[] bladeIndices;
	
	private float[] backwingVertices, backwingColors;
	private int[] backwingIndices;

	public Chopper() {
		initVertexData();
	}
	
	private void initVertexData() {
		initBody();
		initTail();
		initRotorConnection();
		initRotor();
		initRotorBlade();
		initBackwing();
	}

	private void initRotorConnection() {
		Cylinder rotorConnection = new Cylinder(1, 1, 24);
		this.rotorConnectionVertices = rotorConnection.getVertices();
		this.rotorConnectionColors = rotorConnection.getColors();
		this.rotorConnectionIndices = rotorConnection.getIndices();
		
	}

	private void initRotorBlade() {
		Cube blade = new Cube(1, 1, 1);
		this.bladeVertices = blade.getVertices();
		this.bladeColors = blade.getColors();
		this.bladeIndices = blade.getIndices();
	}

	private void initBody() {
		Cube body = new Cube(1, 1, 1);
		this.bodyVertices = body.getVertices();
		this.bodyColors = body.getColors();
		this.bodyIndices = body.getIndices();
	}

	private void initTail() {
		Cylinder tail = new Cylinder(1, 1, 24);
		this.tailVertices = tail.getVertices();
		this.tailColors = tail.getColors();
		this.tailIndices = tail.getIndices();		
	}
	
	private void initRotor() {
		Torus rotor = new Torus(1, 1, 24);
		this.rotorVertices = rotor.getVertices();
		this.rotorColors = rotor.getColors();
		this.rotorIndices = rotor.getIndices();
	}
	
	private void initBackwing() {
		Cube backwing = new Cube(1, 1, 1);
		this.backwingVertices = backwing.getVertices();
		this.backwingColors = backwing.getColors();
		this.backwingIndices = backwing.getIndices();
	}

	public VertexData getBodyVertexData(RenderContext renderContext) {
		VertexData vertexData = renderContext.makeVertexData(this.bodyVertices.length/3); // set size to indices.length
		vertexData.addElement(this.bodyVertices, VertexData.Semantic.POSITION, 3);
		vertexData.addElement(this.bodyColors, VertexData.Semantic.COLOR, 3);
		vertexData.addIndices(this.bodyIndices);
		return vertexData;
	}
	
	public VertexData getTailVertexData(RenderContext renderContext) {
		VertexData vertexData = renderContext.makeVertexData(this.tailVertices.length/3); // set size to indices.length
		vertexData.addElement(this.tailVertices, VertexData.Semantic.POSITION, 3);
		vertexData.addElement(this.tailColors, VertexData.Semantic.COLOR, 3);
		vertexData.addIndices(this.tailIndices);
		return vertexData;
	}
	
	public VertexData getRotorConnectionVertexData(RenderContext renderContext) {
		VertexData vertexData = renderContext.makeVertexData(this.rotorConnectionVertices.length/3); // set size to indices.length
		vertexData.addElement(this.rotorConnectionVertices, VertexData.Semantic.POSITION, 3);
		vertexData.addElement(this.rotorConnectionColors, VertexData.Semantic.COLOR, 3);
		vertexData.addIndices(this.rotorConnectionIndices);
		return vertexData;
	}
	
	public VertexData getRotorVertexData(RenderContext renderContext) {
		VertexData vertexData = renderContext.makeVertexData(this.rotorVertices.length/3); // set size to indices.length
		vertexData.addElement(this.rotorVertices, VertexData.Semantic.POSITION, 3);
		vertexData.addElement(this.rotorColors, VertexData.Semantic.COLOR, 3);
		vertexData.addIndices(this.rotorIndices);
		return vertexData;
	}
	
	public VertexData getRotorBladeVertexData(RenderContext renderContext) {
		VertexData vertexData = renderContext.makeVertexData(this.bladeVertices.length/3); // set size to indices.length
		vertexData.addElement(this.bladeVertices, VertexData.Semantic.POSITION, 3);
		vertexData.addElement(this.bladeColors, VertexData.Semantic.COLOR, 3);
		vertexData.addIndices(this.bladeIndices);
		return vertexData;		
	}
	
	public VertexData getBackwingVertexData(RenderContext renderContext) {
		VertexData vertexData = renderContext.makeVertexData(this.backwingVertices.length/3); // set size to indices.length
		vertexData.addElement(this.backwingVertices, VertexData.Semantic.POSITION, 3);
		vertexData.addElement(this.backwingColors, VertexData.Semantic.COLOR, 3);
		vertexData.addIndices(this.backwingIndices);
		return vertexData;		
	}

}
