package zq.basicclass.demo;

import java.util.Arrays;
import java.util.Comparator;

public class ComparableDemo {
    public static void main(String[] args){
        BinaryTreeComparable bt = new BinaryTreeComparable();
        bt.add(8);
        bt.add(3);
        bt.add(3);
        bt.add(10);
        bt.add(19);
        bt.add(1);
        bt.add(5);
        bt.add(5);
        bt.print();

        Tired2[] t2 = null;
        Arrays.sort(t2, new TiredComparator()); //按指定的规则排序

//        Tired[] ts = {new Tired(666), new Tired(888), new Tired(100)};
//        /**
//         * Arrays.sort
//         * 二叉树排序，中序遍历
//         */
//        Arrays.sort(ts);
//        for (int i = 0; i < ts.length; i++) {
//            System.out.println(ts[i]);
//        }

    }
}


class BinaryTreeComparable{
    private Node root;


    class Node{
        private Comparable data;
        private Node left;
        private Node right;

        public Node(Comparable data){
            this.data = data;
        }
        //需要中序遍历
        public void printNode(){
            if(left != null) left.printNode();
            System.out.print(data + "\t");
            if(right != null) right.printNode();
        }

        public void addNode(Node node){
           if(node.data.compareTo(this.data) < 0){
               if(this.left == null){
                   this.left = node;
               }else {
                   this.left.addNode(node);
               }
           }else {
               if(this.right == null){
                   this.right = node;
               }else {
                   this.right.addNode(node);
               }
           }
        }
    }

    public void add(Comparable data){
        Node node = new Node(data);
        if(this.root == null){
            this.root = node;
        }else {
            this.root.addNode(node);
        }
    }

    public void print(){
        this.root.printNode();
    }
}

class TiredComparator implements Comparator<Tired2>{

    @Override
    public int compare(Tired2 o1, Tired2 o2) {
        return 0;
    }
}

class Tired2{

}

class Tired implements Comparable<Tired> {
    private int level;
    public Tired(int level){
        this.level = level;
    }

    @Override
    public int compareTo(Tired o) {
        if(this.level < o.level){
            return -1;
        }else if(this.level > o.level){
            return 1;
        }else {
            return 0;
        }
    }

    @Override
    public String toString(){
        return  level + " 级累";
    }
}