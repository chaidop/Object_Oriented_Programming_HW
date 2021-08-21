/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ce326.hw2;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;


/**
 *
 * @author user
 */
public class Histogram {
    public final static int MAX_COLOR = 255;
    public int[] histo = new int[MAX_COLOR];
    public int[] equalizedImg = new int[MAX_COLOR];
    
    public Histogram(YUVImage img){
        short y;
        equalizedImg = new int[255];
        //System.out.println("height "+img.height+" and width "+img.width);
        for(int i=0; i<img.height;i = i+1){
            for(int j=0; j<img.width;j = j+1){
                y = img.yuvImg[i][j].getY();
                //System.out.println("y at img"+i+" "+j+": "+y);
                histo[y] = histo[y] + 1;//h thesh i antistoixei se i foteinothta
       //kai kathe thesh exei ton arithmo twn pixel pou exoun tetoia foteinothta
            }
        }
//            for(int j=0; j<255;j = j+1){
//                System.out.println("y value: "+j+"histo at y: "+histo[j]);
//            }
        
    }
    public String toString(){
        String str[] = new String[MAX_COLOR];
        String final_str = new String();
        int[] histo_copy = new int[MAX_COLOR];
        //histo_copy = histo;de mporw giati dixnoyn sto idio antikeieno
        System.arraycopy(histo, 0, histo_copy, 0, MAX_COLOR);
//         for(int j=0; j<255;j = j+1){
//                System.out.println("IN TOSTRING y value: "+j+"histo at y: "+histo[j]);
//         }
        for(int i=0; i<MAX_COLOR;i = i+1){
//               str[i] = String.valueOf(i);
               if(histo[i] >= 1000){
                   for(int l = histo_copy[i]/1000; l>0; l = l-1){
                       if(str[i]==null){
                            str[i] = "#";
                       }
                       else{
                        str[i] = str[i] + "#";
                       }
                        histo_copy[i] = histo_copy[i] - 1000;
                    }
               }
               if(histo_copy[i] >= 100){
                   for(int l = histo_copy[i]/100;l>0;l = l-1){
                       
                        if(str[i]==null){
                            str[i] = "$";
                        }
                       else{
                        str[i] = str[i] + "$";
                       }
                        histo_copy[i] = histo_copy[i] - 100;
                    }
               }
               
               for(int l= histo_copy[i]; l>0;l = l-1){
                   if(str[i]==null){
                            str[i] = "*";
                    }
                   else{
                    str[i] = str[i] + "*";
                   }
               }
               
               if(histo[i] == 0){
                   str[i] = " ";
               }
               final_str = final_str +i+str[i] + "\n";
              
        }
         //System.out.println("histogram: "+final_str);
        return(final_str);
    }
    public void toFile(File file)throws IOException{
        String histostr,str;
        histostr = toString();///kai ektypwse se arxeio....
        try{
        if(file.exists()){
                str = file.getPath();
                System.out.println(str);
                File temp = new File(str);
                file.delete();
                temp.createNewFile();
            }
            FileWriter fw = new FileWriter(file);
            //PrintWriter wr = new PrintWriter(fw);
            fw.write(histostr);
            fw.close();
        }catch(FileNotFoundException ex){
            System.out.println("Error File not found!");
        }
    }
    public void equalize(){
        int total =0;
        double pmf[] = new double[MAX_COLOR];
        double cdf[] = new double[MAX_COLOR];
        //estimate pmf
        for(int i=0; i<MAX_COLOR; i = i+1){
            total = total + histo[i];
            //System.out.println("total="+total+" histo["+i+"]:"+histo[i]);
        }
        //System.out.println("total="+total+"\nPMF:");
        for(int i=0; i<MAX_COLOR; i = i+1){
            pmf[i] = (double)histo[i]/total;
            //.println(pmf[i]);
        }
        //estimate cdf
        //System.out.println("CDF:\tequalized:");
        cdf[0] = pmf[0];
        equalizedImg[0] = (int)(cdf[0]*235);
        for(int i=1; i<MAX_COLOR; i = i+1){
            cdf[i] = cdf[i-1] + pmf[i];
            equalizedImg[i] = (int)(cdf[i]*235);
            //.println(cdf[i]+"\t"+equalizedImg[i]);
        }
//        histo = equalizedImg;
        //an sth thesh x exw fotinothta timhs y, tote ola ta yuvpixels me 
        //foteinothta x tha metatrapoun se yuvpixel me foteinothta y
        //toString();
        //System.out.println("Equalization done!!");
    }
    public short getEqualizedLuminocity(int luminocity){
        short out;
        Integer temp = new Integer(equalizedImg[luminocity]);
        
        out = temp.shortValue();
        
        return(out);
    }
}
