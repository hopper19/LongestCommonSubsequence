/* This Java application is for the purpose of testing a class that purports
** to provide information about the longest common subsequences of two given
** Strings.
**
** Author: R. McCloskey
** Date: April 2020
*/

public class LongComSubseqTester {

   /* Solves the instance of the LCS problem described by two Strings
   ** provided via command line arguments.
   */
   public static void main(String[] args)
   {
      // Name the two given strings x and y
      String x = args[0];
      String y = args[1];

      // Create an object that computes the LCS of x and y
      LongComSubseq lcs = new LongComSubseq(x, y);

      // Print the table indicating LLCS values for all prefixes of x and y.
      System.out.println("\nLLCS table for " + x + " and " + y + ": \n");
      lcs.printLLCSTable();
      System.out.println();

      // Get the length of any LCS of x and y, and report it.
      int lcsLen = lcs.lengthOfLCS();
      System.out.println("Length of any LCS is " + lcsLen);

      // Get one LCS of x and y and report it
      String lcsStr = lcs.longestCommonSubsequence();
      System.out.println("A longest common subsequence is " + lcsStr);

      // If the length of any LCS differs from the length of
      // the LCS that was reported, alert the user.
      if (lcsStr.length() != lcsLen) {
         System.out.println("** Error: Length " + lcsStr.length() +
                            " is inconsistent with LLCS table, which says " +
                            lcsLen + " **");
      }

      // If the reported LCS is not a common subsequence of x and y,
      // alert the user.
      if (!isCommonSubsequence(lcsStr, x, y)) {
         System.out.println("** Error: Not a common subsequence!! **");
      }

      // Get a maximal matching between x and y and report it.
      int[][] matching = lcs.maxMatching();
      System.out.println("A maximal matching is:");
      lcs.printMatching(matching);

      // If it is not a valid matching, alert the user.
      if (!lcs.isValidMaxMatching(matching)) {
         System.out.println("** Error: That is NOT a valid maximal matching!");
      }

      // Print the table indicating NLCS values for all prefixes of x and y.
      System.out.println("\nNLCS table for " + x + " and " + y + ": \n");
      lcs.printNLCSTable();
      System.out.println();
   }


   /* Reports whether the given string s is a subsequence of both x and y.
   */
   private static boolean isCommonSubsequence(String s, String x, String y)
   {
      return isSubsequenceOf(s,x) && isSubsequenceOf(s,y);
   }

   /* Reports whether or not s is a subsequence of v.
   */
   private static boolean isSubsequenceOf(String s, String v)
   {
      final int s_LEN = s.length();
      final int v_LEN = v.length();
      int i=0, j=0;
      // loop invariant: s[0..i) is the longest prefix of s that is
      //                 a subsequence of v[0..j).
      while (i != s_LEN  &&  j != v_LEN  &&  (s_LEN - i <= v_LEN - j)) 
      {
         if (s.charAt(i) == v.charAt(j)) { i = i+1; } 
         j = j+1;
      }
      return i == s_LEN;
   }

}
