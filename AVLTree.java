import sun.reflect.generics.tree.Tree;

/**
 *
 * AVLTree
 *
 * An implementation of an AVL Tree with
 * distinct integer keys and info.
 *
 * ids:318390929,313227001
 *usernames:erezscholz,talshamir
 *names:ארז שולץ, טל שמיר
 */

public class AVLTree {
	private IAVLNode root;
	private IAVLNode MinNode;
	private IAVLNode MaxNode;

	public AVLTree(){
		this.root = null;
		this.MinNode = null;
		this.MaxNode = null;
	}

	/**
	 * public boolean empty()
	 * Returns true if and only if the tree is empty.
	 */
	public boolean empty() { // O(1)
		return this.root == null;
	}

	/**
	 * public String search(int k)
	 * Returns the info of an item with key k if it exists in the tree.
	 * otherwise, returns null.
	 * Complexity - O(logn)
	 */

	public String search(int k) {
		IAVLNode n = searchNodeByKey(k);
		if (n == null){
			return null;
		}
		if (n.getKey() == k){
			return n.getValue();
		}
		return null;
	}
	/**
	 * private void updateMinMaxNode(IAVLNode node)
	 * updates AVLTree's MaxNode and MinNode
	 * Complexity - O(1)
	 */
	private void updateMinMaxNode(IAVLNode node){
		int k = node.getKey();
		if (this.MaxNode !=null) {
			if (this.MaxNode.getKey() < k) {
				this.MaxNode = node;
			}
		}
		else{
			this.MaxNode = node;
		}
		if (this.MinNode !=null) {
			if (this.MinNode.getKey() > k) {
					this.MinNode = node;
				}
		}
		else {
			this.MinNode = node;
		}
	}

	/**
	 * private IAVLNode searchNodeByKey(int k)
	 * returns the Node with key K if it doesn't exsits returns the tree
	 * position which it should be added after.
	 *
	 * Inserts an item with key k and info i to the AVL tree.
	 * The tree must remain valid, i.e. keep its invariants.
	 * Returns the number of re-balancing operations, or 0 if no re-balancing operations were necessary.
	 * A promotion/rotation counts as one re-balance operation, double-rotation is counted as 2.
	 * Returns -1 if an item with key k already exists in the tree.
	 * Complexity - O(logn)Complexity - O(logn), preforms binary search.
	 *
	 **/

	private IAVLNode searchNodeByKey(int k) {
		if (this.empty()) {
	 			return null;
		}
		IAVLNode current = this.getRoot();
		IAVLNode father = this.getRoot();
		while (current.isRealNode()) { // if not external tree
			father = current;
			if (k == current.getKey()) {
				return current;
			} else {
				if (k < current.getKey()) {
					current = current.getLeft();
				} else {
					current = current.getRight();
				}
			}
		}
	  		return father;
	}
	 /**
	 * public insert(int k, String i)
	 * inset a node with key k and info i to the tree.
	  * Complexity - O(logn)
	 */
	public int insert(int k, String i) {

		boolean isAleaf;
		IAVLNode newNode = new AVLNode(k, i); // creating a new node with key - k  and string -i
		updateMinMaxNode(newNode);
		IAVLNode treePos = this.searchNodeByKey(k); // returens either the node with key k or his father if it doesn't exsit.
		if (treePos == null) {
			this.root = newNode;
			updateSizeNHighet(newNode); // Updates node's size and higher fields.
			return 0;
		}

		if (treePos.getKey() == k) { // Node with this key already exists in the tree
			return -1;
		}
		isAleaf = AVLTree.isLeaf(treePos);

		if (treePos.getKey() > k) {
			treePos.setLeft(newNode);
		} else {
			treePos.setRight(newNode);
		}

		newNode.setParent(treePos);

		int numOfOperations = 0;

		if (!isAleaf) {
			updateSizeNHighet(newNode);
			return numOfOperations;
		}
		int[] diff = sonsDifference(treePos);
		while ((diff[0] == 0 && diff[1] == 1) || (diff[0] == 1 && diff[1] == 0)) {
			if (this.root == treePos){
				this.root.setHeight(this.root.getHeight() +1);
				numOfOperations++;
				break;
			}
			treePos.setHeight(treePos.getHeight() + 1);
			treePos = treePos.getParent();
			diff = sonsDifference(treePos);
			numOfOperations++;
		}
		if (isBalancedLocaly(treePos)) {
			updateSizeNHighet(newNode);
			return numOfOperations;
		}
		int rotations = doRotations(treePos);
		updateSizeNHighet(newNode); // O(logn)
		return numOfOperations + rotations;
	}


	/**
	 private int doRotations(IAVLNode node)
	 * The function gets an IAVLNode and preforms the neccesery rebalancing operations
	 * in order to keep the Tree a Valid AVLTree.
	 * Returns the number of re-balancing operations.
	 * Complexity - O(1)
	 */

