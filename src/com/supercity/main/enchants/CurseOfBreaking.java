package com.supercity.main.enchants;

import com.supercity.main.event.result.DamageResult;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

public class CurseOfBreaking extends CustomEnchant {


    @Override
    public String getId() {
        return "breaking_curse";
    }

    @Override
    public String getName() {
        return "Curse of Breaking";
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getTarget() {
        return EnchantmentTarget.ALL;
    }

    @Override
    public boolean hasConflict(Enchantment e) {
        return false;
    }

    @Override
    public boolean hasConflict(CustomEnchant e) {
        return false;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return true;
    }

    @Override
    public EnchRarity getRarity() {
        return EnchRarity.RARE;
    }

    @Override
    protected DamageResult onLoseDurability(Player player, ItemStack item, int damage) {
        return new DamageResult(false,damage * 2);
    }
}
