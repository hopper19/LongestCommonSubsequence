/* An instance of this class can provide information about longest common 
** subsequences of (any prefixes of) two specified character strings.  In
** creating such an instance, the client provides the pair of strings of 
** interest, X and Y.  Let M and N be the lengths of X and Y, respectively.
** For m satisfying 0<=m<=M and n satisfying 0<=n<=N, let LLCS.m.n be the
** Length of the Longest Common Subsequences of X[0..m) and Y[0..n) 
** (i.e., the prefixes of lengths m and n of X and Y).
** A "maximal (m,n)-matching" is a pair of mappings (f,g), 
** f : [0..k) --> [0..m) and g : [0..k) --> [0..n), where k = LLCS.m.n, 
** such that each of f and g is increasing and, for each i in [0..k), 
** X.(f.i) = Y.(g.i).  Thus, f describes a set of positions within X and 
** g describes a set of positions within Y, such that erasing the characters
** in all other positions leaves you with a longest common subsequence (LCS)
** of X[0..m) and Y[0..n).  (A "plain" (m,n)-matching (i.e., one that is not
** necessarily "maximal") has the same description, except that it could
** be that k < LLCS.
**
** Methods in this class are able to do the following:
**
** (1) Print a table showing, for all m<=M and n<=N, the value of LLCS.m.n.
**
** (2) Answer the question:  For given m<=M and n<=N, what is LLCS.m.n?
**
** (3) Determine, for any m<=M and n<=N, an LCS of X[0..m) and Y[0..n).
**
** (4) Determine, for any m<=M and n<=N, a maximal (m,n)-matching.
**
** (5) Determine, for any m<=M and n<=N, the number of distinct 
**     maximal (m,n)-matchings.
**
** Author: R. McCloskey, 2015 (modified most recently in 2020)
*/

public class LongComSubseq {

   // instance variables
   // ------------------
   private final String X, Y; // the Strings of interest
   private final int M, N; // their lengths (redundant data)
   private int[][] llcs; // 2-dimensional array storing values of the
                         // LLCS function applied to X and Y.
   private int[][] nlcs; // 2-dimensional array storing values of the
                         // NLCS

   // constructor (and supporing method)
   // -----------

   /*
    * Establishes the two given Strings as those that are the focus of
    ** this object.
    */
   public LongComSubseq(String s1, String s2) {
      X = s1;
      Y = s2;
      M = X.length();
      N = Y.length();
      // Fill the llcs[][] array with LLCS function values.
      computeLLCS();
      computeNLCS();
   }

   /*
    * Constructs and fills llcs[][] so that, for all i in [0..M]
    ** and j in [0..N] (where M and N are the lengths of X and Y),
    ** llcs[i][j] = LLCS.i.j. Which is to say that, upon completion of
    ** this method's execution, the value of llcs[i][j] is the length of
    ** any longest common subsequence of X[0..i) and Y[0..j).
    */
   private void computeLLCS() {
      llcs = new int[M + 1][N + 1];

      // Fill row zero and column zero with zeros.
      for (int j = 0; j != N + 1; j++) {
         llcs[0][j] = 0;
      }
      for (int i = 0; i != M + 1; i++) {
         llcs[i][0] = 0;
      }

      int m = 1, n = 1;
      /*
       * loop invariant: (A i,j | 0<=i<m & 0<=j<=N : llcs[i][j] = LLCS.i.j) &
       ** (A j | 0<=j<n : llcs[m][j] = LLCS.m.j) &
       ** 1<=m<=M+1 & 0<=n<=N+1
       ** bound function: (M+1)*(N+2) - (m*(N+2) + n).
       */
      while (m != M + 1) {
         if (n == N + 1) {
            m = m + 1;
            n = 0; // row m is now complete; go to next row
         } else if (X.charAt(m - 1) == Y.charAt(n - 1)) {
            llcs[m][n] = llcs[m - 1][n - 1] + 1;
         } else {
            llcs[m][n] = Math.max(llcs[m][n - 1], llcs[m - 1][n]);
         }
         n = n + 1;
      }
   }

