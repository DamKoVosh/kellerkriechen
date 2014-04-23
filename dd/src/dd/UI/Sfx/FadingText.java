package dd.UI.Sfx;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.ObjectInputStream.GetField;

public class FadingText extends Sfx {
	
	private String text;
	private Point position;
	private Color color;
	private long startTime;
	private long endTime;
	// private final String fontName = "FREEDOM 93";
	// private final String fontName = "Bauhaus 93";
	private final String fontName = "Bauhaus 93";
	private Font font = new Font(fontName, Font.PLAIN, 32);
	
	private BufferedImage image;
	
	public FadingText(String text, Point position, Color color) {
		this.text = text;
		this.position = position;
		this.color = color;
		
		startTime = System.currentTimeMillis();
		endTime = startTime + 1000;
		
		createImage();
	}
	
	@Override
	public boolean draw(Graphics g) {
		long time = System.currentTimeMillis();
		
		if (time >= endTime) {
			return false;
		}
		
		int alpha = getAlpha(time);
		Point pos = getPosition(time);
		
		drawText(g, alpha, pos);
		
		return true;
	}
	
	private void drawText(Graphics g, int alpha, Point pos) {
		
		// gi.fillRect(0, 0, 50, 50);
		
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				alpha / 255f);
		((Graphics2D) g).setComposite(ac);
		
		g.drawImage(image, pos.x, pos.y, null);
		
		((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
		
	}
	
	private void createImage() {
		int offset = 2;
		
		BufferedImage temp = new BufferedImage(1, 1,
				BufferedImage.TYPE_INT_ARGB);
		Graphics g = temp.getGraphics();
		
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics();
		
		int width, height;
		width = fm.stringWidth(text) + offset;
		height = fm.getHeight() + offset;
		
		System.out.printf("width: %d height: %d\n", width, height);
		
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		Graphics gi = image.getGraphics();
		gi.setFont(font);
		
		gi.setColor(Color.black);
		gi.drawString(text, offset, offset + fm.getHeight());
		
		gi.setColor(color);
		gi.drawString(text, 0, fm.getHeight());
		
		// gi.fillRect(0, 0, 5, 5);
	}
	
	private int getAlpha(long time) {
		long beginAlpha = startTime + 150;
		long endAlpha = endTime;
		long alphaDur = endAlpha - beginAlpha;
		int alpha;
		
		if (time >= beginAlpha) {
			
			alpha = 255 - (int) (((time - beginAlpha) / (float) alphaDur) * 255);
			
		} else {
			alpha = 255;
		}
		
		Math.max(0, Math.min(255, alpha));
		
		return alpha;
	}
	
	private Point getPosition(long time) {
		Point pos = new Point(position);
		
		int yOffset = 0;
		int speed = -20;
		
		long timePassed = time - startTime;
		
		yOffset = (int) ((timePassed / 1000f) * speed);
		
		pos.translate(0, yOffset);
		
		return pos;
	}
	
}
