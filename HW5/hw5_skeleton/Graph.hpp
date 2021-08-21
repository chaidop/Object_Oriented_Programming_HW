#ifndef _GRAPH_HPP_ 
#define _GRAPH_HPP_
#include <list>
#include <iostream>
#include <queue> 
#include <set>
#include <iterator> 
#include <functional>
#include <fstream>
#define INF 999999
using namespace std;


 class NegativeGraphCycle : public exception {
 public:
     const char* what() const throw() {
         return "Negative Graph Cycle!";
     }
 };

template<typename T>
class Edge {
public:
    T from;
    T to;
    int distance;//to kostos ths akmhs
    Edge(T f, T t, int d) : from(f), to(t), distance(d) {
    }
    bool operator<(const Edge<T>& e) const {
        if (distance < e.distance) {
            return true;
        }
        return false;
    }
    bool operator>(const Edge<T>& e) const {
        return !operator<(e);
    }
    bool operator==(const Edge<T>& v) const {
        if ((from == v.from) && (to == v.to) && (distance == v.distance))
            return true;
        return false;
    }
    template<typename U>
    friend std::ostream& operator<<(std::ostream& out, const Edge<U>& e);

};

//operator overloading for priority queue Compare
//kanei sort me auksousa seira ths tinmhs distance kathe edge
template<typename T>
struct CompareDistance {
    bool operator()(Edge<T> const& e1, Edge<T> const& e2)
    {

        return e1.distance > e2.distance;
    }
};

template<typename T>
std::ostream& operator<<(std::ostream& out, const Edge<T>& e) {
    out << e.from << " -- " << e.to << " (" << e.distance << ")";
    return out;
}

template <typename T>
class Graph {
private:
    bool isDirectedGraph;
    int gsize;//posa nodes exei ews twra
    vector<list<T>> nodes;
    list<Edge<T>> edges;
    //T node_info[];
public:
    Graph(bool isDirectedGraph = true) {
        this->isDirectedGraph = isDirectedGraph;
        gsize = 0;
        //nodes = new vector<list<T>>;

       /* //sthn arxh, gia na ksexwrizw an o i komvos yparxei kai den exw apla kenh thn i thesh,
        //arxikopoiw kathe i thesh/lista tou pinaka kanwntas push ton int i
        for (int i = 0; i < capacity; i++)
            nodes[i]->push_front(i);*/
    }
    bool contains(const T& info) const;
    bool containsedge(const T& from, const T& to) const;
    bool inList(const T& info, list<T> list) const;//elegxei an o info yparxei sthn list
    bool inList(const Edge<T>& info, list<Edge<T>> list) const;
    list<T> dfs_anadromi(const T& info, list<T> dfs_list) const;
    T find_earliest(list<T> list) const;//vriskei ton earliest eisagmeno node sth lista list
    list<T> earliestList(list<T> list) const;//dinei mia taksinomhmenh lista me auksoua seira tou earliest node ston latest ths list
    multiset<Edge<T>> giveEdgeSet(Edge<T>& info, list<Edge<T>> pqueue) const;
    bool addVtx(const T& info);
    bool rmvVtx(const T& info);
    bool addEdg(const T& from, const T& to, int distance);
    bool rmvEdg(const T& from, const T& to);
    Edge<T> giveEdge(const T& from, const T& to) const;
    list<T> dfs(const T& info) const;
    list<T> bfs(const T& info) const;
    list<Edge<T>> mst();
    bool print2DotFile(const char* filename) const;
    list<T> dijkstra(const T& from, const T& to);
    list<T> bellman_ford(const T& from, const T& to);

};

//template <typename T>
//void Graph<T>::expand_table() {
//    std::cout << "Capacity: " << capacity << "->" << 2 * capacity << endl;
//    capacity = 2 * capacity;
//    delete []nodes;
//    nodes = new vector<list<T>>[capacity];
//}
//
//template <typename T>
//void Graph<T>::shrink_table() {
//    std::cout << "Capacity: " << capacity << "->" << capacity/2 << endl;
//    capacity = capacity/2;
//    delete[]nodes;
//    nodes = new list<T>[capacity];
//   
//}
template <typename T>
bool Graph<T>::contains(const T& info) const {
    for (int i = 0; i < gsize; i++) {
        list<T> n = nodes[i];
        for (typename list<T>::iterator it = n.begin(); it != n.end(); ++it) {
            if (*it == info) {
                return true;
            }
        }
    }
    return false;
}

