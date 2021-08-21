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
public class Trie {
    static final int MAX_WORDS = 14;
    static final int MAX_PARENTS = 50;
    public static final String alphabet = "abcdefghijklmnopqrstuvwxyz";
    TrieNode root;
    TrieNode tempNode;//o trexon komvos
    int ch;

    /*constructors*/
    public Trie() {
        root = new TrieNode(false);
        tempNode = new TrieNode(false);
        tempNode = root;
    }
    
    public Trie(String [] words) {
        root = new TrieNode(false);
        tempNode = new TrieNode(false);
        tempNode = root;
        char letter;
        for(int j = 0; j < words.length; j++){
            if(words[j] == null){
                break;
            }
            for(int i=0; i < words[j].length(); i++){
                letter = words[j].charAt(i);//poio gramma thelw
                ch = alphabet.indexOf(letter);
                if(tempNode.children[ch] == null ){//efoson den uparxei,valto
                    if(i == words[j].length() - 1){
                        //ftiaxnei teliko komvo
                        tempNode.children[ch] = new TrieNode(true);
                        tempNode.children[ch].isTerminal = true;
                        tempNode = root;
                    }
                    else{
                        tempNode.children[ch] = new TrieNode(false);
                        tempNode = tempNode.children[ch];
                    }

                }
                else{ 
                    if( i == words[j].length() - 1){
                        tempNode.children[ch].isTerminal = true;
                        tempNode = root;
                        break;
                    }//an uparxei idi
                    tempNode = tempNode.children[ch];
                }
            }
        }
        tempNode = root;
    }
    
    public boolean add(String word){
        tempNode = root;
        char letter;
        for(int i=0; i < word.length(); i++){
            letter = word.charAt(i);//poio gramma thelw
            ch = alphabet.indexOf(letter);
            if(tempNode.children[ch] == null ){//efoson den uparxei,valto
                if(i == word.length() - 1){
                    //ftiaxnei teliko komvo
                    tempNode.children[ch] = new TrieNode(true);
                    tempNode.children[ch].isTerminal = true;
                }
                else{
                    tempNode.children[ch] = new TrieNode(false);
                    tempNode = tempNode.children[ch];
                }
                
            }
            else{ 
                if( i == word.length() - 1){
                    tempNode.children[i].isTerminal = true;
                    return(false);
                }//an uparxei idi
                tempNode = tempNode.children[ch];
            }
        }
        tempNode = root;
        return(true);
    }
    
   public boolean contains(String word){
    tempNode = root;
    
    for(int i=0; i < word.length(); i++){
        ch = (int) word.charAt(i)-'a';//poio gramma thelw
        if(tempNode.children[ch] == null ){//efoson den uparxei
           return(false);     
        }
        tempNode = tempNode.children[ch];
    }
    return(true);
   }
    
   public int size_anadromi(TrieNode startingNode){
       int totalWords = 0;
        
            for( int i=0;i<26 ; i++){
                 if(startingNode.children[i] != null ){
                    if(startingNode.children[i].isTerminal == true){
                        totalWords++;
                     }
                    totalWords += size_anadromi(startingNode.children[i]);
                 }
            }
                          
        return(totalWords);
   }
   public int size(){
        int totalWords;
        
           totalWords = size_anadromi(root);
                          
        return(totalWords);
    }
  
