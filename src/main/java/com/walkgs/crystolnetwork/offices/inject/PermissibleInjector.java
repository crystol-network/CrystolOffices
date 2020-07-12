package com.walkgs.crystolnetwork.offices.inject;

import com.walkgs.crystolnetwork.offices.reflect.SpigotUtils;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.PermissibleBase;

import java.lang.reflect.Field;
import java.util.List;

public final class PermissibleInjector {

    private PermissibleInjector() {}

    private static final Field ENTITY_PERMISSIBLE_FIELD;
    private static final Field PERMISSIBLE_ATTACHMENTS_FIELD;

    static {
        try {
            //TODO: GET PERMISSIBLE CLASS
            Field entityPermissible = getOBCClassDeclaredField("entity.CraftHumanEntity", "perm");
            if (entityPermissible == null)
                entityPermissible = getOBCClassDeclaredField("net.glowstone.entity.GlowHumanEntity", "permissions");
            //TODO: GET PERMISSIBLE CLASS ACESS AND SET
            if (entityPermissible != null)
                entityPermissible.setAccessible(true);
            ENTITY_PERMISSIBLE_FIELD = entityPermissible;
            //TODO GET ATTACHMENTS FIELD AND SET ACESS
            PERMISSIBLE_ATTACHMENTS_FIELD = PermissibleBase.class.getDeclaredField("attachments");
            PERMISSIBLE_ATTACHMENTS_FIELD.setAccessible(true);
        } catch (NoSuchFieldException e) { throw new ExceptionInInitializerError(e); }
    }

    public static Permissible inject(Player player, CrystolPermissible permissible){
        PermissibleBase oldPermissible = null;
        try {
            //TODO: GET OLD PERMISSIBLE
            oldPermissible = (PermissibleBase) ENTITY_PERMISSIBLE_FIELD.get(player);
            //TODO: INSERT ALL OLD PERMISSIBLE IN NEW
            if (oldPermissible != null)
                ((List) PERMISSIBLE_ATTACHMENTS_FIELD.get(permissible)).addAll((List) PERMISSIBLE_ATTACHMENTS_FIELD.get(oldPermissible));
            //TODO: SET NEW PERMISSIBLE
            ENTITY_PERMISSIBLE_FIELD.set(player, permissible);
        } catch (IllegalAccessException e) { e.printStackTrace(); }
        return oldPermissible;
    }

    public static Permissible uninject(Player player){
        Permissible permissible = null;
        try {
            //TODO: GET OLD PERMISSIBLE
            permissible = (Permissible) ENTITY_PERMISSIBLE_FIELD.get(player);
            //TODO: SET DEFAULT PERMISSIBLE
            if (permissible != null)
                ENTITY_PERMISSIBLE_FIELD.set(player, ((CrystolPermissible) permissible).getOldPermissibleBase());
        } catch (IllegalAccessException e) { e.printStackTrace(); }
        return permissible;
    }

    public static PermissibleBase getPermissible(Player player){
        PermissibleBase permissible = null;
        try { permissible = (PermissibleBase) ENTITY_PERMISSIBLE_FIELD.get(player); } catch (IllegalAccessException e) { e.printStackTrace(); }
        return permissible;
    }

    private static Field getOBCClassDeclaredField(String className, String fieldName) {
        Field field = null;
        try {
            Class<?> obcClass = SpigotUtils.obcClass(className);
            if (obcClass != null)
                field = obcClass.getDeclaredField(fieldName);
        } catch (ClassNotFoundException | NoSuchFieldException e) {}
        return field;
    }

}