	private int doRotations(IAVLNode node) { // Does the rotations and returns num of operations.
		if (node.getHeight() - node.getRight().getHeight() == 0) { // Left Rotation
			if (node.getRight().getHeight() - node.getRight().getLeft().getHeight() == 2) {
				rotateLeft(node.getRight());
				return 2; // Case 2
			} else {
				IAVLNode z = node.getRight().getLeft();
				rotateRight(z);
				rotateLeft(z);
				z.setHeight(Math.max(z.getLeft().getHeight(),z.getRight().getHeight())+1);
				return 5; // Case 3
			}
		}
		if (node.getHeight() - node.getLeft().getHeight() == 0) { // Right Rotation
			if (node.getLeft().getHeight() - node.getLeft().getRight().getHeight() == 2) {
				rotateRight(node.getLeft());
				return 2; // Case 2
			} else {
				IAVLNode z = node.getLeft().getRight();
				rotateLeft(z);
				rotateRight(z);
				z.setHeight(Math.max(z.getLeft().getHeight(),z.getRight().getHeight())+1);
				return 5; // Case 3
			}
		}
		return 2;
	}

	/**
	 private static void updateSizeNHighet(IAVLNode node)
	 * The function gets an IAVLNode and updates each node
	 * size and Highet field from this node untill the root.
	 * Complexity - O(Logn), on each node the Complexity of operations is O(1) and there
	 * are O(logn) nodes from the given node untill the root - at most the highet of the tree.
	 */

	private static void updateSizeNHighet(IAVLNode node) { // Updates each Node's size from node to Tree's root.
		if (node == null){
			return;
		}
		while (node.getParent() != null) {
			node.setSize(node.getRight().getSize() + node.getLeft().getSize() + 1);
			node = node.getParent();
			node.setHeight(Math.max(node.getRight().getHeight(), node.getLeft().getHeight()) + 1);
		}
		node.setSize(node.getRight().getSize() + node.getLeft().getSize() + 1);
		node.setHeight(Math.max(node.getRight().getHeight(), node.getLeft().getHeight()) + 1);
	}

	/**
	 private void rotateRight(IAVLNode node)
	 * The function gets an IAVLNode and preforms a right rotation as seen in class.
	 * Complexity - O(1), changing some node's fields
	 */

	private void rotateRight(IAVLNode node) { // X
		IAVLNode previousRightSon = node.getRight(); //C
		IAVLNode previousParent = node.getParent(); // Y

		previousParent.setLeft(previousRightSon);
		previousRightSon.setParent(previousParent);
		node.setRight(previousParent);
		if (previousParent.getParent() != null) {
			if (previousParent.getParent().getKey() < node.getKey()) {
				previousParent.getParent().setRight(node);
			} else {
				previousParent.getParent().setLeft(node);
			}
		}
		else {
			this.root = node;
		}
		node.setParent(previousParent.getParent());
		previousParent.setParent(node);
		if (previousParent != null) {
			updateCurrentRankNSize(previousParent); // update rank and size field of given node only
		}
		updateCurrentRankNSize(node); //update rank and size field of given node only


	}
	/**
	 private void updateCurrentRankNSize(IAVLNode node){
	 * The function gets an IAVLNode and updates it's size and height fields.
	 * Complexity - O(1), changing some node's fields
	 */
	private void updateCurrentRankNSize(IAVLNode node){
		node.setHeight(Math.max(node.getRight().getHeight(),node.getLeft().getHeight())+1);
		node.setSize(node.getRight().getSize() + node.getLeft().getSize() + 1);
	}
	/**
	 private void rotateLeft(IAVLNode node)
	 * The function gets an IAVLNode and preforms a Left rotation as seen in class.
	 * Complexity - O(1), changing some node's fields
	 */
	private void rotateLeft(IAVLNode node) { // Y
		IAVLNode previousLeftSon = node.getLeft(); //B
		IAVLNode previousParent = node.getParent(); // X

		previousParent.setRight(previousLeftSon);
		previousLeftSon.setParent(previousParent);
		node.setLeft(previousParent);
		if (previousParent.getParent() != null) {
			if (previousParent.getParent().getKey() < node.getKey()) {
				previousParent.getParent().setRight(node);
			} else {
				previousParent.getParent().setLeft(node);
			}
		}
		else {
			this.root = node;
		}
		node.setParent(previousParent.getParent());
		previousParent.setParent(node);

		if (previousParent != null) {
			updateCurrentRankNSize(previousParent); // demote
		}
		updateCurrentRankNSize(node);

	}
	/**
	 private static boolean isBalancedLocaly(IAVLNode node)
	 * The function gets an IAVLNode and checks if it is balanced as AVLNode.
	 * Complexity - O(1).
	 */
	private static boolean isBalancedLocaly(IAVLNode node) {
		int withRight = node.getHeight() - node.getRight().getHeight();
		int withLeft = node.getHeight() - node.getLeft().getHeight();
		if ((withRight == 1 && withLeft == 2) || (withRight == 2 && withLeft == 1) || (withRight == 1 && withLeft == 1)) {
			return true;
		}
		return false;
	}