template <typename T>
bool Graph<T>::containsedge(const T& from, const T& to) const {
    for (typename list<Edge<T>>::const_iterator it = edges.cbegin(); it != edges.cend(); ++it) {
        Edge<T> b = *it;
        if ((b.from == from )&& (b.to == to)){
                return true;
        }
    }
    return false;
}

template <typename T>
Edge<T> Graph<T>::giveEdge(const T& from, const T& to) const {
    typename list<Edge<T>>::const_iterator it = edges.cbegin();
    for (; it != edges.cend(); ++it) {
        Edge<T> b = *it;
        if (b.from == from) {
            if (b.to == to) {
                return b;
            }
        }
    }
    //if(it == edges.end()) 
    it = edges.cbegin();
    Edge<T> b = *it;
    b.distance = INF;//sinthiki elegxou an den yparxei
    return b;
}

template <typename T>
bool Graph<T>::addVtx(const T& info) {
    if (!contains(info)) {
        gsize++;
        list<T> n;
        n.push_front(info);
        nodes.push_back(n);//kanw add ton eayto moy, wste na exw thn plhroforia mou
        return true;
    }
    return false;
}

template <typename T>
bool Graph<T>::addEdg(const T& fr, const T& t, int distance) {
    T from = fr;
    T to = t;
    if(contains(from) && contains(to)){
        if (!isDirectedGraph) {
            if (containsedge(to, from)){//an yparxei ontws hdh akmh
                return false;
            }
        }
        if (!containsedge(from, to)) {
            Edge<T> newedge = Edge<T>(from, to, distance);
            edges.push_back(newedge);
            int i;
            int j;
            //list<T> ne;
            for (i = 0; i < gsize; i++) {
                //ne = nodes[i];
                typename list<T>::iterator it = nodes[i].begin();
                if (*it == from)
                    break;
            }

            if (!isDirectedGraph) {
                //an mh kateuthinomeno prosthetw akmh kai apo ton to ston from
                for (j = 0; j < gsize; j++) {
                    typename list<T>::iterator it = nodes[j].begin();
                    if (*it == to)
                        break;
                }
                //list<T>& nr = nodes[j];
                nodes[j].push_back(from);
            }
            //list<T> & n = nodes[i];
            nodes[i].push_back(to);
            //nodes[i].push_back(to);
            return true;
        }
    }
    return false;
}

template <typename T>
bool Graph<T>::rmvVtx(const T& info) {
    if (contains(info)) {
        int i, j;
        //vriskei se poia i thesh tou pinaka geitniashs einai o komvos info
        typename list<T>::iterator it ;
        for (i = 0; i < gsize; i++) {
            it =  nodes[i].begin();
            if (*it == info)
                break;
        }
        //vriskei kathe komvo me ton opoio o info geitniazei kai paei 
        //sthn antistoixh j-osth tou thesh kai kanei remove ton info apo thn lista tou
        ++it;
        for (; it != nodes[i].end(); ++it) {
            rmvEdg(info, *it);
            it = nodes[i].begin();
        }
        for (j = 0; j < gsize; j++) {//diagrafei kai tis akmes apo tous to ston from
            for (typename list<T>::iterator fit = nodes[j].begin(); fit != nodes[j].end(); fit++) {
                if (*fit == info) {
                    it = nodes[i].begin();
                    rmvEdg(nodes[j].front(), info);
                    break;
                }
            }
        }
        it = nodes[i].begin();
        nodes[i].erase(it);
        nodes.erase (nodes.begin()+i);
        gsize--;
        return true;
    }
    return false;
}

template <typename T>
bool Graph<T>::rmvEdg(const T& fr, const T& t) {
    T from = fr;
    T to = t;
    if (!isDirectedGraph) {
        if (!containsedge(from, to)){
            if(!containsedge(to, from))//an den yparxei ontws h akmh
            return false;
        }
        if (containsedge(to, from)) {
                T temp = to;
                to = from;
                from = temp;
        }
    }
    if (containsedge(from, to)) {
        int i;
        int j;
        const Edge<T>& newedge = giveEdge(from, to);
        for (typename list<Edge<T>>::iterator eit = edges.begin(); eit != edges.end(); ++eit) {
            if (*eit == newedge) {//tzakpot ton vrike, delete to node
                edges.erase(eit);
                break;
            }
        }
        //edges.remove(newedge);
        typename list<T>::iterator it;
        for (i = 0; i < gsize; i++) {
            it = nodes[i].begin();
            if (*it == from) {
                break;
            }
        }
        for (; it != nodes[i].end(); ++it) {
            if (*it == to) {//tzakpot ton vrike, delete to node
                nodes[i].erase(it);
                break;
            }
        }
        if (!isDirectedGraph) {
            //const Edge<T>& doublenewedge = giveEdge(to, from);
            //if(doublenewedge.distance != INF)//efoson yparxei tetoia akmh sth lista
            //edges.remove(doublenewedge);
            typename list<T>::iterator it2;
            for (j = 0; j < gsize; j++) {
                it2 = nodes[j].begin();
                if (*it2 == to) {//vriskei ton to gia na kanei delete kai ton from
                      break;
                }
            }                      
            
           ++it2;
           for (; it2 != nodes[j].end(); ++it2) {
                if (*it2 == from) {
                     nodes[j].erase(it2);
                      //nodes[j].erase(it3);
                     break;
                  }
            }
        }
        return true;
    }
    return false;
}

