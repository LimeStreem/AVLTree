package AVLTree;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Stack;

public class BinarySearchTree<T> implements ISearchTree<T>
{
    private Node<T> root;

    private Comparator<T> comparator;

    public BinarySearchTree(Comparator<T> comparator)
    {
        this.comparator = comparator;
    }

    @Override
    public Node<T> getRoot()
    {
        return root;
    }

    @Override
    public Node<T> searchNode(T key)
    {
        if(isEmptyTree())return null;
        return searchNode(key,getRoot());
    }

    private Node<T> searchNode(T key, Node<T> node)
    {
        if(node==null)return null;
        if(node.equals(key))
        {
            return  node;
        }else if(node.isSmall(key))
        {
            return searchNode(key, node.getRightNode());
        }else
        {
            return searchNode(key,node.getLeftNode());
        }
    }

    @Override
    public boolean insert(T key)
    {
        Node<T> node=new Node<T>(key,comparator);
        return insert(node);
    }

    @Override
    public boolean delete(T key)
    {
        Node<T> node=searchNode(key);
        if(node==null)return false;
        if(node.isLeafNode())
        {//葉の場合はそのまま削除できる
            if(node.getParentNode()==null)
            {//ルート要素のとき
                root=null;
                return true;
            }
            if(node.isRightNode())
            {
                node.getParentNode().setRightNode(null);
            }else {
                node.getParentNode().setLeftNode(null);
            }
            return true;
        }else if(node.getRightNode()==null&&node.getLeftNode()!=null)
        {
            if(node.getParentNode()==null)
            {
                root=node.getLeftNode();
                return true;
            }
            if(node.isRightNode())
            {
                node.getParentNode().setRightNode(node.getLeftNode());
            }else
            {
                node.getParentNode().setLeftNode(node.getLeftNode());
            }
        }else if(node.getRightNode()!=null&node.getLeftNode()==null)
        {
            if(node.getParentNode()==null)
            {
                root=node.getRightNode();
                return true;
            }
            if(node.isRightNode())
            {
                node.getParentNode().setRightNode(node.getRightNode());
            }else
            {
                node.getParentNode().setLeftNode(node.getRightNode());
            }
        }else
        {
            //両方にノードがあるとき、左部分木の右端をルート要素に持ってくるものとする
            Node<T> currentNode=node.getLeftNode();
            while(true)
            {
                if(currentNode.getRightNode()==null)break;
                currentNode=currentNode.getRightNode();
            }
            //このノードの親ノードからの参照を切っておく
            if(currentNode.isRightNode())
            {
                currentNode.getParentNode().setRightNode(null);
            }else
            {
                currentNode.getParentNode().setLeftNode(null);
            }
            if(node.getParentNode()==null)
            {
                root=currentNode;
            }else if(node.isRightNode())
            {
                node.getParentNode().setRightNode(currentNode);
            }else
            {
                node.getParentNode().setLeftNode(currentNode);
            }
            currentNode.setLeftNode(node.getLeftNode());
            currentNode.setRightNode(node.getRightNode());
            return true;
        }
        return false;
    }

    private void  insertBreadthFirst(Node<T> node,Node<T> basis)
    {

        Stack<Node<T>> currentStack=new Stack<Node<T>>();
       if(node.getRightNode()!=null){currentStack.add(node.getRightNode());
       node.getRightNode().setBalance(0);}
        if(node.getLeftNode()!=null){currentStack.add(node.getLeftNode());
        node.getLeftNode().setBalance(0);}
        node.setRightNode(null);
        node.setLeftNode(null);
        while(currentStack.size()!=0)
        {
            Stack<Node<T>> nextStack=new Stack<Node<T>>();
            for (Node<T> tNode : currentStack)
            {
                if(tNode==null){
                    continue;
                }

                tNode.setBalance(0);
               if(tNode.getLeftNode()!=null){
                nextStack.add(tNode.getRightNode());
                tNode.setRightNode(null);
               }
                if(tNode.getLeftNode()!=null){
                nextStack.add(tNode.getLeftNode());
                tNode.setLeftNode(null);
                }
                insert(tNode,basis,new Stack<PathItem<T>>());
            }
            currentStack=nextStack;
        }
    }

