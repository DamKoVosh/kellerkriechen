package dd.UI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.Console;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import dd.Board;
import dd.Actions.Attack.Attack;
import dd.Actions.Default.Action;
import dd.Creature.Creature;
import dd.Creature.Creature.DamageType;
import dd.Encounter.Encounter;
import dd.Engine.CreatureEvents;
import dd.Engine.Engine;
import dd.Engine.MissListener;
import dd.Engine.TakeDamageListener;
import dd.Engine.CreatureEbents.Listener.DieListener;
import dd.Engine.CreatureEbents.Listener.GainThpListener;
import dd.Engine.CreatureEbents.Listener.HitListener;
import dd.Intelligence.UserInterfaceIntelligence;
import dd.MapObjects.FreeTerrain;
import dd.MapObjects.MapObject;
import dd.MapTree.MapTreeHa;
import dd.MapTree.MapTreeObject;
import dd.MapTree.MapTreeWare;
import dd.Movement.PathFinding;
import dd.Power.AttackPower;
import dd.UI.Sfx.FadingText;
import dd.UI.Sfx.Sfx;
import dd.UI.Sfx.Tooltips;

public class MapPanel extends JPanel {
	
	static {
		CreatureEvents.addHitListener(new HitListener() {
			
			@Override
			public void hit(Attack attack, Creature target) {
				
				// Engine.log("AHAHAHHAHAAAAAAAAAA!");
				
				Point drawPos = new Point();
				Color c = new Color(60, 255, 60);
				
				Creature attacker = attack.getPerformer();
				
				Point p1 = new Point(), p2 = new Point();
				p1.x = target.getPosition().x * 50;
				p1.y = target.getPosition().y * 50;
				p2.x = attacker.getPosition().x * 50;
				p2.y = attacker.getPosition().y * 50;
				
				drawPos.x = p1.x + ((p2.x - p1.x) / 2);
				drawPos.y = p1.y + ((p2.y - p1.y) / 2);
				
				Engine.getWindow().getMap()
						.addEffect(new FadingText("HIT", drawPos, c));
			}
		});
		
		CreatureEvents.addMissListener(new MissListener() {
			
			@Override
			public void miss(Attack attack, Creature target) {
				Point drawPos = new Point();
				Color c = new Color(255, 60, 60);
				
				Creature attacker = attack.getPerformer();
				
				Point p1 = new Point(), p2 = new Point();
				p1.x = target.getPosition().x * 50;
				p1.y = target.getPosition().y * 50;
				p2.x = attacker.getPosition().x * 50;
				p2.y = attacker.getPosition().y * 50;
				
				drawPos.x = p1.x + ((p2.x - p1.x) / 2);
				drawPos.y = p1.y + ((p2.y - p1.y) / 2);
				
				Engine.getWindow().getMap()
						.addEffect(new FadingText("MISS", drawPos, c));
			}
		});
		
		CreatureEvents.addTakeDamageListener(new TakeDamageListener() {
			
			@Override
			public void takeDamage(Creature creature, int amount,
					DamageType type, Creature damager) {
				
				Point drawPos = new Point();
				Color c = new Color(255, 0, 0);
				
				Point targetPos;
				targetPos = creature.getPosition();
				
				drawPos.x = targetPos.x * 50;
				drawPos.y = targetPos.y * 50;
				
				String text;
				text = "-" + amount;
				
				Engine.getWindow().getMap()
						.addEffect(new FadingText(text, drawPos, c));
				
				// Engine.log(text);
			}
		});
		
		CreatureEvents.addDieListener(new DieListener() {
			
			@Override
			public void die(Creature creature, Creature killer) {
				Point drawPos = new Point();
				Color c = new Color(64, 64, 64);
				
				Point targetPos;
				targetPos = creature.getPosition();
				
				drawPos.x = targetPos.x * 50;
				drawPos.y = targetPos.y * 50;
				
				String text;
				text = "DEATH";
				
				Engine.getWindow().getMap()
						.addEffect(new FadingText(text, drawPos, c));
				
				// Engine.log(text);
			}
		});
		
		CreatureEvents.addGainThpListener(new GainThpListener() {
			
			@Override
			public void gainThp(Creature creature, int amount) {
				Point drawPos = new Point();
				
				Color c = new Color(255, 127, 39);
				
				Point targetPos;
				targetPos = creature.getPosition();
				
				drawPos.x = targetPos.x * 50;
				drawPos.y = targetPos.y * 50;
				
				String text;
				text = "+" + amount + " THP";
				
				Engine.getWindow().getMap()
						.addEffect(new FadingText(text, drawPos, c));
				
				// Engine.log(text);
			}
		});
		
	}
	
