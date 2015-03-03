package com.mygdx.game;

/**
 * Created by LuisMirandela on 03/03/2015.
 */

import  java.util.*;

public class BubbleArray {

    public List<Bubble> items;

    public BubbleArray(){
        items = new ArrayList<Bubble>();
    }

    public void createNew(int screenWidth,int screenHeight,int lvl){
        Bubble bbl = new Bubble(screenWidth,screenHeight,lvl);
        items.add(bbl);
    }

}