    public String[] differByOne(String word){
        char letter,letteronTree;
        int j,i,m=0, difference=0;
        TrieNode currentNode;
        currentNode = root;
        String [] pinakas = new String[size()];//giati to max plithos 
        //leksewn epistrofhs tha einai oles oi lekseis
        StringBuilder WordBuffer = new StringBuilder();//max mikos mias lekshs
        for( i = 0; i < word.length() ;i++){
            letter = word.charAt(i);
            ch = word.indexOf(letter);
            for( j =0; j<26; j++){
                if(currentNode.children[j] != null){
                    if(currentNode.children[j].alreadyTraversedNode != 1){
                        letteronTree = alphabet.charAt(j);
                        if(letter != letteronTree){//an exw allo gramma
                            difference++;
                            if(difference <= 1){
                                WordBuffer.append(letteronTree);/*append(letter);*/
                                if(i == word.length()-1){//teleytaio gramma
                                    //an o komvos pou eimai kleinei mia leksi, tote ton dinw ston pinaka
                                    if(currentNode.children[j].isTerminal == true){
                                        pinakas[m] = new String(WordBuffer);
                                        m++;
                                        WordBuffer = new StringBuilder();
                                        difference = 0;
                                    }
                                    currentNode = currentNode.children[j];
                                    j = 26;//gia na bgei h parakatw sinthiki alithis kai na ksekinisei apo ti riza
                                    break;
                                }
                                currentNode = currentNode.children[j];
                                break;//gia na paei sto epomeno gramma
                            }
                        }
                        else{
                            WordBuffer.append(letteronTree);/*append(letter);*/
                            if(i == word.length()-1){
                                 if(currentNode.children[j].isTerminal == true){
                                     //diladi den einai idia leksi, prepei na diaferei toulaxiston kata 1
                                        if(difference >0){
                                            pinakas[m] = new String(WordBuffer);
                                            m++;
                                            WordBuffer = new StringBuilder();
                                        }
                                        difference = 0;
                                    }
                                    currentNode = currentNode.children[j];
                                    j = 26;
                                    break;
                                }
                            currentNode = currentNode.children[j];
                            break;
                        }
                        
                    }
                }
            }
             if(j >= 26){
                 if(currentNode != root){
                    currentNode.alreadyTraversedNode = 1;//gia na mhn ton ksanaperasw
                    currentNode = root;//kai ksanapaw apo th riza
                    WordBuffer = new StringBuilder();
                    difference = 0;
                    i = -1;//elegxei pali apo thn arxh, ara apto prwto gramma
                 }
                 else{//exei ginei olh h diaperash
                     break;
                 }
            }
        }
        TreeInstantiate(root);
        return(pinakas);
    }
    
    
    public String[] differBy(String word, int max){
        char letter,letteronTree;
        int j,i,m=0, difference=0;
        TrieNode currentNode;
        currentNode = root;
        String [] pinakas = new String[size()];//giati to max plithos 
        //leksewn epistrofhs tha einai oles oi lekseis
        StringBuilder WordBuffer = new StringBuilder();//max mikos mias lekshs
        for( i = 0; i < word.length() ;i++){
            letter = word.charAt(i);
            ch = word.indexOf(letter);
            for( j =0; j<26; j++){
                if(currentNode.children[j] != null){
                    if(currentNode.children[j].alreadyTraversedNode != 1){
                        letteronTree = alphabet.charAt(j);
                        if(letter != letteronTree){//an exw allo gramma
                            difference++;
                            if(difference <= max){
                                WordBuffer.append(letteronTree);/*append(letter);*/
                                if(i == word.length()-1){//teleytaio gramma
                                    //an o komvos pou eimai kleinei mia leksi, tote ton dinw ston pinaka
                                    if(currentNode.children[j].isTerminal == true){
                                        pinakas[m] = new String(WordBuffer);
                                        m++;
                                        WordBuffer = new StringBuilder();
                                        difference = 0;
                                    }
                                    currentNode = currentNode.children[j];
                                    j = 26;//gia na bgei h parakatw sinthiki alithis kai na ksekinisei apo ti riza
                                    break;
                                }
                                currentNode = currentNode.children[j];
                                break;//gia na paei sto epomeno gramma
                            }
                        }
                        else{
                            WordBuffer.append(letteronTree);/*append(letter);*/
                            if(i == word.length()-1){
                                 if(currentNode.children[j].isTerminal == true){
                                     //diladi den einai idia leksi, prepei na diaferei toulaxiston kata 1
                                        if(difference >0){
                                            
                                            pinakas[m] = new String(WordBuffer);
                                            m++;
                                            WordBuffer = new StringBuilder();
                                        }
                                        difference = 0;
                                    }
                                    currentNode = currentNode.children[j];
                                    j = 26;
                                    break;
                                }
                            currentNode = currentNode.children[j];
                            break;
                        }
                        
                    }
                }
            }
             if(j >= 26){
                 if(currentNode != root){
                    currentNode.alreadyTraversedNode = 1;//gia na mhn ton ksanaperasw
                    currentNode = root;//kai ksanapaw apo th riza
                    WordBuffer = new StringBuilder();
                    difference = 0;
                    i = -1;//elegxei pali apo thn arxh, ara apto prwto gramma
                 }
                 else{//exei ginei olh h diaperash
                     break;
                 }
            }
        }
        TreeInstantiate(root);
        return(pinakas);
    }
    