	/**
	 * public int delete(int k)
	 * <p>
	 * Deletes an item with key k from the binary tree, if it is there.
	 * The tree must remain valid, i.e. keep its invariants.
	 * Returns the number of re-balancing operations, or 0 if no re-balancing operations were necessary.
	 * A promotion/rotation counts as one re-balance operation, double-rotation is counted as 2.
	 * Returns -1 if an item with key k was not found in the tree.
	 * Complexity - O(Logn).
	 */
	public int delete(int k) {
		int sumOfOperations = 0;
		IAVLNode nodeToBalance;
		IAVLNode previousFather;
		IAVLNode suc = new AVLNode();
		IAVLNode originalNodeToBalance = new AVLNode();
		boolean hadTwoSons = false;

		if (this.empty()) { //corner case tree is empty
			return -1;
		}

		IAVLNode nodeToDelete = this.searchNodeByKey(k); //O(logn)
		if (nodeToDelete.getKey() == k && this.root.getSize() == 1){ // if k is the root and has no sons.
			this.root = null;
			this.MaxNode = null;
			this.MinNode = null;
			return sumOfOperations; // 0
		}

		if (nodeToDelete.getKey() != k) { // corner case k is not in the tree
			return -1;
		}
		if (nodeToDelete.getRight().isRealNode() && nodeToDelete.getLeft().isRealNode()){ // has two sons
			previousFather = nodeToDelete.getParent();
			suc = getSuccessor(nodeToDelete);
			nodeToBalance = deletedNode(suc,suc.getParent());
			originalNodeToBalance = nodeToBalance;
			hadTwoSons = true;
		}
		else {
			previousFather = nodeToDelete.getParent();
			nodeToBalance = deletedNode(nodeToDelete, previousFather); // father of deleted node
			originalNodeToBalance = nodeToBalance;
		}
		while (nodeToBalance != null){
			int[] arr = sonsDifference(nodeToBalance);
			if ((arr[0] == 2 && arr[1] == 1) || (arr[0] == 1 && arr[1] == 2)){
				break;
			}
			if (arr[0] == 2 && arr[1] == 2){
				nodeToBalance.setHeight(nodeToBalance.getHeight() -1); // demote
				sumOfOperations++;
				nodeToBalance = nodeToBalance.getParent();
				continue;
			}
			sumOfOperations += deleteRotations(nodeToBalance);
			nodeToBalance = nodeToBalance.getParent();
		}
		if (hadTwoSons) {
			String typeOfSon = sonType(nodeToDelete);
			if (typeOfSon.equals("R")) {
				nodeToDelete.getParent().setRight(suc);
				suc.setParent(nodeToDelete.getParent());
			}
			if (typeOfSon.equals("L")) {
				nodeToDelete.getParent().setLeft(suc);
				suc.setParent(nodeToDelete.getParent());
			}
			if (typeOfSon.equals("N")){
				this.root = suc;
				suc.setParent(null);
			}
			suc.setRight(nodeToDelete.getRight());
			suc.setLeft(nodeToDelete.getLeft());
			suc.getRight().setParent(suc);
			suc.getLeft().setParent(suc);

			updateCurrentRankNSize(suc);
			if (previousFather != null) {
				updateCurrentRankNSize(previousFather);
			}
		}
		updateSizeNHighet(originalNodeToBalance);
		this.MaxNode = this.maxNode();
		this.MinNode = this.minNode();
		return sumOfOperations;
	}

	/**
	 * private int deleteRotations(IAVLNode node)
	 * <p>
	 * Preforms the necessary Rotations by different cases as seen in class
	 * after a deletion has been made.
	 * Complexity - O(1).
	 */

