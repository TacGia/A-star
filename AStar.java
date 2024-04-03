/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.astar;

/**
 *
 * @author ADMIN
 */
// Đây là mã nguồn Java sau khi chuyển đổi
// Tôi đã thêm chú thích tiếng Việt vào mã

import java.io.*;
import java.util.*;

class Node {
    int index;  // số thứ tự
    int g;      // khoảng cách từ đỉnh ban đầu đến đỉnh hiện tại
    int f;      // f = h + g
    int h;      // đường đi ngắn nhất
    int color;  // đánh dấu đỉnh đi qua
    int parent; // đỉnh cha

    // Constructor để khởi tạo các giá trị mặc định cho Node
    Node(int index, int g, int h, int color, int parent) {
        this.index = index;
        this.g = g;
        this.h = h;
        this.f = g + h;
        this.color = color;
        this.parent = parent;
    }
}

public class AStar {
    int[][] a = new int[100][100];
    Node[] p = new Node[100];
    Node[] Open = new Node[100];
    Node[] Close = new Node[100];
    
    void ReadInputFile1(int[] b) {
        try {
        File file = new File("D:\\Input1.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            int n = Integer.parseInt(line);
            for (int i = 0; i < n; i++) {
                line = br.readLine();
                b[i] = Integer.parseInt(line);
            }
        }
    } catch (IOException e) {
        System.out.println("Không thể mở được file!");
    }
//        try {
//           File file = new File("D:\\Input1.txt");
//            try (Scanner sc = new Scanner(file)) {
//               int n = sc.nextInt();
//                for (int i = 0; i < n; i++) {
//                    b[i] = sc.nextInt();
//                }
//            }
//        } catch (FileNotFoundException e) {
//            System.out.println("Không thể mở được file!");
//        }
    }

    void ReadInputFile2(int[][] a, int start, int finish) {
        try {
            File file = new File("D:\\Input2.txt");
            try (Scanner sc = new Scanner(file)) {
                int n = sc.nextInt();
                start = sc.nextInt();
                finish = sc.nextInt();
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        a[i][j] = sc.nextInt();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Không thể mở được file!");
        }
    }
    
    int Count(int n, Node[] Open) {
        int count = 0;
        for (int i = 0; i < n; i++) {
            if (Open[i].color == 1)
                count++;
        }
        return count;
    }

    int Find(int n, Node[] Open) {
        for (int i = 0; i < n; i++)
            if (Open[i].color == 1)
                return i;
        return -1;
    }

    int FindMin(int n, Node[] Open) {
        int minIndex = Find(n, Open);
        int min = Open[minIndex].f;
        for (int i = 0; i < n; i++) {
            if (Open[i].f < min && Open[i].color == 1) {
                minIndex = i;
                min = Open[i].f;
            }
        }
        return minIndex;
    }

    void Init(int n, int[] b) {
        for (int i = 0; i < n; i++) {
            p[i] = new Node(i, b[i], 0, 0, 0);
            p[i].f = p[i].g;
        }
    }

    int FindPoint(int n, Node[] q, int o) {
        for (int i = 0; i < n; i++)
            if (q[i].index == o)
                return i;
        return -1;
    }

    void AStar(int[][] a, int n, int start, int finish, int[] b) {
        int l = 0;
        
        Open[l] = p[start];
        Open[l].color = 1;
        Open[l].f = Open[l].h + Open[l].g;
        l++;
        int w = 0;

        while (Count(l, Open) != 0) // kiểm tra xem tập Open có còn phần tử nào không
        {
            int k = FindMin(n, Open); // tìm vị trí nhỏ nhất trong Open
            Open[k].color = 2; // cho điểm tìm được vào Close
            Close[w] = Open[k];
            Close[w].color = 2;
            w++;
            p[FindPoint(n, p, Open[k].index)].color = 2;
            if (FindPoint(n, p, Open[k].index) == finish) {
                System.out.println("Đường đi qua là");
                System.out.print(finish + "\t");
                int y = FindPoint(w, Close, finish);
                int u = Close[y].parent;
                while (u != start) {
                    y = FindPoint(w, Close, u);
                    u = Close[y].parent;
                    System.out.print(u + "\t");
                }
                break;
            } else {
                for (int i = 0; i < n; i++) {
                    if (a[FindPoint(n, p, Open[k].index)][i] != 0 && p[i].color == 0) // nếu chưa có trong Open và Close
                    {
                        Open[l] = p[i];
                        Open[l].color = 1;
                        Open[l].h = a[FindPoint(n, p, Open[k].index)][i] + Open[k].h; // tính h khoảng cách ngắn nhất từ đỉnh bắt đầu đến đỉnh hiện tại 
                        Open[l].f = Open[l].g + Open[l].h;
                        Open[l].parent = FindPoint(n, p, Open[k].index);
                        p[i].color = 1;
                        l++;
                    }
                    if (a[FindPoint(n, p, Open[k].index)][i] != 0 && p[i].color == 1) // nếu đỉnh đã có trong Open
                    {
                        int h = FindPoint(l, Open, p[i].index);
                        Node tempNode = p[i];
                        tempNode.color = 1;
                        tempNode.h = a[FindPoint(n, p, Open[k].index)][i] + Open[k].h;
                        tempNode.parent = k;
                        tempNode.f = tempNode.h + tempNode.g;
                        if (tempNode.f < Open[h].f) // nếu f trạng thái hiện tại bé hơn trạng thái cập nhật trước đó
                            Open[h] = tempNode;
                    }
                    if (a[FindPoint(n, p, Open[k].index)][i] != 0 && p[i].color == 2) // nếu đỉnh đã có trong Close
                    {
                        int h = FindPoint(l, Close, p[i].index);
                        Node tempNode = p[i];
                        tempNode.color = 1;
                        tempNode.h = a[FindPoint(n, p, Open[k].index)][i] + Open[k].h;
                        tempNode.parent = k;
                        tempNode.f = tempNode.h + tempNode.g;
                        if (tempNode.f < Close[h].f) // nếu f trạng thái hiện tại bé hơn trạng thái trước đó
                        {
                            Open[l] = tempNode; // thêm vào Open
                            Close[h].color = 1; // đánh dấu đỉnh đó thuộc Open
                            l++;
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        int n = 0;
        int start = 0;
        int finish = 0;
        int[] b = new int[100];
        

        AStar astar = new AStar();
        
        astar.ReadInputFile1(b);
          for (int i = 0; i < n; i++) {
                    System.out.println(b[i]);
                }
//        astar.ReadInputFile2(astar.a, start, finish);
//        astar.Init(n, b);
//        
//        System.out.println("Đỉnh bắt đầu");
//        System.out.println(start);
//        System.out.println("Đỉnh kết thúc");
//        System.out.println(finish);
//
//        astar.AStar(astar.a, n, start, finish, b);
    }
}


