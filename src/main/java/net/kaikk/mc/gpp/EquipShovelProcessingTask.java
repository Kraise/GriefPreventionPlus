/*
    GriefPrevention Server Plugin for Minecraft
    Copyright (C) 2015 Ryan Hamshire

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.kaikk.mc.gpp;

import org.bukkit.entity.Player;

//tells a player about how many claim blocks he has, etc
//implemented as a task so that it can be delayed
//otherwise, it's spammy when players mouse-wheel past the shovel in their hot bars
class EquipShovelProcessingTask implements Runnable {
	// player data
	private final Player player;

	public EquipShovelProcessingTask(Player player) {
		this.player = player;
	}

	@Override
	public void run() {
		// if he logged out, don't do anything
		if (!this.player.isOnline()) {
			return;
		}

		// if he's not holding the golden shovel anymore, do nothing
		if (this.player.getItemInHand().getType() != GriefPreventionPlus.getInstance().config.claims_modificationTool) {
			return;
		}

		final PlayerData playerData = GriefPreventionPlus.getInstance().getDataStore().getPlayerData(this.player.getUniqueId());

		final int remainingBlocks = playerData.getRemainingClaimBlocks();

		// if in basic claims mode...
		if (playerData.shovelMode == ShovelMode.Basic) {
			// tell him how many claim blocks he has available
			GriefPreventionPlus.sendMessage(this.player, TextMode.Instr, Messages.RemainingBlocks, String.valueOf(remainingBlocks));

			// link to a video demo of land claiming, based on world type
			if (GriefPreventionPlus.getInstance().creativeRulesApply(this.player.getWorld())) {
				GriefPreventionPlus.sendMessage(this.player, TextMode.Instr, Messages.CreativeBasicsVideo2, DataStore.CREATIVE_VIDEO_URL);
			} else {
				GriefPreventionPlus.sendMessage(this.player, TextMode.Instr, Messages.SurvivalBasicsVideo2, DataStore.SURVIVAL_VIDEO_URL);
			}
		}
	}
}
