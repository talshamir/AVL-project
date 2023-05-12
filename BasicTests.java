import sun.java2d.pipe.AAShapePipe;
import sun.reflect.generics.tree.Tree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

public class BasicTests {
    static int randomOperation;
    public static void main(String[] args) {
//      AVLTree t1 = new AVLTree();
//      AVLTree t2 = new AVLTree();
//      t1.insert(45,"45");
//      t2.insert(29,"29");
//      t2.insert(24,"24");
//      TreePrinter.print(t1.getRoot());
//      TreePrinter.print(t2.getRoot());
//      AVLTree.IAVLNode x = new AVLTree.AVLNode(31,"31");
//      t1.join(x,t2);
//      TreePrinter.print(t1.getRoot());
        boolean flag = true;
        AVLTree t1 = new AVLTree();
        AVLTree t2 = new AVLTree();
        AVLTree.IAVLNode newNode = new AVLTree.AVLNode();
        int[] arrayOfkeys = createRandomTree(t1, 100, 0, 999);
        while (flag) {
            if (!checkSplit(200)) {
                flag = false;
                break;
            }
            if (!checkJoin()) {
                flag = false;
                break;
            }
            if (!checkInsertsAndDeletes(t1)) {
                flag = false;
                break;
            }

            for (int j = 0; j < 9999; j++) {
                randomOperation = getRandomNumber(1, 3);
                if (randomOperation == 1) {
                    int random1 = getRandomNumber(1, 999);
                    t1.insert(random1, random1 + "");
                    if (!CheckTree(t1, random1, "Insert", arrayOfkeys)) {
                        flag = false;
                        break;
                    }
                    arrayOfkeys = t1.keysToArray();
                }
                if (randomOperation == 2) {
                    int random1 = getRandomNumber(1, 999);
                    t1.delete(random1);
                    if (!CheckTree(t1, random1, "Delete", arrayOfkeys)) {
                        flag = false;
                        break;
                    }
                    arrayOfkeys = t1.keysToArray();
                }
                if (randomOperation == 3) {
                    int random1 = getRandomNumber(1, 999);
                    AVLTree[] arrayOfTrees = t1.split(random1);
                    t1 = arrayOfTrees[0];
                    t2 = arrayOfTrees[1];
                    if (!CheckTree(t1, random1, "", t1.keysToArray())) {
                        flag = false;
                        break;
                    }
                    if (!CheckTree(t2, random1, "", t2.keysToArray())) {
                        flag = false;
                        break;
                    }
                    arrayOfkeys = t1.keysToArray();
                }
                if (randomOperation == 4) {
                    int random1 = getRandomNumber(1, 999);
                    newNode = new AVLTree.AVLNode(random1, random1 + "");
                    t1.join(newNode, t2);
                    if (!CheckTree(t1, random1, "Join", arrayOfkeys)) {
                        flag = false;
                        break;
                    }
                    arrayOfkeys = t1.keysToArray();
                }

            }
        }
    }
        //AVLTree.IAVLNode newNode = new AVLTree.AVLNode(11, "11");

    public static  boolean checkSplit(int maxtreeSize){
        AVLTree[] arrayOfTrees = new AVLTree[2];
        randomOperation = 3;
        for (int i = 0; i < 50000; i++) {
            int random1 = getRandomNumber(1, maxtreeSize);
            AVLTree t1 = new AVLTree();
            int[] randomTree1Keys = createRandomTree(t1, random1, 0, 100);
            int random2 = getRandomNumber(0, randomTree1Keys.length);
            arrayOfTrees =  t1.split(randomTree1Keys[random2]);
            if (!CheckTree(arrayOfTrees[0], i,"Split",randomTree1Keys)) {
                System.out.println("t1 is wrong");
                return false;
            }
            if (!CheckTree(arrayOfTrees[1], i,"split",randomTree1Keys)) {
                System.out.println("t2 is wrong");
                return false;
            }
            System.out.println("---------------------------------------------------");
        }
        return true;
    }
    public static boolean checkminNmaxNodes(AVLTree t){
        String[] info = t.infoToArray();
        if (t.empty()){
            if (t.max() == null && t.min() == null){
                return true;
            }
            return false;
        }
        if (!t.max().equals(info[info.length-1])){
            System.out.println("false max val");
            return false;
        }
        if (!t.min().equals(info[0])){
            System.out.println("false min val");
            return false;
        }
        TreePrinter.print(t.getRoot());
        System.out.println(t.min() + "  " + t.max());
        return true;
    }

