package tc.oc.pgm.modules;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Set;
import java.util.logging.Logger;
import org.bukkit.Material;
import org.jdom2.Document;
import tc.oc.pgm.api.match.Match;
import tc.oc.pgm.map.MapModule;
import tc.oc.pgm.map.MapModuleContext;
import tc.oc.pgm.match.MatchModule;
import tc.oc.pgm.module.ModuleDescription;
import tc.oc.pgm.util.XMLUtils;
import tc.oc.xml.InvalidXMLException;
import tc.oc.xml.Node;

@ModuleDescription(name = "Tool Repair")
public class ToolRepairModule extends MapModule {
  protected final Set<Material> toRepair;

  public ToolRepairModule(Set<Material> toRepair) {
    this.toRepair = ImmutableSet.copyOf(toRepair);
  }

  @Override
  public MatchModule createMatchModule(Match match) {
    return new ToolRepairMatchModule(match, this.toRepair);
  }

  // ---------------------
  // ---- XML Parsing ----
  // ---------------------

  public static ToolRepairModule parse(MapModuleContext context, Logger logger, Document doc)
      throws InvalidXMLException {
    Set<Material> toRepair = Sets.newHashSet();
    for (Node toolRepairElement :
        Node.fromChildren(doc.getRootElement(), "tool-repair", "toolrepair")) {
      for (Node toolElement : Node.fromChildren(toolRepairElement.getElement(), "tool")) {
        toRepair.add(XMLUtils.parseMaterial(toolElement));
      }
    }
    if (toRepair.size() == 0) {
      return null;
    } else {
      return new ToolRepairModule(toRepair);
    }
  }
}
