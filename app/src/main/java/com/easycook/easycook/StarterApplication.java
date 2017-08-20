package com.easycook.easycook;

import android.app.Application;

import com.easycook.easycook.model.ListaCompra;
import com.easycook.easycook.model.Produto;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;

/** Created by gabriel on 8/16/17. */
public class StarterApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        ParseObject.registerSubclass(Produto.class);
        ParseObject.registerSubclass(ListaCompra.class);

        // Add your initialization code here
        Parse.initialize(this);

        ParseFacebookUtils.initialize(this);
        //ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        //defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
