package com.elvarg.randomevents;

import com.elvarg.world.entity.impl.player.Player;
import com.elvarg.world.model.Item;
import com.elvarg.world.model.Position;

/**
 * Random events that take place during gameplay triggered by a random cycle or
 * event specified action(s).
 * 
 * TODO: Check functionality
 * 
 * @author Dennis
 *
 */
public enum RandomEvent {

	EVIL_CHICKEN {
		@Override
		public void executeEvent(Player player) {
			// TODO Auto-generated method stub
		}
	}, SANDWICH_LADY {
		@Override
		public void executeEvent(Player player) {
			// TODO Auto-generated method stub
		}
	}, ZOMBIE {
		@Override
		public void executeEvent(Player player) {
			// TODO Auto-generated method stub
		}
	}, MYSTERY_BOX {
		@Override
		public void executeEvent(Player player) {
			player.getMovementQueue().reset();
			if (player.getInventory().capacity() < 28)
			{
				player.getInventory().add(new Item(995, 50_000));
			} else {
				player.getPacketSender().sendMessage("You missed out on the Mystery Box random event!");
			}
		}
	}, RANDOM_MOVEMENT {
		//TODO: specify to your needs, just debug testing purposes.
		@Override
		public void executeEvent(Player player) {
			player.getMovementQueue().reset();
			player.getMovementQueue().addStep(
					new Position(player.getPosition().getX() +1 , player.getPosition().getY() , player.getPosition().getZ()));
		}
	}, RANDOM_INTERFACE {
		@Override
		public void executeEvent(Player player) {
			player.getMovementQueue().reset();
			player.getPacketSender().sendInterface(0);
		}
	};
	
	/**
	 * Sets the Random event.
	 * @param event
	 * @return event
	 */
	public static RandomEvent event(RandomEvent event)
	{
		return event;
	}
	
	/**
	 * Specifies the random event handling accordingly.
	 * @param player
	 */
	public abstract void executeEvent(Player player);
}