	private int deleteRotations(IAVLNode node){
		int[] arr = sonsDifference(node);
		int[] sonArr;
		if (arr[0] == 3 && arr[1] == 1){ // case 2
			sonArr = sonsDifference(node.getRight());
			if (sonArr[0] == 1 && sonArr[1] == 1){
				rotateLeft(node.getRight());
				return 3;
			}
			if (sonArr[0] == 2 && sonArr[1] == 1){//case 3
				rotateLeft(node.getRight());
				return 2;
			}
			if (sonArr[0] == 1 && sonArr[1] == 2){//case 4
				rotateRight(node.getRight().getLeft());
				rotateLeft(node.getRight());
				return 5;
			}
		}
		if (arr[0] == 1 && arr[1] == 3){
			sonArr = sonsDifference(node.getLeft());
			if (sonArr[0] == 1 && sonArr[1] == 1){
				rotateRight(node.getLeft());
				return 3;
			}
			if (sonArr[0] == 1 && sonArr[1] == 2){
				rotateRight(node.getLeft());
				return 2;
			}
			if (sonArr[0] == 2 && sonArr[1] == 1){
				rotateLeft(node.getLeft().getRight());
				rotateRight(node.getLeft());
				return 5;
			}
		}
		return 3;
	}
	/**
	 * private int[] sonsDifference(IAVLNode node)
	 * <p>
	 * returns an array representing the Height difference between a node and his two sons.
	 * Complexity - O(1).
	 */
	private int[] sonsDifference(IAVLNode node){
		int[] arr = new int[2];
		arr[1] = Math.abs(node.getHeight() - node.getRight().getHeight());
		arr[0] = Math.abs(node.getHeight() - node.getLeft().getHeight());
		return arr;
	}
	/**
	 * private IAVLNode deletedNode(IAVLNode node,IAVLNode previousFather)
	 * <p>
	 * This functions gets a node, deletes either this node or it's successor from the tree and returns the parent of deleted node.
	 * Complexity - O(logn).
	 */
	private IAVLNode deletedNode(IAVLNode node,IAVLNode previousFather) {
		IAVLNode father;
		IAVLNode externalLeaf = AVLNode.getExternaLeaf();
		if (node == this.root && this.root.getSize() == 1) {
			this.root = externalLeaf;
			return null;
		}
		/**
		checks if the node is a leaf
		 if so, node is deleted from the tree/
		 */
		if (isLeaf(node)) {
			father = node.getParent();
			if (sonType(node).equals("R")) {
				father.setRight(externalLeaf);
				father.setSize(father.getSize() - 1);
				return father;
			} else {
				father.setLeft(externalLeaf);
				father.setSize(father.getSize() - 1);
				return father;
			}
		}
		/**
		 checks if the node is unary,
		 if it is a unary node, we bypass it.
		 */
		if (node.getRight().isRealNode() && !node.getLeft().isRealNode()) {//node has only right son
			if (node == this.getRoot()) { // if the node is the tree's root.
				this.root = node.getRight();
				node.getRight().setParent(this.root);
				this.root.setParent(null);
				return null;
			}
			if (sonType(node).equals("R")) {
				node.getParent().setRight(node.getRight());
				node.getRight().setParent(node.getParent());
				return node.getParent();
			}
			if(sonType(node).equals("L")){
				node.getParent().setLeft(node.getRight());
				node.getRight().setParent(node.getParent());
				return node.getParent();
			}
		}
		if (node.getLeft().isRealNode() && !node.getRight().isRealNode()) {//node has only left son
			if (node == this.getRoot()) {
				this.root = node.getLeft();
				node.getLeft().setParent(this.root);
				this.root.setParent(null);
				return null;
			}
			if (sonType(node).equals("R")) {
				node.getParent().setRight(node.getLeft());
				node.getLeft().setParent(node.getParent());
				return node.getParent();
			}
			if(sonType(node).equals("L")){
				node.getParent().setLeft(node.getLeft());
				node.getLeft().setParent(node.getParent());
				return node.getParent();
			}
		}
		/**
		 the node has 2 sons
		 in that case the successor is being deleted from it's place and replaces node.
		 */
		IAVLNode suc = AVLTree.getSuccessor(node);
		if(node == this.root){
			father = deletedNode(suc,previousFather);
			suc.setParent(null);
			suc.setLeft(this.root.getLeft());
			suc.setRight(this.root.getRight());
			suc.getLeft().setParent(suc);
			suc.getRight().setParent(suc);
			this.root = suc;
			updateSizeNHighet(this.root);
			return null;
		}
		father = deletedNode(suc,previousFather);
		if(sonType(node).equals("R")) {
			node.getParent().setRight(suc);
		}
		else {
			node.getParent().setLeft(suc);
		}
			suc.setRight(node.getRight());
			suc.setLeft(node.getLeft());
			suc.getLeft().setParent(suc);
			suc.getRight().setParent(suc);
			suc.setParent(previousFather);
			return father;
	}
	/**
	 * private String sonType(IAVLNode node)
	 * <p>
	 * This functions gets a node, and returns 'L' if the node is a left son, 'R' for right, 'N' for root
	 * Complexity - O(1).
	 */
	private String sonType(IAVLNode node)
	{
		if( this.root == node){
			return "N";
		}
		if (node.getParent().getRight() == node){
			return "R";
	}
		return "L";
	}



   /**
    * public String min()
    *
    * Returns the info of the item with the smallest key in the tree,
    * or null if the tree is empty.
	* Complexity -  O(1)
    */
   public String min()
   {
	   if(this.MinNode == null){
		   return null;
	   }
		return this.MinNode.getValue();
   }

