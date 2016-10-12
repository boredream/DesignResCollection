package com.boredream.bdcodehelper.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a Assets Database Manager
 * Use it, you can use a assets database file in you application
 * It will copy the database file to "/data/data/[your application package name]/database" when you first time you use it
 * Then you can get a SQLiteDatabase object by the assets database file
 *
 * How to use:
 * 1. Initialize AssetsDatabaseManager
 * 2. Get AssetsDatabaseManager
 * 3. Get a SQLiteDatabase object through database file
 * 4. Use this database object
 *
 * Using example:
 * AssetsDatabaseManager.initManager(getApplication()); // this method is only need call one time
 * AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();   // get a AssetsDatabaseManager object
 * SQLiteDatabase db1 = mg.getDatabase("db1.db");   // get SQLiteDatabase object, db1.db is a file in assets folder
 * db1.???  // every operate by you want
 * Of cause, you can use AssetsDatabaseManager.getManager().getDatabase("xx") to get a database when you need use a database
 */
public class AssetsDatabaseManager {
    private static String tag = "AssetsDatabase"; // for LogCat
    private static String databasepath = "/data/data/%s/database"; // %s is packageName


    // A mapping from assets database file to SQLiteDatabase object
    private Map<String, SQLiteDatabase> databases = new HashMap<String, SQLiteDatabase>();

    // Context of application
    private Context context = null;

    // Singleton Pattern
    private static AssetsDatabaseManager mInstance = null;

    /**
     * Initialize AssetsDatabaseManager
     * @param context, context of application
     */
    public static void initManager(Context context){
        if(mInstance == null){
            mInstance = new AssetsDatabaseManager(context);
        }
    }

    /**
     * Get a AssetsDatabaseManager object
     * @return, if success return a AssetsDatabaseManager object, else return null
     */
    public static AssetsDatabaseManager getManager(){
        return mInstance;
    }

    private AssetsDatabaseManager(Context context){
        this.context = context;
    }

    /**
     * Get a assets database, if this database is opened this method is only return a copy of the opened database
     * @param dbfile, the assets file which will be opened for a database
     * @return, if success it return a SQLiteDatabase object else return null
     */
    public SQLiteDatabase getDatabase(String dbfile) {
        if(databases.get(dbfile) != null){
            Log.i(tag, String.format("Return a database copy of %s", dbfile));
            return (SQLiteDatabase) databases.get(dbfile);
        }
        if(context==null)
            return null;

        Log.i(tag, String.format("Create database %s", dbfile));
        String spath = getDatabaseFilepath();
        String sfile = getDatabaseFile(dbfile);

        File file = new File(sfile);
        SharedPreferences dbs = context.getSharedPreferences(AssetsDatabaseManager.class.toString(), 0);
        boolean flag = dbs.getBoolean(dbfile, false); // Get Database file flag, if true means this database file was copied and valid
        if(!flag || !file.exists()){
            file = new File(spath);
            if(!file.exists() && !file.mkdirs()){
                Log.i(tag, "Create \""+spath+"\" fail!");
                return null;
            }
            if(!copyAssetsToFilesystem(dbfile, sfile)){
                Log.i(tag, String.format("Copy %s to %s fail!", dbfile, sfile));
                return null;
            }

            dbs.edit().putBoolean(dbfile, true).commit();
        }

        SQLiteDatabase db = SQLiteDatabase.openDatabase(sfile, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        if(db != null){
            databases.put(dbfile, db);
        }
        return db;
    }

    private String getDatabaseFilepath(){
        return String.format(databasepath, context.getApplicationInfo().packageName);
    }

    private String getDatabaseFile(String dbfile){
        return getDatabaseFilepath()+"/"+dbfile;
    }

    private boolean copyAssetsToFilesystem(String assetsSrc, String des){
        Log.i(tag, "Copy "+assetsSrc+" to "+des);
        InputStream istream = null;
        OutputStream ostream = null;
        try{
            AssetManager am = context.getAssets();
            istream = am.open(assetsSrc);
            ostream = new FileOutputStream(des);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = istream.read(buffer))>0){
                ostream.write(buffer, 0, length);
            }
            istream.close();
            ostream.close();
        }
        catch(Exception e){
            e.printStackTrace();
            try{
                if(istream!=null)
                    istream.close();
                if(ostream!=null)
                    ostream.close();
            }
            catch(Exception ee){
                ee.printStackTrace();
            }
            return false;
        }
        return true;
    }

    /**
     * Close assets database
     * @param dbfile, the assets file which will be closed soon
     * @return, the status of this operating
     */
    public boolean closeDatabase(String dbfile){
        if(databases.get(dbfile) != null){
            SQLiteDatabase db = (SQLiteDatabase) databases.get(dbfile);
            db.close();
            databases.remove(dbfile);
            return true;
        }
        return false;
    }

    /**
     * Close all assets database
     */
    static public void closeAllDatabase(){
        Log.i(tag, "closeAllDatabase");
        if(mInstance != null){
            for(int i=0; i<mInstance.databases.size(); ++i){
                if(mInstance.databases.get(i)!=null){
                    mInstance.databases.get(i).close();
                }
            }
            mInstance.databases.clear();
        }
    }
}