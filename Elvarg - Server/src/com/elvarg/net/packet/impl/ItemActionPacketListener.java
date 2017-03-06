package com.elvarg.net.packet.impl;

import com.elvarg.cache.impl.definitions.ItemDefinition;
import com.elvarg.net.packet.Packet;
import com.elvarg.net.packet.PacketConstants;
import com.elvarg.net.packet.PacketListener;
import com.elvarg.world.content.Consumables;
import com.elvarg.world.content.skills.herblore.CreateFinishedPotionTask;
import com.elvarg.world.content.skills.herblore.CreateUnfinishedPotionTask;
import com.elvarg.world.content.skills.herblore.HerbIdentification;
import com.elvarg.world.entity.impl.player.Player;
import com.elvarg.world.model.Item;
import com.elvarg.world.model.teleportation.tabs.TabHandler;

@SuppressWarnings("unused")
public class ItemActionPacketListener implements PacketListener {

	private void onItemAction(final Player player, Packet packet) {
		int firstSlot = packet.readShort();
		int secondSlot = packet.readShortA();
		final Item first = player.getInventory().forSlot(firstSlot);
		final Item second = player.getInventory().forSlot(secondSlot);
		if (first == null || second == null) {
			return;
		}
		if (!player.getInventory().contains(new Item[] { first, second })) {
			return;
		}
		if (ItemDefinition.forId(first.getId()).getName().contains("(unf)")) {
			CreateFinishedPotionTask.attempt(player, first, 28);
			return;
		}
		if (first.getId() == CreateUnfinishedPotionTask.VIAL_OF_WATER
				&& ItemDefinition.forId(second.getId()).getName().contains("weed")
				|| ItemDefinition.forId(second.getId()).getName().contains("leaf")) {
			CreateUnfinishedPotionTask.attempt(player, second, 28);
			return;
		}
		player.getPacketSender().sendMessage("Nothing interesting happens..");
	}

	private void firstAction(final Player player, Packet packet) {
		int interfaceId = packet.readUnsignedShort();
		int itemId = packet.readShort();
		int slot = packet.readShort();
		final Item interacted = player.getInventory().forSlot(slot);
		if (interacted == null || interacted.getId() != itemId || interacted.getSlot() != slot) {
			return;
		}
		if (Consumables.isFood(player, interacted)) {
			return;
		}
		if (ItemDefinition.forId(interacted.getId()).getName().contains("Grimy")) {
			HerbIdentification.cleanHerb(player, interacted);
			return;
		}
		TabHandler.onClick(player, interacted);
		switch (interacted.getId()) {

		}
	}

	private void secondAction(final Player player, Packet packet) {
		int interfaceId = packet.readLEShortA();
		int slot = packet.readLEShort();
		int itemId = packet.readShortA();
		final Item interacted = player.getInventory().forSlot(slot);
		if (interacted == null || interacted.getId() != itemId || interacted.getSlot() != slot) {
			return;
		}
		switch (interacted.getId()) {

		}
	}

	private void thirdClickAction(final Player player, Packet packet) {
		int itemId = packet.readShortA();
		int slot = packet.readLEShortA();
		int interfaceId = packet.readLEShortA();
		final Item interacted = player.getInventory().forSlot(slot);
		if (interacted == null || interacted.getId() != itemId || interacted.getSlot() != slot) {
			return;
		}
		switch (interacted.getId()) {

		}
	}

	@Override
	public void handleMessage(Player player, Packet packet) {
		if (player.getHitpoints() <= 0) {
			return;
		}
		switch (packet.getOpcode()) {
		case PacketConstants.SECOND_ITEM_ACTION_OPCODE:
			secondAction(player, packet);
			break;
		case PacketConstants.FIRST_ITEM_ACTION_OPCODE:
			firstAction(player, packet);
			break;
		case PacketConstants.THIRD_ITEM_ACTION_OPCODE:
			thirdClickAction(player, packet);
			break;
		case PacketConstants.ITEM_ON_ITEM:
			onItemAction(player, packet);
			break;
		}
	}

}