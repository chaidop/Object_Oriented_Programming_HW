/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ce326.hw2;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*; 
import java.lang.*;

/**
 *
 * @author user
 */
public class PPMImageStacker{
    PPMImage [] imageList;
    File[] filesinDir;
    LinkedList<File> list;
   // int [][] avgPixel;
    int [][] avgRed ;
    int [][] avgGreen ;
    int [][] avgBlue ;
    public PPMImageStacker(java.io.File dir)throws FileNotFoundException, UnsupportedFileFormatException{
        list = new LinkedList<File>();
        
//        try{
        if(!dir.exists())
            throw new UnsupportedFileFormatException("[ERROR] Directory "+dir+" does not exist!");
        if(!dir.isDirectory())
            throw new UnsupportedFileFormatException("[ERROR] "+dir+" is not a directory!");
//        }catch(UnsupportedFileFormatException ex){
//            System.out.println(ex.msg);
//        }
        
        filesinDir = dir.listFiles();
        list.addAll(Arrays.asList(filesinDir));
    }
    public void stack()throws FileNotFoundException, UnsupportedFileFormatException{
        imageList = new PPMImage [list.size()] ;
        int i = 0;
        int temp_pixel, red_tmp,green_tmp;
        for(File f : list){
            imageList[i] = new PPMImage(f);
            i++;
            
            //System.out.println("FILE "+f.getName()+"Height:"+imageList[0].height+" width: "+imageList[0].width);
        }
        //System.out.println("Number of pictures in list "+imageList.length);
        //avgPixel= new int [imageList[0].height][imageList[0].width];
        avgRed= new int [imageList[0].height][imageList[0].width];
        avgGreen= new int [imageList[0].height][imageList[0].width];
        avgBlue= new int [imageList[0].height][imageList[0].width];
        //System.out.println("Avgpixel:::::: RGB");
        for(i = 0; i < imageList.length; i = i+1){
            for(int j = 0;  j< imageList[0].height; j = j+1){
                for( int k= 0; k < imageList[0].width; k = k+1){//prosthetei pixel kathe eikonas
                    avgRed[j][k] += imageList[i].image[j][k].getRed(); 
                    avgGreen[j][k] += imageList[i].image[j][k].getGreen(); 
                    avgBlue[j][k] += imageList[i].image[j][k].getBlue(); 
                   // avgPixel[j][k] = avgPixel[j][k] + imageList[i].getPixel();
                    //System.out.println(avgPixel[j][k]+" "+imageList[i].image[j][k].getRGB());
                   if(j == 0 && k == 0){
                   //System.out.println("Image "+i+" green value: "+imageList[i].image[j][k].getGreen());
                    //System.out.println(avgPixel[j][k]+" "+imageList[i].image[j][k].getRGB());
                    //System.out.println("AVG GREEN "+i+" : "+avgGreen[j][k]);
                    }
                }
                //System.out.println("NEXT COLUMN");
            }
        }
       
        for(int j = 0;  j< imageList[0].height; j = j+1){
            for( int k= 0; k < imageList[0].width; k = k+1){
                avgRed[j][k] = avgRed[j][k]/imageList.length;
                avgGreen[j][k] = avgGreen[j][k]/imageList.length;
                avgBlue[j][k] = avgBlue[j][k]/imageList.length;
                //System.out.println(avgPixel[j][k]);
            }
        }
//        temp_pixel = avgPixel[0][0];
//        temp_pixel = temp_pixel<<16;
//        temp_pixel = temp_pixel>>>24;
//        green_tmp = (short)temp_pixel;
        //System.out.println("avg "+avgGreen[0][0]);
    }
    public PPMImage getStackedImage()throws FileNotFoundException,UnsupportedFileFormatException {
        //this.stack();
        RGBImage stackedRGB = new RGBImage(imageList[0].width, imageList[0].height,imageList[0].colordepth);
        //System.out.println("stacked width and height::::::"+stackedRGB.width+"  "+stackedRGB.height);
        RGBPixel pxl = new RGBPixel((short)255,(short)255,(short)255);
        for(int j = 0;  j< imageList[0].height; j = j+1){
            for( int k= 0; k < imageList[0].width; k = k+1){
                
                pxl.setRGB((short)avgRed[j][k],(short)avgGreen[j][k],(short)avgBlue[j][k]);
//                if((j == 143) && (k == 249)){
//                System.out.print("avg: "+avgPixel[j][k]+" pixl: "+pxl);
//                }
                stackedRGB.setPixel(j, k, pxl);
            }
        }
        PPMImage stackedImg = new PPMImage(stackedRGB);
        return(stackedImg);
    }
}
