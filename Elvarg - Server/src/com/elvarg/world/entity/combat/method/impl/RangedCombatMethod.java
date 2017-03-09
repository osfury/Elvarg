package com.elvarg.world.entity.combat.method.impl;

import com.elvarg.world.entity.combat.CombatType;
import com.elvarg.world.entity.combat.hit.QueueableHit;
import com.elvarg.world.entity.combat.method.CombatMethod;
import com.elvarg.world.entity.combat.ranged.RangedData;
import com.elvarg.world.entity.combat.ranged.RangedData.AmmunitionData;
import com.elvarg.world.entity.combat.ranged.RangedData.RangedWeaponData;
import com.elvarg.world.entity.combat.ranged.RangedData.RangedWeaponType;
import com.elvarg.world.entity.impl.Character;
import com.elvarg.world.entity.impl.player.Player;
import com.elvarg.world.model.Animation;
import com.elvarg.world.model.Graphic;
import com.elvarg.world.model.GraphicHeight;
import com.elvarg.world.model.Projectile;
/**
 * The ranged combat method.
 * @author Gabriel Hannason
 */
public class RangedCombatMethod implements CombatMethod {

	@Override
	public CombatType getCombatType() {
		return CombatType.RANGED;
	}

	@Override
	public QueueableHit[] fetchDamage(Character character, Character target) {

		//Darkbow is double hits.
		if(character.getCombat().getRangedWeaponData() != null &&
				character.getCombat().getRangedWeaponData() == RangedWeaponData.DARK_BOW) {
			return new QueueableHit[]{new QueueableHit(character, target, this, true, 2), 
					new QueueableHit(character, target, this, true, 3)};
		}

		return new QueueableHit[]{new QueueableHit(character, target, this, true, 2)};
	}

	@Override
	public boolean canAttack(Character character, Character target) {

		if(character.isNpc()) {
			return true;
		}

		Player p = character.getAsPlayer();

		if(!RangedData.checkAmmo(p)) {
			return false;
		}

		return true;
	}

	@Override
	public void onQueueAdd(Character character, Character target) {
		if(character.isPlayer()) {
			AmmunitionData ammo = character.getAsPlayer().getCombat().getAmmunition();
			RangedWeaponData rangedWeapon = character.getCombat().getRangedWeaponData();

			int projectileId = ammo.getProjectileId();
			int delay = 40;
			int speed = 60;
			int heightEnd = 31;
			int heightStart = 43;
			int curve = 0;

			if(rangedWeapon.getType() == RangedWeaponType.CROSSBOW) {
				delay = 46;
				speed = 62;
				heightStart = 44;
				heightEnd = 35;
				curve = 3;
			} else if(ammo == AmmunitionData.TOKTZ_XIL_UL) {
				delay = 30;
				speed = 55;
			}

			new Projectile(character, target, projectileId, delay, speed, heightStart, heightEnd, curve).sendProjectile();

			RangedData.decrementAmmo(character.getAsPlayer(), target.getPosition());

			//Dark bow sends two arrows, so send another projectile and delete another arrow.
			if(character.getCombat().getRangedWeaponData() == RangedWeaponData.DARK_BOW) {
				//new Projectile(character, target, ammo.getProjectileId(), ammo.getProjectileDelay() + 35, ammo.getProjectileSpeed() + 28, ammo.getStartHeight(), ammo.getEndHeight(), 0).sendProjectile();			
				new Projectile(character, target, ammo.getProjectileId(), 60, 80, 43, 31, 0).sendProjectile();
				RangedData.decrementAmmo(character.getAsPlayer(), target.getPosition());
			}
		}
	}

	@Override
	public int getAttackSpeed(Character character) {
		return character.getBaseAttackSpeed();
	}

	@Override
	public int getAttackDistance(Character character) {
		if(character.isPlayer()) {
			return character.getAsPlayer().getCombat().
					getRangedWeaponData().getType().getDistanceRequired();
		}
		return 6;
	}

	@Override
	public void startAnimation(Character character) {
		int animation = character.getAttackAnim();

		if(animation != -1) {
			character.performAnimation(new Animation(animation));
		}

		if(character.isPlayer()) {
			AmmunitionData ammo = character.getAsPlayer().getCombat().getAmmunition();
			if(ammo.getStartGraphic() != null) {
				character.getAsPlayer().performGraphic(ammo.getStartGraphic());
			}
		}
	}

	@Override
	public void finished(Character character) {

	}

	@Override
	public void handleAfterHitEffects(QueueableHit hit) {

	}

}