template <typename T>
bool Graph<T>::inList(const T& info, list<T> llist) const {
    if (!llist.empty()) {
        typename list<T>::iterator it = llist.begin();
        for (; it != llist.end(); ++it) {
            if (*it == info) {//tzakpot ton vrike
                return true;
            }
        }
    }
    return false;
}
template <typename T>
bool Graph<T>::inList(const Edge<T>& info, list<Edge<T>> llist) const {
    typename list<Edge<T>>::const_iterator it = llist.cbegin();
    for (; it != llist.cend(); ++it) {
        Edge<T> b = *it;
        if ((b.from == info.from) && (b.to == info.to)) {//tzakpot ton vrike, delete to node
            return true;
        }
        else if (!isDirectedGraph) {
            if ((b.from == info.to) && (b.to == info.from)) {
                return true;
            }
        }
    }
    return false;
}

template <typename T>//dinei oles tis akmes pou ksekinane apo ton to tou info
multiset<Edge<T>> Graph<T>::giveEdgeSet(Edge<T>& info, list<Edge<T>> pqueue) const{
    multiset<Edge<T>> finalset;
    typename list<Edge<T>>::const_iterator it = pqueue.cbegin();
    for (; it != pqueue.cend(); ++it) {
        Edge<T> b = *it;
        if (b.from == info.to && (!(b.to == info.from))) {//vrika mia akmh mou
            finalset.emplace(*it);
        }
        else if (!isDirectedGraph) {
            if ((b.to == info.to)&& (!(b.from == info.from))) {//efoson den einai o eautos tou
                finalset.emplace(*it);
            }
        }
    }
    return finalset;
}

template <typename T>
T Graph<T>::find_earliest(list<T> llist) const {//vriskei sth dosmenh lista list poios einai o poio nwris eisaxthomenos apo ton pinaka geitniashs nodes
    typename list<T>::const_iterator itList;
    for (int i = 0; i < gsize; i++) {
        list<T> n = nodes[i];
        typename list<T>::const_iterator it = n.cbegin();
        if (inList(*it, llist)) {
            //for (itList = llist.cbegin() ;itList != llist.cend(); itList++) {
            //    if ((*itList == *it)) {//efoson o komvos yparxei kai sth lista kai den einai o eautos mou, tote return earliest node
            //        return *it;
            //    }
            //}
            return *it;
        }
    }
    return *itList;
}

template <typename T>
list<T> Graph<T>::earliestList(list<T> llist) const {//vriskei sth dosmenh lista list poios einai o poio nwris eisaxthomenos apo ton pinaka geitniashs nodes
    list<T>sorted;
    list<T>tempList = llist;
    //typename list<T>::iterator itlist = tempList.begin();
    int sizze = llist.size();
    for (int i = 0; i < sizze; i++) {//kanei size-1 fore, mexri o tempList na meinei mono me ton eauto tou
        T earliest = find_earliest(tempList);
        sorted.push_back(earliest);
        tempList.remove(earliest);
    }
    return sorted;
}

template <typename T>
list<T> Graph<T>::dfs(const T& info) const {
    list<T> dfs_list;
    if (contains(info)) { 
        dfs_list = dfs_anadromi(info, dfs_list);
    }
    return dfs_list;
}

