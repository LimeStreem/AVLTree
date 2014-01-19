import java.util.Comparator;
import java.util.Stack;

/**
 Copyright (C) 2014 LimeStreem(K.I)
 https://github.com/LimeStreem/AVLTree
 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

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

    private boolean insert(Node<T> node)
    {
        if(isEmptyTree())
        {
            root=node;
            return true;
        }else
        {
            Stack<RootItem<T>>root = new Stack<RootItem<T>>();
            boolean ret=insert(node,getRoot(),root);
            //getString();
            return ret;
        }
    }


    private boolean insert(Node<T> newNode,Node<T> basis,Stack<RootItem<T>> root)
    {
        if(basis.equals(newNode.getValue()))return false;
        if(basis.isSmall(newNode.getValue()))
        {
            if(basis.getRightNode()==null)
            {
                basis.setRightNode(newNode);
               //basis.setBalance(basis.getBalance()-1);
                return true;
            }
            else
            {
                root.add(new RootItem<T>(basis,RootTyoe.RIGHT));
                boolean ret=insert(newNode, basis.getRightNode(),root);
                //if(ret)basis.setBalance(basis.getBalance()-1);
                return ret;
            }
        }else {
            if(basis.getLeftNode()==null)
            {
                basis.setLeftNode(newNode);
               // basis.setBalance(basis.getBalance()+1);
                return true;
            }
            else
            {
                root.add(new RootItem<T>(basis,RootTyoe.LEFT));
                boolean ret=insert(newNode, basis.getLeftNode(),root);
               // if(ret)basis.setBalance(basis.getBalance()+1);
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

    private class RootItem<T>
    {
        private RootItem(Node<T> node, RootTyoe type)
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
