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
public class UnsupportedFileFormatException extends java.lang.Exception{
    String msg;
    public UnsupportedFileFormatException(){
        msg = null;
    }
    public UnsupportedFileFormatException(String msg){
        this.msg = msg;
    }
}
