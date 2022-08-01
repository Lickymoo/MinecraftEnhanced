package com.buoobuoo.minecraftenhanced.core.npc.pathfinder.test;

import lombok.AllArgsConstructor;

public class AS {

    static class BL{
        Location loc;
        Material mat;

        public BL(Location loc, Material mat){
            this.loc = loc;
            this.mat = mat;
        }

        boolean start = false;
        boolean end = false;
    }

    @AllArgsConstructor
    static class Location{
        double x;
        double y;
        double z;
    }

    enum Material{
        AIR,
        BLOCK
    }

    public static BL[][] blockMap = new BL[10][10];
    public static void main(String[] args) {
        for(int x = 0; x < 10; x++){
            for(int z = 0; z < 10; z++){
                blockMap[x][z] = new BL(new Location(x, 0, z), Material.AIR);
            }
        }

        for(int x = 0; x < 8; x++){
            blockMap[1][x] = new BL(new Location(x, 0, 1), Material.BLOCK);
        }

        for(int x = 2; x < 10; x++){
            blockMap[4][x] = new BL(new Location(x, 0, 4), Material.BLOCK);
        }

        blockMap[0][0].end = true;
        blockMap[9][9].start = true;

        for(int x = 0; x < 10; x++){
            for(int z = 0; z < 10; z++){
                BL block = blockMap[x][z];
                if (block.start || block.end){
                    System.out.print(block.start ? "S " : "E ");
                    continue;
                }
                System.out.print(block.mat == Material.AIR ? "O " : "? ");
            }
            System.out.println("");
        }

    }
}
