import java.util.*;

public class Q1Test {
     public static void main(String[] args) {

          String result, traversal1, traversal2, traversal3;

          traversal1 = "1-2";
          traversal2 = "2-1-3";
          traversal3 = "1-3-2";
          result = verify(traversal1, traversal2, traversal3);
          System.out.println("Traversal 1 : " + traversal1);
          System.out.println("Traversal 2 : " + traversal2);
          System.out.println("Traversal 3 : " + traversal3);
          System.out.println("Expected Result : Invalid traversals");
          System.out.println("  Actual Result : " + result);
          System.out.println();

          traversal1 = "1-2-3";
          traversal2 = "2-1-3";
          traversal3 = "1-3-2";
          result = verify(traversal1, traversal2, traversal3);
          System.out.println("Traversal 1 : " + traversal1);
          System.out.println("Traversal 2 : " + traversal2);
          System.out.println("Traversal 3 : " + traversal3);
          System.out.println(
                    "Expected Result : Traversal 1 - Inorder, Traversal 2 - Preorder, Traversal 3 - Postorder");
          System.out.println("  Actual Result : " + result);
          System.out.println();

          traversal1 = "1-2-3";
          traversal2 = "3-2-1";
          traversal3 = "2-3-1";
          result = verify(traversal1, traversal2, traversal3);
          System.out.println("Traversal 1 : " + traversal1);
          System.out.println("Traversal 2 : " + traversal2);
          System.out.println("Traversal 3 : " + traversal3);
          System.out.println("Expected Result : Invalid traversals");
          System.out.println("  Actual Result : " + result);
          System.out.println();

          traversal1 = "3-1-2-5-4";
          traversal2 = "2-1-4-5-3";
          traversal3 = "1-2-3-4-5";
          result = verify(traversal1, traversal2, traversal3);
          System.out.println("Traversal 1 : " + traversal1);
          System.out.println("Traversal 2 : " + traversal2);
          System.out.println("Traversal 3 : " + traversal3);
          System.out.println(
                    "Expected Result : Traversal 1 - Preorder, Traversal 2 - Postorder, Traversal 3 - Inorder");
          System.out.println("  Actual Result : " + result);
          System.out.println();

          traversal1 = "10-20-30-40-50";
          traversal2 = "20-10-30-40-50";
          traversal3 = "10-50-40-30-20";
          result = verify(traversal1, traversal2, traversal3);
          System.out.println("Traversal 1 : " + traversal1);
          System.out.println("Traversal 2 : " + traversal2);
          System.out.println("Traversal 3 : " + traversal3);
          System.out.println(
                    "Expected Result : Traversal 1 - Inorder, Traversal 2 - Preorder, Traversal 3 - Postorder");
          System.out.println("  Actual Result : " + result);
          System.out.println();
     } 

      // write your codes here
     public static String verify(String traversal1, String traversal2, String traversal3) {
        try {
            int[][] traversals = {parse(traversal1), parse(traversal2), parse(traversal3)};
            int length = traversals[0].length;
            if (traversals[1].length != length || traversals[2].length != length
                    || !sameValues(traversals[0], traversals[1])
                    || !sameValues(traversals[0], traversals[2])) {
                return "Invalid traversals";
            }

            String[] names = {"Inorder", "Preorder", "Postorder"};
            String[] result = new String[3];
            boolean[] used = new boolean[3];
            if (!assign(0, traversals, names, result, used)) {
                return "Invalid traversals";
            }
            return "Traversal 1 - " + result[0] + ", Traversal 2 - " + result[1]
                    + ", Traversal 3 - " + result[2];
        } catch (RuntimeException e) {
            return "Invalid traversals";
        }
    }

    private static boolean assign(int position, int[][] traversals, String[] names,
                                  String[] result, boolean[] used) {
        if (position == 3) {
            int inorderIndex = -1, preorderIndex = -1, postorderIndex = -1;
            for (int i = 0; i < 3; i++) {
                if (result[i].equals("Inorder")) inorderIndex = i;
                else if (result[i].equals("Preorder")) preorderIndex = i;
                else postorderIndex = i;
            }
            return matches(traversals, inorderIndex, preorderIndex, postorderIndex);
        }
        for (int order = 0; order < 3; order++) {
            if (!used[order]) {
                used[order] = true;
                result[position] = names[order];
                if (assign(position + 1, traversals, names, result, used)) return true;
                used[order] = false;
            }
        }
        return false;
    }
    private static boolean matches(int[][] traversals, int inorderIndex, int preorderIndex, int postorderIndex) {
        int[] inorder = traversals[inorderIndex];
        int[] preorder = traversals[preorderIndex];
        int[] postorder = traversals[postorderIndex];
        for (int i = 1; i < inorder.length; i++) {
            if (inorder[i - 1] >= inorder[i]) return false;
        }
        return checkRange(inorder, preorder, postorder, 0, inorder.length - 1, 0, postorder.length - 1, 0);
    }

    private static boolean checkRange(int[] in, int[] pre, int[] post,
                                      int inStart, int inEnd, int postStart, int postEnd,
                                      int preStart) {
        if (inStart > inEnd) return postStart > postEnd;
        if (postStart > postEnd || inEnd - inStart != postEnd - postStart
                || preStart >= pre.length) return false;

        int root = pre[preStart];
        if (root != post[postEnd]) return false;
        int rootIndex = inStart;
        while (rootIndex <= inEnd && in[rootIndex] != root) rootIndex++;
        if (rootIndex > inEnd) return false;

        int leftSize = rootIndex - inStart;
        return checkRange(in, pre, post, inStart, rootIndex - 1,
                          postStart, postStart + leftSize - 1, preStart + 1)
                && checkRange(in, pre, post, rootIndex + 1, inEnd,
                              postStart + leftSize, postEnd - 1,
                              preStart + leftSize + 1);
    }
    private static int[] parse(String text) {
        if (text == null || text.trim().isEmpty()) throw new IllegalArgumentException();
        String[] parts = text.split("-", -1);
        int[] values = new int[parts.length];
        for (int i = 0; i < parts.length; i++) values[i] = Integer.parseInt(parts[i].trim());
        return values;
    }

    private static boolean sameValues(int[] first, int[] second) {
        if (first.length != second.length) return false;
        int[] a = first.clone();
        int[] b = second.clone();
        Arrays.sort(a);
        Arrays.sort(b);
        return Arrays.equals(a, b);
    }
}