package tc.oc.pgm.itemmeta;

import java.util.Set;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import tc.oc.item.Items;
import tc.oc.material.MaterialMatcher;

public class ItemRule {
  final MaterialMatcher items;
  final PotionMeta meta;

  public ItemRule(MaterialMatcher items, PotionMeta meta) {
    this.items = items;
    this.meta = meta;
  }

  public boolean matches(ItemStack stack) {
    return items.matches(stack);
  }

  public void apply(ItemStack stack) {
    Items.addPotionEffects(stack, this.meta.getCustomEffects());

    ItemMeta meta = stack.getItemMeta();
    if (meta != null) {
      if (this.meta.hasDisplayName()) {
        meta.setDisplayName(this.meta.getDisplayName());
      }

      if (this.meta.hasLore()) {
        meta.setLore(this.meta.getLore());
      }

      Set<ItemFlag> flags = this.meta.getItemFlags();
      meta.addItemFlags(flags.toArray(new ItemFlag[flags.size()]));

      Items.addEnchantments(meta, this.meta.getEnchants());

      for (String attribute : this.meta.getModifiedAttributes()) {
        for (AttributeModifier modifier : this.meta.getAttributeModifiers(attribute)) {
          meta.addAttributeModifier(attribute, modifier);
        }
      }

      if (this.meta.spigot().isUnbreakable()) meta.spigot().setUnbreakable(true);
      meta.setCanDestroy(Items.unionMaterials(meta.getCanDestroy(), this.meta.getCanDestroy()));
      meta.setCanPlaceOn(Items.unionMaterials(meta.getCanPlaceOn(), this.meta.getCanPlaceOn()));

      stack.setItemMeta(meta);
    }
  }
}
