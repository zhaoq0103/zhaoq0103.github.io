package cn.zq.basicclass;


//这个递归方法还挺不错的
public class Link {
    private Node linkNode;

    public  void addNode(String node){
        Node n = new Node(node);
        if(this.linkNode == null){
            this.linkNode = n;
        }else {
            this.linkNode.add(n);
        }
    }

    public void deleteNode(String node){
        if(this.contains(node)){
            if(this.linkNode.data.equals(node)){
                this.linkNode = this.linkNode.next;
            }else {
                this.linkNode.next.delete(this.linkNode, node);
            }
        }
    }

    public boolean contains(String node){
        return  this.linkNode.search(node);
    }

    public void printNode(){
        if(this.linkNode != null){
            this.linkNode.print();
        }
    }

    class Node{
        private String data;
        private Node next;

        public Node(String data){
            this.data = data;
        }

        public void add(Node node){
            if(this.next == null){
                this.next = node;
            }else {
                this.next.add(node);
            }
        }

        public void delete(Node preNode, String node){//preNode
            if(this.data.equals(node)){
               preNode.next = this.next;
            }else{
                if(this.next != null){
                    this.next.delete(this,node);
                }
            }
        }

        public boolean search(String node){
            if(node.equals(this.data)){
                return true;
            }else {
                if(this.next != null){
                    return this.next.search(node);
                }
            }
            return false;
        }

        public void print(){
            System.out.print(this.data + "\t");
            if(this.next != null){
                this.next.print();
            }
        }
    }

   public  static void main(String[] args){
        Link link = new Link();
        link.addNode("A");
       link.addNode("B");
       link.addNode("C");
       link.addNode("D");
       link.addNode("E");
       link.printNode();

       System.out.println();
       link.deleteNode("A");
       link.deleteNode("E");
       link.printNode();

       boolean res = link.contains("C");
       System.out.println("res:" + res);
   }
}