template <typename T>
list<T> Graph<T>::dfs_anadromi(const T& info, list<T> dfs_list) const {
    list<T> templist;
    int i = 0;
        dfs_list.push_back(info);
        for (; i < gsize; i++) {
            if (nodes[i].front() == info)
                break;
        }
        list<T> earliest_children = earliestList(nodes[i]);
        //kanei dfs anadromika gia ola ta paidia tou, arxizontas apo to earliest
        for (typename list<T>::iterator ite = earliest_children.begin(); ite != earliest_children.end(); ite++) {
            if (!inList(*ite, dfs_list)) {
                dfs_list = dfs_anadromi(*ite, dfs_list);
            }
        }
        //if (!templist.empty()) {//copy new elements to dfs_list
        //    for (typename list<T>::iterator ite = templist.begin(); ite != templist.end(); ite++) {
        //        if (!inList(*ite, dfs_list))
        //            dfs_list.push_back(*ite);
        //    }
        //}
    return dfs_list;
}


//xwris anadromi, me fifo
template <typename T>
list<T> Graph<T>::bfs(const T& info) const {
    list<T> bfs_list;
    queue<T> fifo;
    T curent = info;
    if (contains(info)) {
        int i;
        fifo.push(curent);
        while (fifo.size() > 0 ) {
            if (!fifo.empty()) {
               curent = fifo.front();
               fifo.pop();
           }
            //fifo.pop();
            //find all children of info node by getting its i-th neighbour list 
            for (i = 0; i < gsize; i++) {
                if (nodes[i].front() == curent) {
                    break;
                }
            }
           if (!inList(curent, bfs_list)){
            bfs_list.push_back(curent);
            list<T> earlychildren = earliestList(nodes[i]);//pare ta paidia tou apo earliest mexri latest
            //prospathise na ta valeis sth fifo
            for (typename list<T>::iterator lit = earlychildren.begin(); lit != earlychildren.end(); lit++) {
                if (!(*lit == curent)) {//elegkse na mhn parw ton eauto mou
                    if (!inList(*lit, bfs_list)) {//efoson den exei hdh diaperastei
                        //valton sth fifo
                        fifo.push(*lit);

                    }
                }
            }
            //pop next node from fifo and next iteration add it to bfslist as already traversed
            
            }
           
        }
        //bfs_list.push_back(curent);
    }
    return bfs_list;
}
template <typename T>
bool Graph<T>::print2DotFile(const char* filename) const {
    std::ofstream outt;
    std::string strfile = filename;
    strfile.append(".dot");
    outt.open(strfile, ios::out | ios::trunc);
    list<Edge<T>> edges_traversed;
    if (!isDirectedGraph)
        outt << "graph {\n";
    else
        outt << "digraph {\n";

if(gsize == 0)return false;
        /*hash<T> hash_fn;
        gsize_t str_hash = hash_fn(*it.from);*/
        // out << "\t" << str_hash << " [label==" << *it << " ,shape=circle, color=black]" << endl;
        for (typename list<Edge<T>>::const_iterator it = edges.cbegin(); it != edges.cend(); it++) {
            /*hash<T> hash_fn;
            hash<T> hash_begin;
            gsize_t endhashcode = hash_fn(*it.to);
            size_t beginhashcode = hash_begin(*it.from);*/
            Edge<T> n = *it;
            if (!isDirectedGraph) {
                //logw mh kateuthinomenou, mporei na ektypwsei edge pou exei ektypwthei hdh
               // Edge<T> doubleedge(n.to, n.from, n.distance);//antistrefw ta from to, giati an exw diplh akmh tote h hdh ektypwmenh akmh eixe prin ws from ton twra to
                //if (!inList(doubleedge, edges_traversed)) {
                    outt << "\t" << n.from << " -- " << n.to << "[label = " << n.distance << ", weight = " << n.distance << "]\n";
                //    edges_traversed.push_back(n);
                //}
            }
            else
                outt << "\t" << n.from << " -> " << n.to << "[label = " << n.distance << ", weight = " << n.distance << "]\n";
        
        }
        for (int j = 0; j < gsize; j++) {
            if (nodes[j].size() == 1)
                outt << "\t" << nodes[j].front() << ";\n";
        }
    outt << "}\n";
    outt.close();
    return true;
}

