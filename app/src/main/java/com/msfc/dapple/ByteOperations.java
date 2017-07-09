package com.msfc.dapple;

/**
 * Created by jtb20 on 7/8/2017.
 */
public class ByteOperations {
    public static byte[] shortToBytes(short s)
    {
        byte[] toReturn={(byte)((s>>>8)&0xff),(byte)(s&0xff)};
        return toReturn;
    }
    public static byte bitsToByte(byte[] bits)
    {
        byte b=0;
        for(int i=7;i>=0;i--)
            b+=(byte)(bits[i]*Math.pow(2,i));
        return b;
    }
    public static short bytesToShort(byte[] bytes)
    {
        return (short)((bytes[0]&0xff)*256+(bytes[1]&0xff));
    }
    public static short bytesToShort(byte one, byte two){
        return (short)((one&0xff)*256+(two&0xff));
    }
    public static byte[] getSubArray(byte[] original, int start, int length)
    {
        byte[] toReturn=new byte[length];
        for(int i = 0;i<length;i++)
        {
            toReturn[i]=original[start+i];
        }
        return toReturn;
    }
    public static byte intToByte(int i)
    {
        return (byte)(i&0xff);
    }
    public static String bytesToString(byte[] bytes)
    {
        String toReturn = "";
        for(int i=0;i<bytes.length;i++)
        {
            toReturn+=""+bytes[i]+" ";
        }
        return toReturn;
    }
}