    private boolean insert(Node<T> basis)
    {
        if(isEmptyTree())
        {
            root=basis;
            return true;
        }else
        {
            Stack<PathItem<T>>path = new Stack<PathItem<T>>();
            boolean ret=insert(basis,getRoot(),path);
            System.out.println("修正前\n"+toString());
            int count=0;
            while(true)
            {
                count++;
                boolean isChanged=false;
                Stack<Node<T>> currentStack=new Stack<Node<T>>();
                currentStack.add(root);
                while (currentStack.size()!=0)
                {
                    Stack<Node<T>> nextStack=new Stack<Node<T>>();
                    for (Node<T> tNode : currentStack)
                    {
                        Node<T> pathNode =tNode;
                        if(pathNode.getBalance()>1)
                        {
                            Node<T> altNode = pathNode.getLeftNode().getMinimumNode();
                            altNode.removeFromParent();
                            altNode.setBalance(0);
                            pathNode.setBalance(0);
                            if(pathNode.isRootNode())
                            {
                                root=altNode;
                            }else if(pathNode.isRightNode())
                            {
                                pathNode.getParentNode().setRightNode(altNode);
                            }else
                            {
                                pathNode.getParentNode().setLeftNode(altNode);
                            }
                            insertBreadthFirst(pathNode,altNode);
                            pathNode.setRightNode(null);
                            pathNode.setLeftNode(null);
                            Stack<PathItem<T>> dummy=new Stack<PathItem<T>>();
                            if(altNode.getRightNode()!=null)
                                insert(pathNode,altNode.getRightNode(),dummy);
                            else{
                                altNode.setBalance(altNode.getBalance()-1);
                                altNode.setRightNode(pathNode);
                            }
                            if(tNode.getRightNode()!=null)nextStack.add(tNode.getRightNode());
                            if(tNode.getLeftNode()!=null)nextStack.add(tNode.getLeftNode());
                            isChanged=true;
                            break;
                        }else if(pathNode.getBalance()<-1)
                        {
                            Node<T> altNode = pathNode.getRightNode().getMaximumNode();
                            altNode.removeFromParent();
                            altNode.setBalance(0);
                            pathNode.setBalance(0);
                            if(pathNode.isRootNode())
                            {
                                root=altNode;
                            }else if(pathNode.isRightNode())
                            {
                                pathNode.getParentNode().setRightNode(altNode);
                            }else
                            {
                                pathNode.getParentNode().setLeftNode(altNode);
                            }
                            insertBreadthFirst(pathNode,altNode);
                            pathNode.setRightNode(null);
                            pathNode.setLeftNode(null);
                            Stack<PathItem<T>> dummy=new Stack<PathItem<T>>();
                            if(altNode.getLeftNode()!=null)
                                insert(pathNode,altNode.getLeftNode(),dummy);
                            else
                            {
                                altNode.setLeftNode(pathNode);
                                altNode.setBalance(altNode.getBalance()+1);
                            }
                            if(tNode.getRightNode()!=null)nextStack.add(tNode.getRightNode());
                            if(tNode.getLeftNode()!=null)nextStack.add(tNode.getLeftNode());
                            isChanged=true;
                            break;
                        }
                        if(tNode.getRightNode()!=null)nextStack.add(tNode.getRightNode());
                        if(tNode.getLeftNode()!=null)nextStack.add(tNode.getLeftNode());
                    }
                    if(isChanged){
                        System.out.printf("Phase%d\n%s\n", count, toString());
                        break  ;
                    }


                    currentStack=nextStack;
                }

                if(!isChanged)break;
            }
            System.out.println("修正後\n"+toString());
            //getString();
            return ret;
        }
    }


    private boolean insert(Node<T> newNode,Node<T> basis,Stack<PathItem<T>> root)
    {
        if(basis.equals(newNode.getValue()))return false;
        if(basis.isSmall(newNode.getValue()))
        {
            if(basis.getRightNode()==null)
            {
                basis.setRightNode(newNode);
               basis.setBalance(basis.getBalance()-1);
                return true;
            }
            else
            {
                root.add(new PathItem<T>(basis,RootTyoe.RIGHT));
                boolean ret=insert(newNode, basis.getRightNode(),root);
                if(ret)basis.setBalance(basis.getBalance()-1);
                return ret;
            }
        }else {
            if(basis.getLeftNode()==null)
            {
                basis.setLeftNode(newNode);
               basis.setBalance(basis.getBalance()+1);
                return true;
            }
            else
            {
                root.add(new PathItem<T>(basis,RootTyoe.LEFT));
                boolean ret=insert(newNode, basis.getLeftNode(),root);
               if(ret)basis.setBalance(basis.getBalance()+1);
                return ret;
            }
        }
    }

    /**
     * 右回転
     * @param node 回転中心
     * @return 回転後の回転中心
     */
    public Node<T> rotateRight(Node<T> node)
    {
        Node<T> leftNode=node.getLeftNode();
        node.setLeftNode(leftNode.getRightNode());
        leftNode.setRightNode(node);
        return leftNode;
    }

    /**
     * 左回転
     * @param node 回転中心
     * @return 回転後の中心
     */
    public Node<T> rotateLeft(Node<T> node)
    {
        Node<T> rightNode=node.getRightNode();
        node.setRightNode(rightNode.getLeftNode());
        rightNode.setLeftNode(node);
        return rightNode;
    }

    @Override
    public boolean isEmptyTree()
    {
        return getRoot()==null;
    }

    @Override
    public String toString()
    {
        if(isEmptyTree())return "(空のツリー)";
        return getString();
    }

    private String getString()
    {
        StringBuilder builder=new StringBuilder();
        builder.append("###################################\n");
        getString(root, 0, builder);
       builder.append("###################################\n");
        return builder.toString();
    }

    private void getString(Node<T> node, int n, StringBuilder builder)
    {
        if(node!=null)
        {

            getString(node.getLeftNode(), n + 1, builder);
            for (int i=0;i<n;i++)
            {
                builder.append("      ");
            }
            builder.append(String.format("%s(%d)\n",node.getValue().toString(),node.getBalance()));
            getString(node.getRightNode(), n + 1, builder);
        }
    }



    private class PathItem<T>
    {
        private PathItem(Node<T> node, RootTyoe type)
        {
            this.node = node;
            this.type = type;
        }

        private Node<T> node;

        private RootTyoe type;

        public Node<T> getNode()
        {
            return node;
        }

        public void setNode(Node<T> node)
        {
            this.node = node;
        }

        public RootTyoe getType()
        {
            return type;
        }

        public void setType(RootTyoe type)
        {
            this.type = type;
        }
    }

    private enum RootTyoe
    {
        LEFT,RIGHT
    }
}
