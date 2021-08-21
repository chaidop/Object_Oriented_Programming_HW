/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
#include "AVL.hpp"
#include <iostream>
#include <fstream>
#include <stack>
#include <string>
#include <bits/stl_stack.h>

AVL::Node::Node(const string& e, AVL::Node *parent, AVL::Node *left, AVL::Node *right){
       height = 1;
       element = e;

      if(parent != nullptr)
        this->parent = parent;
      else
        this->parent = nullptr;
      if(left != nullptr)
        this->left = left;
      else
        this->left = nullptr;
      if(right != nullptr)
        this->right = right;
      else
        this->right = nullptr;
      
      updateHeight();
      }
    AVL::Node::Node(const string& e/*, Node *parent*/){
      height = 1;
      element = e;
      parent = nullptr;
      left = nullptr;
      right=nullptr;
    }
    //copy constructor 
    AVL::Node::Node(Node& n){
      //cout<< "copy constructor node"<<endl;
      height = n.getHeight();
      element = n.getElement();
      if(n.getParent() != nullptr)
        this->parent = n.getParent();
      else
        this->parent = nullptr;
      
      if(n.getLeft() != nullptr){
        this->left =  n.getLeft();
     }
      else
        this->left = nullptr;
        
      if(n.getRight() != nullptr){
        this->right =  n.getRight();
        }
      else
        this->right = nullptr;
      updateHeight();
    }
    AVL::Node::Node(const AVL::Node& n){
      height = n.getHeight();
      element = n.getElement();
      if(n.getParent() != nullptr)
        this->parent = n.getParent();
      else
        this->parent = nullptr;
      
      if(n.getLeft() != nullptr){
        this->left =  n.getLeft();
     }
      else
        this->left = nullptr;
        
      if(n.getRight() != nullptr){
        this->right =  n.getRight();
        }
      else
        this->right = nullptr;
      updateHeight();
    }
    
    AVL::Node::~Node(){
        if(this != nullptr){
            if(this->getLeft() != nullptr)
                delete this->getLeft();
            if(this->getRight() != nullptr)
                delete this->getRight();
        }
    }
    AVL::Node*  AVL::Node::getParent() const{ return parent;}
    AVL::Node*  AVL::Node::getLeft() const{ return left;}
    AVL::Node*  AVL::Node::getRight() const{ return right;}
    string AVL::Node::getElement() const{ return element;}
    int    AVL::Node::getHeight() const{ return height;}
  
    void AVL::Node::setLeft(AVL::Node* n){
        if(this!= nullptr){
        left = n;
        updateHeight();
        }
    }
    void AVL::Node::setRight(AVL::Node* n){

        if(this != nullptr){
        right = n;
        updateHeight();
       }
    }
    void AVL::Node::setParent(AVL::Node* n){
        if(this != nullptr){
        parent = n;
        if(n!=nullptr)
        n->updateHeight();
       }
    }
    void AVL::Node::setElement(string e){element = e;}
    void AVL::Node::setHeight(int a){height = a;}
    AVL::Node&  AVL::Node::operator=(const AVL::Node& n){
      height = n.height;
      element = n.getElement();
      parent = new (nothrow) AVL::Node(n.getParent()->getElement(),n.getParent()->getParent(),n.getParent()->getLeft(),n.getParent()->getRight());
      parent->height = n.getParent()->getHeight();
      left = new (nothrow) AVL::Node(n.getLeft()->getElement(),n.getLeft()->getParent(),n.getLeft()->getLeft(),n.getLeft()->getRight());
      left->height = n.getLeft()->getHeight();
      right = new (nothrow) AVL::Node(n.getRight()->getElement(),n.getRight()->getParent(),n.getRight()->getLeft(),n.getRight()->getRight());
      right->height = n.getRight()->getHeight();
      return *this;
    }
    bool  AVL::Node::operator==(const AVL::Node& n){
      if(height == n.height && element == n.getElement()){
          return true;
      }
      return false;
    }
    bool  AVL::Node::operator!=(const AVL::Node& n){
        return !operator==(n);
    }
     bool AVL::Node::isLeft() const{
      if(getParent() != nullptr && (getParent())->getLeft() == this){
        return true;
      }
      return false;
    }
    bool AVL::Node::isRight() const{
      if(getParent() == nullptr){
        return false;
      }
      return !isLeft();
    }
    int  AVL::Node::rightChildHeight() const{
      if(right != nullptr)
        return getRight()->getHeight();
      return 0;
    }
    int  AVL::Node::leftChildHeight() const{
      if(left != nullptr)
        return getLeft()->getHeight();
      return 0;
    }
    int  AVL::Node::updateHeight(){
      if(rightChildHeight()>leftChildHeight()){
        height = rightChildHeight() + 1;
        return height;
      }
      height = leftChildHeight() + 1;
      return height;
    }
    bool AVL::Node::isBalanced(){
      if(abs(rightChildHeight()-leftChildHeight())<=MAX_HEIGHT_DIFF)
        return true;
      return false;
    }
    
    void AVL::pre_order(ostream& out){
  //create an iterator with first root node the begin() node
  AVL::Iterator it(this->begin()) ;
  if(it != end()){
  while ((it.AVLstack).empty() == false) { 
    AVL::Node* node = (it.AVLstack).top(); 
    out << node->getElement() <<" ";
    (it.AVLstack).pop(); 
    if (node->getRight()) 
      (it.AVLstack).push(node->getRight()); 
    if (node->getLeft()) 
      (it.AVLstack).push(node->getLeft()); 
  } 
  }
}