   /*
    * 
    */
   private void computeNLCS() {
      nlcs = new int[M + 1][N + 1];

      // Fill row zero and column zero with zeroes
      for (int i = 0; i != M+1; i++) {
         nlcs[i][0] = 1;
      }
      for (int j = 0; j != N+1; j++) {
         nlcs[0][j] = 1;
      }

      int m, n;
      m = 1;
      while (m <= M) {
         n = 1;
         while (n <= N) {
            if (X.charAt(m - 1) == Y.charAt(n - 1)) {
               nlcs[m][n] = nlcs[m - 1][n - 1] + nlcs[m - 1][n] + nlcs[m][n - 1];
            } else {
               nlcs[m][n] = nlcs[m - 1][n] + nlcs[m][n - 1];
            }
            n++;
         }
         m++;
      }
   }

   // observers
   // ---------

   /*
    * Return the first string of interest.
    */
   public String getX() {
      return X;
   }

   /*
    * Return the second string of interest.
    */
   public String getY() {
      return Y;
   }

   /*
    * Returns LLCS.M.N, the maximum of the lengths of common subsequences
    ** of X and Y.
    */
   public int lengthOfLCS() {
      return lengthOfLCS(M, N);
   }

   /*
    * Returns LLCS.m.n, the maximum of the lengths of common subsequences
    ** of X[0..m) and Y[0..n) (the prefixes of X and Y of lengths m and n,
    ** respectively).
    */
   public int lengthOfLCS(int m, int n) {
      return llcs[m][n];
   }

   /*
    * Returns a maximal (M,N)-matching of X and Y.
    */
   public int[][] maxMatching() {
      return maxMatching(M, N);
   }

   /*
    * Returns a maximal (m,n)-matching (f,g) of X[0..m) and Y[0..n) in the
    ** form of a two-row matrix, the first row corresponding to f and the
    ** second row to g.
    */
   public int[][] maxMatching(int m, int n) {
      int[][] matching = new int[2][llcs[m][n]];
      int i = m, j = n, k = llcs[m][n] - 1;

      while (i > 0 && j > 0) {
         if (X.charAt(i - 1) == Y.charAt(j - 1)) {
            matching[0][k] = i - 1;
            matching[1][k] = j - 1;
            k--;
            i--;
            j--;
         } else if (llcs[i - 1][j] > llcs[i][j - 1]) {
            i--;
         } else {
            j--;
         }
      }

      return matching;
   }

   /*
    * Returns the number of distinct maximal (M,N)-matchings.
    */
   public int numMaxMatchings() {
      return numMaxMatchings(M, N);
   }

   /*
    * Returns the number of distinct maximal (m,n)-matchings.
    */
   public int numMaxMatchings(int m, int n) {
      return nlcs[m][n];
   }

   /*
    * Returns a longest common subsequence of X and Y.
    */
   public String longestCommonSubsequence() {
      return longestCommonSubsequence(M, N);
   }

   /*
    * Returns a longest common subsequence of X[0..m) and Y[0..n).
    */
   public String longestCommonSubsequence(int m, int n) {
      StringBuilder sb = new StringBuilder();

      int i = m, j = n;
      /*
       * Loop invariant: sb contains the longest common subsequence of
       *                 X[i..m) and Y[j..n).
       * Bound function: i + j.
       */
      while (i > 0 && j > 0) {
         if (X.charAt(i - 1) == Y.charAt(j - 1)) {
            sb.insert(0, X.charAt(i - 1));
            i--;
            j--;
         } else if (llcs[i - 1][j] > llcs[i][j - 1]) {
            i--;
         } else {
            j--;
         }
         // System.out.printf("%d %d %s %s %s\n", i, j, X.substring(i, M), Y.substring(j, N), sb.toString());
      }

      return sb.toString();
   }

   /*
    * Prints the values of the LLCS function in a table format.
    */
   public void printLLCSTable() {
      printLLCSTable(M, N);
   }

   /*
    * Prints the values of the LLCS function in a table format,
    ** but only up to LLCS.m.n.
    */
   public void printLLCSTable(int m, int n) {
      int maxNumLen = ("" + llcs[m][n]).length();
      String format = "%" + maxNumLen + "d ";
      for (int i = m; i != -1; i--) {
         System.out.printf("%3d", i);
         System.out.print("|");
         for (int j = 0; j != n + 1; j++) {
            System.out.printf(format, llcs[i][j]);
         }
         System.out.println();
      }
      System.out.print("   +");
      printString("-", (maxNumLen + 1) * (n + 1));
      System.out.println();
      System.out.print("    ");
      for (int j = 0; j != n + 1; j++) {
         System.out.printf(format, j);
      }
      System.out.println();
   }

