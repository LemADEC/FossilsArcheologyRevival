package fossilsarcheology.server.item;

import fossilsarcheology.Revival;
import fossilsarcheology.server.entity.EntityDinosaurEgg;
import fossilsarcheology.server.entity.EntityPrehistoric;
import fossilsarcheology.server.enums.PrehistoricAI;
import fossilsarcheology.server.enums.PrehistoricEntityType;
import fossilsarcheology.server.message.MessageUpdateEgg;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DinoEggItem extends Item {
    public static final int TypeCount = PrehistoricEntityType.values().length;
    private PrehistoricEntityType dino;

    public DinoEggItem(PrehistoricEntityType dino) {
        super();
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.maxStackSize = 1;
        this.dino = dino;
    }

    public static boolean spawnCreature(World world, PrehistoricEntityType prehistoricEnum, double x, double y, double z) {
        Object egg;
        if (!prehistoricEnum.isAquatic()) {
            egg = new EntityDinosaurEgg(world, prehistoricEnum);
            ((Entity) egg).setLocationAndAngles(x, y + 1.0F, z, world.rand.nextFloat() * 360.0F, 0.0F);
            if (!world.isRemote) {
                world.spawnEntityInWorld((Entity) egg);
            }
            ((EntityDinosaurEgg) egg).selfType = prehistoricEnum;
            if (!world.isRemote)
                Revival.NETWORK_WRAPPER.sendToAll(new MessageUpdateEgg(((EntityDinosaurEgg) egg).getEntityId(), prehistoricEnum.ordinal()));
            return true;
        } else {
            egg = prehistoricEnum.invokeClass(world);
            if (egg != null) {
                ((Entity) egg).setLocationAndAngles(x, y + 1, z, world.rand.nextFloat() * 360.0F, 0.0F);
                if (!world.isRemote) {
                    world.spawnEntityInWorld((Entity) egg);
                }
                if (egg instanceof EntityPrehistoric) {
                    EntityPrehistoric prehistoric = (EntityPrehistoric) egg;
                    if (prehistoric.getTameType() == PrehistoricAI.Taming.IMPRINTING) {
                        prehistoric.setTamed(true);
                        prehistoric.setAgeInDays(1);
                        prehistoric.setOwnerName(world.getClosestPlayerToEntity(prehistoric, 10).getDisplayName());
                    }
                }
            }
        }
        return egg != null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iicon) {
        this.itemIcon = iicon.registerIcon("fossil:prehistoric/dinoEggs/" + dino.name() + "_Egg");
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, int i, float f, float f1, float f2) {
        boolean b = spawnCreature(world, dino, (double) ((float) x + 0.5F), (double) ((float) y), (double) ((float) z + 0.5F));
        if (b && !player.capabilities.isCreativeMode) {
            --stack.stackSize;
        }
        return b;
    }
}