bool AVL::contains(string e){
  
  if( root == nullptr)
      return false;
  if(root->getElement() == e){
      return true;
  }
  AVL::Iterator it(begin());
  it.next();
  while(it != end()){
    if(it.curNode->getElement() == e){
      return true;
    }
    it.next();
  }
  return false;
}

bool AVL::add(string e){
   AVL::Node* new_node = new (nothrow) AVL::Node(e);
  if( !new_node ){
      cout<<"OUT OF MEMORY BOUNDS!"<<endl;
    return false;// Out of memory
  }
  if(contains(e) != true){
       //dhladh an exw ftijei ena avl me ton default constructor, tote apla 
    if(root == nullptr){
        root = new_node;
        return true;
    }
    else{
    AVL::Iterator it(this->begin());
    while (it != end()) {  
        //an h leksh e einai alphabitika mikroterh apton trexon komvo tou iterator
        //tote phgaine sto left child tou trexon komvou
        if(e.compare(it.curNode->getElement())<=0){
            if(it.curNode->getLeft() == nullptr){//efoson den exei left child, valto ekei
                it.curNode->setLeft(new_node);
                new_node->setParent(it.curNode);
                //an den exei allo paidi, aykshse anadromika to ypsos
                if(it.curNode->getRight() == nullptr)
                    recursiveIncreaseHeight(new_node);
                break;
            }
            else{//an exei left child, synexise thn preorder diatreksh
                ++it;
                continue;
            }
        }  
        else{
            if(it.curNode->getRight() == nullptr){
                it.curNode->setRight(new_node);
                new_node->setParent(it.curNode);
                if(it.curNode->getLeft() == nullptr)//efoson den exei left child,
                    //tote prosthetontas ton new_node tha auksisw recursively to ypsos
                    recursiveIncreaseHeight(new_node);
                break;
            }
            else{
                Iterator it_fromright_subtree(it.curNode->getRight());
                it = it_fromright_subtree;
                
                continue;
            }
        }
    }
      //molis teleiwsei h while, o curNode tou iterator einai to rightmost child tou dentrou
      //kai psaxnw na dw pou tha ton prosthesw
      ++size;
      
      AVL::Node* unbalanced = getUnbalancedNode(new_node);
      if(unbalanced !=nullptr){//dhladh efoson yparxei ontws paraviash tou balance
        rotateAVL(unbalanced);//peristrefei apo ton node pou yparxei h paraviash
      }
    }
    return true;
  }
  else{
      delete new_node;
  }
  return false;
}

