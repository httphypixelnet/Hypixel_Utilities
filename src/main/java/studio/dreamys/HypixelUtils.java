package studio.dreamys;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.scoreboard.*;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.ClientCommandHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HypixelUtils {
    public static final Minecraft mc = Minecraft.getMinecraft();

    public static boolean hasLine(String sbString) {
        ScoreObjective sbo;
        if (mc != null && mc.thePlayer != null && (sbo = mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(1)) != null) {
            List<String> scoreboard = getSidebarLines();
            scoreboard.add(StringUtils.stripControlCodes((String)sbo.getDisplayName()));
            for (String s : scoreboard) {
                String validated = stripString(s);
                if (!validated.contains(sbString)) continue;
                return true;
            }
        }
        return false;
    }


    public static String stripString(String s) {
        char[] nonValidatedString = StringUtils.stripControlCodes((String)s).toCharArray();
        StringBuilder validated = new StringBuilder();
        for (char a : nonValidatedString) {
            if (a >= '\u007f' || a <= '\u0014') continue;
            validated.append(a);
        }
        return validated.toString();
    }

    private static List<String> getSidebarLines() {
        ArrayList<String> lines = new ArrayList<String>();
        Scoreboard scoreboard = mc.theWorld.getScoreboard();
        if (scoreboard == null) {
            return lines;
        }
        ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(1);
        if (objective == null) {
            return lines;
        }
        Collection<Score> scores = scoreboard.getSortedScores(objective);
        ArrayList list = new ArrayList();
        for (Score s : scores) {
            if (s == null || s.getPlayerName() == null || s.getPlayerName().startsWith("#")) continue;
            list.add(s);
        }
        scores = list.size() > 15 ? Lists.newArrayList((Iterable) Iterables.skip(list, (int)(scores.size() - 15))) : list;
        for (Score score : scores) {
            ScorePlayerTeam team = scoreboard.getPlayersTeam(score.getPlayerName());
            lines.add(ScorePlayerTeam.formatPlayerName((Team)team, (String)score.getPlayerName()));
        }
        return lines;
    }

    public static void sendCommand(String command) {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            ClientCommandHandler.instance.executeCommand(Minecraft.getMinecraft().thePlayer, command);
        });
    }
}