//Kruskal
template <typename T>
list<Edge<T>> Graph<T>::mst() {
    list<Edge<T>> mstlist;
    list<Edge<T>> mstlist_unordered;
    multiset<Edge<T>> edges_cpy;
    vector<list<T>> Vsubtrees;// = new list<T>[gsize];

    if (!isDirectedGraph) {
        int edgessize = 0;
        for (typename list<Edge<T>>::iterator it = edges.begin(); it != edges.end(); ++it, ++edgessize)
            edges_cpy.emplace(*it);

        //to Vsubtrees arxika exei se kathe thesh tou ton kathe komvo
        for (int i = 0; i < gsize; ++i) {
            list<T> initlist;
            initlist.push_back(nodes[i].front());
            Vsubtrees.push_back(initlist);
        }


        //prepei prin ton prosthesw sth lista edge na elegksw oti den paragei kyklo
        //psakse se poio subtree einai o komvos from tou smallest edge
        //an sto subtree tou yparei hdh kai o komvos to, tote den prosthetw to smallest sthn telikh lista mse
        
        for (int j = 0; !edges_cpy.empty(); ++j) {
            typename set<Edge<T>>::iterator it2 = edges_cpy.begin();
            Edge<T>smallest = *it2;
            edges_cpy.erase(it2);
            //search the subtree of from node
            for (int i = 0; i < gsize; ++i) {
                //an sto trexon subtree einai o from node
                if (inList(smallest.from, Vsubtrees[i])) {
                    /*an den yparxei sto idio subtree, den exw kyklo*/
                    if (!inList(smallest.to, Vsubtrees[i])) {
                        //prosthetw thn akmh enwnontas ta subtrees
                        mstlist_unordered.push_back(smallest);
                        for (int k = 0; k < gsize; k++) {//psaxnei poio htan to subtree tou to gia na synenwsei ta subtree
                            if (inList(smallest.to, Vsubtrees[k])) {
                                for (typename list<T>::iterator it3 = Vsubtrees[k].begin(); it3 != Vsubtrees[k].end(); ) {
                                    T tocopy = *it3;
                                    Vsubtrees[k].remove(*it3);
                                    Vsubtrees[i].push_back(tocopy);
                                    if (Vsubtrees[k].empty())
                                        break;
                                    it3 = Vsubtrees[k].begin();
                                }//enwnei ta subtrees
                                break;
                            }
                        }
                    }
                    break;
                }
            }
            edgessize--;
        }
        //pleon oles oi theseis ektos apo mia toy Vsubtrees exoun kenes listes
        //valto sth lista kai meta allakse ta from kai to an o to einai pio early apo ton from
        //find wether from or to is earlier
        int i = 0;
        for (i = 0; i < gsize; ++i) {
            if (!Vsubtrees[i].empty()) {
                break;
            }
        }
        list<T> earliest = earliestList(Vsubtrees[i]);
        //to earliest einai mia lista pou pleon exei 
        //olous tous komvous ths Vsubtrees[i] apo ton earliest mexri ton latest

        //gia kathe akmh tou mstlist_unordered kane elegxo gia earliest
        for (typename list<Edge<T>>::iterator it = mstlist_unordered.begin(); it != mstlist_unordered.end(); ++it) {
            Edge<T> b = *it;
            mstlist.push_back(b);
            typename list<T>::iterator it3;
            for (it3 = earliest.begin(); it3 != earliest.end(); ++it3) {
                if (*it3 == b.to || *it3 == b.from)
                    break;
            }
            if (*it3 == b.to) {//an ontws o to einai earlier, kane swap ta from kai to
                T temp = (mstlist.back()).from;
                (mstlist.back()).from = (mstlist.back()).to;
                (mstlist.back()).to = temp;
            }
        }
    }
    return mstlist;
}


//exw arxika ena priority queue me ena edge apo to from pros kathe allo komvo sto grafhma, ola me distance = 999999
//se mia for pairnw kai elegxv gia ola ta paidia tou trexon komvou a th sinthiki djikstra(dld an dist[a] + distance[a->b]<distance[b] tote swap
//antikathistw('h oxi) tis nees times distance (kai allazw an xreiastei kai to pedio from tou trexon relaxed edge)
//kai o priority queue ta kanei sort me to megalytero sthn arxh. Meta kanw pop to teleutaio edge(dld to mikrotero se distance) 
//tou priority queue kai to vazw se mia allh lista 'h vector pou tha apoteleitai apo ta telika edges gia to syntomotero monopati