AVL::Node* AVL::getUnbalancedNode(AVL::Node* addedNode){
  //desmeyw mnhmh sto heap etsi wste na mh diagraei to antikeimeno otan epistrepsei apo th synarthsh
    AVL::Node* curParent;

    curParent = addedNode->getParent();
   
  while(curParent != nullptr){
    if(curParent->isBalanced() == false)
      return curParent;
    curParent = curParent->getParent();
  }
  //twra exoume ton komvo ston opoio tha ginei h peristrofh
  //elexgo akoma an o cur einai o root kai einai unbalanced
    return nullptr;
}

void AVL::rotateAVL(AVL::Node* unb){
  //vriskei to paidi me megalutero ypsos
AVL::Node* maxsubtree;
int child;
  if(unb->rightChildHeight() >= unb->leftChildHeight()){
    maxsubtree = unb->getRight();
    child = 2;//dld to deksi paidi
  }
  else{
    maxsubtree = unb->getLeft();
    child = 1;//left child
  }
  //vriskei twra ti eidos peristrofh tha kanei anloga me to poia meria tou komvou paidiou exei megalutero height
  if(maxsubtree->rightChildHeight()>maxsubtree->leftChildHeight()){
      if(child == 2){//tote aplh aristerh peristrofh
        //to aristero paidi tou tha to parei o goneas tou         
        if( unb!=root && unb->isLeft() == true ) {
            unb->getParent()->setLeft(maxsubtree);
        }
        else if( unb!=root && unb->isRight() == true ) {
            unb->getParent()->setRight(maxsubtree);
        }
        
        if(unb == root){
            root = maxsubtree;
        }
        unb->setRight(maxsubtree->getLeft());
        maxsubtree->setParent(unb->getParent());
        maxsubtree->getLeft()->setParent(unb);
        maxsubtree->setLeft(unb);
        unb->setParent(maxsubtree);
        unb->updateHeight();
        maxsubtree->updateHeight();
        
      }
      else{
        //diplh peristrofh, h prwth mia aplh aristerh sto eswteriko 

        unb->setLeft(maxsubtree->getRight());
        maxsubtree->setParent(maxsubtree->getRight());
        maxsubtree->getRight()->setParent(unb);
        maxsubtree->setRight(maxsubtree->getRight()->getLeft());
        maxsubtree->getRight()->setParent(maxsubtree);
        unb->getLeft()->setLeft(maxsubtree);
        unb->updateHeight();
        
        maxsubtree->updateHeight();
        maxsubtree = unb->getLeft();
        maxsubtree->updateHeight();
        //kai mia aplh deksia
        
        if( unb!=root && unb->isLeft() == true ) {
            unb->getParent()->setLeft(maxsubtree);
        }
        else if( unb!=root && unb->isRight() == true ) {
            unb->getParent()->setRight(maxsubtree);
        }
         
        if(unb == root){
            root = maxsubtree;
        }
        //maxsubtree->setParent(unb->getParent());
        unb->setLeft(maxsubtree->getRight()); 
        maxsubtree->setParent(unb->getParent());
        maxsubtree->getRight()->setParent(unb);
        maxsubtree->setRight(unb);
        unb->setParent(maxsubtree);
        unb->updateHeight();
        maxsubtree->updateHeight();
        
       
      }
  }
  else if(maxsubtree->rightChildHeight()<maxsubtree->leftChildHeight()){
      if(child == 1){//tote aplh deksia peristrofh
       
        if( unb!=root && unb->isLeft() == true ) {
            unb->getParent()->setLeft(maxsubtree);
        }
        else if( unb!=root && unb->isRight() == true ) {
            unb->getParent()->setRight(maxsubtree);
        }
        
        if(unb == root){
            root = maxsubtree;
        }
        unb->setLeft(maxsubtree->getRight()); 
        maxsubtree->setParent(unb->getParent());
        maxsubtree->getRight()->setParent(unb);
        maxsubtree->setRight(unb);
        unb->setParent(maxsubtree);
        unb->updateHeight();
        maxsubtree->updateHeight();
        
      }
      else{
        //diplh peristrofh, h prwth mia aplh deksia sto eswteriko 
      
        unb->setRight(maxsubtree->getLeft());
        maxsubtree->setParent(maxsubtree->getLeft());
        maxsubtree->getLeft()->setParent(unb);
        maxsubtree->setLeft(maxsubtree->getLeft()->getRight());
        maxsubtree->getLeft()->setParent(maxsubtree);
        unb->getRight()->setRight(maxsubtree);
        unb->updateHeight();
        maxsubtree->updateHeight();
        maxsubtree = unb->getRight();
        maxsubtree->updateHeight();
        
        
        //kai mia aplh aristerh
        
        if( unb!=root && unb->isLeft() == true ) {
            unb->getParent()->setLeft(maxsubtree);
        }
        else if( unb!=root && unb->isRight() == true ) {
            unb->getParent()->setRight(maxsubtree);
        }
        
        if(unb == root){
            root = maxsubtree;
        }
        unb->setRight(maxsubtree->getLeft());
        maxsubtree->setParent(unb->getParent());
        maxsubtree->getLeft()->setParent(unb);
        maxsubtree->setLeft(unb);
        unb->setParent(maxsubtree);
        unb->updateHeight();
        maxsubtree->updateHeight();
        
      }
  }
    else if (maxsubtree->rightChildHeight()==maxsubtree->leftChildHeight()){
        if(child == 2){//tote aplh aristerh peristrofh
        //to aristero paidi tou tha to parei o goneas tou 
        
        if( unb!=root && unb->isLeft() == true ) {
            unb->getParent()->setLeft(maxsubtree);
        }
        else if( unb!=root && unb->isRight() == true ) {
            unb->getParent()->setRight(maxsubtree);
        }
        
        if(unb == root){
            root = maxsubtree;
        }
        unb->setRight(maxsubtree->getLeft());
        maxsubtree->setParent(unb->getParent());
        maxsubtree->getLeft()->setParent(unb);
        maxsubtree->setLeft(unb);
        unb->setParent(maxsubtree);
        unb->updateHeight();
        maxsubtree->updateHeight();
        
      }
      else{
        //aplh deksia
        
        if( unb!=root && unb->isLeft() == true ) {
            unb->getParent()->setLeft(maxsubtree);
        }
        else if( unb!=root && unb->isRight() == true ) {
            unb->getParent()->setRight(maxsubtree);
        }
        
        if(unb == root){
            root = maxsubtree;
        }
        unb->setLeft(maxsubtree->getRight()); 
        maxsubtree->setParent(unb->getParent());
        maxsubtree->getRight()->setParent(unb);
        maxsubtree->setRight(unb);
        unb->setParent(maxsubtree);
        unb->updateHeight();
        maxsubtree->updateHeight();
        
    }
    }
recursiveUpdateHeight(maxsubtree);
}
//******************************************************************************
//******************************************************************************
////*********AVL METHODS *******************************************************

