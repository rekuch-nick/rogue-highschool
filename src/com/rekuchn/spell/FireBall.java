package com.rekuchn.spell;


import com.rekuchn.AsciiPanel;
import com.rekuchn.World;
import com.rekuchn.creature.Creature;
import com.rekuchn.model.Effect;
import com.rekuchn.service.Cords;

public class FireBall implements Action {

    @Override
    public void resolve(Creature creature, int x, int y) {
        World world = creature.world();

        int lastX = x; int lastY = y;
        for(Cords p : world.getLineBetween(creature.x, creature.y, x, y)){
            if(p.x == creature.x && p.y == creature.y){ continue; }
            world.effects().add(new Effect(p.x, p.y, AsciiPanel.brightYellow, AsciiPanel.red, 0, 0, 0, 0, 6));
            world.burnTile(p.x, p.y);

            lastX = p.x; lastY = p.y;
        }

        for(int a=lastX-2; a<=lastX+2; a++){ for(int b=lastY-2; b<=lastY+2; b++) {
            if(a < 0 || b < 0 || a >= world.width() || b >= world.height()){ continue; }

            world.effects().add(new Effect(a, b, AsciiPanel.brightWhite, AsciiPanel.red, 0, 0, 0, 0, 6));
            world.burnTile(a, b);
            Creature c = creature.world().creature(a, b);
            if (c != null && !c.isPlayer && !c.isWaifu) {
                creature.attack(world.creature(a, b), "Magic", "Fire", "Accurate");
            }
        }}





    }

    @Override
    public String aimType() { return "Line"; }
}






