/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ce326.hw2;
import java.io.*;
import java.lang.*;
import java.util.Scanner;
/**
 *
 * @author user
 */
public class PPMImage extends RGBImage{//uses UnsuportedFileException
    //RGBPixel ppmimage [][];
    String str;
    //int width, height, colordepth;
    public PPMImage(java.io.File file)throws FileNotFoundException,UnsupportedFileFormatException{     
        super(128, 128, 255);
        Scanner sc;
        String temp;
        if(!file.exists() || !file.canRead())
            throw new FileNotFoundException();
        
            sc = new Scanner(file);
            int i=0;
            int j = 0;
            
            if(sc.hasNext()){
                temp = sc.next();
                if(!"P3".equals(temp)){
                    throw new UnsupportedFileFormatException("Error Image is NOT in supported PPM form!");
                }
            }
            if(sc.hasNext()) {
                super.width = sc.nextInt();
//                super.width = Integer.valueOf(temp1);
                super.height = sc.nextInt();
//                super.height = Integer.valueOf(temp2);
            }
            if(sc.hasNext()) {
                super.colordepth = sc.nextInt();
            }
            short [][] pixels = new short[super.height][super.width*3];
            while(sc.hasNext()) {
                pixels[j][i] = sc.nextShort();
                short sh = pixels[j][i];
               i++;
               //System.out.print(sh+" ");
                if(i == super.width*3) {
                    i = 0;
                    j++;
                    //System.out.println("\n");
                }
            }
            super.resizeImg(super.height,super.width);
            for(j =0;j<super.height;j++){
                for(i=0;i<super.width;i = i+1){
                    RGBPixel pix =  new RGBPixel(pixels[j][3*i],pixels[j][(3*i)+1],pixels[j][(3*i)+2]);
                    super.image[j][i] = new RGBPixel(pix);
                    //System.out.println(super.image[j][i].getRed()+" "+super.image[j][i].getGreen()+" "+super.image[j][i].getBlue()+" ");
                }
            }
    }
    public PPMImage(RGBImage img){
        super(128, 128, 255);
        StringBuilder strbuilder = new StringBuilder();
        short redn, greenn, bluen;
        super.width = img.getWidth();
        super.height = img.getHeight();
        super.colordepth = img.getColorDepth();
        resizeImg(super.height,super.width);
        strbuilder.append("P3\n").append(super.width).append(" ").append(super.height).append("\n").append(super.colordepth).append("\n");
        for(int i = 0;i<img.getHeight();i = i+1){
            for(int j = 0; j<img.getWidth();j = j+1){
                super.image[i][j] = new RGBPixel(img.getPixel(i, j));
                redn = img.image[i][j].getRed();
                greenn = img.image[i][j].getGreen();
                bluen = img.image[i][j].getBlue();
                strbuilder.append(redn).append(" ").append(greenn).append(" ").append(bluen).append("\n");
            }
        }
        str = strbuilder.toString();
    }
    public PPMImage(YUVImage img){
        super(128, 128, 255);
        short redn, greenn, bluen;
        StringBuilder strbuilder = new StringBuilder();
        RGBImage rgb = new RGBImage(img);
        super.width = rgb.getWidth();
        super.height = rgb.getHeight();
        super.colordepth = rgb.getColorDepth();
        resizeImg(super.height,super.width);
        strbuilder.append("P3\n").append(super.width).append(" ").append(super.height).append("\n").append(super.colordepth).append("\n");
        for(int i = 0;i<rgb.getHeight();i = i+1){
            for(int j = 0; j<rgb.getWidth();j = j+1){
                super.image[i][j] = new RGBPixel(rgb.getPixel(i, j));
                redn = rgb.image[i][j].getRed();
                greenn = rgb.image[i][j].getGreen();
                bluen = rgb.image[i][j].getBlue();
                strbuilder.append(redn).append(" ").append(greenn).append(" ").append(bluen).append("\n");
            }
        }
        str = strbuilder.toString();
    }
    public String toString(){
        short redn, greenn, bluen;
        StringBuilder strbuilder = new StringBuilder();
        strbuilder.append("P3\n").append(super.width).append(" ").append(super.height).append("\n").append(super.colordepth).append("\n");
        for(int i = 0;i<super.height;i = i+1){
            for(int j = 0; j<super.width;j = j+1){
                redn = super.image[i][j].getRed();
                greenn = super.image[i][j].getGreen();
                bluen = super.image[i][j].getBlue();
                strbuilder.append(redn).append(" ").append(greenn).append(" ").append(bluen).append("\n");
            }
        }
        str = strbuilder.toString();
        return(str);
    }
    
    public void toFile(java.io.File file){
        toString();
        //System.out.println("width: "+width+"height: "+height);
        try{
            if(!file.exists()){
                file.createNewFile();
            }
            PrintWriter wr = new PrintWriter(file);
            wr.write(str);
            wr.close();
        }catch(FileNotFoundException ex){
            System.out.println("Error File not found!");
        }
        catch(IOException io){
            System.out.println("Error File not found!");
        }
        //System.out.println("OK PPM FILE SAVED!");
    }
}