    public static boolean checkJoin() {
        randomOperation = 4;
        for (int i = 0; i < 50000; i++) {
            AVLTree t1 = new AVLTree();
            AVLTree t2 = new AVLTree();
            System.out.println("---------------------------------------------------");
            int random1 = getRandomNumber(0, 30);
            int random2 = getRandomNumber(0, 30);
            int[] t1Keys = createRandomTree(t1, random1, 32, 50);
            TreePrinter.print(t1.getRoot());
            createRandomTree(t2, random2, 0, 30);
            TreePrinter.print(t2.getRoot());
            if(t2.size() == 1) {
                System.out.println("check");
            }
            AVLTree.IAVLNode newNode = new AVLTree.AVLNode(31, "31");
            t1.join(newNode, t2);
            if (!CheckTree(t1, i,"Join",t1Keys)) {
                System.out.println("wrong");
                return false;
            }
            TreePrinter.print(t1.getRoot());
            System.out.println("---------------------------------------------------");
        }
        return true;
    }



    public static boolean checkInsertsAndDeletes(AVLTree t){
        int[] keys= t.keysToArray();
        int i = 0;
        int random1 = 0;
        int random2 = 0;
        while (i < 1000) {
            random1 = getRandomNumber(0, 10);
            random2 = getRandomNumber(0, 200);
            if (random1 <= 3) {
                System.out.println("trying to insert " + random2);
                t.insert(random2, "" + random2);
                randomOperation = 1;
                if (!CheckTree(t, random2,"Insert",keys)) {
                    System.out.println("wrong");
                    return false;
                }
                System.out.println("after insertation of " + random2);
                TreePrinter.print(t.getRoot());
            } else {
                System.out.println("trying to deleteCheck " + random2);
                t.delete(random2);
                randomOperation = 2;
                System.out.println("after deletion of " + random2);
                TreePrinter.print(t.getRoot());
            }

            System.out.println("The Tree is a correct AVL tree");
            TreePrinter.print(t.getRoot());
        }
        return true;
    }

    public static boolean CheckTree(AVLTree t, int key, String operation,int[] previouskeys) {
        if (isBst(t.getRoot()) == false){
            System.out.println("Tree is not a bst" + " the operation was: " + operation + " node was: " + key);
            TreePrinter.print(t.getRoot());
            return false;
        }

        if (isBalanced(t.getRoot()) == false) {
            System.out.println("Tree is not a balanced" + " the operation was: " + operation + " node was: " + key);
            TreePrinter.print(t.getRoot());
            return false;
        }
        if (isTreeConsistent(t.getRoot()) == false) {
            System.out.println("Tree is not consistent" + " the operation was: " + operation + " node was: " + key);
            TreePrinter.print(t.getRoot());
            return false;
        }
        if (isBalanced(t.getRoot()) == false) {
            System.out.println("Tree contains a loop" + " the operation was: " + operation + " node was: " + key);
            TreePrinter.print(t.getRoot());
            return false;
        }
        if (isSizeCorrect(t.getRoot()) == false) {
            System.out.println("Size is not correct" + " the operation was: " + operation + " node was: " + key);
            TreePrinter.print(t.getRoot());
            return false;
        }
        if (!checkminNmaxNodes(t)){
            System.out.println("problem with min/max node");
            return false;
        }
        if (isHavingAllKeys(t,previouskeys,operation,key) == false){}
        return true;
    }


    public static boolean isBalanced(AVLTree.IAVLNode node) {
        int lh; /* for height of left subtree */

        int rh; /* for height of right subtree */

        /* If tree is empty then return true */
        if (node == null) {
            return true;
        }
        if (!node.isRealNode())
            return true;

        /* Get the height of left and right sub trees */
        lh = node.getLeft().getHeight();
        rh = node.getRight().getHeight();

        if (Math.abs(lh - rh) <= 1
                && isBalanced(node.getLeft())
                && isBalanced(node.getRight()))
            return true;

        /* If we reach here then tree is not height-balanced */
        return false;
    }

    public static boolean isSizeCorrect(AVLTree.IAVLNode node) {
        int ls; /* for size of left subtree */

        int rs; /* for size of right subtree */

        /* If tree is empty then return true */
        if (node == null) {
            return true;
        }
        if (!node.isRealNode())
            return true;

        /* Get the size of left and right sub trees */
        ls = node.getLeft().getSize();
        rs = node.getRight().getSize();

        if (node.getSize() == ls + rs + 1
                && isBalanced(node.getLeft())
                && isBalanced(node.getRight()))
            return true;


        /* If we reach here then tree is not size-balanced */
        return false;
    }

