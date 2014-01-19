import java.util.Comparator;

public class Node<T>
{
    private T value=null;

    private Node<T> parentNode=null;

    private Node<T> leftNode=null;

    private Node<T> rightNode=null;

    private Comparator<T> nodeComparator=null;

    private int balance=0;

    public Node(T value,Comparator<T> comparator)
    {
        this.nodeComparator=comparator;
        this.value=value;
    }

    public void setRightNode(Node<T> rightNode)
    {
        this.rightNode = rightNode;
        if(rightNode!=null)rightNode.setParentNode(this);
    }

    public void setValue(T value)
    {
        this.value = value;
    }

    public void setParentNode(Node<T> parentNode)
    {
        this.parentNode = parentNode;
    }

    public void setLeftNode(Node<T> leftNode)
    {
        this.leftNode = leftNode;
        if(leftNode!=null)leftNode.setParentNode(this);
    }

    public T getValue()
    {
        return value;
    }

    public Node<T> getParentNode()
    {
        return parentNode;
    }

    public Node<T> getLeftNode()
    {
        return leftNode;
    }

    public Node<T> getRightNode()
    {
        return rightNode;
    }

    public boolean isRootNode()
    {
        return getParentNode()==null;
    }

    public boolean isLeftNode()
    {
        return getParentNode()!=null&&getParentNode().getLeftNode()==this;
    }

    public boolean isRightNode()
    {
        return getParentNode()!=null&&getParentNode().getRightNode()==this;
    }

    public boolean isLeafNode()
    {
        return getLeftNode()==null&&getRightNode()==null;
    }

    @Override
    public boolean equals(Object obj)
    {
        return getValue().equals(obj);//キーが等しいとき同一と判定する
    }

    public boolean isSmall(T key)
    {
        return nodeComparator.compare(getValue(),key)<0;
    }

    public int getBalance()
    {
        return balance;
    }

    public void setBalance(int balance)
    {
        this.balance = balance;
    }
}
