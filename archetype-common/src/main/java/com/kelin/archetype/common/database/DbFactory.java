package com.kelin.archetype.common.database;

/**
 * @author Kelin Tan
 */
public class DbFactory {
    public static String computeDataSourceName(String name) {
        return name + ".dataSource";
    }
}
