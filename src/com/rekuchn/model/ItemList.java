package com.rekuchn.model;


import com.rekuchn.AsciiPanel;
import com.rekuchn.creature.Creature;
import com.rekuchn.spell.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.rekuchn.Global.level;

public class ItemList {
    static String desc;
    static Item item;


    public static Item get(Random random){
        int r = random.nextInt(35) + 1;

        if(r == 1){ return get("Sword", random); }
        if(r == 2){ return get("Axe", random); }
        if(r == 3){ return get("Polearm", random); }
        if(r == 4){ return get("Mace", random); }
        if(r == 5){ return get("Rapid", random); }

        if(r >= 6 && r <= 10){ return get("Armor", random); }
        if(r >= 11 && r <= 15){ return get("Gear", random); }

        if(r >= 16 && r <= 33){ return get("Potion", random); }
        if(r >= 34 && r <= 35){ return get("Ring", random); }

        return null;
    }

    private static int bonus(Random random){
        int b = 0;
        for(int i=4; i<level; i+= 5){ if(random.nextBoolean()){ b ++; } }
        return b;
    }

    private static Color color(int n) {

        if(n > 5){ return AsciiPanel.brightPurple; }
        if(n > 3){ return AsciiPanel.brightBlue; }
        if(n > 1){ return AsciiPanel.brightGreen; }
        if(n > 0){ return AsciiPanel.white; }
        return AsciiPanel.brightBlack;
    }

    private static String[] traits(String type, int bonus, Random random){
        List<String> t = new ArrayList<>();

        switch(type){
            case "Sword": case "Axe": case "Polearm": case "Mace": case "Rapid":
                t.add(type);

                int spec = 0;
                if(bonus > 0){ spec = random.nextInt(bonus); }

                //spec = 12;

                while(spec > 0) {
                    int r = random.nextInt(6) + 1;

                    if(r == 1 && !t.contains("Speedy")){ t.add("Speedy"); spec --; }
                    if(r == 2 && !t.contains("Striking")){ t.add("Striking"); spec --; }
                    if(r == 3 && !t.contains("Power")){ t.add("Power"); spec --; }
                    if(r == 4 && !t.contains("Rending")){ t.add("Rending"); spec --; }
                    if(r == 5 && !t.contains("Slowing")){ t.add("Slowing"); spec --; }
                    if(r == 6 && !t.contains("Poison Strike")){ t.add("Poison Strike"); spec --; }


                    if(random.nextBoolean()){ spec --; }
                }

                break;
            case "Armor":
                int r = random.nextInt(10) + 1;
                if(r == 1){ t.add("Light Armor"); }
                if(r == 2){ t.add("Light Armor"); }
                if(r == 3){ t.add("Hide Armor"); }
                if(r == 4){ t.add("Hide Armor"); }
                if(r == 5){ t.add("Chain Armor"); }
                if(r == 6){ t.add("Chain Armor"); }
                if(r == 7){ t.add("Mail Armor"); }
                if(r == 8){ t.add("Mail Armor"); }
                if(r == 9){ t.add("Splint Armor"); }
                if(r == 10){ t.add("Plate Armor"); }

                //TODO: random armor traits

                break;
        }

        String[] traits = new String[t.size()];
        for(int i=0; i<traits.length; i++){ traits[i] = t.get(i); }
        return traits;
    }



