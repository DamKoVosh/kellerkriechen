package dd.UI.Sfx;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Tooltips {
	
	private static class StringMetrics {
		public int width;
		public int height;
	}
	
	private static final BufferedImage temp = new BufferedImage(1, 1,
			BufferedImage.TYPE_INT_ARGB);
	private static final Graphics tempGraphics = temp.getGraphics();
	
	public static BufferedImage ttAttack;
	
	private final static String fontName = "Arial";
	private static Font font = new Font(fontName, Font.BOLD, 14);
	
	public static void drawOutlinedText(Graphics g, String str, Point p) {
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		
		FontRenderContext frc = g2.getFontRenderContext();
		Font f = new Font("Helvetica", Font.BOLD, 30);
		String s = new String(str);
		TextLayout textTl = new TextLayout(s, f, frc);
		AffineTransform transform = new AffineTransform();
		Shape outline = textTl.getOutline(null);
		Rectangle outlineBounds = outline.getBounds();
		transform = g2.getTransform();
		transform.translate(p.x, p.y);
		
		g2.setStroke(new BasicStroke(1.0f));
		
		g2.transform(transform);
		g2.setColor(Color.red);
		g2.fill(outline);
		g2.setColor(Color.black);
		g2.draw(outline);
		g2.setClip(outline);
	}
	
	public static void drawText(Graphics g, String str, Point p, Color color) {
		int offset = 2;
		
		Font f = new Font("Helvetica", Font.BOLD, 30);
		
		g.setFont(f);
		
		g.setColor(Color.black);
		g.drawString(str, p.x + offset, p.y + offset);
		
		g.setColor(color);
		g.drawString(str, p.x, p.y);
		
		
		
		// Tooltips.drawOutlinedText(g, "Attack", mP);
		
	}
	
	public static void initialize() {
		
		String text;
		StringMetrics sm;
		
		text = "Attack";
		sm = getStringMetrics(text, font);
		
		ttAttack = new BufferedImage(sm.width, sm.height,
				BufferedImage.TYPE_INT_ARGB);
		
		Graphics gi = ttAttack.getGraphics();
		gi.setFont(font);
		gi.setColor(Color.red);
		gi.drawString(text, 0, sm.height);
		
	}
	
	private static StringMetrics getStringMetrics(String text, Font font) {
		StringMetrics sm = new StringMetrics();
		
		Graphics g = getTempImageGraphic();
		
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics();
		sm.width = fm.stringWidth(text);
		sm.height = fm.getHeight();
		
		return sm;
	}
	
	private static Graphics getTempImageGraphic() {
		return tempGraphics;
	}
}
