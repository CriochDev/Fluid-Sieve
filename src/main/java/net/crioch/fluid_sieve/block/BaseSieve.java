package net.crioch.fluid_sieve.block;

import net.crioch.fluid_sieve.loot.context.FluidSieveLootContextTypes;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.context.ContextParameterMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

import java.util.Iterator;
import java.util.List;

public class BaseSieve extends Block implements Waterloggable {
    private static final VoxelShape selectionShape = VoxelShapes.union(
            createCuboidShape(0, 0, 0, 1, 16, 1),
            createCuboidShape(15, 0, 0, 16, 16, 1),
            createCuboidShape(0, 0, 15, 1, 16, 16),
            createCuboidShape(15, 0, 15, 16, 16, 16),
            createCuboidShape(1, 1, 0, 15, 14, 1),
            createCuboidShape(1, 1, 15, 15, 14, 16),
            createCuboidShape(0, 1, 1, 1, 14, 16),
            createCuboidShape(15, 1, 1, 16, 14, 16)
    );

    public BaseSieve(Settings settings, Identifier key) {
        super(settings.ticksRandomly().nonOpaque().registryKey(RegistryKey.of(RegistryKeys.BLOCK, key)));
        this.setDefaultState(
                this.getStateManager()
                        .getDefaultState()
                        .with(Properties.WATERLOGGED, false)
        );
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        if (state.get(Properties.WATERLOGGED)) {
            return Fluids.WATER.getStill(false);
        }
        return super.getFluidState(state);
    }

    @Override
    protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        boolean waterlogged = state.get(Properties.WATERLOGGED);

        Identifier id = Registries.FLUID.getId(waterlogged ? Fluids.WATER.getStill() : Fluids.EMPTY);

        List<ItemStack> loot = getLoot(id, world, state, pos, random);

        if (!loot.isEmpty()) {
            // Get the block entity below the sieve
            BlockPos blockPos = pos.down();
            BlockEntity blockEntity = world.getBlockEntity(blockPos);

            if (blockEntity instanceof Inventory inventory) {
                int inventorySize = inventory.size();
                boolean inventoryChanged = false;

                Iterator<ItemStack> iterator = loot.iterator();
                int firstEmptySlot = 0;
                while (iterator.hasNext() && firstEmptySlot < inventorySize) {
                    ItemStack stack = iterator.next();
                    int initialCount = stack.getCount();
                    firstEmptySlot = insert(stack, inventory, firstEmptySlot);
                    if (stack.isEmpty()) {
                        inventoryChanged = true;
                        iterator.remove();
                    } else if (initialCount - stack.getCount() > 0) {
                        inventoryChanged = true;
                    }
                }

                if (inventoryChanged) {
                    inventory.markDirty();
                }

                if (!loot.isEmpty()) {
                    spawnStacksInWorld(world, pos, loot);
                }
            } else {
                spawnStacksInWorld(world, pos, loot);
            }
        }
    }

    @Override
    protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockState downState = world.getBlockState(pos.down());
        return downState.isSideSolid(world, pos.up(), Direction.UP, SideShapeType.FULL) || downState.isOf(Blocks.HOPPER);
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
        if (this.canPlaceAt(state, world, pos)) {
            return state;
        }

        return Blocks.AIR.getDefaultState();
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos blockPos = ctx.getBlockPos();
        FluidState fluidState = ctx.getWorld().getFluidState(blockPos);
        return this.getDefaultState().with(Properties.WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.WATERLOGGED);
    }


    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return selectionShape;
    }

    @Override
    public VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
        return selectionShape;
    }

    private List<ItemStack> getLoot(Identifier fluidId, ServerWorld world, BlockState state, BlockPos pos, Random random) {
        Identifier path = fluidId.withPrefixedPath("sieve/");
        RegistryKey<LootTable> key = RegistryKey.of(RegistryKeys.LOOT_TABLE, path);

        LootTable lootTable = world.getServer().getReloadableRegistries().getLootTable(key);

        // Exit early if the loot table isn't defined
        if (lootTable.equals(LootTable.EMPTY)) {
            return List.of();
        }

        ContextParameterMap.Builder builder = new ContextParameterMap.Builder()
                .add(LootContextParameters.BLOCK_STATE, state)
                .add(LootContextParameters.ORIGIN, pos.toCenterPos());


        // Get all Entities within the sieve
        List<? extends Entity> entitiesWithinBlock = world.getEntitiesByType(TypeFilter.instanceOf(Entity.class), (livingEntity -> livingEntity.getBlockPos().equals(pos)));

        // If any are within it, add a random one as the 'this' entity for the loot table
        if (!entitiesWithinBlock.isEmpty()) {

            builder.addNullable(LootContextParameters.THIS_ENTITY, entitiesWithinBlock.get(random.nextInt(entitiesWithinBlock.size())));
        }

        ContextParameterMap map = builder.build(FluidSieveLootContextTypes.FLUID_SIEVE);

        LootWorldContext context = new LootWorldContext(world, map, null, 0);

        return lootTable.generateLoot(context);
    }

    private static void spawnStacksInWorld(ServerWorld world, BlockPos pos, List<ItemStack> stacks) {
        for (ItemStack stack : stacks) {
            Block.dropStack(world, pos, stack);
        }
    }

    private static int insert(ItemStack stack, Inventory inventory, int firstEmptySlot) {
        if (stack.isStackable()) {
            for (int slotIndex = firstEmptySlot; slotIndex < inventory.size(); slotIndex++) {
                ItemStack slot = inventory.getStack(slotIndex);
                if (slot.isOf(stack.getItem())) {
                    int maxStack = Math.min(slot.getMaxCount(), inventory.getMaxCountPerStack());
                    int amount = Math.min(maxStack - slot.getCount(), stack.getCount());
                    slot.increment(amount);
                    inventory.setStack(slotIndex, slot);
                    stack.decrement(amount);
                } else if (slot.isEmpty()) {
                    inventory.setStack(slotIndex, stack.copy());
                    stack.decrement(stack.getCount());
                }

                if (firstEmptySlot - slotIndex == 0 && slot.getMaxCount() - slot.getCount() == 0) {
                    firstEmptySlot++;
                }

                if (stack.isEmpty()) {
                    break;
                }
            }
        } else {
            for (int slotIndex = firstEmptySlot; slotIndex < inventory.size(); slotIndex++) {
                ItemStack slot = inventory.getStack(slotIndex);
                if (slot.isEmpty()) {
                    inventory.setStack(slotIndex, stack.split(1));
                    if (firstEmptySlot - slotIndex == 0) {
                        firstEmptySlot++;
                    }
                    if (stack.isEmpty()) {
                        break;
                    }
                }
            }
        }

        return firstEmptySlot;
    }
}
