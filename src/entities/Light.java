package entities;

import org.lwjgl.util.vector.Vector3f;

public class Light {
	
	private Vector3f position;
	private Vector3f colour;
	private Vector3f attenuation = new Vector3f(1, 0, 0);
	
	private float radius;
	private float angle;
	
	public Light(Vector3f position, Vector3f colour) {
		this.position = position;
		this.colour = colour;
		this.radius = (float) Math.sqrt(Math.pow(position.x, 2) + Math.pow(position.z, 2));
		this.angle = (float) Math.atan(position.z / position.x);
	}
	
	public Light(Vector3f position, Vector3f colour, Vector3f attenuation) {
		this.position = position;
		this.colour = colour;
		this.attenuation = attenuation;
		this.radius = (float) Math.sqrt(Math.pow(position.x, 2) + Math.pow(position.z, 2));
		this.angle = (float) Math.atan(position.z / position.x);
	} 
	
	public Vector3f getAttenuation() {
		return attenuation;
	}

	public void angleIncrease(float angle) {
		this.angle += Math.toRadians(angle);
		float xOffset = (float) (radius * Math.cos(this.angle));
		float zOffset = (float) (radius * Math.sin(this.angle));
		this.position.x = xOffset;
		this.position.z = zOffset;
	}
	
	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getColour() {
		return colour;
	}

	public void setColour(Vector3f colour) {
		this.colour = colour;
	}

}
