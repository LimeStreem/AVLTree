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
