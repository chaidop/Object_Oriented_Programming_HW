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
public class RGBImage implements Image{
    final static int MAX_COLORDEPTH = 255;
    int width, height, colordepth;
    RGBPixel image[][];
    public RGBImage(int width, int height, int colordepth){
        this.width = width;
        this.height = height;
        this.colordepth = colordepth;
        short zero = MAX_COLORDEPTH;
        image = new RGBPixel[height][width];
        for(int i = 0; i < height; i = i+1){
            for(int j = 0; j < width; j = j+1){
                image[i][j] = new RGBPixel(zero, zero, zero);
            }
        }
    }
    public RGBImage(RGBImage copyImg){
        this(copyImg.width, copyImg.height, copyImg.colordepth);
        for(int i = 0; i < height; i = i+1){
            for(int j = 0; j < width; j = j+1){
                image[i][j] = copyImg.image[i][j];
            }
        }
    }
    public RGBImage(YUVImage YUVImg){
        this(YUVImg.width, YUVImg.height, MAX_COLORDEPTH);
        for( int i=0; i<height; i = i+1){
            for( int j=0; j<width; j = j+1){
                RGBPixel temp = new RGBPixel(YUVImg.yuvImg[i][j]);
                image[i][j] = temp;
            }
        }
    }
    void resizeImg(int rows, int cols){
        image = new RGBPixel[rows][cols];
    }
    int getWidth(){
        return(width);
    }
    int getHeight(){
        return(height);
    }
    int getColorDepth(){
        return(colordepth);
    }
    RGBPixel getPixel(int row, int col){
        return(image[row][col]);
    }
    void setPixel(int row, int col, RGBPixel pixel){
        image[row][col] = new RGBPixel(pixel);
    }
    public void grayscale(){
        short Gray;
        short Red, Green, Blue;
        for(int i = 0; i < height; i = i+1){
            for(int j = 0; j < width; j = j+1){
                Red = image[i][j].getRed();
                Green = image[i][j].getGreen();
                Blue = image[i][j].getBlue();
                Gray = (short)((Red * 0.3) +( Green * 0.59) +( Blue * 0.11));
                image[i][j].setRed(Gray);
                image[i][j].setGreen(Gray);
                image[i][j].setBlue(Gray);
            }
        }
    }
    public void doublesize(){
      RGBImage reference  = new RGBImage(this);
      image = new RGBPixel[2*height][2*width];
      for(int i = height-1; i >= 0; i = i-1)  {
          for(int j = width-1; j >= 0; j = j-1)  {
              image[2*i][2*j] = reference.image[i][j];
              image[(2*i)+1][2*j] = reference.image[i][j];
              image[2*i][(2*j)+1] = reference.image[i][j];
              image[(2*i)+1][(2*j)+1] = reference.image[i][j];
          }
      }
      height = 2*height;
      width = 2*width;
      //System.out.println("width:::::"+width+"height:::::"+height);
    }
    public void halfsize(){
        int total_red, total_green, total_blue;
        short avgRed, avgGreen, avgBlue;
        RGBImage copyTemp = new RGBImage(this);
        image = new RGBPixel[height/2][width/2];
        height = height/2;
        width = width/2;
        for(int i = 0; i < height; i = i+1)  {
          for(int j = 0; j <width; j = j+1)  {
            total_red =  copyTemp.image[2*i][2*j].getRed() + copyTemp.image[(2*i)+1][2*j].getRed() +copyTemp.image[2*i][(2*j)+1].getRed() + copyTemp.image[(2*i)+1][(2*j)+1].getRed() ;
            total_green =  copyTemp.image[2*i][2*j].getGreen() + copyTemp.image[(2*i)+1][2*j].getGreen() +copyTemp.image[2*i][(2*j)+1].getGreen() + copyTemp.image[(2*i)+1][(2*j)+1].getGreen() ;
            total_blue =  copyTemp.image[2*i][2*j].getBlue() + copyTemp.image[(2*i)+1][2*j].getBlue() +copyTemp.image[2*i][(2*j)+1].getBlue() + copyTemp.image[(2*i)+1][(2*j)+1].getBlue() ;
            avgRed = (short)(total_red/4);
            avgGreen = (short)(total_green/4);
            avgBlue = (short)(total_blue/4);
//            RGBPixel temp = new RGBPixel((short)128, (short)128, (short)128);
//            temp.setRGB(total);
            image[i][j] = new RGBPixel(avgRed,avgGreen, avgBlue);
          }
        }
        //System.out.println("width:::::"+width+"height:::::"+height);
    }
    public void rotateClockwise(){
        int temp;
        RGBImage clone_img = new RGBImage(height, width, colordepth);
        for(int i=0, j = height-1; i<height;i++,j--){
          for(int k=0; k<width; k++){
               clone_img.image[k][j] = image[i][k];
          }
        }
        resizeImg(width,height);
        image = clone_img.image;
        temp = width;
        width = height;
        height = temp;
    }
    
}
