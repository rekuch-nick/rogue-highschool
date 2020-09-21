package com.rekuchn.creature;


import com.rekuchn.AsciiPanel;
import com.rekuchn.model.Effect;
import com.rekuchn.model.Status;
import com.rekuchn.spell.ScrambleMemory;
import com.rekuchn.creature.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class CreatureAttack {



    public static void attack(Creature self, Creature other, String ... note){
        if(self.hp < 1){ return; }

        List<String> tags = new ArrayList<>();
        if(note != null && note.length > 0){
            tags = new ArrayList<>(Arrays.asList(note));
        }

        self.world().effects().add(new Effect(other.x, other.y, AsciiPanel.brightWhite, AsciiPanel.red, 5, 1));

        int attackRoll = self.world().random.nextInt(100);
        if(tags.contains("Melee") && self.worn[0] != null){ attackRoll += self.worn[0].bonus; }
        if(self.hasTrait("Blind")){ attackRoll -= 40; }


        int evadeRoll = other.evade();

        if(self.hasTrait("Focus")){ attackRoll += 20; }
        if(tags.contains("Accurate")){ attackRoll += 100; }
        if(tags.contains("Sword")){ attackRoll += 20; }
        if(attackRoll >= evadeRoll){



            int damageRoll = self.world().random.nextInt(self.might()) + 1;

            if(tags.contains("Magic")){
                while(damageRoll < self.might() / 2){
                    damageRoll = self.world().random.nextInt(self.might()) + 1;
                }
            }




            if(tags.contains("Melee")){
                int n = 0;
                if(self.hasTrait("Striking")){ n += 1; }
                if(self.hasTrait("Striking 2")){ n += 2; }
                if(self.hasTrait("Striking 3")){ n += 3; }
                if(self.hasTrait("Striking 4")){ n += 5; }
                if(self.hasTrait("Striking 5")){ n += 8; }
                if(self.hasTrait("Striking 6")){ n += 13; }
                if(self.hasTrait("Striking 7")){ n += 21; }
                if(self.hasTrait("Striking 8")){ n += 34; }
                damageRoll += n;
            }

            if(tags.contains("Melee") && self.worn[0] != null){
                damageRoll += self.worn[0].bonus;
            }


            if(tags.contains("Magic")){
                int n = 0;
                if(self.hasTrait("Power")){ n += 3; }
                if(self.hasTrait("Power 2")){ n += 5; }
                if(self.hasTrait("Power 3")){ n += 8; }
                if(self.hasTrait("Power 4")){ n += 13; }
                if(self.hasTrait("Power 5")){ n += 21; }
                if(self.hasTrait("Power 6")){ n += 34; }

                damageRoll += n;
            }

            if(tags.contains("Magic")){
                int n = 0;
                if(self.hasTrait("Wizardry")){ n = 2; }
                if(self.hasTrait("Wizardry 2")){ n = 4; }
                if(self.hasTrait("Wizardry 3")){ n = 6; }
                if(self.hasTrait("Wizardry 4")){ n = 8; }
                damageRoll += n;
            }



            if(tags.contains("Strong")){ damageRoll += 4; }


            if(!tags.contains("Magic") && !tags.contains("Mace")){
                damageRoll -= other.defend();
            }

            if(tags.contains("Magic") && other.hasTrait("Blessed")){ damageRoll -= 6; }

            if(tags.contains("Magic") && other.hasTrait("Runic Resistance")){ damageRoll -= 2; }


            damageRoll = Math.max(1, damageRoll);

            //self.notify(AsciiPanel.cyan, "You hit the %s for %d damage.", other.name, damageRoll);
            //other.notify(AsciiPanel.red, "The %s hits you for %d damage.", self.name, damageRoll);
            Color hitColor = self.hostile ? AsciiPanel.red : AsciiPanel.brightCyan;
            self.doAction(hitColor, "hit the %s for %d damage", other.name, damageRoll);
            //other.doAction(AsciiPanel.red, "The %s hits you for %d damage.", self.name, damageRoll);

            if(self.hasTrait("Memory Strike") && self.world().random.nextBoolean() && other.isPlayer){
                new ScrambleMemory().resolve(self, self.x, self.y);
            }

            if(self.hasTrait("Poison Strike") && tags.contains("Melee") && self.world().random.nextBoolean()){
                other.addStatus(new Status("Poison", AsciiPanel.green, 10, true, true, other));
                other.doAction("are poisoned");
            }

            if(self.hasTrait("Rending") && tags.contains("Melee") && self.world().random.nextBoolean() && self.world().random.nextBoolean() && other.hasBlood){
                other.addStatus(new Status("Hemorrhage", AsciiPanel.brightRed, 10, true, true, other));
                other.doAction("are hemorrhaged");
            }

            if(tags.contains("Blinding") && self.world().random.nextBoolean() && other.hasEyes){
                other.addStatus(new Status("Blind", AsciiPanel.blue, 40, true, true, other));
                other.doAction("are blinded");
            }

            if(tags.contains("Slowing") && self.world().random.nextBoolean() && other.hasEyes){
                other.addStatus(new Status("Slow", AsciiPanel.blue, 60, true, true, other));
                other.doAction("are slowed");
            }

            other.takeDamage(damageRoll);
            if(other.hp < 1){
                if(!tags.contains("Quick Attack")) {
                    //self.notify(AsciiPanel.brightCyan, "The %s is defeated.", other.name);
                    //other.notify(AsciiPanel.brightRed, "You are defeated.");
                    Color killColor = self.hostile ? AsciiPanel.red : AsciiPanel.brightGreen;
                    self.doAction(killColor, "defeat the %s", other.name);
                }

                //world.remove(other);
            }

        } else {
            //self.notify(AsciiPanel.white, "You miss the %s.", other.name);
            //other.notify(AsciiPanel.brightWhite, "The %s misses you.", self.name);
            self.doAction(AsciiPanel.white, "miss the %s", other.name);

        }


        //world.remove(other);
    }

}
