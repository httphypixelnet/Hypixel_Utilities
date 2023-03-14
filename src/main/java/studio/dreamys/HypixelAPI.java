package studio.dreamys;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import net.minecraft.client.Minecraft;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

public class HypixelAPI {
    public static String key = "f84fb818-27dc-491d-89b4-bb467a20bb2a";

    private static Gson gson = new Gson();

    public static String request(String url) {
        HttpClient client = HttpClientBuilder.create().build();

        HttpGet request = new HttpGet(url);
        request.addHeader("User-Agent", "Mozilla/5.0");
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Accept", "application/json");

        try {
            HttpResponse response = client.execute(request);
            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static double getBedwarsLevel(UUID uuid) {
        double level = -1;
        LinkedTreeMap data = gson.fromJson(request(String.format("https://api.hypixel.net/player?key=%s&uuid=%s",key,uuid)), LinkedTreeMap.class);
        if ((boolean)data.get("success")) {
            level = (double) ((LinkedTreeMap) ((LinkedTreeMap) data.get("player")).get("achievements")).get("bedwars_level");
        }
        return level;
    }

}