	/**
	 * private IAVLNode minNode()
	 *
	 * Returns the node with minimal key in this tree.
	 * or null if the tree is empty.
	 * Complexity -  O(logn)
	 */

	private IAVLNode minNode()
	{
		if (this.empty()){
			return null;
		}
		IAVLNode current = this.root;
		while (current.getLeft().isRealNode()){
			current = current.getLeft();
		}
		return current;
	}

   /**
    * public String max()
    * Returns the info of the item with the largest key in the tree,
    * or null if the tree is empty.
	* Complexity - O(1)
    */
   public String max()
   {
	   if (this.MaxNode == null){
		   return null;
	   }
		return this.MaxNode.getValue();
   }
   private IAVLNode maxNode(){
	   if (this.empty()){
		   return null;
	   }
	   IAVLNode current = this.root;
	   while (current.getRight().isRealNode()){
		   current = current.getRight();
	   }
	   return current;
   }

  /**
   * public int[] keysToArray()
   *
   * Returns a sorted array which contains all keys in the tree,
   * or an empty array if the tree is empty.
   * Complexity - O(n)
   */
  public int[] keysToArray() {
	  int[] array = new int[0];
	  if (this.empty() == true) { // corner case
		  return array;
	  }
	  int[] keys = new int[this.size()];
	  int counter = 0;
	  IAVLNode currMin = this.minNode();
	  while (currMin !=null){
		  keys[counter] = currMin.getKey();
		  counter++;
		  currMin = AVLTree.getSuccessor(currMin);
	  }
	  return keys;
  }

	/**
	 * private static IAVLNode getMinNode(IAVLNode node)
	 * returns The min of subtree with root = node (given node).
	 * Complexity - O(logn)
	 */

  private static IAVLNode getMinNode(IAVLNode node){
	  while (node.getLeft().isRealNode()){
		  node = node.getLeft();
	  }
	  return node;
  }
	/**
	 * private static IAVLNode getSuccessor(IAVLNode primary)
	 * returns the successor of the give node.
	 * Complexity - O(logn)
	 */
  private static IAVLNode getSuccessor(IAVLNode primary){
	  if (primary.getRight().isRealNode()){
		  return getMinNode(primary.getRight());
	  }
	  IAVLNode successor = primary.getParent();
	  while (successor != null && primary == successor.getRight()){
		  primary = successor;
		  successor = successor.getParent();
	  }
	  return  successor;
  }
	/**
	 * private static boolean isLeaf(IAVLNode node)
	 * returns true if the given node is a leaf. false otherwise.
	 * Complexity - O(1)
	 */

  private static boolean isLeaf(IAVLNode node){
	  if (node.getRight().getKey() == -1 && node.getLeft().getKey() == -1){
		  return true;
	  }
	  return false;
  }

  /**
   * public String[] infoToArray()
   *
   * Returns an array which contains all info in the tree,
   * sorted by their respective keys,
   * or an empty array if the tree is empty.
   * Complexity - O(n)
   */
  public String[] infoToArray()
  {
	  String[] array = new String[0];
	  if (this.empty() == true) { // corner case
		  return array;
	  }
	  String[] info = new String[this.size()];
	  int counter = 0;
	  IAVLNode currMin = AVLTree.getMinNode(this.getRoot());
	  while (currMin !=null) {
		  info[counter] = currMin.getValue();
		  counter++;
		  currMin = AVLTree.getSuccessor(currMin);
	  }

	  return info;
  }

   /**
    * public int size()
    *
    * Returns the number of nodes in the tree.
	* Complexity - O(1)
    */
   public int size() {
	   if (this.empty()) {
		   return 0;
	   }
	   return this.getRoot().getSize();
   }
   /**
    * public int getRoot()
    *
    * Returns the root AVL node, or null if the tree is empty
	* Complexity - O(1)
    */
   public IAVLNode getRoot()
   {
	   if (this.empty()){
		   return null;
	   }
	   return this.root;
   }
   