AVL::AVL(){
    root = nullptr;
    size = 0;
}
AVL::AVL(const AVL& a){
      size = 0;
      root = nullptr;
    if(a.root != nullptr){
        root = new (nothrow) AVL::Node(a.root->getElement()/*,a.root*/);
        AVL::Iterator it(a.root);
        it.next();
        it.next();
        while(it != end()){
        //o iterator trexei ana preorder ara prosthetei kai ana preorder
            add(it.curNode->getElement());
            it.next();
        }
    }
  }

AVL::~AVL(){
      //
    if(root != nullptr){
        size = 0;
        delete root;
    }
  }
//vriskei leftostchild tou deksiou ypodentrou
AVL::Node* AVL::findLeftmostNode(Node* root){
    AVL::Node* current = root;
  if( root != nullptr){
//    AVL::Iterator it(root);
//    while(it != end()){
//      if(it.curNode->getHeight() == 1){//dld an exw ftasei sto prwto leaf node
//        return it.curNode;
//      }
//      it.next();
//    }
      while(current->getLeft() != nullptr){
          current = current->getLeft();
      }
  return current;
  }
  return nullptr;
}

bool AVL::rmv(string e){
    AVL::Node* swapNode;
    //o parent tou node gia svisimo(ton thelw gia na elegxw apo eei kai panw th sinthiki balance
    AVL::Node* prevParent;
    AVL::Node* prevGrandParent;//o pateras tou prevParent, leitourgei gia elegxo sinthikis
  if(contains(e) == true){
    AVL::Iterator it(begin());
    it.next();
    while(it != end()){
        //trexei mexri na vrei pou einai o zhtoumenos komvos
      if(it.curNode->getElement()== e){
          //afou to vrei, elegxei an exei deksi subtree
          if(it.curNode->getRight() !=nullptr)
            swapNode = findLeftmostNode(it.curNode->getRight());
          else if(it.curNode->getLeft() !=nullptr)
            swapNode = findLeftmostNode(it.curNode->getLeft());
          else//an o node gia diagrafh einai fyllo ('h riza xwris paidia)
              swapNode = it.curNode;
          
          //check if the removed node was root, then the leftmost child would be nullptr
            prevParent = swapNode->getParent();
//            if(it.curNode == prevParent){
//                
//                prevParent = it.curNode->getParent();
//            }
            //if(swapNode != it.curNode){
            it.curNode->setElement(swapNode->getElement());
//            if(it.curNode == prevParent){
//                prevParent = it.curNode->getParent();
//            }
            //elegxei an o leftmost exei right subtree
            if(swapNode->getRight() != nullptr){
                //if(swapNode->isLeft()){
                    swapNode->getRight()->setParent(swapNode->getParent());
                    swapNode->getParent()->setRight(swapNode->getRight());
                    
                //}
//                else if(swapNode->isRight()){
//                    swapNode->getRight()->setParent(swapNode->getParent());
//                    swapNode->getParent()->setLeft(swapNode->getLeft());
//                }
                    
            }
//            if(swapNode != it.curNode){
//                if(it.curNode->isLeft())
//                it.curNode->getParent()->setLeft(swapNode);
//                else if(it.curNode->isRight())
//                it.curNode->getParent()->setRight(swapNode);
//                else if(!it.curNode->isRight()&& !it.curNode->isLeft())//o rmved einai h riza
//                    root->setElement(swapNode->getElement());
//            }
            
            if(swapNode == it.curNode){
             if(!it.curNode->isRight()&& !it.curNode->isLeft()){//o rmved einai h riza
                 if(swapNode->isLeft()){
                    swapNode->getParent()->setLeft(nullptr);
                  }
                  else{
                    swapNode->getParent()->setRight(nullptr);
                  }
                  swapNode->setParent(nullptr);
                 delete swapNode;
                 root = nullptr;
            }
             else{
                 if(swapNode->isLeft()){
                    swapNode->getParent()->setLeft(nullptr);
                  }
                  else{
                    swapNode->getParent()->setRight(nullptr);
                  }
                  swapNode->setParent(nullptr);
                 delete swapNode;
             }
            }
            else{
                if(swapNode->isLeft()){
                    if(swapNode->getRight() == nullptr)
                    swapNode->getParent()->setLeft(nullptr);
                     else
                        swapNode->setRight(nullptr);
                  }
                  else{
                    if(swapNode->getRight() == nullptr)
                    swapNode->getParent()->setRight(nullptr);
                    else
                        swapNode->setRight(nullptr);
                  }
                  swapNode->setParent(nullptr);
            delete swapNode;
            }
            //replace node to remove with its leftmost child

//          if(swapNode != it.curNode){
//            
//            it.curNode->setLeft(nullptr);
//            it.curNode->setRight(nullptr);
//            it.curNode->setParent(nullptr);
//            it.curNode->setHeight(0);
//            delete it.curNode;
//            it.curNode = swapNode;
//          }
//          else{
//            if(swapNode->isLeft()){
//              prevParent->setLeft(nullptr);
//            }
//            else if(swapNode->isRight()){
//              prevParent->setRight(nullptr);
//            }
//            it.curNode->setLeft(nullptr);
//            it.curNode->setRight(nullptr);
//            it.curNode->setParent(nullptr);
//            it.curNode->setHeight(0);
//            delete it.curNode;
//          }
          
          if(prevParent !=nullptr){//dhladh an o rmvd node den htan riza pou den eixe alla paidia
          recursiveUpdateHeight(prevParent);
          while(prevParent != nullptr){
          //kanei update to height olwn tvn kommvwn
          //gia na elegksei an yparxei provlhma gia anastrofh
          
          //an yparxei provlima sth sinthiki avl, tote ayth tha emfanistei ston parent tou removed Node

          //get unbalanced node
            while(prevParent != nullptr){
              if(prevParent->isBalanced() == false)
                  break;
              prevParent = prevParent->getParent();
            }
          
            //efoson yparxei unbalanced node
            if(prevParent != nullptr){
            prevGrandParent = prevParent->getParent();
            rotateAVL(prevParent);
            recursiveUpdateHeight(prevParent);
            prevParent = prevGrandParent;
            }
            else{
                return true;
            }
          }
          return true;
          }
          else{
              return true;
          }
      }
      else
          ++it;
    }
  }
  return false;
}

