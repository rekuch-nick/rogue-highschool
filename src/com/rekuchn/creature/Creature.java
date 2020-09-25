package com.rekuchn.creature;


import com.rekuchn.AsciiPanel;
import com.rekuchn.Global;
import com.rekuchn.model.*;
import com.rekuchn.World;
import com.rekuchn.service.EffectManager;
import com.rekuchn.service.PowerManager;
import com.rekuchn.spell.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Creature {
    private World world;
    public World world(){ return world; }
    public void setWorld(World world){ this.world = world; }
    public int x; public int y; public int hp; private int mhp; public int mp; private int mmp;
    private int evade; private int defend; private int might;
    public boolean isPlayer, isWaifu, hasMind, waterOnly, hasBlood, hasEyes, isFast;
    public int summonTime, swimChance, stunTime, digTime, hearts, cash;
    public int readyItem; private char glyph; private Color color; public String name;
    public boolean hostile; public boolean passive; public boolean justSwapped;
    public List<Status> status;
    private Inventory inventory;
    public Item[] worn;
    public List<String> powers;
    public List<Spell> spellsKnown;
    public Spell[] spells;
    public CreatureAi ai;
    public void setCreatureAi(CreatureAi ai) { this.ai = ai; }
    public int dropChance;
    public int[] waifuAffection; public int[] waifuPosition; public boolean[] waifuGifted; public boolean[] waifuTalked;

    public Creature(String name, World world, char glyph, Color color, int hp, int mp, int evade, int defend, int might, String ... params){
        this.name = name; this.world = world; this.glyph = glyph; this.color = color; this.hp = hp; this.mhp = hp; this.mp = mp; this.mmp = mp;
        this.evade = evade; this.defend = defend; this.might = might;
        this.visionRadius = 9;
        justSwapped = false;
        hostile = true;
        passive = false;
        isPlayer = false;
        isWaifu = false;
        hasMind = true; hasBlood = true; hasEyes = true; isFast = false;
        this.inventory = new Inventory(30);
        worn = new Item[18];
        readyItem = 0;
        this.stunTime = 0;
        this.summonTime = -1;
        this.status = new ArrayList<>();
        this.powers = new ArrayList<>();
        if(params != null && params.length > 0){
            for(int i=0; i<params.length; i++){
                this.powers.add(params[i]);
            }
        }
        waterOnly = false; swimChance = 20;
        this.spellsKnown = new ArrayList<>();
        this.spells = new Spell[10];
        digTime = 0;
        dropChance = 20;
        waifuAffection = new int[12]; waifuPosition = new int[12]; waifuGifted = new boolean[12]; waifuTalked = new boolean[12];

        //powers.add("Earth Conjurer");
        //powers.add("Berserker");
        //powers.add("Arachnophilia");
        //powers.add("Thinker");
        PowerManager.update(this);
        //Spell testSpell = new Spell("Test", new Gust(), 2, this); spellsKnown.add(testSpell); spells[1] = testSpell;


    }

    public char glyph() { return glyph; }
    public Color color() { return color; }
    public Inventory inventory() { return inventory; }

    public void wear(Item item){
        if(item == null){ return; }
        if(item.slot() == -1){ return; }

        if(item.slot() == 7){ //rings
            int min = 7; int max = 8;
            if(hasTrait("Ring Master")){ max = 16; }
            for(int i=min; i<=max; i++){
                if(worn[i] == null){
                    worn[i] = item;
                    PowerManager.update(this);
                    doAction("equip a %s", item.name());
                    return;
                }
            }
            return;
        }


        if(worn[item.slot()] != null){ return; }

        worn[item.slot()] = item;
        PowerManager.update(this);
        doAction("equip a %s", item.name());
    }

    public void takeOff(Item item){
        if(item == null){ return; }

        for(int i=0; i<17; i++){

            if(worn[i] != null && worn[i] == item){

                doAction("un-equip a %s", item.name());
                worn[i] = null;
            }
        }
        PowerManager.update(this);
    }

    public boolean isWearing(Item item){
        for(Item i : worn){
            if(i == item){ return true; }
        }
        return false;
    }

    public void addStatus(Status s){
        if(hasStatus(s.trait) == null) {
            status.add(s);
        } else {
            hasStatus(s.trait).time = Math.min(999, hasStatus(s.trait).time + s.time);
        }
    }
    public Status hasStatus(String s){
        for(Status sta : status){
            if(sta.trait.equals(s)){
                return sta;
            }
        }
        return null;
    }

    public void cycleSpell(int slot, int direction){

        if(spellsKnown.size() == 0){ return; }

        int cur = direction == 1 ? -1 : spellsKnown.size();
        for(int i=0; i<spellsKnown.size(); i++){
            if(spells[slot] == spellsKnown.get(i)){
                cur = i; break;
            }
        }

        cur += direction;

        if(cur < 0 || cur > spellsKnown.size() - 1){ spells[slot] = null; return; }
        spells[slot] = spellsKnown.get(cur);
    }

    public String getDescription(){
        return ai.getDescription(name);
    }

    public void moveBy(int mx, int my){ moveBy(mx, my, false); }

    public void moveBy(int mx, int my, boolean pushed){
        if(!pushed && hasTrait("Prone")){ return; }

        if(mx == 0 && my == 0){
            //ai.triggerTile(x, y, world.tile(x, y));
            triggerTile(x, y, world.tile(x, y));
            return; }

        Creature other = world.creature(x+mx, y+my);

        if(!pushed && hasTrait("Polearm")){
            Creature oneAway = world.creature(x+mx+mx, y+my+my);
            Creature twoAway = world.creature(x+mx+mx+mx, y+my+my+my);

            boolean hitMonster = false;
            if(oneAway != null && !oneAway.isWaifu && hostile != oneAway.hostile && world.lineBetweenIgnoreCreatures(x, y, oneAway.x, oneAway.y)){
                attack(oneAway, "Melee"); hitMonster = true;
            }
            //if(twoAway != null && !twoAway.isWaifu && hostile != twoAway.hostile && world.lineBetweenIgnoreCreatures(x, y, twoAway.x, twoAway.y)){
            //    attack(twoAway, "Melee"); hitMonster = true;
            //}
            if(other != null && !other.isWaifu && hostile != other.hostile){
                attack(other, "Melee"); hitMonster = true;
            }
            if(hitMonster){ return; }
        }



        if(other == null){

            if(!pushed){
                if(world.tile(x+mx, y+my) != Tile.WATER && waterOnly){
                    mx = 0; my = 0;
                }

                if(world.tile(x, y) != Tile.WATER && world.tile(x+mx, y+my) == Tile.WATER && world.random.nextInt(100) >= swimChance){
                    mx = 0; my = 0;
                }

                if(world.tile(x, y) == Tile.WEB && !hasTrait("Web Walking") && !hasTrait("Free Movement") && !hasTrait("Flying") &&!hasTrait("Arachnophilia")){
                    if(world.random.nextBoolean()){
                        mx = 0; my = 0;
                        doAction(AsciiPanel.yellow, "stick in the webs");
                        EffectManager.slip(x, y, world);
                    }
                }
            }

            ai.onEnter(x+mx, y+my, world.tile(x+mx, y+my), pushed);
        } else {
            if(pushed){
                doAction("crash into the %s", other.name);
                other.doAction("get bumped by the %s", this.name);
                takeDamage(2, "Bump");
                other.takeDamage(2, "Bump");
            }

            if(isPlayer && !other.hostile){
                if(other.isWaifu){ Global.chat = other; return; }
                world.swapCreatures(this, other);
                return;
            }

            if(!isPlayer && !other.isPlayer && hostile == other.hostile && world.random.nextInt(4) == 0){
                justSwapped = true;
                other.justSwapped = true;
                world.swapCreatures(this, other);
                return;
            }
            if(hostile == other.hostile){ return; }
            if(other.isWaifu){ return; }

            if(hasTrait("Axe")){
                for(int tx=x-1; tx<=x+1; tx++){
                    for(int ty=y-1; ty<=y+1; ty++){
                        if(x == tx && y == ty){ continue; }
                        Creature c = world.creature(tx, ty);
                        if(c != null && !c.isWaifu && c.hostile != hostile){
                            attack(c, "Melee");
                        }
                    }
                }
                return;
            }
            if(hasTrait("Mace")){ attack(other, "Mace", "Melee"); return; }
            if(hasTrait("Sword")){ attack(other, "Sword", "Melee"); return; }

            if(hasTrait("Rapid") && world.random.nextBoolean()){ attack(other, "Quick Attack", "Melee"); }
            if(hasTrait("Haste") && world.random.nextBoolean()){ attack(other, "Quick Attack", "Melee"); }

            attack(other, "Melee");


            triggerTile(x, y, world.tile(x, y));
        }
    }

    public Creature creature(int x, int y){
        return world.creature(x, y);
    }


    public void attack(Creature other, String ... note){
        CreatureAttack.attack(this, other, note);
    }

    public void openDoor(int x, int y){
        world.openDoor(x, y);
    }

    public void open(int x, int y){
        if(x < 1 || y < 1 || x >= world.width() - 2 || y >= world.height() - 2){ return; }
        Tile t = world.tile(x, y);
        if( t != Tile.WALL_SWITCH && !t.isWall()){ return; }
        world.setTile(x, y, Tile.FLOOR);
    }

    public void update(){

        List<Status> temp = new ArrayList<>(status);
        for(Status s : temp){
            if(s.decayEachTurn){ s.time --; }
            if(s.time < 1){
                status.remove(s);
            }
        }

        if(hasTrait("Sickening") && !hasTrait("Sick") && world.random.nextInt(99) == 0) {
            doAction("get become ill");
            addStatus(new Status("Sick", AsciiPanel.green, 1, false, true, this));
        }

        if(hasTrait("Poison")){
            if(hp - 1 < 1){ doAction(AsciiPanel.yellow, "fall from the poison"); }
            takeDamage(1);
        }
        if(hasTrait("Hemorrhage")){
            if(hp - 1 < 1){ doAction(AsciiPanel.yellow, "fall from the hemorrhage"); }
            takeDamage(1, "Blood");
        }


        if(summonTime > -1){
            if(summonTime > 0){ summonTime --; }
            if(summonTime == 0){
                int d = world.random.nextInt(6) + 1;
                if(hp - d < 1){ doAction(AsciiPanel.yellow, "dissipate"); }
                takeDamage(d);
            }
        }

        if(stunTime > 0){
            stunTime --;
            return;
        }




        ai.onUpdate();



    }

    public void triggerTile(int x, int y, Tile tile){
        if(tile == Tile.LAVA && !hasTrait("Flying")) {
            doAction("step in the lava");
            takeDamage(15, "Fire");
        };

        if(tile == Tile.FIRE_1 || tile == Tile.FIRE_2 || tile == Tile.FIRE_3 || tile == Tile.FIRE_4){
            doAction("step in the fire");
            takeDamage(6, "Fire");
        };

        if(tile == Tile.SPIKE && !hasTrait("Flying")) {
            doAction("step in the spikes");
            EffectManager.hurt(x, y, world);
            takeDamage(2, "Foot");
        };

        if(tile == Tile.GREASE && !hasTrait("Flying") && world.random.nextBoolean() && !hasTrait("Prone")
                && !hasTrait("Balance") //|| ( world.random.nextBoolean() && world.random.nextBoolean() ))
                    ){
            int n = isPlayer ? 2 : 3;
            addStatus(new Status("Prone", AsciiPanel.darkPink, n, true, true, this));
            if(world.player().canSee(x, y)) {
                doAction("fall in the grease");
                EffectManager.slip(x, y, world);
            }
            if(world.random.nextInt(4) == 0){ world.setTile(x, y, Tile.FLOOR); }
        };

        if(tile == Tile.VINE && !hasTrait("Invisible") && hasTrait("Hiding")){
            doAction(AsciiPanel.blue, "hide in the shadows");
            status.add(new Status("Invisible", AsciiPanel.blue, 10, true, true, this));
        }
    }

    public boolean canEnter(int x, int y){
        return (world.tile(x, y).canEnter(this) && world.creature(x, y) == null);
    }

    public int visionRadius;


    public boolean canSee(int wx, int wy){
        if(hasTrait("Clairvoyance")){ return true; }
        if(hasTrait("X-Ray Vision") && (Math.abs(wx - x) + Math.abs(wy - y)) < visionRadius){ return true; }
        if(hasTrait("Telepathy") && world.creature(wx, wy) != null && world.creature(wx, wy).hasMind){ return true; }

        return ai.canSee(wx, wy);
    }

    public Tile tile(int wx, int wy) {
        return world.tile(wx, wy);
    }

    public void takeDamage(int n, String ... tags){
        List<String> notes = new ArrayList<>();
        for(String s : tags){ notes.add(s); }
        boolean aliveWhenThisStarted = false;
        if(hp > 0){ aliveWhenThisStarted = true; }

        if(notes.contains("Fire") && hasTrait("Resist Fire")){ n /= 2; }

        if(n < 1){ n = 1; }
        hp -= n;



        if(hp < 1){
            if(aliveWhenThisStarted){ ai.deathRattle(); }

            for(Item i : inventory().getItems()){
                if (i != null) {
                    drop(i);
                }
            }



            if(world.random.nextInt(20) == 0){
                drop(ItemList.get("Mana Orb"));
            } else if(world.random.nextInt(10) == 0){
                drop(ItemList.get("Health Orb"));
            }

            if(name.equals("Kannushi")){ world.unsealShrine(); }

            world.remove(this);
        }
    }



    public Creature clearShotAt(int range){
        for(Creature c : world.creatures){
            if(hostile == c.hostile){ continue; }
            if(c.isWaifu){ continue; }

            if( ((c.x - x) * (c.x - x)) + ((c.y - y) * (c.y - y)) <= range*range ){
                if(canSee(c.x, c.y) && world.lineBetween(x, y, c.x, c.y)){

                    return c;
                }
            }

        }

        return null;
    }



    public void pickup(){
        Item item = world.item(x, y);



        if (inventory.isFull()) {
            doAction("cannot pickup the %s", item.name());

        } else if (item == null){
            doAction("wait a moment");

        } else {
            doAction("grab a %s", item.name());

            world.remove(x, y);
            inventory.add(item);

            world.eventTest("Grabbed Key", x, y, this);
        }

        readyItem();
    }

    public void drop(Item item){

        if(world.addAtEmptySpace(item, x, y)){
            doAction("drop a " + item.name());
            inventory.remove(item);
        } else {
            doAction("see nowhere to drop the item");
        }
    }

    public void readyItem(){
        if(inventory.get(readyItem) == null || inventory.get(readyItem).action == null){
            for(int i=0; i<30; i++){
                Item item = inventory.get(i);
                if(item != null && item.action != null){
                    readyItem = i;
                    break;
                }
            }
        }
    }
    public void readyItem(Item item){
        if(item == null){ readyItem(); }
        for(int i=0; i<30; i++){
            Item itm = inventory.get(i);
            if(itm != null && item.name().equals(itm.name())){
                readyItem = i; return;
            }
        }
        readyItem(readyItem);
    }
    public void readyItem(int i){
        if(readyItem >= 30){ readyItem = 0; }
        if(readyItem < 0){ readyItem = 29; }
        Item old = inventory().get(i);
        if(old != null && old.action == null){ old = null; }

        readyItem ++;
        if(readyItem >= 30){ readyItem = 0; }
        while(readyItem != i){
            Item item = inventory.get(readyItem);
            if( item != null && item.action != null){
                if(old == null || old.name() != item.name()) {
                    break;
                }
            }
            readyItem ++;
            if(readyItem >= 30){ readyItem = 0; }
        }
    }

    public void readyItemBack(int i){
        if(readyItem >= 30){ readyItem = 0; }
        if(readyItem < 0){ readyItem = 29; }
        Item old = inventory().get(i);
        if(old != null && old.action == null){ old = null; }

        readyItem --;
        if(readyItem < 0){ readyItem = 29; }
        while(readyItem != i){
            Item item = inventory.get(readyItem);
            if( item != null && item.action != null){
                if(old == null || old.name() != item.name()) {
                    break;
                }
            }
            readyItem --;
            if(readyItem < 0){ readyItem = 29; }
        }
    }


    public void notify(Color c, String message, Object ... params){
        ai.onNotify(c, String.format(message, params));
    }


    public void doAction(String message, Object ... params) {
        doAction(AsciiPanel.white, message, params);
    }
    public void doAction(Color c, String message, Object ... params){
        int r = 9;
        for (int ox = -r; ox < r+1; ox++){
            for (int oy = -r; oy < r+1; oy++){
                if (ox*ox + oy*oy > r*r)
                    continue;

                Creature other = world.creature(x+ox, y+oy);

                if (other == null)
                    continue;

                if (other == this)
                    other.notify(c, "You " + message + ".", params);
                else
                    other.notify(c, String.format("The %s %s.", name, makeSecondPerson(message)), params);
            }
        }
    }

    private String makeSecondPerson(String text) {
        String[] words = text.split(" ");
        if (words[0].equals("are")) {
            words[0] = "is";
        } else if (words[0].equals("can")) {

        } else {
            words[0] = words[0] + "s";
        }

        StringBuilder builder = new StringBuilder();
        for (String word : words){
            builder.append(" ");
            builder.append(word);
        }

        return builder.toString().trim();
    }

    public void detailInventory() {
        ai.detailInventory();
    }

    public Action useItem() {
        return ai.useItem();
    }

    public void consumeItem(Item item){
        inventory.remove(item);
    }

    public void attackDoor(Tile door, int x, int y){
        /*
        int bashRoll = world.random.nextInt(100) + (might() * 2);

        if(bashRoll >= 95) {
            doAction(AsciiPanel.white, "break the door down");
            world.setTile(x, y, Tile.OPEN_DOOR);
        } else {
            doAction(AsciiPanel.white, "bash at the door");
        }
        */



    }


    public int evade() {
        int n = evade;
        if(hasTrait("Invisible")){ n += 20; }
        if(hasTrait("Sports Hero")){ n += 10; }

        int plus = 0;
        if(hasTrait("Deflection")){ plus = 5; }
        if(hasTrait("Deflection 2")){ plus = 10; }
        if(hasTrait("Deflection 3")){ plus = 15; }
        if(hasTrait("Deflection 4")){ plus = 20; }
        n += plus;

        plus = 0;
        if(hasTrait("Dodging")){ plus = 5; }
        if(hasTrait("Dodging 2")){ plus = 10; }
        if(hasTrait("Dodging 3")){ plus = 15; }
        if(hasTrait("Dodging 4")){ plus = 20; }
        n += plus;


        if(worn[1] != null){
            int acp = Math.max(worn[1].armorCheck - worn[1].bonus, 0);
            if(might() < acp){
                n -= 5 * (acp - might());
            }
        }

        if(hasTrait("Slow")){ n -= 50; }

        if(n < 0){ n = 0; }
        if(world.tile(x, y) == Tile.WEB && !hasTrait("Web Walking") && !hasTrait("Free Movement") ){ n = 0; }
        return n;
    }
    public int defend() {
        int n = defend;
        if(worn[1] != null){ n += worn[1].bonus + worn[1].armor; }
        if(hasTrait("Stone Skin")){ n += 4; }
        if(hasTrait("Sports Padding")){ n += 1; }

        if(hasTrait("Shattered")){ n = 0; }

        return n;
    }


    public int might() {
        int n = might;

        if(hasTrait("Mighty")){ n += 4; }
        if(hasTrait("Mole Hands")){ n += 2; }
        if(hasTrait("Frail")){ n -= 4; }
        if(hasTrait("Woozy")){ n -= 2; }

        return n;
    }
    public void alterMight(int n){
        might += n;
    }

    public void alterLife(int n){
        mhp += n;
    }


    public int mhp() {
        int n = this.mhp;
        if(hasTrait("Stamina")){ n += 20; }
        if(hasTrait("Hardy")){ n *= 1.2; }
        if(hp > n){ hp = n; }
        return n;
    }
    public int mmp() {
        int n = this.mmp;
        if(hasTrait("Stamina")){ n += 10; }

        int b = 0;
        if(hasTrait("Wizardry")){ b = 10; }
        if(hasTrait("Wizardry 2")){ b = 20; }
        if(hasTrait("Wizardry 3")){ b = 30; }
        if(hasTrait("Wizardry 4")){ b = 40; }
        n += b;


        if(mp > n){ mp = n; }
        return n;
    }


    public boolean hasTrait(String t){
        for(Status s : status){
            if(s.trait.equals(t)){ return true; }
        }

        for(String s : powers){
            if(s.equals(t)){ return true; }
        }

        for(Item i : worn){
            if(i == null){ continue; }
            for(String s : i.traits){
                if(s.equals(t)){ return true; }
            }
        }
        return false;
    }

    public boolean hasPower(String t){
        for(String s : powers){
            if(s.equals(t)){ return true; }
        }
        return false;
    }


}