   /**
    * public AVLTree[] split(int x)
    *
    * splits the tree into 2 trees according to the key x. 
    * Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
    * 
	* precondition: search(x) != null (i.e. you can also assume that the tree is not empty)
    * postcondition: none
	* Complexity - O(logn)
    */   
   public AVLTree[] split(int x)
   {
	   IAVLNode splittingNode = this.searchNodeByKey(x);
	   IAVLNode splittingNodeParent = splittingNode.getParent();
	   AVLTree smallerKeys = new AVLTree();
	   AVLTree largerKeys = new AVLTree();
	   AVLTree tempTree = new AVLTree();

	   AVLTree[] arrayOfTrees = new AVLTree[2];
	   arrayOfTrees[0] = smallerKeys;
	   arrayOfTrees[1] = largerKeys;

	   if (this.size() == 1 && this.root.getKey() == x){ //corner case only one node, split by that node.
		   return arrayOfTrees;
	   }

	   if (splittingNode.getLeft().isRealNode()) {
		   smallerKeys.root = splittingNode.getLeft();
		   smallerKeys.root.setParent(null);
	   }
	   if (splittingNode.getRight().isRealNode()) {
		   largerKeys.root = splittingNode.getRight();
		   largerKeys.root.setParent(null);
	   }


	   while (splittingNodeParent != null){
		   tempTree.root = null; // clears the temporary tree

		   if (splittingNode.getKey() < x){
			   if (splittingNode.getLeft().isRealNode()) {
				   tempTree.root = splittingNode.getLeft();
				   tempTree.root.setParent(null);
			   }

			   smallerKeys.join(splittingNode,tempTree);
		   }
		   if (splittingNode.getKey() > x){
			   if (splittingNode.getRight().isRealNode()) {
				   tempTree.root = splittingNode.getRight();
				   tempTree.root.setParent(null);

			   }
			   splittingNode.setParent(null);
			   largerKeys.join(splittingNode,tempTree);
		   }
		   splittingNode = splittingNodeParent;
		   splittingNodeParent = splittingNodeParent.getParent();
	   }
	   tempTree.root = null; // clears the temporary tree

	   if (splittingNode.getKey() < x){
		   if (splittingNode.getLeft().isRealNode()) {
			   tempTree.root = splittingNode.getLeft();
			   tempTree.root.setParent(null);
		   }
		   smallerKeys.join(splittingNode,tempTree);
	   }
	   if (splittingNode.getKey() > x){
		   if (splittingNode.getRight().isRealNode()) {
			   tempTree.root = splittingNode.getRight();
			   tempTree.root.setParent(null);
		   }
		   largerKeys.join(splittingNode,tempTree);
	   }
	   arrayOfTrees[0].MaxNode = arrayOfTrees[0].maxNode();
	   arrayOfTrees[1].MaxNode = arrayOfTrees[1].maxNode();
	   arrayOfTrees[0].MinNode = arrayOfTrees[0].minNode();
	   arrayOfTrees[1].MinNode = arrayOfTrees[1].minNode();
	   return arrayOfTrees;
   }
   
   /**
    * public int join(IAVLNode x, AVLTree t)
    *
    * joins t and x with the tree. 	
    * Returns the complexity of the operation (|tree.rank - t.rank| + 1).
	*
	* precondition: keys(t) < x < keys() or keys(t) > x > keys(). t/tree might be empty (rank = -1).
    * postcondition: none
	* Complexity - O(logn)
    */   
   public int join(IAVLNode x, AVLTree t)
   {
	   x.setParent(null);
	   x.setRight(AVLNode.getExternaLeaf());
	   x.setLeft(AVLNode.getExternaLeaf());
	   x.setHeight(0);
	   x.setSize(1);

	   if (t.empty() && this.empty()){ // both trees are empty
		   this.root = x;
		   updateCurrentRankNSize(this.root);
		   this.MinNode = x;
		   this.MaxNode = x;
		   return 1;
	   }
	   if (t.empty() && !this.empty()){
			this.joinToTree(x);
			this.MaxNode = this.maxNode();
			this.MinNode = this.minNode();
			return this.getRoot().getHeight() + 2;
	   }
	   if (!t.empty() && this.empty()){
		   t.joinToTree(x);
		   this.root = t.getRoot();
		   this.MaxNode = this.maxNode();
		   this.MinNode = this.minNode();
		   return t.getRoot().getHeight() + 2;
	   }
	   if (this.getRoot().getHeight() == t.getRoot().getHeight()){
			this.joinTreesOfSameHeight(t,x);
			this.root = x;
		   	this.MaxNode = this.maxNode();
		   	this.MinNode = this.minNode();
			return 1;
	   }
	   int[] diffc = new int[2];
	   int[] diffx = new int[2];
	   AVLTree higestTree = this.higerThan(t); // The tree with bigger rank/height.
	   AVLTree shortestTree = this;
	   if (this == higestTree){
		   shortestTree = t;
	   }

	   int minHigh = this.minRanks(t);
	   boolean isbigger = (higestTree.getRoot().getKey() > x.getKey()); // true if higestTree's keys are bigger than x.
	   IAVLNode b =  higestTree.joinNode(isbigger,minHigh);
	   IAVLNode c =  b.getParent();
	   IAVLNode a = shortestTree.getRoot();
	   if (c == null){
		   this.root = x;
	   }
	   if (isbigger){ // higestTree's keys are bigger than x therfore we went left
		   x.setRight(b);
		   x.setLeft(a);
		   x.setParent(c);
		   a.setParent(x);
		   if (c!=null) {
			   c.setLeft(x);
		   }
		   b.setParent(x);
		   updateCurrentRankNSize(x);
	   }
	   else{ // higestTree's keys are smaller than x therfore we went right
		   x.setRight(a);
		   x.setLeft(b);
		   x.setParent(c);
		   a.setParent(x);
		   if (c!=null) {
			   c.setRight(x);
		   }
		   b.setParent(x);
		   updateCurrentRankNSize(x);
	   }
	   if (higestTree != this){
		   this.root = higestTree.getRoot();
	   }
	   // we reached b and joined the trees , x is now updated
		if (x.getParent() != null) {
			if (x.getParent().getHeight() - x.getHeight() == 1) {
				updateSizeNHighet(x);
				this.MaxNode = this.maxNode();
				this.MinNode = this.minNode();
				return (Math.abs(t.getRoot().getHeight() - this.getRoot().getHeight()) + 1); // done
			}
		}

	   // check special case
	   if (c !=null) {
		   diffc = sonsDifference(c);
		   diffx = sonsDifference(x);
		   if ((diffc[0] == 0 && diffc[1] == 2) && (diffx[0] == 1 && diffx[1] == 1)) // then its a speical case:
		   {
			   rotateRight(x);
		   } else {
			   if ((diffc[0] == 2 && diffc[1] == 0) && (diffx[0] == 1 && diffx[1] == 1)) // then its a speical case:
			   {
				   rotateLeft(x);
			   }
		   }
	   }
	   this.balanceJoin(x.getParent());
	   this.MaxNode = this.maxNode();
	   this.MinNode = this.minNode();
	   updateSizeNHighet(x);
	   return (Math.abs(t.getRoot().getHeight() - this.getRoot().getHeight()) +1);
   }
	/**
	 * private int balanceJoin(IAVLNode treePos)
	 * Performs the balancing operations as seen in class after a join as been made.
	 * Complexity - O(logn)
	 */