template <typename T>
list<T> Graph<T>::dijkstra(const T& from, const T& to) {
    //panta to to ths pqueue deixnei ston komvo prorismou(den exw thema gia mh katefthinomeno
    //kai panta to distance einai to oliko varos tou komvou to(dist[to])
    multiset<Edge<T>>pqueue;//apothikeuei sto distance to oliko varos apo ton from ston to komvo tou kahe edge
    //den periexei dld to katharo varos akmhs, alla to dist[to]
    list<Edge<T>> relaxed_edges; //periexei oles tis telikes relaxed akmes
    list<T> dijkstra_list;
    //h thesh i tou pinaka antistoixei sto i-osto stoixeio thw listas geitniashs nodes[]

    if (contains(from)) {
        //initialise set me ena megalo noumero gia to distance

        for (int i = 0; i < gsize; ++i) {
            list<T> n = nodes[i];
            if (!(from == nodes[i].front())) {//vale kathe komvo sto set me dist = INFINITE, ektos apo ton from
                Edge<T> ne = Edge<T>(from, n.front(), 999999);
                pqueue.emplace(ne);
            }
        }
        Edge<T> same = Edge<T>(from, from, 0);
        //pqueue.emplace(same);//vazw ton eauto mou wste na paw sto telos tou queue kai na bgw kateutheian
        //psaxnw kai enhmerwnw ton pinaka me ta nodes pou gnwrizw thn apostash
        //mexri na valw sto relaxed_edges ton komvo to kai kanw break
        //sth xeiroterh mexri na adeiasei to priority queue
        relaxed_edges.push_back(same);
        Edge<T> re = relaxed_edges.back();
        Edge<T> smallest_edge = same;
        int distance = smallest_edge.distance;
        //to distance einai to dist[u] dld h minimum athristikh apostash pou exei  h diadromh apo from ews u
        while (pqueue.size()>0) {
            
            multiset<Edge<T>> childedges = giveEdgeSet(smallest_edge, edges);//dinei oles tis akme pou exei o trexon komvos
            //std::cout<<endl;
            //std::cout<<distance<<" from "<<smallest_edge.to;
            for (typename multiset<Edge<T>>::iterator it = childedges.begin(); it != childedges.end(); ++it) {
                Edge<T> b = *it;
                
                //gia undirected graph
                if (!(isDirectedGraph) && (smallest_edge.to == b.to)) {
                    T temp = b.to;
                    b.to = b.from;
                    b.from = temp;
                }
                //std::cout<<" - "<<b.to;
                //pleon to b exei ws from ton to komvo ths trexousas smallest akmhs
                //kai ws to exei to komvo pou tha elegxei me vash aytou an exei hdh xalarwthei
                //kapoia akmh tou 'h an einai hdh sto pqueue
                //an h akmh den yparxei, elegxo an o komvos to einai hdh relaxed
                //typename list<Edge<T>>::iterator reit;//elegxw panta ton komvo to sto relaxed
                //for (reit = relaxed_edges.begin(); reit != relaxed_edges.end(); ++reit) {
                //    Edge<T> bc = *reit;
                //    if (b.to == bc.to) {
                //        if (smallest_edge.to == b.to && !(smallest_edge.to == smallest_edge.from))//an einai o eautos mou
                //            break;
                //    }
                //    else if (b.from == bc.to) {
                //        if (smallest_edge.to == b.from && !(smallest_edge.to == smallest_edge.from))
                //            break;
                //    }
                //}

                if (!inList(b, relaxed_edges)) { //efoson o komvos to den exei ginei hdh relaxed
                     //search in priority queue if edge already exists              
                    typename multiset<Edge<T>>::iterator it2 = pqueue.begin();//find(b);
                    for (; it2 != pqueue.end(); ++it2) {
                        Edge<T> cure = *it2;
                        // cout<<" curent in pqueue "<<cure.from<<" - "<<cure.to<<endl;
                        // cout<<"b is "<<b.from<<" - "<<b.to<<endl;
                        if (b.to == cure.to){
                            break;
                        }
                        
                    }
                    
                    if (it2 == pqueue.end()) {//tote yphrxe sto relaxed, opote continue kai elegxw gia epomeno paidi
                        continue;
                    }
                    else {
                        Edge<T> edge_to_relax = *it2;
                        int d = b.distance;//edges weight
                        if ((d + distance) < (edge_to_relax.distance)) {//elegxos djikstra gia relaxation
                        //delete old edge from set and add a new with updated distance value
                         typename multiset<Edge<T>>::iterator it3 = pqueue.begin();
                        //  for (; it3 != pqueue.end(); ++it3) {
                        //         Edge<T> cre = *it3;
                        //         cout<<" BEFORE ERASE curent in pqueue "<<cre.from<<" - "<<cre.to<<" "<<cre.distance<<endl;
                        //     }
                            typename multiset<Edge<T>>::iterator somanyiterators = pqueue.begin();
                            for (; somanyiterators != pqueue.end(); somanyiterators++) {
                                Edge<T> bs = *somanyiterators;
                                if (bs.to == edge_to_relax.to)
                                    break;
                            }
                             pqueue.erase(somanyiterators);
                            //  for (it3 = pqueue.begin(); it3 != pqueue.end(); ++it3) {
                            //     Edge<T> cre = *it3;
                            //     cout<<" AFTER ERASE curent in pqueue "<<cre.from<<" - "<<cre.to<<" "<<cre.distance<<endl;
                            // }
                            // Updating distance of v 
                            // cout<<" old unrelaxed from "<<edge_to_relax.from<<" to "<<edge_to_relax.to<<" dist = "<<edge_to_relax.distance<<endl;
                            // cout<<" may be replaced with from "<<b.from<<" to "<<b.to<<" dist = "<<b.distance + distance<<endl;
                            edge_to_relax.distance = d + distance;
                            if (b.from == edge_to_relax.to)
                                edge_to_relax.from = b.to;
                            else
                                edge_to_relax.from = b.from;
                            pqueue.emplace(edge_to_relax);
                             //cout<<" relaxed from "<<edge_to_relax.from<<" to "<<edge_to_relax.to<<" dist = "<<edge_to_relax.distance<<endl;
                        }
                    }
                }
            }
            
            typename multiset<Edge<T>>::iterator it = pqueue.begin();
            smallest_edge = *it;//pop next smallest edge from priority queue
            if(smallest_edge.distance != 999999)
                relaxed_edges.push_back(*it);
            pqueue.erase(it);
            distance = smallest_edge.distance;
            re = relaxed_edges.back();
        }


        //an yparxei monopati apo 1 sto 4
        /*Edge<T> check(from, to, 999999);
        if (inList(check, relaxed_edges))*/
        typename list<Edge<T>>::iterator it;
        for (it = relaxed_edges.begin(); it != relaxed_edges.end(); ++it) {
            Edge<T> b = *it;
            if (b.to == to){
                break;
            }
        }
        //den yparxei monopati apo from se to, epistrefei kenh lista
        if (it == relaxed_edges.end())
            return dijkstra_list;
        //kane reverse search giana dhmiourghthei to syntomo monopati
       
        Edge<T> cure = *it;
        relaxed_edges.pop_back();
        dijkstra_list.push_front(cure.to);
        T next = cure.from;//pairnei ton komvo ton opoio tha paw anadromika pros ta pisw mexri na vrw ton from
        T cur = cure.to;//pairnei ton komvo to

        while (!(next == from)) {
            for (it = relaxed_edges.begin(); it != relaxed_edges.end(); ++it) {
                Edge<T> b = *it;
                if (b.to == next) {
                    cur = b.to;
                    next = b.from;
                    dijkstra_list.push_front(b.to);
                    it = relaxed_edges.begin();
                }
            }
        }
        dijkstra_list.push_front(from);
    }
    return dijkstra_list;
}