void AVL::print2DotFile(char *filename){
  AVL::Iterator it(begin());
  string dotstr = "graph Trie {\r\n";
  ofstream out(filename);
  out << dotstr;
  for(int i = 0; it!=end(),i<size ;i++){
    out << "\t"<< i <<" [label=="<<*it<<" ,shape=circle, color=";
    if(it.curNode->getLeft() == nullptr && it.curNode->getRight() == nullptr )
      out << "red]\r\n";
    else
      out << "black]\r\n";
    ++it;
  }
  out << "}";
  out.close();
}

void AVL::recursiveIncreaseHeight(AVL::Node* addedNode){
  //kanei updateheight() ton curNode->getParent() kai paei anadromika se kathe parent tou 
  //mexri na ftasei sth riza
  AVL::Node* curParent = addedNode->getParent();
  
  while(curParent != begin().curNode){
    curParent->updateHeight();
    curParent = curParent->getParent();
  }
  //molis curParent == root
  begin().curNode->updateHeight();
}

void AVL::recursiveUpdateHeight(AVL::Node* curParent){
  //kanei updateheight() ton curNode->getParent() kai paei anadromika se kathe parent tou 
  //mexri na ftasei sth riza
  
  while(curParent != begin().curNode){
    curParent->updateHeight();
    curParent = curParent->getParent();
  }
  //molis curParent == root
  begin().curNode->updateHeight();
}

