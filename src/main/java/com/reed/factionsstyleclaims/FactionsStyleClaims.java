package com.reed.factionsstyleclaims;

import com.mojang.logging.LogUtils;
import com.reed.factionsstyleclaims.util.FSCSavedData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkConstants;
import org.slf4j.Logger;
import net.minecraftforge.fml.ModLoadingContext;

import java.util.Map;
import java.util.stream.Collectors;
@Mod(FactionsStyleClaims.MOD_ID)
public class FactionsStyleClaims
{
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "factionsstyleclaims";
    public FSCSavedData fscSavedData;
    public static MinecraftServer SERVER;

    public FactionsStyleClaims()
    {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // Some example code to dispatch IMC to another mod
        InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // Some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.messageSupplier().get()).
                collect(Collectors.toList()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
        SERVER = event.getServer();
        fscSavedData = SERVER.overworld().getDataStorage().computeIfAbsent(FSCSavedData::load, FSCSavedData::create, FSCSavedData.FSC_DATA);
        Map<String, Integer> teamPowerMap = fscSavedData.getTeamPowerMap();
        for(PlayerTeam team : SERVER.getScoreboard().getPlayerTeams()) {
            String name = team.getName();
            if (teamPowerMap.containsKey(name)) {
                LOGGER.info(name + " power : " + teamPowerMap.get(name).toString());
            } else {
                int power = 0;
                int maxPower = 0;
                LOGGER.info("Registering team power data for " + name);
                for (String player : team.getPlayers()) {
                    if (fscSavedData.getPowerMap().containsKey(player)) {
                        power += fscSavedData.getPowerMap().get(player);
                        maxPower += fscSavedData.getMaxPowerMap().get(player);
                    } else {
                        fscSavedData.getPowerMap().put(player, 10);
                        fscSavedData.getMaxPowerMap().put(player, 25);
                        power += 10;
                        maxPower += 25;
                    }
                }
                fscSavedData.getTeamPowerMap().put(name, power);
                fscSavedData.getTeamMaxPowerMap().put(name, maxPower);
                LOGGER.info(fscSavedData.getTeamPowerMap().toString());
                LOGGER.info(fscSavedData.getTeamMaxPowerMap().toString());
                fscSavedData.setDirty();
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Map<String, Integer> powerMap = fscSavedData.getPowerMap();
        String name = event.getPlayer().getScoreboardName();
        if (powerMap.containsKey(name)) {
            LOGGER.info(name + " power : " + powerMap.get(name).toString());
        } else {
            LOGGER.info("Registering power data for " + name);
            fscSavedData.getMaxPowerMap().put(name, 25);
            fscSavedData.getPowerMap().put(name, 10);
            LOGGER.info(fscSavedData.getPowerMap().toString());
            LOGGER.info(fscSavedData.getMaxPowerMap().toString());
            fscSavedData.setDirty();
        }
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents
    {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent)
        {
            // Register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }
}
