package net.raccoon.will.sapientia.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.raccoon.will.sapientia.common.item.RevolverItem;
import net.raccoon.will.sapientia.core.registry.SapComponents;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RouletteCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("roulette")
                        //roulette bullets
                        .then(
                                Commands.literal("bullets")
                                        .then(
                                                Commands.argument("amount", IntegerArgumentType.integer(1, 12))
                                                        .executes(context -> {
                                                            Player player = context.getSource().getPlayerOrException();
                                                            int amount = IntegerArgumentType.getInteger(context, "amount");


                                                            ItemStack[] stacks = {player.getMainHandItem(), player.getOffhandItem()};
                                                            ItemStack gunStack = null;

                                                            for (ItemStack stack : stacks) {
                                                                if (stack.getItem() instanceof RevolverItem) {
                                                                    gunStack = stack;
                                                                    break;
                                                                }
                                                            }

                                                            if (gunStack != null) {
                                                                RevolverItem gun = (RevolverItem) gunStack.getItem();
                                                                int chambers = gun.getNumChambers(gunStack);

                                                                List<Integer> bullets = new ArrayList<>();
                                                                Random random = new Random();
                                                                while (bullets.size() < amount) {
                                                                    int pos = random.nextInt(chambers);
                                                                    if (!bullets.contains(pos)) bullets.add(pos);
                                                                }

                                                                gunStack.set(SapComponents.BULLET_CHAMBERS.get(), bullets);
                                                                gunStack.set(SapComponents.CURRENT_CHAMBER.get(), random.nextInt(chambers));

                                                                context.getSource().sendSuccess(
                                                                        () -> Component.literal("Loaded " + amount + " bullets into the gun"), false
                                                                );
                                                            } else {
                                                                context.getSource().sendFailure(
                                                                        Component.literal("You must hold a Revolver!")
                                                                );
                                                            }
                                                            return 1;}
                                                        )
                                        )
                        )
                        //roulette chambers
                        .then(
                                Commands.literal("chambers")
                                        .then(
                                                Commands.argument("amount", IntegerArgumentType.integer(1, 12))
                                                        .executes(context -> {
                                                            Player player = context.getSource().getPlayerOrException();
                                                            int amount = IntegerArgumentType.getInteger(context, "amount");

                                                            ItemStack[] stacks = {player.getMainHandItem(), player.getOffhandItem()};
                                                            ItemStack gunStack = null;

                                                            for (ItemStack stack : stacks) {
                                                                if (stack.getItem() instanceof RevolverItem) {
                                                                    gunStack = stack;
                                                                    break;
                                                                }
                                                            }


                                                            if (gunStack != null) {
                                                                gunStack.set(SapComponents.NUM_CHAMBERS.get(), amount);

                                                                List<Integer> bullets = new ArrayList<>(gunStack.getOrDefault(SapComponents.BULLET_CHAMBERS.get(), new ArrayList<>()));
                                                                bullets.removeIf(b -> b >= amount);
                                                                gunStack.set(SapComponents.BULLET_CHAMBERS.get(), bullets);

                                                                int current = gunStack.getOrDefault(SapComponents.CURRENT_CHAMBER.get(), 0);
                                                                if (current >= amount) gunStack.set(SapComponents.CURRENT_CHAMBER.get(), 0);

                                                                context.getSource().sendSuccess(
                                                                        () -> Component.literal("Set gun chambers to " + amount), false
                                                                );
                                                            } else {
                                                                context.getSource().sendFailure(
                                                                        Component.literal("You must hold a Revolver!")
                                                                );
                                                            }
                                                            return 1;
                                                        })
                                        )
                        )
        );
    }
}