#ifndef _GRAPH_UI_
#define _GRAPH_UI_
#include <string>
#include <list>
#include <iostream>
#include <queue> 
#include <set>
#include <iterator> 
#include <functional>
using namespace std;

template <typename T>
int graphUI() {

    string option, line;
    int distance;
    bool digraph = false;

    cin >> option;
    if (!option.compare("digraph"))
        digraph = true;
    Graph<T> g(digraph);

    while (true) {

        std::stringstream stream;
        cin >> option;

        //epeidh to compare pairnei string,apo olh h grammh inpout pairnei mexri to 1o whitespacewhitespace
        if (!option.compare("av")) {
            getline(std::cin, line);
           stream << line;
            T vtx(stream);
            if (g.addVtx(vtx))
                cout << "av " << vtx << " OK\n";
            else
                cout << "av " << vtx << " NOK\n";
        }
        else if (!option.compare("rv")) {
            getline(std::cin, line);
            stream << line;
            T vtx(stream);
            if (g.rmvVtx(vtx))
                cout << "rv " << vtx << " OK\n";
            else
                cout << "rv " << vtx << " NOK\n";
        }
        else if (!option.compare("ae")) {
            getline(std::cin, line);
            stream << line;
            T from(stream);
            T to(stream);
            //std::string::size_type sz;   // alias of size_t
            //int distance = std::stoi(line, &sz);
            stream >> distance;
            if (g.addEdg(from, to, distance))
                cout << "ae " << from << " " << to << " OK\n";
            else
                cout << "ae " << from << " " << to << " NOK\n";
        }
        else if (!option.compare("re")) {
            getline(std::cin, line);
            stream << line;
            T from(stream);
            T to(stream);

            if (g.rmvEdg(from, to))
                cout << "re " << from << " " << to << " OK\n";
            else
                cout << "re " << from << " " << to << " NOK\n";
        }

        else if (!option.compare("dot")) {
            getline(std::cin, line);
            stream << line;
            const char* filename = line.c_str();
            if (g.print2DotFile(filename))
                cout << "dot " << filename << " OK\n";
            else
                cout << "dot " << filename << " NOK\n";
        }
        else if (!option.compare("bfs")) {
            getline(std::cin, line);
            stream << line;
            T node(stream);
            cout << "\n----- BFS Traversal -----\n";
            list<T> bf = g.bfs(node);
            if (!bf.empty()) {
                typename list<T>::iterator it = bf.begin();
                cout << *it;
                ++it;
                for (; it != bf.end(); ++it)
                    cout << " -> " << *it;
            }
            cout << "\n-------------------------\n";
        }
        else if (!option.compare("dfs")) {
            getline(std::cin, line);
            stream << line;
            T node(stream);
            cout << "\n----- DFS Traversal -----\n";
            list<T> df = g.dfs(node);
            if (!df.empty()) {
                typename list<T>::iterator it = df.begin();
                cout << *it;
                ++it;
                for (; it != df.end(); ++it)
                    cout << " -> " << *it;
            }
            cout << "\n-------------------------\n";
        }
        else if (!option.compare("dijkstra")) {
            getline(std::cin, line);
            stream << line;
            T from(stream);
            T to(stream);
            cout << "Dijkstra ("<<from<<" - "<<to<<"): ";
            std::list<T> dij = g.dijkstra(from, to);
            if (!dij.empty()) {
                typename list<T>::iterator it = dij.begin();
                cout << *it;
                ++it;
                for (; it != dij.end(); ++it)
                    cout << ", " << *it;
                
            }
            cout << endl;
        }
        else if (!option.compare("bellman-ford")) {
        getline(std::cin, line);
        stream << line;
        T from(stream);
        T to(stream);
            cout << "Bellman-Ford ("<<from<<" - "<<to<<"): ";
            try {
                std::list<T> bf = g.bellman_ford(from, to);
                if (!bf.empty()) {
                    typename std::list<T>::iterator it = bf.begin();
                    cout << *it;
                    ++it;
                    for (; it != bf.end(); ++it)
                        cout << ", " << *it;
                   
                }
                 cout << endl;
            }
            catch (NegativeGraphCycle& nc)
            {
                cout << nc.what() << endl;
            }
        }

        else if (!option.compare("mst")) {

            cout << "\n--- Min Spanning Tree ---\n";
            list<Edge<T>>m = g.mst();
            int sum = 0;
            typename list<Edge<T>>::iterator it = m.begin();
            for (; it != m.end(); ++it) {
                Edge<T> b = *it;
                cout << b.from << " -- " << b.to << " (" << b.distance<< ")" << endl;
                sum += b.distance;
            }
            cout << "MST Cost: " << sum << endl;
        }

        else if (!option.compare("q")) {
            //cout << "bye bye...\n";
            return 0;
        }

        else if (!option.compare("#")) {
            string line;
            getline(cin, line);
            //cout << "Skipping line: " << line << endl;
        }
        else {
            cout << "INPUT ERROR\n";
            return -1;
        }
    }
    return -1;
}

#endif
