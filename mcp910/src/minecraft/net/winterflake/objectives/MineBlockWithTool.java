package net.winterflake.objectives;

import java.util.ArrayList;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * TODO make this class work
 * 
 * @author leijurv
 */
public class MineBlockWithTool extends MultiAndObjective {
	
	final int x;
	final int y;
	final int z;
	final Item itemType;
	
	public MineBlockWithTool(int x, int y, int z, Item itemType) {
		super(create(x, y, z, itemType));
		this.x = x;
		this.y = y;
		this.z = z;
		this.itemType = itemType;
	}
	
	public static ArrayList<Objective> create(int x, int y, int z, Item itemType) {
		ArrayList<Objective> res = new ArrayList<>();
		AquireItemObjective item = AquireItemObjective.getAquireItemObjective(new ItemStack(itemType, 1), Need.MULTI);
		res.add(item);// Aquire the tool needed
		res.add(new MovementObjective(10000, x, y, z, true));
		res.add(new LookAtBlockObjective(x, y, z));
		res.add(new DoMineBlockObjective(x, y, z, item));
		return res;
	}
	
	@Override
	public double getPriority(Objective o) {
		// Return getBlock(x,y,z).getAmountofTimetoMineWithTool(itemID)
		throw new UnsupportedOperationException("Not supported yet."); // To
																		// change
																		// body
																		// of
																		// generated
																		// methods,
																		// choose
																		// Tools
																		// |
																		// Templates.
	}
}
