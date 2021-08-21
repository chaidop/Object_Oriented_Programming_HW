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
public class RGBPixel {
    static final int MAX_COLOR = 255;
    private int pixel;

    public RGBPixel(short red, short green, short blue){
        pixel=red;
        pixel = pixel<<8;
        pixel += green;
        pixel = pixel<<8;
        pixel += blue;
    }
    public RGBPixel(RGBPixel n_pixel){
      short n_red, n_green, n_blue;
      n_red = n_pixel.getRed();
      //System.out.println("RED "+n_red);
      n_green = n_pixel.getGreen();
      //System.out.println("GREEN "+n_green);
      n_blue = n_pixel.getBlue();
      //System.out.println("BLUE "+n_blue);
      pixel=n_red;
      pixel = pixel<<8;
      pixel += n_green;
      pixel = pixel<<8;
      pixel += n_blue;
    }
    public RGBPixel(YUVPixel ypixel){
        short Y, U, V;
        int C, D, E, R, G, B;
        Y = ypixel.getY();
        U = ypixel.getU();
        V = ypixel.getV();
        C = Y - 16;
        D = U - 128;
        E = V - 128;
        R = clip(( 298 * C + 409 * E + 128) >> 8);
        G = clip(( 298 * C - 100 * D - 208 * E + 128) >> 8);
        B = clip(( 298 * C + 516 * D + 128) >> 8);
        pixel=R;
        pixel = pixel<<8;
        pixel += G;
        pixel = pixel<<8;
        pixel += B;
    }
    public int clip(int num){
        if(num < 0){
            return(0);
        }
        else{
            if(num > 255){
                return(255);
            }
            else{
                return(num);
            }
        }
    }
    
    public short getRed(){
        short red_tmp;
        int temp_pixel;
        temp_pixel = pixel;
        temp_pixel = temp_pixel>>>16;
        red_tmp = (short)temp_pixel;
        return(red_tmp);
    }
    public short getGreen(){
        short green_tmp;
        int temp_pixel;
        temp_pixel = pixel;
        temp_pixel = temp_pixel<<16;
        temp_pixel = temp_pixel>>>24;
        green_tmp = (short)temp_pixel;
        return(green_tmp);
    }
    public short getBlue(){
        short blue_tmp;
        int temp_pixel;
        temp_pixel = pixel;
        temp_pixel = temp_pixel<<24;
        temp_pixel = temp_pixel>>>24;
        blue_tmp = (short)temp_pixel;
        return(blue_tmp);
    }
    void setRed(short red){
        short green_tmp, blue_tmp;
        Integer temp2, temp3;
        temp2 = pixel;
        temp3 = pixel;
        
        temp2 = temp2<<16;//apomonwnei to green
        temp2 = temp2>>>24;
        temp3 = temp3<<24;//apomonwnei to blue
        temp3 = temp3>>>24;
        
        green_tmp = temp2.shortValue();
        blue_tmp = temp3.shortValue();
        pixel=red;
        pixel = pixel<<8;
        pixel += green_tmp;
        pixel = pixel<<8;
        pixel += blue_tmp; 
    }
     void setGreen(short green){
        short red_tmp, blue_tmp;
        Integer temp2, temp3;
        temp2 = pixel;
        temp3 = pixel;
        
        temp2 = temp2<<8;//apomonwnei to red
        temp2 = temp2>>>24;
        temp3 = temp3<<24;//apomonwnei to blue
        temp3 = temp3>>>24;
        
        red_tmp = temp2.shortValue();
        blue_tmp = temp3.shortValue();
        pixel=red_tmp;
        pixel = pixel<<8;
        pixel += green;
        pixel = pixel<<8;
        pixel += blue_tmp;
    }
      void setBlue(short blue){
        short green_tmp, red_tmp;
        Integer temp2, temp3;
        temp2 = pixel;
        temp3 = pixel;
        
        temp2 = temp2<<8;//apomonwnei to red
        temp2 = temp2>>>24;
        temp3 = temp3<<16;//apomonwnei to green
        temp3 = temp3>>>24;
        red_tmp = temp2.shortValue();
        green_tmp = temp3.shortValue();
        pixel=red_tmp;
        pixel = pixel<<8;
        pixel += green_tmp;
        pixel = pixel<<8;
        pixel += blue; 
    }
      int getRGB(){
          return(pixel);
      }
      void setRGB(int value){
          this.pixel = value;
      }
      final void setRGB(short red, short green, short blue){
        pixel=red;
        pixel = pixel<<8;
        pixel += green;
        pixel = pixel<<8;
        pixel += blue;
      }
    public String toString(){
        short n_red, n_green, n_blue;
        StringBuilder str = new StringBuilder();
        String finalstr;
        Integer temp1, temp2, temp3;
        temp1 = pixel;
        temp2 = pixel;
        temp3 = pixel;
        temp1 = temp1<<8;//apomonwnei to red
        temp1 = temp1>>>24;
        temp2 = temp2<<16;//green
        temp2 = temp2>>>24;
        temp3 = temp3<<24;//blue
        temp3 = temp3>>>24;
        n_red = temp1.shortValue();
        n_green = temp2.shortValue();
        n_blue = temp3.shortValue();
        str.append(n_red).append(" ").append(n_green).append(" ").append(n_blue);
        finalstr = str.toString();
        return(finalstr);
    }
}
