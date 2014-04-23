package dd.Creature.Kaspar;

import java.awt.Toolkit;
import java.util.List;

import dd.Actions.Default.Action;
import dd.Creature.Creature;
import dd.Encounter.Encounter;
import dd.Encounter.StatusEffects.StatusEffect;
import dd.Engine.CreatureEffects;
import dd.Engine.CreatureEvents;
import dd.Engine.Engine;
import dd.Engine.CreatureEbents.Listener.DieListener;
import dd.Engine.CreatureEbents.Listener.GetPerformableActionsListener;
import dd.Engine.Durations.Effect;
import dd.Engine.Durations.UntilStartOfNextTurn;
import dd.Power.Power;
import dd.Power.Power.ActionType;

/**
 * Level 1 Hexblade
 * Dragonborn
 * Infernal Pact
 * @author kaspar
 *
 */
public class Rimas extends Creature {
	
	private boolean bladeManifested = false;
	
	private class ManifestBladeAction extends Action {
		
		public ManifestBladeAction(Creature performer) {
			super(performer);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public ActionType getType() {
			return ActionType.Minor;
		}
		
		@Override
		protected void execute() {
			((Rimas) performer).bladeManifested = true;
		}
		
		@Override
		public String getDescription() {
			return "Manifest Blade of Annihilation";
		}
		
	}
	
	static {
		
		CreatureEvents
				.addGetPerformableActionsListener(new GetPerformableActionsListener() {
					
					@Override
					public void getPerformableActions(Creature c,
							List<Action> actions) {
						if (c instanceof Rimas) {
							Rimas r = (Rimas) c;
							
							actions.add(new EldritchBolt(r));
							
							if (!r.bladeManifested) {
								actions.add(r.getManifestAction());
							} else {
								actions.add(new SoulEater(r));
							}
						}
					}
				});
		CreatureEvents.addDieListener(new DieListener() {
			
			@Override
			public void die(Creature creature, Creature killer) {
				
				if (killer instanceof Rimas) {
					((Rimas) killer).soulFeast();
				}
				
				for (Creature c : Encounter.getCurrentEncounter().combatants) {
					if (c == killer)
						continue;
					
					if (Engine.getCreatureDistance(c, creature) == 1) {
						if (c instanceof Rimas) {
							((Rimas) c).soulFeast();
						}
					}
				}
				
			}
		});
	}
	
	private class SoulFeastCooldown extends StatusEffect {
		
		@Override
		public String getDescription() {
			return "Soul Feast cooldown";
		}
		
	}
	
	public Rimas() {
		super("Rimas", Toolkit.getDefaultToolkit().getImage(
				"Images\\Rimas.png"));
		
		this.faction = "Hero";
		
		this.ac = 17;
		this.fort = 15;
		this.ref = 12;
		this.will = 15;
		
		this.maxHp = 30;
		this.hp = maxHp;
		
		this.ini = 1;
		
	}
	
	protected Action getManifestAction() {
		return new ManifestBladeAction(this);
	}
	
	private void soulFeast() {
		
		if (CreatureEffects.hasStatusEffect(this,
				SoulFeastCooldown.class)) {
			return;
		}
		
		CreatureEffects.addStatusEffect(new Effect(this,
				new SoulFeastCooldown(), new UntilStartOfNextTurn(this)));
		
		int amount = 4;
		Engine.log("%s gains %d temporary hitpoints (%s)", getName(), amount,
				"Soul Feast");
		
		this.giveTemporaryHitpoints(amount);
	}
	
	@Override
	public int getDefaultSpeed() {
		return 5;
	}

	@Override
	public List<Action> getOpportunityActions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Power> getPowers() {
		// TODO Auto-generated method stub
		return null;
	}
}
