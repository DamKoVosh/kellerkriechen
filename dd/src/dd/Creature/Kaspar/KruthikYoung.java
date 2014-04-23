package dd.Creature.Kaspar;

import java.awt.Toolkit;
import java.util.List;

import dd.Actions.Attack.MeleeAttack;
import dd.Actions.Default.Action;
import dd.Creature.Creature;
import dd.Creature.Creature.DamageType;
import dd.Encounter.Encounter;
import dd.Encounter.Roll;
import dd.Engine.CreatureEvents;
import dd.Engine.Engine;
import dd.Engine.CreatureEbents.Listener.EndTurnListener;
import dd.Engine.CreatureEbents.Listener.GetPerformableActionsListener;
import dd.Intelligence.MeleeAI;
import dd.Power.Power;

/**
 * MonsterManual p.170
 * 
 * @author kaspar
 * 
 */
public class KruthikYoung extends Creature {
	
	static {
		CreatureEvents.addEndTurnListener(new EndTurnListener() {
			
			@Override
			public void endTurn(Creature c) {
				
				for (Creature comb : Encounter.getCurrentEncounter().combatants) {
					if (comb instanceof KruthikYoung) {
						if (c.isEnemyOf(comb)) {
							if (Engine.getCreatureDistance(comb, c) <= 1) {
								
								c.takeDamage(2, DamageType.Untyped, comb,
										"Gnashing Horde Aura");
								
							}
						}
					}
				}
				
			}
		});
		
		CreatureEvents
				.addGetPerformableActionsListener(new GetPerformableActionsListener() {
					
					@Override
					public void getPerformableActions(Creature c,
							List<Action> actions) {
						if (c instanceof KruthikYoung) {
							actions.add(new MeleeAttack(c) {
								
								@Override
								protected int getAttackBase() {
									return 5;
								}
								
								@Override
								protected void rollDamage() {
									damage = Roll.r("1d8 + 2");
								}
								
								@Override
								public String getDescription() {
									return "Claw";
								}
								
							});
						}
					}
				});
	}
	
	public KruthikYoung() {
		super(
				"Kruthik Young",
				Toolkit.getDefaultToolkit()
						.getImage(
								"Images\\Kruthik young.png"));
		
		this.intelligence = new MeleeAI();
		
		this.faction = "Kruthik";
		
		this.ac = 15;
		this.fort = 13;
		this.ref = 14;
		this.will = 11;
		
		this.maxHp = 43;
		this.hp = maxHp;
		
		this.ini = 4;
	}
	
	@Override
	protected int getDefaultSpeed() {
		return 3;
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