	private void balanceJoin(IAVLNode treePos)
	{
		int numOfOperations = 0;
		if (treePos == null){
			updateCurrentRankNSize(this.root);
			return;
		}
		int[] diff = sonsDifference(treePos);
		while ((diff[0] == 0 && diff[1] == 1) || (diff[0] == 1 && diff[1] == 0)) {
			if (this.root == treePos){
					updateCurrentRankNSize(treePos);
				break;
			}
			treePos.setHeight(treePos.getHeight() + 1);
			treePos = treePos.getParent();
			diff = sonsDifference(treePos);
			numOfOperations++;
		}
		if (isBalancedLocaly(treePos)) {
			updateSizeNHighet(treePos);
			return;
		}
		int rotations = doRotations(treePos);
		updateSizeNHighet(treePos);
		return;
	}

	/**
	 * private void joinTreesOfSameHeight(AVLTree t , IAVLNode x)
	 * Join this tree and t tree by node x.
	 * Precondition - this tree and t are of same Height.
	 * Complexity - O(1)
	 */

   private void joinTreesOfSameHeight(AVLTree t , IAVLNode x){
	   if (this.root.getKey() > x.getKey()){
		   x.setRight(this.getRoot());
		   x.setLeft(t.getRoot());
		   this.root.setParent(x);
		   t.getRoot().setParent(x);
		   this.root = x;
		   updateCurrentRankNSize(x);
	   }
	   else {
		   x.setRight(t.getRoot());
		   x.setLeft(this.getRoot());
		   this.root.setParent(x);
		   t.getRoot().setParent(x);
		   this.root = x;
		   updateCurrentRankNSize(x);
	   }

   }

	/**
	 * private void joinToTree(IAVLNode x)
	 * The function get a node x representing a root and joins the two trees.
	 * Precondition - x is either larger than all tree's keys or smaller.
	 * Complexity - O(1)
	 */
   private void joinToTree(IAVLNode x){
	   if (x.getKey() > this.getRoot().getKey()){
		   this.insert(this.maxNode().getKey() + 1, "MaxVal " + this.maxNode().getKey() + 1); //insert new maximum key node
		   IAVLNode temp = this.maxNode(); // get the max - key node
		   temp.getParent().setRight(x);
		   x.setParent(temp.getParent());
		   x.setLeft(temp.getLeft());
		   if (x.getLeft().isRealNode()){
			   x.getLeft().setParent(x);
		   }
		   x.setRight(temp.getRight());
	   }
	   else {
		   this.insert(this.minNode().getKey() - 1, "MinVal");
		   IAVLNode temp = this.minNode();
		   temp.getParent().setLeft(x);
		   x.setParent(temp.getParent());
		   x.setRight(temp.getRight());
		   if (x.getRight().isRealNode()){
			   x.getRight().setParent(x);
		   }
		   x.setLeft(temp.getLeft());
	   }
	   updateCurrentRankNSize(x);
   }

