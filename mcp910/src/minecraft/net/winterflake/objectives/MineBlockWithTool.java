package net.winterflake.objectives;
import java.util.ArrayList;
/**
 *
 * @author leijurv
 */
public class MineBlockWithTool extends MultiAndObjective {
    public MineBlockWithTool(int x, int y, int z, int itemID) {
        super(create(x, y, z, itemID));
    }
    public static ArrayList<ChildObjective> create(int x, int y, int z, int itemID) {
        ArrayList<ChildObjective> res = new ArrayList<>();
        AquireItemObjective item = AquireItemObjective.getAquireItemObjective(itemID, 1, false);
        res.add(item);//Aquire the tool needed
        res.add(new MovementObjective(10000, x, y, z, true));
        res.add(new LookAtBlockObjective(x, y, z));
        res.add(new DoMineBlockObjective(x, y, z, item));
        return res;
    }
    @Override
    public double getPriority(ChildObjective o) {
        //Return getBlock(x,y,z).getAmountofTimetoMineWithTool(itemID)
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
