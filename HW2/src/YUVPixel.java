/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ce326.hw2;

/**
 *
 * @author user
 */
public class YUVPixel {
    private int pixel;
    public YUVPixel(short Y, short U, short V){
        pixel=Y;
        pixel = pixel<<8;
        pixel += U;
        pixel = pixel<<8;
        pixel += V;
    }
    public YUVPixel(YUVPixel n_pixel){
      short Y, U, V;
      Y = n_pixel.getY();
      U = n_pixel.getU();
      V = n_pixel.getV();
      pixel=Y;
      pixel = pixel<<8;
      pixel += U;
      pixel = pixel<<8;
      pixel += V;
    }
    public YUVPixel(RGBPixel RGBpixel){
        short Y, U, V;
        short R, G ,B;
        R= RGBpixel.getRed();
        G = RGBpixel.getGreen();
        B = RGBpixel.getBlue();
        Y =(short) ((short) ( (  (66 * R) + (129 * G) +  (25 * B) + 128) >> 8) +  16);
        U = (short) ((short) ((( -38 * R )-  (74 * G )+ (112 * B) + 128) >> 8) + 128);
        V = (short) ((short)(( (112 * R )-  (94 * G )-  (18 * B )+ 128) >> 8) + 128);
        pixel=Y;
        pixel = pixel<<8;
        pixel += U;
        pixel = pixel<<8;
        pixel += V;
    }
    
     public short getY(){
        short Y_tmp;
        Integer temp_pixel;
        temp_pixel = pixel;
        temp_pixel = temp_pixel>>>16;
        Y_tmp = temp_pixel.shortValue();
        return(Y_tmp);
    }
    public short getU(){
        short U_tmp;
        Integer temp_pixel;
        temp_pixel = pixel;
        temp_pixel = temp_pixel<<16;
        temp_pixel = temp_pixel>>>24;
        U_tmp = temp_pixel.shortValue();
        return(U_tmp);
    }
    public short getV(){
        short V_tmp;
        Integer temp_pixel;
        temp_pixel = pixel;
        temp_pixel = temp_pixel<<24;
        temp_pixel = temp_pixel>>>24;
        V_tmp = temp_pixel.shortValue();
        return(V_tmp);
    }
    void setY(short y){
        short U_tmp, V_tmp;
        Integer temp2, temp3;
        temp2 = pixel;
        temp3 = pixel;
        
        temp2 = temp2<<16;//apomonwnei to u
        temp2 = temp2>>>24;
        temp3 = temp3<<24;//apomonwnei to v
        temp3 = temp3>>>24;
        
        U_tmp = temp2.shortValue();
        V_tmp = temp3.shortValue();
        pixel=y;
        pixel = pixel<<8;
        pixel += U_tmp;
        pixel = pixel<<8;
        pixel += V_tmp; 
    }
     void setU(short green){
        short Y_tmp, V_tmp;
        Integer temp2, temp3;
        temp2 = pixel;
        temp3 = pixel;
        
        temp2 = temp2<<8;//apomonwnei to red
        temp2 = temp2>>>24;
        temp3 = temp3<<24;//apomonwnei to blue
        temp3 = temp3>>>24;
        
        Y_tmp = temp2.shortValue();
        V_tmp = temp3.shortValue();
        pixel=Y_tmp;
        pixel = pixel<<8;
        pixel += green;
        pixel = pixel<<8;
        pixel += V_tmp;
    }
      void setV(short blue){
        short U_tmp, Y_tmp;
        Integer temp2, temp3;
        temp2 = pixel;
        temp3 = pixel;
        
        temp2 = temp2<<8;//apomonwnei to red
        temp2 = temp2>>>24;
        temp3 = temp3<<16;//apomonwnei to green
        temp3 = temp3>>>24;
        Y_tmp = temp2.shortValue();
        U_tmp = temp3.shortValue();
        pixel=Y_tmp;
        pixel = pixel<<8;
        pixel += U_tmp;
        pixel = pixel<<8;
        pixel += blue; 
    }
}
