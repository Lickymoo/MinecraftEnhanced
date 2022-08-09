package com.buoobuoo.minecraftenhanced.core.util;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.ToolType;
import com.buoobuoo.minecraftenhanced.core.block.CustomBlock;
import com.buoobuoo.minecraftenhanced.core.util.unicode.CharRepo;
import com.buoobuoo.minecraftenhanced.core.util.unicode.UnicodeSpaceUtil;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Util {
    public static int getToolSpeed(ItemStack itemStack){
        String matName = itemStack.getType().name();
        matName = matName.replaceAll("_AXE|_PICKAXE|_SWORD|_SHOVEL|_HOE", "");
        switch (matName){
            default:
                return 1;
            case "WOODEN":
                return 2;
            case "STONE":
                return 4;
            case "IRON":
                return 6;
            case "DIAMOND":
                return 8;
            case "NETHERITE":
                return 9;
            case "GOLD":
                return 12;
        }
    }

    public static boolean isBestTool(ItemStack item, CustomBlock block){
        if(block.getBestTool() == null){
            return true;
        }

        return itemMatchesToolType(block.getBestTool(), item);
    }

    public static boolean itemMatchesToolType(ToolType type, ItemStack item){
        String matName = item.getType().name();
        if(matName.contains(type.name())){
            return true;
        }
        return false;
    }

    public static boolean canHarvest(ItemStack item, CustomBlock block){
        if(block.getExclusiveHarvestTool() == null)
            return true;

        if(itemMatchesToolType(block.getExclusiveHarvestTool(), item))
            return true;

        return false;
    }

    public static int calculateTimeToBreak(Player player, ItemStack item, CustomBlock block){
        if(block == null){
            return 1;
        }

        float speedMultiplier = 1;

        float toolMultiplier = getToolSpeed(item);
        float hardness = block.getHardness();

        boolean canHarvest = canHarvest(item, block);
        boolean isBestTool = isBestTool(item, block);

        int efficiencyLevel = !item.hasItemMeta() ? 0 : item.getItemMeta().getEnchantLevel(Enchantment.DIG_SPEED);
        boolean hasEfficiency = efficiencyLevel != 0;

        boolean hasHaste = player.hasPotionEffect(PotionEffectType.FAST_DIGGING);
        int hasteLevel = !hasHaste ? 0 : player.getPotionEffect(PotionEffectType.FAST_DIGGING).getAmplifier();

        boolean onGround = player.isOnGround();

        if(isBestTool){
            speedMultiplier = toolMultiplier;
            if(!canHarvest){
                speedMultiplier = 1;
            }else if(hasEfficiency){
                speedMultiplier += (efficiencyLevel * efficiencyLevel) + 1;
            }
        }

        if(hasHaste){
            speedMultiplier *= 0.2 * hasteLevel + 1;
        }
        if(!onGround){
            speedMultiplier /= 5;
        }

        float damage = speedMultiplier / hardness;

        if(canHarvest){
            damage /= 30f;
        }else{
            damage /= 100f;
        }

        return (int)Math.ceil(1 / damage);
    }

    public static int randomInt(int min, int max){
        if(min == max) return min;
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    public static double randomDouble(double min, double max){
        if(min == max) return min;
        return ThreadLocalRandom.current().nextDouble(min, max);
    }


    public static void sendPacketGlobal(Packet<?> packet){
        sendPacket(packet, Bukkit.getOnlinePlayers().toArray(new Player[0]));
    }

    public static void sendPacket(Packet<?> packet, Player... players){

        for(Player p : players) {

            CraftPlayer cp = (CraftPlayer) p;
            ServerPlayer sp = cp.getHandle();

            ServerGamePacketListenerImpl ps = sp.connection;
            ps.send(packet);
        }
    }


    public static String formatColour(String str){
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public static List<String> formatColour(Collection<String> str){
        List<String> out = new ArrayList<>();
        for(String s : str){
            out.add(formatColour(s));
        }
        return out;
    }

    public static String[] padEmpty(String[] arr, int size){
        String[] ret = new String[size];
        for(int i = 0; i < arr.length; i++){
            ret[i] = arr[i];
        }
        for(int i = arr.length; i < size; i++){
            ret[i] = "";
        }
        return ret;
    }

    public static void sendDialogueBox(Player player, CharRepo icon, String... strs){
        strs = padEmpty(strs, 6);
        player.sendMessage(Util.formatColour(CharRepo.UI_DIALOGUE_BORDER + UnicodeSpaceUtil.getNeg(47) + icon  + UnicodeSpaceUtil.getPos(1) + " " + strs[0]));
        for(int i = 1; i < strs.length; i++){
            player.sendMessage(Util.formatColour("             " + strs[i]));
        }
    }

    public static void sendDialogueBox(Player player, CharRepo icon, TextComponent... strs){
        TextComponent iconCom = new TextComponent(CharRepo.UI_DIALOGUE_BORDER + UnicodeSpaceUtil.getNeg(47) + icon  + UnicodeSpaceUtil.getPos(1) + " ");
        TextComponent txt = new TextComponent(strs[0]);
        player.spigot().sendMessage(iconCom, txt);

        for(int i = 1; i < strs.length; i++) {
            if (strs[i] != null) {
                TextComponent pad = new TextComponent("             ");
                player.spigot().sendMessage(pad, strs[i]);
            }else{
                player.sendMessage("");
            }
        }
    }

    public static <T> T[] addToArr(T[] arr, T... str){
        List<T> list = new ArrayList<>();
        list.addAll(Arrays.asList(arr));
        list.addAll(Arrays.asList(str));
        return list.toArray((T[]) new Object[0]);
    }

    public static <T> T[] addToBeginningOfArray(T[] elements, T element)
    {
        T[] newArray = Arrays.copyOf(elements, elements.length + 1);
        newArray[0] = element;
        System.arraycopy(elements, 0, newArray, 1, elements.length);

        return newArray;
    }

    public static String fromList(List<String> strList){
        StringBuilder builder = new StringBuilder();
        for(String str : strList){
            builder.append(str);
            builder.append(",");
        }
        return builder.toString();
    }

    public static String formatDouble(double value) {
        String[] suffixes = {"K", "M", "B", "T", "Qad", "Qin", "Sext", "Sept", "Oct", "Non", "Dec", "Und", "Duod", "Tred", "Quat", "Quind", "Sexd", "Septe", "Octo", "Nov", "Vigin"};
        if(value > 1000) {
            for (int pwr = 3; pwr <= 63; pwr += 3) {
                int index = (pwr / 3) - 1;

                double powVal = Math.pow(10, pwr);
                if (value < powVal) {
                    String str = String.format("%.2f" + suffixes[index - 1], value / Math.pow(10, pwr - 3));
                    str = str.replace(".00", "");
                    return str;
                }
            }
        }

        String str = String.format("%.0f", value);
        str = str.replace(".00", "");
        return str;
    }

    public static float clamp(float val, float min, float max){
        if(Float.isNaN(val))
            return 0;

        if(val < min)
            return min;
        if(val > max)
            return max;

        return val;
    }

    public static String[] removeOccurrences(String[] arr, String str){
        List<Integer> marks = new ArrayList<>();
        for(int i = 0; i < arr.length; i++){
            if(arr[i] == null)
                continue;

            if(arr[i].equals(str))
                marks.add(i);
        }

        for(int i : marks){
            arr[i] = null;
        }
        return arr;
    }

    public static String getNBTString(MinecraftEnhanced plugin, ItemStack item, String tag){
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        return pdc.get(new NamespacedKey(plugin, tag), PersistentDataType.STRING);
    }

    public static boolean hasEmptyStr(String[] arr){
        for (String obj : arr) {
            if (obj == null || isEmpty(obj))
                return true;
        }
        return false;
    }

    public static String[] insertEmptyStr(String[] arr, String val) {
        for(int i = 0; i < arr.length; i++){
            if(arr[i] == null ||  isEmpty(arr[i]) || arr[i].equals("null")) {
                arr[i] = val;
                return arr;
            }
        }
        return arr;
    }

    public static <T> boolean hasEmpty(T[] arr) {
        for (T obj : arr) {
            if (obj == null)
                return true;
        }
        return false;
    }

    public static <T> T[] insertEmpty(T[] arr, T val) {
        for(int i = 0; i < arr.length; i++){
            if(arr[i] == null) {
                arr[i] = val;
                return arr;
            }
        }
        return arr;
    }

    public static boolean isEmpty(String str){
        if(str == null)
            return true;
        if(str.isEmpty())
            return true;
        if(str.isBlank())
            return true;

        return false;
    }

    public static <T> boolean arrContains(T[] arr, T obj){
        for(T o : arr){
            if(o == null)
                continue;

            if(o.equals(obj))
                return true;
        }
        return false;
    }

    public static String[] stringToArr(String str){
        return str.split(",");
    }

    public static String arrToString(String[] arr){
        StringBuilder sb = new StringBuilder();
        for(String str : arr){
            sb.append(str);
            sb.append(",");
        }
        return sb.toString();
    }

    public static <T> T[] setArrSize(T[] arr, int size){
        return Arrays.copyOf(arr, size);
    }

    public static String randomString(){
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
    }
}





























