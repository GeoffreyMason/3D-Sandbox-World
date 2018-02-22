package postProcessing;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import bloom.BrightFilter;
import bloom.CombineFilter;
import gaussianBlur.HorizontalBlur;
import gaussianBlur.VerticalBlur;
import models.RawModel;
import renderEngine.Loader;

public class PostProcessing {
	
	private static final float[] POSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };	
	private static RawModel quad;
	private static HorizontalBlur hBlur;
	private static VerticalBlur vBlur;
	private static ContrastChanger contrastChanger;
	private static BrightFilter brightFilter;
	private static CombineFilter combineFilter;

	public static void init(Loader loader){
		quad = loader.loadToVAO(POSITIONS, 2);
		brightFilter = new BrightFilter(Display.getWidth() / 2, Display.getHeight() / 2);
		hBlur = new HorizontalBlur(Display.getWidth(), Display.getHeight());
		vBlur = new VerticalBlur(Display.getWidth(), Display.getHeight());
		combineFilter = new CombineFilter();
		contrastChanger = new ContrastChanger();
	}
	
	public static void doPostProcessing(int colourTexture){
		start();
		brightFilter.render(colourTexture);
		hBlur.render(brightFilter.getOutputTexture());
		vBlur.render(hBlur.getOutputTexture());
		combineFilter.render(colourTexture, vBlur.getOutputTexture());
		end();
	}
	
	public static void blur(int colourTexture){
		start();
		hBlur.render(colourTexture);
		vBlur.render(hBlur.getOutputTexture());
		contrastChanger.render(vBlur.getOutputTexture());
		end();
	}
	
	public static void cleanUp(){
		brightFilter.cleanUp();
		hBlur.cleanUp();
		vBlur.cleanUp();
		combineFilter.cleanUp();
		contrastChanger.cleanUp();
	}
	
	private static void start(){
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}
	
	private static void end(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}


}