    public static Item get(String name, Random random){

        if(name.equals("Weapon")){
            int r = random.nextInt(5) + 1;
            if(r == 1){ name = "Sword"; }
            if(r == 2){ name = "Axe"; }
            if(r == 3){ name = "Polearm"; }
            if(r == 4){ name = "Mace"; }
            if(r == 5){ name = "Rapid"; }
        }


        int bonus = bonus(random); String desc = ""; int roll = 0; String t;
        String[] traits = traits(name, bonus, random);
        switch (name) {
            case "Sword":
                return new Item("Katana", '(', color(bonus),  0, bonus, traits);
            case "Axe":
                return new Item("Ono", '(', color(bonus),  0, bonus, traits);
            case "Polearm":
                return new Item("Yari", '(', color(bonus),  0, bonus, traits);
            case "Mace":
                return new Item("Kanbo", '(', color(bonus),  0, bonus, traits);
            case "Rapid":
                return new Item("Nunchaku", '(', color(bonus),  0, bonus, traits);


            case "Armor":
                Item armor = new Item("Armor", (char)147, color(bonus),  1, bonus, traits);
                if(traits[0].equals("Light Armor")){ armor.armor = 1; armor.armorCheck = 6; }
                if(traits[0].equals("Hide Armor")){ armor.armor = 2; armor.armorCheck = 8; }
                if(traits[0].equals("Chain Armor")){ armor.armor = 3; armor.armorCheck = 10; }
                if(traits[0].equals("Mail Armor")){ armor.armor = 4; armor.armorCheck = 12; }
                if(traits[0].equals("Splint Armor")){ armor.armor = 6; armor.armorCheck = 16; }
                if(traits[0].equals("Plate Armor")){ armor.armor = 8; armor.armorCheck = 20; }
                return armor;


            case "Gear":
                List<String> gears = new ArrayList<>();
                gears.add("Helm of Health");
                gears.add("Sports Helmet");
                gears.add("Mask of the Tiger");
                gears.add("Cloak of Shadows");
                gears.add("Fireman's Cape");
                gears.add("Football Pads");
                gears.add("Belt of Striking");
                gears.add("Belt of Stamina");
                gears.add("Belt of Balance");
                gears.add("Bracers of Deflection");
                gears.add("Heart Bangle");
                gears.add("Gauntlets of Power");
                gears.add("Boots of Speed");
                gears.add("Spider Boots");
                gears.add("Silk Slippers");
                gears.add("Boots of Dodging");

                gears.add("Rock"); gears.add("Rock"); gears.add("Rock"); gears.add("Rock"); gears.add("Rock");
                roll = random.nextInt(16) + (level / 20);
                return get(gears.get(roll));










            case "Ring":
                List<String> rings = new ArrayList<>();
                rings.add("Gold Ring");
                rings.add("Rune Ring");
                rings.add("Fire Band");
                rings.add("Ring of Wizardry");
                rings.add("Ring of Poison");
                rings.add("Wind Ring");
                rings.add("Rock Band");
                rings.add("Uranium Ring");

                rings.add("Rock"); rings.add("Rock"); rings.add("Rock"); rings.add("Rock"); rings.add("Rock");

                roll = random.nextInt(rings.size());
                // roll = random.nextInt(7) + (level / 20);
                if(random.nextBoolean()){ roll = 0; }
                return get(rings.get(roll));





            case "Potion":
                List<String> pots = new ArrayList<>();
                pots.add("Potion of Health");
                pots.add("Potion of Disintegrate");
                pots.add("Potion of Clairvoyance");
                pots.add("Potion of Telepathy");
                pots.add("Oil of Weapon Enchantment");
                pots.add("Oil of Armor Enchantment");
                pots.add("Potion of Invisibility");
                pots.add("Potion of Might");
                pots.add("Potion of Warp");
                pots.add("Energy Drink");
                pots.add("Potion of Poison Fog");
                pots.add("Potion of Fire");
                pots.add("Wand of Fire");
                pots.add("Potion of FireBall");
                pots.add("Potion of Frailty");
                pots.add("Potion of Web");
                pots.add("Potion of Blessing");
                pots.add("Potion of Levitation");
                pots.add("Potion of Stone Skin");
                pots.add("Potion of Summon Waifu");
                pots.add("Potion of Rats");
                pots.add("Potion of Wolves");
                pots.add("Wand of Frailty");
                pots.add("Potion of Grease");
                pots.add("Wand of Grease");
                pots.add("Nuke Shard");
                pots.add("Wand of Wind");
                pots.add("Potion of Mute");
                pots.add("Potion of Mole Hands");
                pots.add("Potion of Speed");
                pots.add("Potion of Haste");
                pots.add("Potion of Focus");
                pots.add("Wand of Mute");
                pots.add("Elixir of Might");
                roll = random.nextInt(pots.size());
                return get(pots.get(roll));




        }

        return null;
    }






