//package features.invertedindexing;
//
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.regex.Pattern;
//
//public class BTree {
//    private int t;
//    private BTreeNode root;
//
//    public BTree(int t) {
//        this.t = t;
//        this.root = new BTreeNode();
//    }
//
//    public void insert(String key, String document, int frequency) {
//        BTreeNode root = this.root;
//        if (root.keys.size() == (2 * t) - 1) {
//            BTreeNode newRoot = new BTreeNode();
//            this.root = newRoot;
//            newRoot.children.add(root);
//            splitChild(newRoot, 0);
//            insertNonFull(newRoot, key, document, frequency);
//        } else {
//            insertNonFull(root, key, document, frequency);
//        }
//    }
//
//    private void insertNonFull(BTreeNode x, String key, String document, int frequency) {
//        int i = x.keys.size() - 1;
//
//        if (x.children.isEmpty()) {
//            while (i >= 0 && key.compareTo(x.keys.get(i)) < 0) {
//                i--;
//            }
//            i++;
//            x.keys.add(i, key);
//            x.values.add(i, new HashMap<>(Collections.singletonMap(document, frequency)));
//        } else {
//            while (i >= 0 && key.compareTo(x.keys.get(i)) < 0) {
//                i--;
//            }
//            i++;
//
//            if (x.children.get(i).keys.size() == (2 * t) - 1) {
//                splitChild(x, i);
//                if (key.compareTo(x.keys.get(i)) > 0) {
//                    i++;
//                }
//            }
//
//            insertNonFull(x.children.get(i), key, document, frequency);
//        }
//    }
//
//    private void splitChild(BTreeNode x, int i) {
//        BTreeNode y = x.children.get(i);
//        BTreeNode z = new BTreeNode();
//        x.children.add(i + 1, z);
//        x.keys.add(i, y.keys.get(t - 1));
//        x.values.add(i, y.values.get(t - 1));
//
//        z.keys.addAll(y.keys.subList(t, y.keys.size()));
//        z.values.addAll(y.values.subList(t, y.values.size()));
//        y.keys.subList(t - 1, y.keys.size()).clear();
//        y.values.subList(t - 1, y.values.size()).clear();
//
//        if (!y.children.isEmpty()) {
//            z.children.addAll(y.children.subList(t, y.children.size()));
//            y.children.subList(t, y.children.size()).clear();
//        }
//    }
//
//    public Map<String, Integer> search(String key) {
//        return search(root, key);
//    }
//
//    private Map<String, Integer> search(BTreeNode x, String key) {
//        int i = 0;
//        while (i < x.keys.size() && key.compareTo(x.keys.get(i)) > 0) {
//            i++;
//        }
//
//        if (i < x.keys.size() && key.equals(x.keys.get(i))) {
//            return x.values.get(i);
//        } else if (x.children.isEmpty()) {
//            return null;
//        } else {
//            return search(x.children.get(i), key);
//        }
//    }
//
////    private Map<String, Integer> search(BTreeNode x, String keyPattern) {
////        int i = 0;
////        Pattern pattern = Pattern.compile(Pattern.quote(keyPattern));
////
////        while (i < x.keys.size() && !pattern.matcher(x.keys.get(i)).matches()) {
////            i++;
////        }
////
////        if (i < x.keys.size() && pattern.matcher(x.keys.get(i)).matches()) {
////            return x.values.get(i);
////        } else if (x.children.isEmpty()) {
////            return null;
////        } else {
////            return search(x.children.get(i), keyPattern);
////        }
////    }
//}
//
