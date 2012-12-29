package com.p000ison.dev.simpleclans2.database.data;

import com.p000ison.dev.sqlapi.TableObject;
import com.p000ison.dev.sqlapi.annotation.DatabaseColumn;
import com.p000ison.dev.sqlapi.annotation.DatabaseTable;

import java.sql.Timestamp;

/**
 * Represents a BBTable
 */
@SuppressWarnings("unused")
@DatabaseTable(name = "sc2_bb")
public class BBTable implements TableObject {

    @DatabaseColumn(position = 0, databaseName = "id", id = true)
    public int id;
    @DatabaseColumn(position = 1, databaseName = "clan")
    public int clan;
    @DatabaseColumn(position = 2, databaseName = "text")
    public String text;
    @DatabaseColumn(position = 3, databaseName = "date")
    public Timestamp date;
}
