
public interface ISearchTree<T>
{
    /**
     * ルートのノードを返す
     * @return ツリーのルート要素
     */
    Node<T> getRoot();

    /**
     * 指定したキーを保持するノードを捜す
     * @param key 検索するキー
     * @return 結果
     */
    Node<T> searchNode(T key);

    /**
     * このツリーが要素を持つかどうか返します
     * @return 空かどうか
     */
    boolean isEmptyTree();

    /**
     * 指定したキーを追加します
     * @param key
     */
    boolean insert(T key);

    /**
     * 指定したキーを削除します
     * @param key
     * @return
     */
    boolean delete(T key);
}
