package water;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import toolbox.Maths;

public class WaterRenderer {
	
	private static final String DUDV_MAP = "waterDUDV";
	
	private int dudvTexture;

	private WaterShader shader;
	private WaterFrameBuffers fbos;
	private static final float WAVE_SPEED = 0.05f;
	private float time = 0;

	public WaterRenderer(Loader loader, WaterShader shader, Matrix4f projectionMatrix, WaterFrameBuffers fbos) {
		this.shader = shader;
		this.fbos = fbos;
		dudvTexture = loader.loadTexture(DUDV_MAP);
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	public void render(List<WaterModel> waters) {
		updateTime();
		for (WaterModel water : waters) {
			prepareWater(water);
			loadModelMatrix(water);
			GL11.glDrawElements(GL11.GL_TRIANGLES, water.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			unbindRawModel();
		}
	}

	public void prepareWater(WaterModel water) {
		RawModel model = water.getModel();
		GL30.glBindVertexArray(model.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getReflectionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, dudvTexture);
	}

	private void unbindRawModel() {
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}

	private void loadModelMatrix(WaterModel water) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(new Vector3f(water.getX(), 0, water.getZ()), 0,
				0, 0, 1);
		shader.loadTransformationMatrix(transformationMatrix);
	}
	
	private void updateTime() {
		time += DisplayManager.getFrameTimeSeconds() * WAVE_SPEED;
		time %= 1;
		shader.loadTime(time);
	}
}