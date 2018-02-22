package renderEngine;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import entities.Entity;
import models.RawModel;
import shaders.StaticShader;
import toolbox.Maths;

public class EntityRenderer {


	private StaticShader shader;
	private static final float WAVE_SPEED = 0.2f;
	private float time = 0;

	public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	public void render(Map<RawModel, List<Entity>> entities) {
		updateTime();
		for (RawModel model:entities.keySet()) {
			prepareRawModel(model);
			List<Entity> batch = entities.get(model);
			for (Entity entity:batch) {
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindRawModel();
		}
	}

	public void prepareRawModel(RawModel model) {
		GL30.glBindVertexArray(model.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(2);
		GL20.glEnableVertexAttribArray(3);
	}

	private void unbindRawModel() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(2);
		GL20.glDisableVertexAttribArray(3);
		GL30.glBindVertexArray(0);
	}

	private void prepareInstance(Entity entity) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(),
				entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
	}
	
	private void updateTime() {
		time += DisplayManager.getFrameTimeSeconds() * WAVE_SPEED;
		time %= 1;
		shader.loadTime(time);
	}
}
