package com.buoobuoo.minecraftenhanced.core.util.unicode;

public class UnicodeSpaceUtil {
    public static final String NEG1 ="\uF801";
    public static final String NEG2 ="\uF802";
    public static final String NEG4 ="\uF804";
    public static final String NEG8 ="\uF808";
    public static final String NEG16 ="\uF809";
    public static final String NEG32 ="\uF80A";
    public static final String NEG64 ="\uF80B";
    public static final String NEG128="\uF80C";
    public static final String NEG256="\uF80D";
    public static final String NEG512="\uF80E";
    public static final String NEG1024="\uF80F";

    public static final String POS1 ="\uF821";
    public static final String POS2 ="\uF822";
    public static final String POS4 ="\uF824";
    public static final String POS8 ="\uF828";
    public static final String POS16 ="\uF829";
    public static final String POS32 ="\uF82A";
    public static final String POS64 ="\uF82B";
    public static final String POS128="\uF82C";
    public static final String POS256="\uF82D";
    public static final String POS512="\uF82E";
    public static final String POS1024="\uF82F";

    public static String getNeg(int pixel) {
        String binary = new StringBuilder(Integer.toBinaryString(pixel)).reverse().toString();
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for(char c : binary.toCharArray()){
            if(c != '0')
            {
                sb.append(NegativeChar.getCharByWeight((int)Math.pow(2, index)).s );
            }
            index++;
        }

        return sb.toString();
    }

    public static String getPos(int pixel) {
        String binary = new StringBuilder(Integer.toBinaryString(pixel)).reverse().toString();
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for(char c : binary.toCharArray()){
            if(c != '0')
            {
                sb.append(PositiveChar.getCharByWeight((int)Math.pow(2, index)).s );
            }
            index++;
        }

        return sb.toString();
    }


    private enum NegativeChar{
        NEG1(1, UnicodeSpaceUtil.NEG1),
        NEG2(2, UnicodeSpaceUtil.NEG2),
        NEG4(4, UnicodeSpaceUtil.NEG4),
        NEG8(8, UnicodeSpaceUtil.NEG8),
        NEG16(16, UnicodeSpaceUtil.NEG16),
        NEG32(32, UnicodeSpaceUtil.NEG32),
        NEG64(64, UnicodeSpaceUtil.NEG64),
        NEG128(128, UnicodeSpaceUtil.NEG128),
        NEG256(256, UnicodeSpaceUtil.NEG256),
        NEG512(512, UnicodeSpaceUtil.NEG512),
        NEG1024(1024, UnicodeSpaceUtil.NEG1024);

        private int weight;
        private String s;
        NegativeChar(int weight, String s){
            this.weight = weight;
            this.s = s;
        }

        static NegativeChar getCharByWeight(int weight)
        {
            for(NegativeChar c : NegativeChar.values())
                if(c.weight==weight)
                    return c;
            return null;
        }
    }

    private enum PositiveChar{
        POS1(1, UnicodeSpaceUtil.POS1),
        POS2(2, UnicodeSpaceUtil.POS2),
        POS4(4, UnicodeSpaceUtil.POS4),
        POS8(8, UnicodeSpaceUtil.POS8),
        POS16(16, UnicodeSpaceUtil.POS16),
        POS32(32, UnicodeSpaceUtil.POS32),
        POS64(64, UnicodeSpaceUtil.POS64),
        POS128(128, UnicodeSpaceUtil.POS128),
        POS256(256, UnicodeSpaceUtil.POS256),
        POS512(512, UnicodeSpaceUtil.POS512),
        POS1024(1024, UnicodeSpaceUtil.POS1024);

        private int weight;
        private String s;
        PositiveChar(int weight, String s){
            this.weight = weight;
            this.s = s;
        }

        static PositiveChar getCharByWeight(int weight)
        {
            for(PositiveChar c : PositiveChar.values())
                if(c.weight==weight)
                    return c;
            return null;
        }
    }

}
