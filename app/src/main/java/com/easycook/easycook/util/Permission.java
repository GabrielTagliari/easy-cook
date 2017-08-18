package com.easycook.easycook.util;

import java.util.ArrayList;

/** Created by gabriel on 8/17/17. */
public class Permission {

    private static ArrayList<String> permissions;

    public Permission() {
        permissions = new ArrayList<>();
        permissions.add("email");
    }

    public static ArrayList<String> getPermissions() {
        return permissions;
    }
}