    //methodos pou ksanakanei olous tous komvous alreadytraversed = 0, wste na mporoun na diaperastoun
    public void TreeInstantiate(TrieNode startingNode){
        for(int j = 0; j < 26; j++){
            if(startingNode.children[j] != null){
                startingNode.children[j].alreadyTraversedNode = 0;
                startingNode.children[j].alreadyInArray = false;
                TreeInstantiate(startingNode.children[j]);
            }
        }
    }
    
    public String[] wordsOfPrefix(String prefix){
       char letter,letteronTree;
        int j,i,m=0;
        TrieNode currentNode;
        currentNode = root;
        TrieNode afterPrefixRootNode;
        String [] pinakas = new String[size()];
        StringBuilder WordBuffer = new StringBuilder();
        StringBuilder PrefixWord = new StringBuilder();
        StringBuilder Word = new StringBuilder();
        //arxikopooiw tous komvous gia na mporoun na diaperastoun
        TreeInstantiate(root);
        //Psakse arxika an yparxei tetoio prefix sto dentro
        for( i = 0; i < prefix.length() ;i++){
            letter = prefix.charAt(i);
            ch = alphabet.indexOf(letter);
                if(currentNode.children[ch] != null){
                    PrefixWord.append(letter);
                    currentNode = currentNode.children[ch];
                    if(i == prefix.length()-1){
                        if(currentNode.isTerminal == true){//an to prothema yparxei kai os aytonomh leksi
                            pinakas[m] = new String(PrefixWord);
                            m++;                   
                        }
                    }
                }
                else{
                    for(m = 0 ;m< pinakas.length;m++){
                        pinakas[m] = null;
                        return(pinakas);//den yparxei prothema,dinei null pinaka
                    }
                }
        }
        afterPrefixRootNode = currentNode;//komvos anaforas anadromhs
//teleiwses me elegxo yparkshs prefix, phgaine vres tis lekseis twra
            for( j =0; j<26; j++){
                if(currentNode.children[j] != null){
                    if(currentNode.children[j].alreadyTraversedNode != 1){
                        letteronTree = alphabet.charAt(j);
                        WordBuffer.append(letteronTree);/*append(letter);*/
                        if(currentNode.children[j].isTerminal == true){//telos leksis
                            if(currentNode.children[j].alreadyInArray == false){
                                Word.append(PrefixWord);
                                Word.append(WordBuffer);
                                pinakas[m] = new String(Word);
                                m++;
                                WordBuffer = new StringBuilder();
                                Word = new StringBuilder();
                                currentNode = currentNode.children[j];
                                j = 26;
                            }
                            else{
                                currentNode = currentNode.children[j];
                                j = -1;
                            }
                        }
                        else{//an den teleiwse h leksh
                            currentNode = currentNode.children[j];
                            j = -1;//gia na ksekinhsei na skanarei apo thn arxh tou komvou
                        }
                        
                    }
                    if(j == 25){//an dhladh o komvos einai fyllo
                        if(currentNode != afterPrefixRootNode){
                            if(currentNode.children[j].alreadyTraversedNode == 1){
                                currentNode.alreadyTraversedNode = 1;//gia na mhn ton ksanaperasw
                                currentNode = afterPrefixRootNode;//kai ksanapaw apo th riza
                                WordBuffer = new StringBuilder();
                                Word = new StringBuilder();
                                j = -1;//ksekina apo thn arxi        
                            }
                        }
                    }
                }
                else{
                    if(j == 25){//an dhladh o komvos einai fyllo
                        if(currentNode != afterPrefixRootNode){
                            currentNode.alreadyTraversedNode = 1;//gia na mhn ton ksanaperasw
                            currentNode = afterPrefixRootNode;//kai ksanapaw apo th riza
                            WordBuffer = new StringBuilder();
                            Word = new StringBuilder();
                            j = -1;//ksekina apo thn arxi        
                        }
                    }
                }
                if(j >= 26){
                    if(currentNode != afterPrefixRootNode){
                        currentNode.alreadyInArray = true;
                        currentNode = afterPrefixRootNode;//kai ksanapaw apo th riza
                        WordBuffer = new StringBuilder();
                        Word = new StringBuilder();
                        j = -1;//ksekina apo thn arxi        
                    }
                }
            }
        //ksanaarxikopooiw tous komvous
        TreeInstantiate(root);
        return(pinakas);
    }
    