std::ostream& operator<<(std::ostream& out,const AVL& tree){
  AVL::Iterator it(tree.begin()) ;
  it.next();
  while (it != tree.end()){ 
    out << it.curNode->getElement() <<" ";
    ++it;
  }
  return out;
}

AVL &AVL::operator=(const AVL& a){
   //elegxei an einai keno, alliws anadromika kanei delete
   //efoson den einai keno, ektelei kwdika tou destructor
  if(size > 0){
    if(root != nullptr){//oso den exei ftasei sto telos
        delete root;
    }
  }
    size = 0;
    root = nullptr;
    if(a.root != nullptr){
        root = new (nothrow) AVL::Node(a.root->getElement()/*,a.root*/);
        AVL::Iterator it(a.root);
        it.next();
        it.next();
        while(it != end()){
        //o iterator trexei ana preorder ara prosthetei kai ana preorder
            add(it.curNode->getElement());
            it.next();
        }
    }
  return *this;
}

AVL AVL::operator+(const AVL& avl){
   AVL merge(*this);//ftiaxnei me preorder ton merge apo ton this
   Iterator it(avl.begin());
   it.next();
   while(it != avl.end()){
    merge.add(it.curNode->getElement());
    it.next();
  }
  return merge;
}

AVL& AVL::operator +=(const AVL& avl){
  AVL::Iterator it(avl.begin());
  ++it;
  while(it != avl.end()){
    add(it.curNode->getElement());
    ++it;
  }
  return *this;
}

