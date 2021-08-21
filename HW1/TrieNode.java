/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ce326.hw1;

/**
 *
 * @author User
 */
public class TrieNode {

    TrieNode children [];
    //TrieNode fatherNode;
    boolean isTerminal;
    //voithane sth diasxish toy dentrou,an == 1 'h antistoixa true,tote exei elexthei
    int alreadyTraversedNode;
    boolean alreadyInArray;

    public TrieNode(boolean term){
        children = new TrieNode[26];
        isTerminal = term;
        alreadyInArray = false;
        alreadyTraversedNode = 0;
    }
}
