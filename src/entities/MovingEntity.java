package entities;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;
import terrain.Chunk;

public class MovingEntity extends Entity {
	
	private Vector3f position;
	private Vector2f velocity;

	public MovingEntity(RawModel model, Vector3f position, Vector2f velocity, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.position = position;
		this.velocity = velocity;
	}
	
	public void move(Chunk terrain) {
		float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
		position.y = terrainHeight;
		super.increasePosition(velocity.x, 0, velocity.y);
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector2f getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2f velocity) {
		this.velocity = velocity;
	}
	
	

}
