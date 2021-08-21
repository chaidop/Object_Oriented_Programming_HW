/* To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   avl.cpp
 * Author: User
 *
 * Created on 6 Μαΐου 2020, 10:33 μμ
 */
#ifndef __AVL_HPP_
#define __AVL_HPP_

#include <iostream>
#include <fstream>
#include <stack>
#include <string>
#include <bits/stl_stack.h>

using namespace std;

class AVL {
public:
private:
  
  class Node {
  private:
    Node *parent, *left, *right;
    int height;
    string element;

  public:
      
    Node(const string& e, Node *parent, Node *left, Node *right);
    Node(const string& e);
    //copy constructor 
    Node(Node& n);
    Node(const Node& n);
    ~Node();
    Node*  getParent() const;
    Node*  getLeft() const;
    Node*  getRight() const;
    string getElement() const;
    int    getHeight() const;
  
    void setLeft(Node* n);
    void setRight(Node* n);
    void setParent(Node* n);
    void setElement(string e);
    void setHeight(int a);
    bool isLeft() const;
    bool isRight() const;
    int rightChildHeight() const;
    int leftChildHeight() const;
    int updateHeight();
    bool isBalanced();
    Node&  operator=(const Node& n);
    bool  operator==(const Node& n);
    bool  operator!=(const Node& n);
  };
private:
  int   size;
  Node* root;//pointer to root
  //o root einai null
  
public:
    class Iterator;
    class Iterator {
      public:
      
      Iterator(AVL::Node* root);
      //copy constructor
      Iterator(const Iterator& it);
      //destructor
      ~Iterator();
      //check if stack still has nodes
      bool hasNext();
      AVL::Node* next();
      Iterator& operator++();
      Iterator operator++(int a);
      string operator*();
      bool operator!=(Iterator it);
      void operator =(Iterator it);
      bool operator==(Iterator it);
      public:
      stack<AVL::Node*> AVLstack; //stack of tree nodes
      AVL::Node* curNode;
      AVL* parenttree;
    };
    
  Iterator begin() const{
    Iterator it(this->root);
    return it;
  } 
  Iterator end() const{
    Iterator it(nullptr);
    return it;
  }
  
  static const int MAX_HEIGHT_DIFF = 1;
  AVL();
  AVL(const AVL& a);
  ~AVL();
  
  Node* getRoot(){ return root;}
  bool contains(string e);
  bool add(string e);
  bool rmv(string e);
  void print2DotFile(char *filename);
  void pre_order(ostream& out);
  //elegxei th sinthiki twn avl, dld lepei anadromika me th voithia tou iterator ka itis stack tou
  //an ta ypodentra kathe komvou exoun maximum diafora ypsous = MAX_HEIGHT_DIFF = 1
  Node* getUnbalancedNode(AVL::Node* n);
  //efoson den threi th sinthiki, peristrefei to dentro pali me th voithia enos iterator allazontas 
  //ta pedia parent, left kai right twn nodes
  void rotateAVL(AVL::Node* unbalancednode);
  //enhmerwnei anadromika to upsos kathe node
  void recursiveIncreaseHeight(AVL::Node* addedNode);
  //vriskei to leftmost child tou dothedos tree me root ton node riza tou
  Node* findLeftmostNode(AVL::Node* root);
  void recursiveUpdateHeight(AVL::Node* curParent);
  
  friend std::ostream& operator<<(std::ostream& out, const AVL& tree);  
  AVL& operator  =(const AVL& avl);
  AVL  operator  +(const AVL& avl);
  AVL& operator +=(const AVL& avl);
  AVL& operator +=(const string& e);
  AVL& operator -=(const string& e);
  AVL  operator  +(const string& e);
  AVL  operator  -(const string& e);
};

#endif