    public String PreOrder(TrieNode startingNode){
        int i;
        char letter;
        String str = new String();
        for(i = 0;i<26;i++){
            if(startingNode.children[i] != null){
                letter = alphabet.charAt(i);
                str = str+" "+letter;
                if(startingNode.children[i].isTerminal == true){
                    str = str + "!";
                }
                str = str + PreOrder(startingNode.children[i]);
            }
        }
        return(str);
    }
    public String toString(){
        String str ;
        str = PreOrder(root);
        return(str);
    }
    
    public String toDotString(/*TrieNode startingNode*/){
        int i,m = 0;
        int [] nodeHashcodes = new int[5*size()];
        char letter;
        String [] label = new String[5*size()];
        String [] color = new String[5*size()];
        TrieNode startingNode = root;
        String[] str = new String[5*size()];
        //gia Windows,to newline einai \r\n sta Linux einai \n
        String dot = "graph Trie {"+"\r\n";
        label[0] = "ROOT";
        color[0] = "black";
        nodeHashcodes[0] = startingNode.hashCode();
        for(i = 0;i<26;i++){
            if(startingNode.children[i] != null){
                //efoson yparxei klados grapse:
                if(startingNode.children[i].alreadyTraversedNode != 1){
                    if(startingNode.children[i].alreadyInArray != true){
                        startingNode.children[i].alreadyInArray = true;
                        if(startingNode.children[i].isTerminal){
                            color[m+1] = "red";
                        }
                        else{
                            color[m+1] = "black";
                        }
                        nodeHashcodes[m+1] = startingNode.children[i].hashCode();
                        letter = alphabet.charAt(i);
                        str[m] = "\t"+startingNode.hashCode()+" -- "+ startingNode.children[i].hashCode()+"\t\n";
                        m++;
                        label[m] = String.valueOf(letter);
                    }
                    startingNode  = startingNode.children[i];
                    i = -1;
                }
            }
            else{
                if(startingNode != root){
                    if(i == 25){
                        startingNode.alreadyTraversedNode = 1;
                        startingNode = root;
                        i = -1;
                    }
                }
            }
        }
        for(m=0; m<str.length;m++){
            if(nodeHashcodes[m] != 0 ){
                        dot = dot +"\t"+nodeHashcodes[m]+"[label=\""+label[m];
                        dot = dot + "\" ,shape=circle, color="+color[m]+"]\n";
                        if(str[m] != null){
                            dot = dot +str[m];
                        }
            }
            else{
                break;
            }
        }
        TreeInstantiate(root);
        dot = dot + "}";
        return(dot);
    }
 }