	private static final long serialVersionUID = 1L;
	private Board b;
	private Creature dragObj;
	private Point dragMousePos;
	private MapObject moveStepObject;
	private List<Point> highlightedPath;
	private int highlightedPathLengthLimit;
	private boolean canDrag = false;
	
	private Timer t;
	
	private List<Sfx> effects = new CopyOnWriteArrayList<Sfx>();
	private boolean movePrepared;
	private Point moveTargetPos;
	private List<Point> movePath;
	
	private static final Color[] treeColors = { new Color(255, 0, 0),
			new Color(0, 255, 0), new Color(0, 0, 255), new Color(255, 255, 0),
			new Color(255, 0, 255), new Color(0, 255, 255) };
	
	public void showMoveSteps(MapObject c) {
		moveStepObject = c;
		repaint();
	}
	
	public MapPanel(Board b) {
		
		this.setFocusable(true);
		
		final MapPanel thisMap = this;
		
		Tooltips.initialize();
		
		t = new Timer(50, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				thisMap.repaint();
				// Engine.log("yeah!");
			}
		});
		t.start();
		
		this.b = b;
		
		addMouseMotionListener(new MouseMotionListener() {
			
			public void mouseDragged(java.awt.event.MouseEvent evt) {
				formMouseDragged(evt);
			}
			
			public void mouseMoved(java.awt.event.MouseEvent evt) {
				formMouseMoved(evt);
			}
			
		});
		
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				formMouseClicked(e);
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				formMousePressed(e);
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				formMouseReleased(e);
			}
			
		});
		
		addMouseWheelListener(new MouseWheelListener() {
			
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				formMouseWheelMoved(e);
			}
			
		});
		
		addKeyListener(new KeyListener() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				formKeyReleased(e);
			}
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	private void formKeyReleased(KeyEvent e) {
		
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			UserInterfaceIntelligence in = UserInterfaceIntelligence
					.getCurrentUii();
			
			if (in == null)
				return;
			
			in.uiEndTurn();
		}
	}
	
	private Point translateMousePosToBoardPos(Point mousePos) {
		Point p = new Point();
		
		p.x = mousePos.x / 50;
		p.y = mousePos.y / 50;
		
		return p;
	}
	
	private void checkMoveClicked(MouseEvent e) {
		UserInterfaceIntelligence in = UserInterfaceIntelligence
				.getCurrentUii();
		
		if (in == null)
			return;
		
		if (SwingUtilities.isRightMouseButton(e)) {
			movePrepared = false;
			return;
		}
		
		Point pos;
		pos = translateMousePosToBoardPos(new Point(e.getX(), e.getY()));
		
		if (movePrepared) {
			Point endPos;
			endPos = highlightedPath.get(highlightedPath.size() - 1);
			if (endPos.equals(pos)) {
				Engine.log("Auto move path");
				in.autoMove(highlightedPath);
			}
		}
		
		if (!(b.getObjectAtCoordinates(pos.x, pos.y) instanceof FreeTerrain)) {
			return;
		}
		
		Creature actor = Encounter.getCurrentEncounter().getCurrentTurnActor();
		
		if (actor == null)
			return;
		
		Engine.log("Clicked to move");
		in.clickMove(pos, this);
		
	}
	
	private void checkMovePointClicked(MouseEvent e) {
		int mX, mY;
		mX = e.getX();
		mY = e.getY();
		
		if (moveStepObject == null) {
			return;
		}
		
		int x, y;
		x = mX / 50;
		y = mY / 50;
		
		if (x == moveStepObject.getPosition().x
				&& y == moveStepObject.getPosition().y) {
			UserInterfaceIntelligence.clickStep(new Point(x, y));
		}
		
		if (!(b.getObjectAtCoordinates(x, y) instanceof FreeTerrain)) {
			return;
		}
		
		UserInterfaceIntelligence.clickStep(new Point(x, y));
		
	}
	
	private void formMouseWheelMoved(MouseWheelEvent e) {
		UserInterfaceIntelligence in = UserInterfaceIntelligence
				.getCurrentUii();
		
		if (in == null)
			return;
		
		in.uiSelectNextAttack();
		repaint();
	}
	
	private void formMouseClicked(MouseEvent e) {
		
	}
	
	private void checkAttackOrder(Point mP) {
		
		UserInterfaceIntelligence in = UserInterfaceIntelligence
				.getCurrentUii();
		
		if (in == null)
			return;
		
		Point p;
		p = translateMousePosToBoardPos(mP);
		
		MapObject mObj = b.getObjectAtCoordinates(p.x, p.y);
		Creature actor = Encounter.getCurrentEncounter().getCurrentTurnActor();
		
		if (mObj instanceof Creature) {
			
			if (actor.isEnemyOf((Creature) mObj)) {
				
				AttackPower attack = getCurrentAttack();
				if (attack != null && attack.getAttack(actor).isValidTarget((Creature) mObj)) {
					
					in.uiPrepareTarget((Creature) mObj);
					in.uiPerformAttack(attack);
					
				}
				
			}
		}
	}
	
	private void formMouseReleased(MouseEvent e) {
		// checkMovePointClicked(e);
		checkAttackOrder(new Point(e.getX(), e.getY()));
		
		
		checkMoveClicked(e);
		
		if (dragObj != null) {
			Point p;
			p = translateMousePosToBoardPos(new Point(e.getX(), e.getY()));
			
			finishCreatureDrag(p.x, p.y);
		}
	}
	
	private void formMousePressed(MouseEvent evt) {
		int mX, mY;
		mX = evt.getX();
		mY = evt.getY();
		
		MapObject obj = b.getObjectAtCoordinates(mX / 50, mY / 50);
		
		if (obj instanceof Creature && canDrag) {
			System.out.println("Dragging " + obj.toString());
			startCreatureDrag((Creature) obj);
		} else {
			// System.out.println("Nothing clicked");
		}
		
	}
	
	private void startCreatureDrag(Creature c) {
		dragObj = c;
	}
	
	private void finishCreatureDrag(int x, int y) {
		if (dragObj != null) {
			
			if (!isLegalDragLocation(x, y)) {
				System.out.println("Square already occupied");
				dragObj = null;
				repaint();
			} else {
				System.out.println("Finish drag");
				b.updatePosition(dragObj, x, y);
				dragObj = null;
				repaint();
			}
			
		}
	}
	
	private boolean isLegalDragLocation(int x, int y) {
		if (!(b.getObjectAtCoordinates(x, y) instanceof FreeTerrain)) {
			return false;
		} else {
			return true;
		}
	}
	
	private void formMouseDragged(MouseEvent evt) {
		// dragMousePos.setLocation(evt.getPoint());
		repaint();
	}
	
	private void formMouseMoved(MouseEvent evt) {
		requestFocus();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		
		g.clearRect(0, 0, getWidth(), getHeight());
		
		drawObjects(g);
		
		drawGrid(g);
		
		drawHighlightedPath(g);
		
		// drawTreeGrid(g);
		
		drawMoveSteps(g);
		
		drawDrag(g);
		
		drawEffects(g);
		
		drawMouseOverInfo(g);
		drawHead(g);
	}
	
	private void drawHead(Graphics g) {
		Creature actor = Encounter.getCurrentEncounter().getCurrentTurnActor();
		
		if (actor == null)
			return;
		
		UserInterfaceIntelligence in = UserInterfaceIntelligence
				.getCurrentUii();
		
		if (in == null)
			return;
		
		AttackPower attack = getCurrentAttack();
		
		if (attack == null)
			return;
		
		Tooltips.drawText(g, "Current attack: " + attack.getDescription(),
				new Point(10, 30), Color.white);
		
	}
	
	private AttackPower getCurrentAttack() {
		UserInterfaceIntelligence in = UserInterfaceIntelligence
				.getCurrentUii();
		
		if (in == null)
			return null;
		
		return in.getUiSelectedAttack();
	}
	
	private void drawMouseOverInfo(Graphics g) {
		
		Point mP = getMousePosition();
		
		if (mP == null) {
			return;
		}
		
		Point p;
		p = translateMousePosToBoardPos(mP);
		
		MapObject mObj = b.getObjectAtCoordinates(p.x, p.y);
		Creature actor = Encounter.getCurrentEncounter().getCurrentTurnActor();
		
		if (mObj instanceof Creature) {
			
			if (actor.isEnemyOf((Creature) mObj)) {
				
				AttackPower attack = getCurrentAttack();
				if (attack != null && attack.getAttack(actor).isValidTarget((Creature) mObj)) {
					// g.drawImage(Tooltips.ttAttack, mP.x - 50, mP.y - 20,
					// null);
					// Engine.log("InfoDebug!");
					
					g.setColor(new Color(255, 0, 0));
					((Graphics2D) g).setStroke(new BasicStroke(5));
					g.drawRect((mP.x / 50) * 50, (mP.y / 50) * 50, 50, 50);
					
					Tooltips.drawText(g, "Attack", mP, Color.red);
				}
				
			}
		}
		
	}
	
	private void drawEffects(Graphics g) {
		Iterator<Sfx> it = effects.iterator();
		Sfx e;
		boolean ret;
		while (it.hasNext()) {
			e = it.next();
			ret = e.draw(g);
			if (!ret) {
				effects.remove(e);
			}
		}
	}
	
	private void drawDrag(Graphics g) {
		if (dragObj == null)
			return;
		
		Image i = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
		
		Graphics ig = i.getGraphics();
		
		dragObj.draw(ig);
		
		Point b = getMousePosition();
		
		if (b == null) {
			return;
		}
		
		int x = (int) b.getX();
		int y = (int) b.getY();
		
		if (isLegalDragLocation(x / 50, y / 50)) {
			g.setColor(new Color(0, 255, 0));
		} else {
			g.setColor(new Color(255, 0, 0));
		}
		((Graphics2D) g).setStroke(new BasicStroke(5));
		g.drawRect((x / 50) * 50, (y / 50) * 50, 50, 50);
		
		g.drawImage(i, x - 25, y - 25, 50, 50, null);
	}
	
	private void drawTreeGrid(Graphics g) {
		Point topleft = new Point(0, 0);
		Point bottomright = new Point(20, 20);
		drawTreeGridSub(g, b.getTree(), topleft, bottomright, 0);
	}
	
	private void drawTreeGridSub(Graphics g, MapTreeObject obj, Point topleft,
			Point bottomright, int color) {
		MapTreeWare w;
		
		Graphics2D g2d = (Graphics2D) g;
		
		if (obj instanceof MapTreeWare) {
			w = (MapTreeWare) obj;
			
			g2d.setColor(treeColors[color % treeColors.length]);
			g2d.setStroke(new BasicStroke(5));
			
			int x1, y1, x2, y2;
			
			if (w.isVertical()) {
				
				x1 = w.getPosition();
				y1 = topleft.y;
				x2 = w.getPosition();
				y2 = bottomright.y;
				
				g2d.drawLine(x1 * 50, y1 * 50, x2 * 50, y2 * 50);
				
				drawTreeGridSub(g, w.getPartA(), topleft,
						new Point(w.getPosition(), bottomright.y), color + 1);
				
				drawTreeGridSub(g, w.getPartB(), new Point(w.getPosition(),
						topleft.y), bottomright, color + 1);
				
			} else {
				x1 = topleft.x;
				y1 = w.getPosition();
				x2 = bottomright.x;
				y2 = w.getPosition();
				g2d.drawLine(x1 * 50, y1 * 50, x2 * 50, y2 * 50);
				
				drawTreeGridSub(g, w.getPartA(), topleft, new Point(
						bottomright.x, w.getPosition()), color + 1);
				
				drawTreeGridSub(g, w.getPartB(),
						new Point(topleft.x, w.getPosition()), bottomright,
						color + 1);
			}
			
		}
		
	}
	
	private void drawObjects(Graphics g) {
		
		drawObjectsSub(g, b.getTree());
		
	}
	
	private void drawObjectsSub(Graphics g, MapTreeObject obj) {
		if (obj instanceof MapTreeWare) {
			drawObjectsSub(g, ((MapTreeWare) obj).getPartA());
			drawObjectsSub(g, ((MapTreeWare) obj).getPartB());
		} else if (obj instanceof MapTreeHa) {
			for (MapObject o : ((MapTreeHa) obj).getObjects()) {
				drawObject(g, o);
			}
		}
	}
	
	private void drawObject(Graphics g, MapObject obj) {
		Image i = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
		
		if (Encounter.getCurrentEncounter().getCurrentTurnActor() == obj) {
			
			((Graphics2D) g).setColor(new Color(0, 255, 0, 128));
			
			g.fillRect((int) obj.getPosition().getX() * 50, (int) obj
					.getPosition().getY() * 50, 50, 50);
		}
		
		obj.draw(i.getGraphics());
		
		g.drawImage(i, (int) obj.getPosition().getX() * 50, (int) obj
				.getPosition().getY() * 50, 50, 50, null);
		
	}
	
	private void drawGrid(Graphics g) {
		int size = 50;
		int maxX = getSize().width / size;
		int maxY = getSize().height / size;
		
		((Graphics2D) g).setColor(new Color(0, 0, 0, 255));
		
		for (int x = 0; x <= maxX; x++) {
			
			g.drawLine(x * size, 0, x * size, getSize().height);
			
		}
		
		for (int y = 0; y <= maxY; y++) {
			
			g.drawLine(0, y * size, getSize().width, y * size);
			
		}
		
	}
	
	private void drawMoveSteps(Graphics g) {
		if (moveStepObject == null) {
			return;
		}
		
		int[] tX = { -1, 0, 1, -1, 1, -1, 0, 1 };
		int[] tY = { -1, -1, -1, 0, 0, 1, 1, 1 };
		
		Point p = moveStepObject.getPosition();
		
		((Graphics2D) g).setColor(new Color(0, 255, 0, 128));
		
		int x, y;
		for (int i = 0; i < tX.length; i++) {
			
			x = p.x + tX[i];
			y = p.y + tY[i];
			
			if (!(b.getObjectAtCoordinates(x, y) instanceof FreeTerrain)) {
				continue;
			}
			
			g.fillOval(x * 50 + 20, y * 50 + 20, 10, 10);
			
		}
		
	}
	
	private void drawHighlightedPath(Graphics g) {
		if (!movePrepared || highlightedPath == null) {
			return;
		}
		
		int i = 0;
		Color[] colors;
		for (Point p : highlightedPath) {
			int x, y;
			x = p.x;
			y = p.y;
			
			i++;
			if (i > highlightedPathLengthLimit) {
				colors = new Color[] { new Color(255, 128, 64, 64),
						new Color(200, 100, 48) };
			} else {
				colors = new Color[] { new Color(0, 255, 0, 64),
						new Color(0, 200, 0) };
			}
			
			// Engine.log("x: %d, y: %d", x, y);
			
			((Graphics2D) g).setStroke(new BasicStroke(5));
			
			g.setColor(colors[0]);
			g.fillRect(x * 50, y * 50, 50, 50);
			
			g.setColor(colors[1]);
			g.drawRect(x * 50, y * 50, 50, 50);
			
		}
		
	}
	
	public void showPath(List<Point> path, int limit) {
		if (path == null) {
			movePrepared = false;
			highlightedPath = null;
		} else {
			movePrepared = true;
			highlightedPath = path;
			highlightedPathLengthLimit = limit;
		}
		
	}
	
	public void addEffect(Sfx effect) {
		effects.add(effect);
	}

	public boolean isUnoccupied(int x, int y) {
		return false;
	}
	
}