    public static boolean isTreeConsistent(AVLTree.IAVLNode root) {
        if (root == null || root.isRealNode() == false) {
            return true;
        }
        if (containsLoops(root, new HashSet<Integer>())) {
            System.out.println("Loop");
            return false;
        }
        boolean ret = true;
        if (root.getLeft().isRealNode()) {
            if (root.getLeft().getParent() != root) {
                System.out.println(root.getKey() + " Left son.");
                return false;
            }
            ret = ret && isTreeConsistent(root.getLeft());
        }
        if (root.getRight().isRealNode()) {
            if (root.getRight().getParent() != root) {
                System.out.println(root.getKey() + " Right son.");
                return false;
            }
            ret = ret && isTreeConsistent(root.getRight());
        }
        return ret;
    }

    public static boolean containsLoops(AVLTree.IAVLNode cur, Set<Integer> set) {
        if (cur == null)
            return false;
        if (!cur.isRealNode())
            return false;
        if (set.contains(cur.getKey()))
            return true;
        set.add(cur.getKey());
        boolean leftLoops = containsLoops(cur.getLeft(), set);
        boolean rightLoops = containsLoops(cur.getRight(), set);
        return leftLoops || rightLoops;
    }
    public static boolean searchInArray(int[] array,int key){
        for (int item:array){
            if (item == key){
                return true;
            }
        }
        return false;
    }
    public static boolean isHavingAllKeys(AVLTree t , int[] previousKeys,String operation,int key){
        if(operation.equals("Insert")){
            int[] arr= t.keysToArray();
            for (int item:previousKeys){
                if (searchInArray(arr,item) == false){
                    System.out.println("problem in keys. key:" + item + " is not in keys to array");
                    return false;
                }
                if (searchInArray(arr,key) == false){
                    System.out.println("problem in keys. key:" + key + " is not in keys to array");
                    return false;
                }
            }
        }
        if(operation.equals("Delete")){
            int[] arr= t.keysToArray();
            for (int item:previousKeys){
                if (searchInArray(arr,item) == false && key!=item){
                    System.out.println("problem in keys. key:" + item + " is not in keys to array");
                    return false;
                }
                if (searchInArray(arr,key) == true){
                    System.out.println("problem in keys. key:" + key + " was not deleted, but should have been");
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isBst(AVLTree.IAVLNode root){
        return isBSTUtil(root,Integer.MIN_VALUE,Integer.MAX_VALUE);
    }

    public static boolean isBSTUtil(AVLTree.IAVLNode node, int min, int max)
    {
        /* an empty tree is BST */
        if (node == null || !node.isRealNode())
            return true;

        /* false if this node violates the min/max constraints */
        if (node.getKey() < min || node.getKey() > max) {
            System.out.println(node.getKey());
            return false;
        }

        /* otherwise check the subtrees recursively
        tightening the min/max constraints */
        // Allow only distinct values
        return (isBSTUtil(node.getLeft(), min, node.getKey()-1) &&
                isBSTUtil(node.getRight(), node.getKey()+1, max));
    }


    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static int[] arrayOfRandoms(int min, int max, int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = getRandomNumber(min, max);
        }
        return array;
    }

    public static int[] createRandomTree(AVLTree t, int size,int min,int max) {
        int[] array = arrayOfRandoms(min, max, size);
        for (int num : array) {
            t.insert(num, "" + num);
        }
        return array;
    }

    public static void deleteCheck(int numberOfItemToDelete, int[] array, AVLTree t) {
        int[] keys = t.keysToArray();
        int randomindex;
        for (int i = 0; i < numberOfItemToDelete; i++) {
            randomindex = getRandomNumber(0, array.length);
            TreePrinter.print(t.getRoot());
            System.out.println(t.size());
            if (t.size() == 1) {
                System.out.println("asdas");
            }
            System.out.println("deleted item is: " + array[randomindex]);
            t.delete(array[randomindex]);
            keys = t.keysToArray();
            if (!CheckTree(t, array[randomindex],"Delete",keys)) {
                System.out.println("error");
                TreePrinter.print(t.getRoot());
                break;
            }
        }
    }
}



