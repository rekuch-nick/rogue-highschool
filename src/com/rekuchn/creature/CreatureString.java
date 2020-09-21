package com.rekuchn.creature;

import java.util.ArrayList;
import java.util.List;

public class CreatureString {

    public String name;
    public int cr;
    public List<String> notes;

    public CreatureString(String name, int cr) {
        this.name = name;
        this.cr = cr;
        this.notes = new ArrayList<>();
    }
}