//ekteleite V-1 fores, opou V o arithmos komvwn
//pairnei kathe epanalipsi me thn idia seira tis akmes kai elegxei gia xalarwsh
//an meta tis V-1 epanalipseis thelei kai allh xalarwsh, tote exw arnhtiko vroxo
//kai petaei exception
template <typename T>
list<T> Graph<T>::bellman_ford(const T& from, const T& to) {
    int src;
    int des;
    int i;
    list<Edge<T>> bf_edges;
    list<T> bf_list;//telikh lista
    vector<Edge<T>> distances;//pinakas me edges opou ta distances kathe komvou apo ton from, h i-osth thesh einai gia ton i-osto komvo tou nodes[]
   //kathe to komvos einai kathe j-ostos komvos pou exoume sto grafima kai epishs kathe komvos from einai o komvos me ton opoio syndeetai sto monopati
   //arxikopoihsh olwn twn apostasewn apo to from se olous tous allous komvous se 999999
    for (int i = 0; i < gsize; i++){
        Edge<T> vedg(nodes[i].front(),nodes[i].front(),INF);//arxika vazw from kai to ton eauto mou
        if (nodes[i].front() == from) {
            vedg.distance = 0;
        }
        distances.push_back(vedg);
    }

    for (i = 0; i < gsize - 1; i++) {//trexei V-1 fores
        for (typename list<Edge<T>>::iterator it = edges.begin(); it != edges.end(); ++it) {//elegxei kathe edge th synthiki xalarwshs
            //psaxnw to src kai des index pou einai h src-osth kai des-osth thesh twn from kai to komvown ston pinaka distance antistoixa
            Edge<T> b = *it;
            //search for src-th and des-th positions in the neighbor list of from and to nodes correspondingly
            for (src = 0; src < gsize; src++) {
                if (nodes[src].front() == b.from) {
                    break;
                }
            }
            for (des = 0; des < gsize; des++) {
                if (nodes[des].front() == b.to) {
                    break;
                }
            }
            int dstnc = (distances.at(src)).distance + b.distance ;
            if ((distances.at(src)).distance != 999999 && (dstnc < (distances.at(des)).distance)) {
                (distances.at(des)).distance = dstnc;
                //an yparxei hdh akmh pou na phgainei ston to, th vgazw efoson vrhka mia pou einai mikroterou kostous
                for(typename list<Edge<T>>::iterator edgit =  bf_edges.begin(); edgit != bf_edges.end();edgit++){
                    Edge<T> anotherone = *edgit;
                        if(b.to == anotherone.to){
                            (distances.at(des)).from = b.from;
                            bf_edges.erase(edgit);
                            bf_edges.push_back(b);
                            break;
                        }
                    
                }
                if ((distances.at(des)).from == (distances.at(des)).to) {
                    (distances.at(des)).from = b.from;
                    bf_edges.push_back(b);
                }
               
            }
            if(!isDirectedGraph){//einai san na exw 2 fores thn idia akmh, opote elegxw kai gia tis 2 kateuthinseis
                int dstnc = (distances.at(src)).distance + b.distance ;
                if ((distances.at(des)).distance != 999999 && (dstnc < (distances.at(src)).distance)) {
                (distances.at(src)).distance = dstnc;
                //cout << "new distance : "<<(distances.at(src))<<endl;
                for(typename list<Edge<T>>::iterator edgit =  bf_edges.begin(); edgit != bf_edges.end();edgit++){
                    Edge<T> anotherone = *edgit;
                        if(b.from == anotherone.to){
                            (distances.at(src)).from = b.to;
                            bf_edges.erase(edgit);
                            T temp = b.to;
                            b.to = b.from;
                            b.from = temp;
                            bf_edges.push_back(b);
                            break;
                        }
                }
                if ((distances.at(src)).from == (distances.at(src)).to) {
                    (distances.at(src)).from = b.to;
                    T temp = b.to;
                    b.to = b.from;
                    b.from = temp;
                    bf_edges.push_back(b);
                }
                }
            }
        }
    }

    //teleutaios elegxos
    for (typename list<Edge<T>>::iterator it = edges.begin(); it != edges.end(); ++it) {//elegxei kathe edge th synthiki xalarwshs
            //psaxnw to src kai des index pou einai h src-osth kai des-osth thesh twn from kai to komvown ston pinaka distance antistoixa
            //list<T> n = nodes[src];
            //list<T> nd = nodes[des];
        Edge<T> b = *it;
        //search for src-th and des-th positions in the neighbor list of from and to nodes correspondingly
        for (src = 0; src < gsize; src++) {
            if (nodes[src].front() == b.from) {
                break;
            }
        }
        for (des = 0; des < gsize; des++) {
            if (nodes[des].front() == b.to) {
                break;
            }
        }
        int dstnc = (distances.at(src)).distance + b.distance ;
        if (distances[src].distance != 999999 && (dstnc< distances[des].distance)) {
            //throw exception
            NegativeGraphCycle ex;
            throw ex;
        }
        if(!isDirectedGraph){
            if (distances[des].distance != 999999 && (dstnc< distances[src].distance)) {
                NegativeGraphCycle ex;
                throw ex;
            }
        }
    }
    
    T cur;
    //diatrexw tis akmes tou bf_edges kai vriskw thn telikh lista
    typename list<Edge<T>>::iterator it = bf_edges.begin();
    Edge<T> b = *it;
    for (; it != bf_edges.end(); ++it) {
        b = *it;
        if (b.to == to) {
            cur = b.to;
            break;
        }
    }
    if (it == bf_edges.end()) {//dhladh an den yparxei monopati na pao apo from sto to
        list<T> emptyl;//gyrnaw mia adeia lista
        return emptyl;
    }

    bf_list.push_back(cur);
    T next = b.from;
    while (!(next == from)) {
        for (it = bf_edges.begin(); it != bf_edges.end(); ++it) {
            Edge<T>b = *it;
            if (b.to == next) {
                cur = b.to;
                next = b.from;
                bf_edges.remove(b);
                bf_list.push_back(cur);
                break;
            }
        }
    }
    bf_list.push_back(from);
    list<T> final;
    for (typename list<T>::const_reverse_iterator rit=bf_list.crbegin(); rit!=bf_list.crend(); rit++) {
        final.push_back(*rit);
    }
    // delete[] distances;
    return final;
}


#endif
