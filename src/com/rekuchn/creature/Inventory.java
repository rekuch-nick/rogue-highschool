package com.rekuchn.creature;

import com.rekuchn.model.Item;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Inventory {

    private Item[] items;
    public Item[] getItems() { return items; }
    public Item get(int i) {
        if(i < 0){ i = 29; }
        if(i > 29){ i = 0; }
        return items[i]; }

    public Inventory(int max){
        items = new Item[max];
    }

    public void add(Item item){
        for (int i = 0; i < items.length; i++){
            if (items[i] == null){
                items[i] = item;
                break;
            }
        }
    }
    public void remove(Item item){
        for (int i = 0; i < items.length; i++){
            if (items[i] == item){
                items[i] = null;
                return;
            }
        }
    }

    public boolean isFull(){
        int size = 0;
        for (int i = 0; i < items.length; i++){
            if (items[i] != null)
                size++;
        }
        return size == items.length;
    }

    public boolean hasItem(String name){
        for(Item i : items){
            if(i != null && i.name().equals(name)){ return true; }
        }
        return false;
    }

    public Item getItem(String name){
        for(Item i : items){
            if(i != null && i.name().equals(name)){ return i; }
        }
        return null;
    }

    public void removeAll(String name){
        for (int i = 0; i < items.length; i++){
            if (items[i] != null && items[i].name() == name) {
                items[i] = null;
            }
        }
    }

    public void sort(){

        List<Item> wep = new ArrayList<>();
        List<Item> arm = new ArrayList<>();
        List<Item> hlm = new ArrayList<>();
        List<Item> clk = new ArrayList<>();
        List<Item> blt = new ArrayList<>();
        List<Item> glo = new ArrayList<>();
        List<Item> bot = new ArrayList<>();
        List<Item> rng = new ArrayList<>();
        List<Item> otr = new ArrayList<>();


        for(int i=0; i<items.length; i++){
            Item it = items[i];
            if(it == null){ continue; }
            if(it.slot() == 0){ wep.add(it); }
            if(it.slot() == 1){ arm.add(it); }
            if(it.slot() == 2){ hlm.add(it); }
            if(it.slot() == 3){ clk.add(it); }
            if(it.slot() == 4){ blt.add(it); }
            if(it.slot() == 5){ glo.add(it); }
            if(it.slot() == 6){ bot.add(it); }
            if(it.slot() == 7){ rng.add(it); }
            if(it.slot() == -1){ otr.add(it); }
        }

        //wep.stream().sorted(Comparator.comparing(Item::name));
        wep.sort((object1, object2) -> object1. name().compareTo(object2. name()));
        arm.sort((object1, object2) -> object1. name().compareTo(object2. name()));
        hlm.sort((object1, object2) -> object1. name().compareTo(object2. name()));
        clk.sort((object1, object2) -> object1. name().compareTo(object2. name()));
        blt.sort((object1, object2) -> object1. name().compareTo(object2. name()));
        glo.sort((object1, object2) -> object1. name().compareTo(object2. name()));
        bot.sort((object1, object2) -> object1. name().compareTo(object2. name()));
        rng.sort((object1, object2) -> object1. name().compareTo(object2. name()));
        otr.sort((object1, object2) -> object1. name().compareTo(object2. name()));




        List<Item> sortedItems = new ArrayList<>();
        for(Item i : wep){ sortedItems.add(i); }
        for(Item i : arm){ sortedItems.add(i); }
        for(Item i : hlm){ sortedItems.add(i); }
        for(Item i : clk){ sortedItems.add(i); }
        for(Item i : blt){ sortedItems.add(i); }
        for(Item i : glo){ sortedItems.add(i); }
        for(Item i : bot){ sortedItems.add(i); }
        for(Item i : rng){ sortedItems.add(i); }
        for(Item i : otr){ sortedItems.add(i); }

        for(int i=0; i<items.length; i++){
            if(sortedItems.size() > i){

                items[i] = sortedItems.get(i);
            } else {
                items[i] = null;
            }
        }
    }


}
