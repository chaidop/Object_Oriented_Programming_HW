/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ce326.hw2;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.io.File;

/**
 *
 * @author user
 */
public class YUVImage {
    YUVPixel yuvImg[][];
    int width, height;
    static final int MAX_BRIGHTNESS = 255;
    //short Y, U, V;
    String yuv;
    public YUVImage(int width, int height){
        short  y, u, v;
        y = 16;
        u = 128;
        v = 128;
        this.width = width;
        this.height = height;
        yuvImg = new YUVPixel[height][width];
        for( int i=0; i<height; i = i+1){
            for( int j=0; j<width; j = j+1){
                yuvImg[i][j] = new YUVPixel(y, u, v);
            }
        }
    }
    public YUVImage(YUVImage copyImg){
        this(copyImg.width, copyImg.height);
        //yuvImg = new YUVPixel[height][width];
        for( int i=0; i<height; i = i+1){
            for( int j=0; j<width; j = j+1){
                yuvImg[i][j] = copyImg.yuvImg[i][j];
            }
        }
    }
    public YUVImage(RGBImage RGBImg){
        width = RGBImg.getWidth();
        height = RGBImg.getHeight();
        yuvImg = new YUVPixel[height][width];
        for( int i=0; i<height; i = i+1){
            for( int j=0; j<width; j = j+1){
                YUVPixel temp = new YUVPixel(RGBImg.image[i][j]);
                yuvImg[i][j] = temp;
            }
        }
    }
    public YUVImage(java.io.File file)throws FileNotFoundException,UnsupportedFileFormatException{
        short Y, U, V;
        String temp,temp1,temp2,temp3;
        if(!file.exists())
            throw new FileNotFoundException();
        
            Scanner sc = new Scanner(file);
            int i=0;
            int j = 0;
            if(sc.hasNext()){
                temp = sc.next();
                if(!"YUV3".equals(temp)){
                    throw new UnsupportedFileFormatException("Error Image is NOT in supported YUV form!");
                }
            }
            if(sc.hasNext()) {
                width = sc.nextInt();
                height = sc.nextInt();
                //System.out.println("width:"+width+" height: "+height);
            }
            short [][] pixels = new short[height][width*3];
            while(sc.hasNext()) {
                
                pixels[j][i++] = sc.nextShort();
                
               //i++;
                if(i == width*3) {
                    i = 0;
                    j++;
                }
            }
            //System.out.println(pixels[0][0]);
            yuvImg = new YUVPixel[height][width];
            for(j =0;j<height;j++){
                for(i=0;i<width;i++){
                    Y= pixels[j][3*i];
                    U= pixels[j][(3*i)+1];
                    V= pixels[j][(3*i)+2];
                    yuvImg[j][i] = new YUVPixel(Y, U, V);
                }
            }
    }
    public String toString(){
        short y, u, v;
        StringBuilder yuvbuilder = new StringBuilder();
        yuv = "YUV3"+"\n"+width+" "+ height+"\n";
        for(int i = 0;i<height;i = i+1){
            for(int j = 0; j<width;j = j+1){
                y = yuvImg[i][j].getY();
                u = yuvImg[i][j].getU();
                v = yuvImg[i][j].getV();
                yuvbuilder.append(y).append(" ").append(u).append(" ").append(v).append("\n");
            }
        }
        yuv = yuvbuilder.toString();
        return(yuv);
    }
    public void toFile(java.io.File file){
        toString();
        String str;
        try{
            if(file.exists()){
                str = file.getPath();
                //System.out.println(str);
                File temp = new File(str);
                file.delete();
                temp.createNewFile();
            }
            FileWriter fw = new FileWriter(file);
            //PrintWriter wr = new PrintWriter(fw);
            fw.write(yuv);
            fw.close();
        }catch(FileNotFoundException ex){
            System.out.println("Error File not found!");
        }
        catch(IOException ex){
            System.out.println("Error File not found!");
        }
        
        //System.out.println("OK YUV FILE SAVED!");
    }
    public void equalize(){
        String strh;
        short newY;
        short oldY;
        Histogram hist = new Histogram(this);
        strh = hist.toString();
//        System.out.println("histogram: "+strh);
        hist.equalize();
        for(int i = 0; i < height; i = i+1){
            for(int j = 0; j < width; j = j+1){
            oldY = yuvImg[i][j].getY();
            newY = hist.getEqualizedLuminocity(/*hist.equalizedImg[i]*/oldY);
            //System.out.println("WTF "+newY+" "+hist.equalizedImg[oldY]);
            yuvImg[i][j].setY(newY);
            }
        }
//        for(int i =0; i < height; i = i+1){
//            for(int j =0; j < width; j= j+1){
//                oldY = yuvImg[i][j].getY();
//                yuvImg[i][j].setY(newY[oldY]);
//                System.out.println("old and new y for "+i+" "+j+": "+oldY +" "+ newY);
//            }
//        }
        //System.out.println("eq done from yuv!!!!!!!!!");
    }
    
}