   /*
    * Prints the values of the NLCS function in a table format.
    */
   public void printNLCSTable() {
      printNLCSTable(M, N);
   }

   /*
    * Prints the values of the NLCS function in a table format,
    ** but only up to NLCS.m.n.
    */
   public void printNLCSTable(int m, int n) {
      int maxNumLen = ("" + maxOf(nlcs, m, n)).length();
      String format = "%" + maxNumLen + "d ";
      for (int i = m; i != -1; i--) {
         System.out.printf("%3d", i);
         System.out.print("|");
         for (int j = 0; j != n + 1; j++) {
            System.out.printf(format, numMaxMatchings(i, j));
         }
         System.out.println();
      }
      System.out.print("   +");
      printString("-", (maxNumLen + 1) * (n + 1));
      System.out.println();
      System.out.print("    ");
      for (int j = 0; j != n + 1; j++) {
         System.out.printf(format, j);
      }
      System.out.println();
   }

   /*
    * Returns the largest value among the elements in the first rowMax
    ** rows and colMax columns in the given two-dimensional array.
    */
   private int maxOf(int[][] ary, int rowMax, int colMax) {
      int maxSoFar = ary[0][0];
      for (int i = 0; i != rowMax; i++) {
         for (int j = 0; j != colMax; j++) {
            if (ary[i][j] > maxSoFar) {
               maxSoFar = ary[i][j];
            }
         }
      }
      return maxSoFar;
   }

   /*
    * Reports whether the given two-dimensional array is a valid maximal
    ** (M,N)-matching.
    */
   public boolean isValidMaxMatching(int[][] matching) {
      return isValidMaxMatching(matching, M, N);
   }

   /*
    * Reports whether the given two-dimensional array is a valid maximal
    ** (m,n)-matching.
    */
   public boolean isValidMaxMatching(int[][] matching, int m, int n) {
      return matching[0].length == llcs[m][n] &&
            isValidMatching(matching, m, n);
   }

   /*
    * Reports whether the given two-dimensional array is a valid (but not
    ** necessarily maximal) (M,N)-matching.
    */
   public boolean isValidMatching(int[][] matching) {
      return isValidMatching(matching, M, N);
   }

   /*
    * Reports whether the given two-dimensional array is a valid (but not
    ** necessarily maximal) (m,n)-matching, which is to say that it has two
    ** rows of the same length, the values in each row are increasing, and
    ** they describe a common subsequence of X[0..m) and Y[0..n).
    */
   public boolean isValidMatching(int[][] matching, int m, int n) {
      if (matching.length != 2) {
         return false;
      } else {
         int[] f = matching[0];
         int[] g = matching[1];
         if (f.length != g.length) {
            return false;
         } else if (!isIncreasing(f)) {
            return false;
         } else if (!isIncreasing(g)) {
            return false;
         } else if (f[0] < 0 || f[f.length - 1] >= m) {
            return false;
         } else if (g[0] < 0 || g[g.length - 1] >= n) {
            return false;
         } else { // verify that f and g describe same string
            final int LEN = f.length;
            int i = 0;
            while (i != LEN && X.charAt(f[i]) == Y.charAt(g[i])) {
               i = i + 1;
            }
            return i == LEN;
         }
      }
   }

   /*
    * Prints both components in the specified matching.
    ** pre: mat is a valid matching
    */
   public void printMatching(int[][] mat) {
      final int LEN = mat[0].length;
      for (int i = 0; i != 2; i++) {
         for (int j = 0; j != LEN; j++) {
            System.out.printf("%3d ", mat[i][j]);
         }
         System.out.println();
      }
   }

   // private methods
   // ---------------

   /*
    * Prints the specified string the specified # of times
    ** (used in printing the LLCS table).
    */
   private void printString(String str, int k) {
      for (int j = 0; j != k; j++) {
         System.out.print(str);
      }
   }

   /*
    * Reports whether the values in the given array are increasing.
    */
   private boolean isIncreasing(int[] a) {
      final int LEN = a.length;
      if (LEN <= 1) {
         return true;
      } else {
         int i = 1;
         // loop invariant: a[0..i) is increasing.
         while (i != a.length && a[i - 1] < a[i]) {
            i = i + 1;
         }
         return i == a.length;
      }
   }

}
