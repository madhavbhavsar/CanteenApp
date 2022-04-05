package com.madapps.canteen.callbacks;

import java.util.HashMap;

public interface ItemsCallback {

    void onCallback(HashMap<String,String> data,HashMap<String,String> dataprice,HashMap<String,String> itemidname);
}
