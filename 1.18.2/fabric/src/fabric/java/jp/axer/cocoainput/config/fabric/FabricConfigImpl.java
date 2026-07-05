package jp.axer.cocoainput.config.fabric;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class FabricConfigImpl {

    private static final Pattern INTEGER_ONLY = Pattern.compile("(-?[0-9]*)");
    private static final Pattern DECIMAL_ONLY = Pattern.compile("-?([\\d]+\\.?[\\d]*|[\\d]*\\.?[\\d]+|\\.)");

    final List<EntryInfo> entries = new ArrayList<>();

    protected static class EntryInfo {
        Field field;
        Object widget;
        int width;
        String comment;
        Method dynamicTooltip;
        Map.Entry<EditBox,TextComponent> error;
        Object defaultValue;
        Object value;
        String tempValue;
        boolean inLimits = true;
    }

    private final Class<?> configClass;
    private final String translationPrefix;
    private final Path path;

    private static final Gson gson = new GsonBuilder()
            .excludeFieldsWithModifiers(Modifier.TRANSIENT)
            .excludeFieldsWithModifiers(Modifier.PRIVATE)
            .setPrettyPrinting()
            .create();

    public FabricConfigImpl(String modid, Path apath, Class<?> config) {
        translationPrefix = modid + ".tinyconfig.";
        configClass = config;
        path = apath;

        for (Field field : config.getFields()) {
            Entry e;
            try { e = field.getAnnotation(Entry.class); }
            catch (Exception ignored) { continue; }
            if(e==null) {continue;}
            Class<?> type = field.getType();
            EntryInfo info = new EntryInfo();
            info.width = e.width();
            info.field = field;

            if (type == int.class)         textField(info, Integer::parseInt, INTEGER_ONLY, e.min(), e.max(), true);
            else if (type == double.class) textField(info, Double::parseDouble, DECIMAL_ONLY, e.min(), e.max(),false);
            else if (type == String.class) textField(info, String::length, null, Math.min(e.min(),0), Math.max(e.max(),1),true);
            else if (type == boolean.class) {
                Function<Object,TextComponent> func = value -> new TextComponent((Boolean) value ? "True" : "False");
                info.widget = new AbstractMap.SimpleEntry<Button.OnPress, Function<Object, TextComponent>>(button -> {
                    info.value = !(Boolean) info.value;
                    button.setMessage(func.apply(info.value));
                }, func);
            }
            else if (type.isEnum()) {
                List<?> values = Arrays.asList(field.getType().getEnumConstants());
                Function<Object,BaseComponent> func = value -> new TranslatableComponent(translationPrefix + "enum." + type.getSimpleName() + "." + info.value.toString());
                info.widget = new AbstractMap.SimpleEntry<Button.OnPress, Function<Object,BaseComponent>>( button -> {
                    int index = values.indexOf(info.value) + 1;
                    info.value = values.get(index >= values.size()? 0 : index);
                    button.setMessage(func.apply(info.value));
                }, func);
            }
            else
                continue;

            entries.add(info);

            try { info.defaultValue = field.get(null); }
            catch (IllegalAccessException ignored) {}

            try {
                info.dynamicTooltip = config.getMethod(e.dynamicTooltip());
                info.dynamicTooltip.setAccessible(true);
            } catch (Exception ignored) {}
            info.comment=e.comment();
        }

        try { gson.fromJson(Files.newBufferedReader(path), config); }
        catch (Exception e) { write(); }

        for (EntryInfo info : entries) {
            try {
                info.value = info.field.get(null);
                info.tempValue = info.value.toString();
            }
            catch (IllegalAccessException ignored) {}
        }

    }

    private void textField(EntryInfo info, Function<String,Number> f, Pattern pattern, double min, double max, boolean cast) {
        boolean isNumber = pattern != null;
        info.widget = (BiFunction<EditBox, Button, Predicate<String>>) (t, b) -> s -> {
            s = s.trim();
            if (!(s.isEmpty() || !isNumber || pattern.matcher(s).matches()))
                return false;

            Number value = 0;
            boolean inLimits = false;
            System.out.println(((isNumber ^ s.isEmpty())));
            System.out.println(!s.equals("-") && !s.equals("."));
            info.error = null;
            if (!(isNumber && s.isEmpty()) && !s.equals("-") && !s.equals(".")) {
                value = f.apply(s);
                inLimits = value.doubleValue() >= min && value.doubleValue() <= max;
                info.error = inLimits? null : new AbstractMap.SimpleEntry<>(t, new TextComponent(value.doubleValue() < min ?
                        "§cMinimum " + (isNumber? "value" : "length") + (cast? " is " + (int)min : " is " + min) :
                        "§cMaximum " + (isNumber? "value" : "length") + (cast? " is " + (int)max : " is " + max)));
            }

            info.tempValue = s;
            t.setTextColor(inLimits? 0xFFFFFFFF : 0xFFFF7777);
            info.inLimits = inLimits;
            b.active = entries.stream().allMatch(e -> e.inLimits);

            if (inLimits)
                info.value = isNumber? value : s;

            return true;
        };
    }

    public void write() {
        try {
            if (!Files.exists(path)) Files.createFile(path);
            Files.write(path, gson.toJson(this).getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Screen getScreen(Screen parent) {
        return new FabricConfigScreen(parent, this);
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Entry {
    	String comment() default "";
        String dynamicTooltip() default "";
        int width() default 75;
        double min() default Double.MIN_NORMAL;
        double max() default Double.MAX_VALUE;
    }

}