    public static Item get(String name){
        switch (name) {


            case "Helm of Health":
                desc = "Increases your HP 20%";
                return new Item(name, (char)148, color(3), desc, 2, "Hardy");
            case "Sports Helmet":
                desc = "Increases your Defend by 1";
                return new Item(name, (char)148, color(1), desc, 2, "Sports Padding");
            case "Mask of the Tiger":
                desc = "Your attacks have a small chance to make the target bleed.";
                return new Item(name, (char)148, color(3), desc, 2, "Rending");

            case "Cloak of Shadows":
                desc = "Gain Invisibility when in dense plants or darkness.";
                return new Item(name, (char)149, color(3), desc, 3, "Hiding");
            case "Fireman's Cape":
                desc = "Take half damage from fire, lava, and heat.";
                return new Item(name, (char)149, color(3), desc, 3, "Resist Fire");
            case "Football Pads":
                desc = "Increases your Defend by 1.";
                return new Item(name, (char)149, color(1), desc, 3, "Sports Padding");

            case "Belt of Striking":
                desc = "Do an extra damage with melee attacks.";
                return new Item(name, (char)150, color(3), desc, 4, "Striking");
            case "Belt of Stamina":
                desc = "Minor boost to HP and MP.";
                return new Item(name, (char)150, color(3), desc, 4, "Stamina");
            case "Belt of Balance":
                desc = "You're much less likely to slip and fall.";
                return new Item(name, (char)150, color(3), desc, 4, "Balance");

            case "Bracers of Deflection":
                desc = "Can be used to avoid light attacks.";
                return new Item(name, (char)151, color(1), desc, 5, "Deflection");
            case "Gauntlets of Power":
                desc = "Deal 3 extra damage with magic attacks.";
                return new Item(name, (char)151, color(3), desc, 5, "Power");
            case "Heart Bangle":
                desc = "Health orbs are twice as effective.";
                return new Item(name, (char)151, color(3), desc, 5, "Healthy Orbs");


            case "Boots of Speed":
                desc = "Lets you outrun most monsters.";
                return new Item(name, (char)152, color(3), desc, 6, "Speedy");
            case "Spider Boots":
                desc = "Lets you move and evade like normal on spiderwebs.";
                return new Item(name, (char)152, color(3), desc, 6, "Web Walking");
            case "Silk Slippers":
                desc = "Using any potion will also recover 2 MP.";
                return new Item(name, (char)152, color(3), desc, 6, "Potion Connoisseur");
            case "Boots of Dodging":
                desc = "Lets you spring out of the way of some attacks.";
                return new Item(name, (char)152, color(3), desc, 6, "Dodging");





            case "Gold Ring":
                desc = "Doesn't do much, but it sure looks nice.";
                item = new Item(name, (char)248, AsciiPanel.brightYellow, desc, 7);
                return item;
            case "Rune Ring":
                desc = "Protects the wearer from magic damage.";
                item = new Item(name, (char)248, AsciiPanel.yellow, desc, 7, "Runic Resistance");
                return item;
            case "Fire Band":
                desc = "Grants the wearer the ability to shoot bolts of fire.";
                item = new Item(name, (char)248, AsciiPanel.brightRed, desc, 7,"Cast Fire Bolt");
                return item;
            case "Ring of Wizardry":
                desc = "Grants the wearer extended spell-casting capacity.";
                item = new Item(name, (char)248, AsciiPanel.brightMagenta, desc, 7, "Wizardry");
                return item;
            case "Ring of Poison":
                desc = "Grants the wearer poison melee strikes.";
                item = new Item(name, (char)248, AsciiPanel.brightGreen, desc, 7, "Poison Strike");
                return item;
            case "Wind Ring":
                desc = "Grants the wearer the ability to shoot blasts of air.";
                item = new Item(name, (char)248, AsciiPanel.brightCyan, desc, 7,"Cast Gust");
                return item;
            case "Rock Band":
                desc = "Grants the wearer the ability to turn to stone.";
                item = new Item(name, (char)248, AsciiPanel.yellow, desc, 7,"Cast Stone Skin");
                return item;
            case "Uranium Ring":
                desc = "Grants the wearer the ability blast radiation all around.";
                item = new Item(name, (char)248, AsciiPanel.brightGreen, desc, 7,"Cast Radiate");
                return item;





            case "Key":
                desc = "This can unlock one door, but only on this floor.";
                item = new Item(name, '+', AsciiPanel.brightYellow, desc); //231
                return item;
            case "Health Orb":
                return new Item(name, (char)7, AsciiPanel.brightRed, "");
            case "Mana Orb":
                return new Item(name, (char)7, AsciiPanel.brightBlue, "");
            case "Heart":
                return new Item(name, (char)3, AsciiPanel.pink, "");
            case "Yen":
                Item yen = new Item(name, (char)4, AsciiPanel.brightWhite, "");
                yen.charges = 20 + level;
                return yen;






            case "Rock":
                desc = "It's just a rock, it doesn't do anything. Well, I guess you could throw it.";
                item = new Item(name, (char)7, AsciiPanel.white, desc);
                item.action = new RockThrow();
                return item;
            case "Potion of Health":
                desc = "Drink to restore all of your HP.";
                item = new Item(name, (char)173, AsciiPanel.brightWhite, desc);
                item.action = new FullHeal();
                return item;
            case "Potion of Might":
                desc = "Drink to gain great might.";
                item = new Item(name, (char)173, AsciiPanel.red, desc);
                item.action = new GainMight();
                return item;
            case "Potion of Warp":
                desc = "Drink to teleport to a random spot.";
                item = new Item(name, (char)173, AsciiPanel.brightMagenta, desc);
                item.action = new RandomTeleport();
                return item;
            case "Potion of Telepathy":
                desc = "Drink to see minds in the dark.";
                item = new Item(name, (char)173, AsciiPanel.brightCyan, desc);
                item.action = new GainTelepathy();
                item.aimType = "Self";
                return item;
            case "Energy Drink":
                desc = "Drink to recover MP.";
                item = new Item(name, (char)173, AsciiPanel.cyan, desc);
                item.action = new GainMana();
                item.aimType = "Self";
                return item;
            case "Potion of Invisibility":
                desc = "Drink to become invisible.";
                item = new Item(name, (char)173, AsciiPanel.brightBlue, desc);
                item.action = new GainInvisibility();
                item.aimType = "Self";
                return item;
            case "Potion of Fire":
                desc = "Throw to strike with fire.";
                item = new Item(name, (char)173, AsciiPanel.brightRed, desc);
                item.action = new FireBolt();
                item.aimType = "Line";
                return item;
            case "Potion of FireBall":
                desc = "Throw to produce fire.";
                item = new Item(name, (char)173, AsciiPanel.brightRed, desc);
                item.action = new FireBall();
                item.aimType = "Line";
                return item;
            case "Potion of Disintegrate":
                desc = "Throw to deconstruct.";
                item = new Item(name, (char)173, AsciiPanel.brightBlack, desc);
                item.action = new Disintegrate();
                item.aimType = "Line";
                return item;
            case "Potion of Clairvoyance":
                desc = "Drink to see.";
                item = new Item(name, (char)173, AsciiPanel.brightWhite, desc);
                item.action = new GainClairvoyance();
                item.aimType = "Self";
                return item;
            case "Potion of Frailty":
                desc = "Throw to cause frailty.";
                item = new Item(name, (char)173, AsciiPanel.magenta, desc);
                item.action = new CauseFrail();
                item.aimType = "Line";
                return item;
            case "Potion of Stone Skin":
                desc = "Drink to gain tough skin.";
                item = new Item(name, (char)173, AsciiPanel.yellow, desc);
                item.action = new GainStoneSkin();
                item.aimType = "Self";
                return item;
            case "Potion of Blessing":
                desc = "Drink to gain a blessing against magic damage.";
                item = new Item(name, (char)173, AsciiPanel.brightYellow, desc);
                item.action = new GainBlessed();
                item.aimType = "Self";
                return item;
            case "Potion of Levitation":
                desc = "Drink to fly.";
                item = new Item(name, (char)173, AsciiPanel.brightBlue, desc);
                item.action = new Action() {
                    @Override
                    public void resolve(Creature creature, int x, int y) {
                        creature.addStatus(new Status("Flying", AsciiPanel.brightBlue, 120, true, true, creature));
                        creature.doAction("can fly");
                    }
                    @Override
                    public String aimType() { return "Self"; }
                };
                item.aimType = "Self";
                return item;
            case "Potion of Poison Fog":
                desc = "Throw to poison foes.";
                item = new Item(name, (char)173, AsciiPanel.brightGreen, desc);
                item.action = new PoisonCloud();
                item.aimType = "Line";
                return item;
            case "Potion of Web":
                desc = "Throw to create a sticky spiderweb.";
                item = new Item(name, (char)173, AsciiPanel.brightGrey, desc);
                item.action = new ThrowWebs();
                item.aimType = "Line";
                return item;
            case "Nuke Shard":
                desc = "Break to burn with radiation.";
                item = new Item(name, (char)7, AsciiPanel.brightGreen, desc);
                item.action = new Radiation();
                item.aimType = "Self";
                return item;
            case "Oil of Weapon Enchantment":
                desc = "Rub on your equipped weapon to improve its bonus.";
                item = new Item(name, (char)173, AsciiPanel.peach, desc);
                item.action = new Action() {
                    @Override
                    public void resolve(Creature creature, int x, int y) {
                        if(creature.worn[0] != null) {
                            creature.worn[0].bonus ++;
                            creature.worn[0].setColor(color(creature.worn[0].bonus));
                            creature.doAction("rub the precious oil on the weapon");
                            creature.doAction("improve the weapon's bonus");
                        } else {
                            creature.doAction("spill the precious oil on the ground");
                        }
                    }
                    @Override
                    public String aimType() { return "Self"; }
                };
                item.aimType = "Self";
                return item;
            case "Oil of Armor Enchantment":
                desc = "Rub on your equipped armor to improve its bonus.";
                item = new Item(name, (char)173, AsciiPanel.peach, desc);
                item.action = new Action() {
                    @Override
                    public void resolve(Creature creature, int x, int y) {
                        if(creature.worn[1] != null) {
                            creature.worn[1].bonus ++;
                            creature.worn[1].setColor(color(creature.worn[1].bonus));
                            creature.doAction("rub the precious oil on the armor");
                            creature.doAction("improve the armor's bonus");
                        } else {
                            creature.doAction("spill the precious oil on the ground");
                        }
                    }
                    @Override
                    public String aimType() { return "Self"; }
                };
                item.aimType = "Self";
                return item;
            case "Wand of Wind":
                desc = "Point to shoot gusts of air.";
                item = new Item(name, (char)253, AsciiPanel.brightCyan, desc);
                item.action = new Gust();
                item.aimType = "Line";
                item.charges = 10;
                return item;
            case "Wand of Fire":
                desc = "Point to shoot bolts of fire.";
                item = new Item(name, (char)253, AsciiPanel.brightRed, desc);
                item.action = new FireBolt();
                item.aimType = "Line";
                item.charges = 4;
                return item;
            case "Potion of Summon Waifu":
                desc = "Drink to call a fellow student. Doesn't always work in narrow areas.";
                item = new Item(name, (char)173, AsciiPanel.brightMagenta, desc);
                item.action = new SummonWaifu();
                item.aimType = "Self";
                return item;
            case "Wand of Frailty":
                desc = "Point to cause frailty.";
                item = new Item(name, (char)253, AsciiPanel.magenta, desc);
                item.action = new CauseFrail();
                item.aimType = "Line";
                item.charges = 4;
                return item;
            case "Potion of Grease":
                desc = "Throw to make a patch of grease.";
                item = new Item(name, (char)173, AsciiPanel.yellow, desc);
                item.action = new ThrowGrease();
                item.aimType = "Line";
                return item;
            case "Wand of Grease":
                desc = "Point to make a patch of grease.";
                item = new Item(name, (char)253, AsciiPanel.yellow, desc);
                item.action = new ThrowGrease();
                item.aimType = "Line";
                item.charges = 6;
                return item;
            case "Potion of Rats":
                desc = "Throw to make a swarm of rats.";
                item = new Item(name, (char)173, AsciiPanel.green, desc);
                item.action = new SummonMonster("Rat", 200, 6);
                item.aimType = "Self";
                return item;
            case "Potion of Wolves":
                desc = "Throw to make a pack of wolves.";
                item = new Item(name, (char)173, AsciiPanel.green, desc);
                item.action = new SummonMonster("Wolf", 260, 4);
                item.aimType = "Self";
                return item;
            case "Potion of Mute":
                desc = "Throw to prevent spell casting.";
                item = new Item(name, (char)173, AsciiPanel.magenta, desc);
                item.action = new CauseStatus("Mute", AsciiPanel.magenta, 40, AsciiPanel.brightYellow, AsciiPanel.darkPink);
                item.aimType = "Line";
                return item;
            case "Wand of Mute":
                desc = "Point to prevent spell casting.";
                item = new Item(name, (char)253, AsciiPanel.magenta, desc);
                item.action = new CauseStatus("Mute", AsciiPanel.magenta, 12, AsciiPanel.brightYellow, AsciiPanel.darkPink);
                item.aimType = "Line";
                item.charges = 5;
                return item;
            case "Potion of Haste":
                desc = "Drink to outrun monsters and make extra attacks.";
                item = new Item(name, (char)173, AsciiPanel.brightCyan, desc);
                item.action = new GainStatus("Haste", AsciiPanel.brightCyan, 40);
                item.aimType = "Self";
                return item;
            case "Potion of Speed":
                desc = "Drink to outrun most monsters.";
                item = new Item(name, (char)173, AsciiPanel.brightGreen, desc);
                item.action = new GainStatus("Speedy", AsciiPanel.brightGreen, 40);
                item.aimType = "Self";
                return item;
            case "Potion of Mole Hands":
                desc = "Drink to gain might and dig fast.";
                item = new Item(name, (char)173, AsciiPanel.orange, desc);
                item.action = new GainStatus("Mole Hands", AsciiPanel.orange, 160);
                item.aimType = "Self";
                return item;
            case "Potion of Focus":
                desc = "Drink to gain a boots to aim.";
                item = new Item(name, (char)173, AsciiPanel.yellow, desc);
                item.action = new GainStatus("Focus", AsciiPanel.yellow, 120);
                item.aimType = "Self";
                return item;
            case "Elixir of Might":
                desc = "Drink to gain might.";
                item = new Item(name, (char)173, AsciiPanel.peach, desc);
                item.action = new AlterMight(1);
                item.aimType = "Self";
                return item;





        }
        return null;
    }


}
