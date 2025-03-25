package com.kaleblangley.examples.util;

import net.minecraft.world.item.enchantment.Enchantment;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.util.Set;

public class ClassUtil {

    //TODO: 并不能获取，也许是开发环境的问题？
    public static Set<Class<? extends Enchantment>> getAllEnchantment() {
        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .setUrls(ClasspathHelper.forClass(Enchantment.class))
                        .setScanners(new SubTypesScanner())
        );
        Set<Class<? extends Enchantment>> subclasses = reflections.getSubTypesOf(Enchantment.class);
        subclasses.forEach(x -> System.out.println(x));
        return subclasses;
    }
}