AVL& AVL::operator +=(const string& e){
    add(e);
  return *this;
}

AVL AVL::operator +(const string& e){
  AVL newtree(*this);
  newtree.add(e);
  return newtree;
}

AVL& AVL::operator-=(const string& e){
  rmv(e);
  return *this;
}

AVL AVL::operator-(const string& e){
  AVL newavl = *this;
  newavl.rmv(e);
  return newavl;
}

/*******************************************************************************
 *******************************************************************************
 ***********ITERATOR METHODS*/


AVL::Iterator::Iterator(AVL::Node* root) {  //tha vazw to Iterator::begin()
        if (root != nullptr) { 
          AVLstack.push(root); // add to end of queue 
          curNode = root;
          //next();
        }  
        else{
            curNode = nullptr;
        }
} 
      
      //copy constructor
AVL::Iterator::Iterator(const AVL::Iterator& it){
        //elegxei an exei kapoio komvo mesa sth stack
        if(it.curNode != nullptr){
            curNode = it.curNode;
            parenttree = it.parenttree;
           
            AVLstack = it.AVLstack;
        }
        else{
            curNode = nullptr;
            parenttree = nullptr;
        }
      }

      //destructor
      AVL::Iterator::~Iterator(){
        curNode = nullptr;
       }
      //check if stack still has nodes
      bool AVL::Iterator::hasNext() {  
          return !AVLstack.empty();  
      }  

      AVL::Node* AVL::Iterator::next() {  
        if (!hasNext()) { 
            curNode = nullptr;
            return nullptr;
        }  
        else{
          AVL::Node* res = AVLstack.top(); // retrieve and remove the head of queue
          AVLstack.pop();
          curNode = res;
          if (res->getRight() != nullptr) AVLstack.push(res->getRight());  
          if (res->getLeft() != nullptr) AVLstack.push(res->getLeft());         
          return curNode;  
        }
        return curNode;
      }  

      AVL::Iterator& AVL::Iterator::operator++(){
        next();
        return *this;  
      }  
      AVL::Iterator AVL::Iterator::operator++(int a){
        Iterator it = *this;
        ++*this;
        return it;  
      }
        
      string AVL::Iterator::operator*(){
       if(!AVLstack.empty() && AVLstack.top() == curNode){next();}
       return curNode->getElement();
//          if(curNode != nullptr){
//              if(AVLstack.top() == curNode){next();}//dhladh an einai h riza,, alla an den exei prolavei na valei kati sth stack akoma( provlhma arxikopoihshs iterator)
//              return curNode->getElement();
//          }
//          else{
//              return "KENO";
//          }
      } 

      bool AVL::Iterator::operator!=(Iterator it){

          
        //an den deixnoun ston idio
        if(curNode != it.curNode){
          return true;
        }
        return false;
      }
       void AVL::Iterator::operator =(Iterator it){
        
        if(curNode != nullptr){
            curNode = nullptr;
        }
        if(it.curNode != nullptr){
            curNode = it.curNode;
            parenttree = it.parenttree;
            AVLstack = it.AVLstack;
        }
        else{
            curNode = nullptr;
            parenttree = nullptr;
        }
        
      }
      bool AVL::Iterator::operator==(Iterator it){
        return !operator!=(it);
      }       