	/**
	 * private IAVLNode joinNode(boolean isbigger,int stopRank)
	 * The function get a node x and joins this tree with node x.
	 * Precondition - x is either larger than all tree's keys or smaller.
	 * Complexity - O(1)
	 */

   private IAVLNode joinNode(boolean isbigger,int stopRank){
	   IAVLNode root = this.getRoot();
	   IAVLNode rootParent = this.getRoot();
		   while (root.getHeight() > stopRank && root.isRealNode()) { // didn't reach shorter tree's rank
			   if (isbigger) { // this.keys > x.key - value is constant.
				   rootParent =root;
				   root = root.getLeft(); //proceed left
			   }
			   else {
				   rootParent =root;
				   root = root.getRight(); // proceed right
			   }
		   }
		   if (root.isRealNode()){
			   return root;
		   }
		   root.setParent(rootParent);
		   return root;
   }
	/**
	 * private int minRanks(AVLTree t)
	 * The function gets a tree -t and returns the min rank between t and this.
	 * Complexity - O(1)
	 */
   private int minRanks(AVLTree t){
	   if (this.empty()){
		   return t.getRoot().getHeight();
	   }
	   if (t.empty()){
		   return this.getRoot().getHeight();
	   }
	   return Math.min(this.getRoot().getHeight(), t.getRoot().getHeight());
   }
	/**
	 * public AVLTree higerThan(AVLTree t)
	 * The function gets a tree -t and returns the tree with higer rank.
	 * Complexity - O(1)
	 */
   public AVLTree higerThan(AVLTree t){
	   if (this.empty()){
		   return t;
	   }
	   if (t.empty()){
		   return this;
	   }
	   if (this.getRoot().getHeight() >= t.getRoot().getHeight()){
		   return this;
	   }
	   return t;
   }

	/** 
	 * public interface IAVLNode
	 * ! Do not deleteCheck or modify this - otherwise all tests will fail !
	 */
	public interface IAVLNode{	
		public int getKey(); // Returns node's key (for virtual node return -1).
		public String getValue(); // Returns node's value [info], for virtual node returns null.
		public void setLeft(IAVLNode node); // Sets left child.
		public IAVLNode getLeft(); // Returns left child, if there is no left child returns null.
		public void setRight(IAVLNode node); // Sets right child.
		public IAVLNode getRight(); // Returns right child, if there is no right child return null.
		public void setParent(IAVLNode node); // Sets parent.
		public IAVLNode getParent(); // Returns the parent, if there is no parent return null.
		public boolean isRealNode(); // Returns True if this is a non-virtual AVL node.
    	public void setHeight(int height); // Sets the height of the node.
    	public int getHeight(); // Returns the height of the node (-1 for virtual nodes).
		public void setSize(int size); // Sets size.
		public int getSize(); // Returns the size of the node.
			}

   /** 
    * public class AVLNode
    *
    * If you wish to implement classes other than AVLTree
    * (for example AVLNode), do it in this file, not in another file. 
    * 
    * This class can and MUST be modified (It must implement IAVLNode).
    */

   public static class AVLNode implements IAVLNode {
	  private int key;
	  private String value;
	  private int rank;
	  private int size;
	  private IAVLNode parent;
	  private IAVLNode right;
	  private IAVLNode left;
	  private static IAVLNode externaleaf = new AVLNode();

	  public AVLNode(){
		  this.key = -1;
		  this.value = "-1";
		  this.rank = -1;
		  this.size = 0;
		  this.parent = null;
		  this.right = null;
		  this.left = null;
	  }
	   public AVLNode(int key,String value){
		   this.key = key;
		   this.value = value;
		   this.rank = 0;
		   this.size = 0;
		   this.parent = null;
		   this.right = externaleaf;
		   this.left = externaleaf;
	   }

	   /**
		* All methods are O(1) - get/set values.
		*/

		public static IAVLNode getExternaLeaf(){
		  return externaleaf;
		}
		public int getKey()
		{
			return this.key;
		}
		public String getValue()
		{
			return this.value;
		}
		public void setLeft(IAVLNode node)
		{
			this.left = node;
		}
		public IAVLNode getLeft()
		{
			return this.left;
		}
		public void setRight(IAVLNode node)
		{
			this.right =  node;
		}
		public IAVLNode getRight()
		{
			return this.right;
		}
		public void setParent(IAVLNode node)
		{
			this.parent = node;
		}
		public IAVLNode getParent()
		{
			return this.parent;
		}
		public boolean isRealNode()
		{
			return this.key != -1;
		}
	    public void setHeight(int height)
	    {
	      this.rank = height;
	    }
	    public int getHeight()
	    {
	      return this.rank;
	    }
		public int getSize(){
		  return  this.size;
		}
		public void setSize(int size){
		  this.size = size;
		}

  }
}
  
