package com.example.sneakgrow;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.state.BlockState;

public class SneakGrow implements ModInitializer {
   public static final int RADIUS = 4;
   public static final int VERTICAL = 2;
   public static final int INTERVAL = 4;
   public static final int EXTRA_TICKS = 4;
   private int counter = 0;

   public void onInitialize() {
      ServerTickEvents.END_SERVER_TICK.register((ServerTickEvents.EndTick)(var1) -> {
         if (++this.counter % 4 == 0) {
            for(ServerPlayer var3 : var1.getPlayerList().getPlayers()) {
               if (!var3.isSpectator() && var3.isShiftKeyDown()) {
                  boostAround(var3);
               }
            }

         }
      });
   }

   private static boolean isBoostable(Block var0) {
      return var0 instanceof CropBlock || var0 instanceof StemBlock || var0 instanceof SaplingBlock || var0 instanceof SugarCaneBlock;
   }

   private static void boostAround(ServerPlayer var0) {
      ServerLevel var1 = var0.level();
      RandomSource var2 = var1.getRandom();
      BlockPos var3 = var0.blockPosition();

      for(BlockPos var5 : BlockPos.betweenClosed(var3.offset(-4, -2, -4), var3.offset(4, 2, 4))) {
         BlockState var6 = var1.getBlockState(var5);
         Block var7 = var6.getBlock();
         if (isBoostable(var7)) {
            BlockPos var8 = var5.immutable();

            for(int var9 = 0; var9 < 4; ++var9) {
               var6 = var1.getBlockState(var8);
               if (var6.getBlock() != var7) {
                  break;
               }

               var6.randomTick(var1, var8, var2);
            }

            if (var2.nextInt(6) == 0) {
               var1.levelEvent(1505, var8, 0);
            }
         }
      }

   }
}
