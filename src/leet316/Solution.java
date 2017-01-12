package leet316;

import java.util.*;

/**
 * Created by littlersmall on 17/1/11.
 */
/*
解题思路:
想了很久,没有特别好的方法.
题目最难的地方在输出字典序最小的字符串上.
最后使用标记搜索+递归的方式做的,并加了一些细微的优化方式.
1 把连续的字符删除比如: aabbaa->aba
2 对字符串建立一个出现次数的数组,这个数组描述了当前的元素在其序号后面又出现了几次
比如 abcabcdabc
   [2221110000]
主要为了优化后面的搜索查询
下面说递归的思路:
每一次递归,要确定的找到一个元素,这样最多递归26次,因此不会有超时风险.
为了达到这个目标,需要准备一个当前已经选定的字符set: charSet
确定一个字符的条件:
没有在charSet中
    -> 当前字符唯一
    -> 如果该字符后面还会出现,则向后寻找,找到第一个不在charSet中且唯一的字符（在向后寻找的过程中,更新当前字符为不唯一且没有在charSet中的最小字符）
        -> 如果该元素 < 唯一字符 则该字符为需要找的字符,把该字符加入charSet中,继续从这个字符位置的下一个位置开始搜索
        -> 如果该元素 > 唯一字符,则当前的唯一字符为需要找的字符,将该字符加入charSet中,继续从唯一字符的下一个位置开始搜索
结束条件:搜索到达字符串末尾
 */
public class Solution {
    public String removeDuplicateLetters(String str) {
        String newStr = mergeFilter(str);
        Integer[] times = conTimes(newStr);

        return removeLetters(times, new HashSet<>(), newStr, 0);
    }

    //删除连续的重复字符 aabbaa -> aba
    private String mergeFilter(String str) {
        int len = str.length();
        char[] chars = str.toCharArray();

        for (int i = 1; i < str.length(); i++) {
            if (str.charAt(i - 1) == str.charAt(i)) {
                chars[i] = 0;
                len--;
            }
        }

        char[] res = new char[len];

        for (int i = 0, j = 0; i < str.length(); i++) {
            if (chars[i] != 0) {
                res[j++] = chars[i];
            }
        }

        return String.valueOf(res);
    }

    //统计一个字符串中每个字符后面出现了几次
    // abcabcd
    //[1110000]
    private Integer[] conTimes(String str) {
        int len = str.length();
        Integer[] times = new Integer[len];
        Map<Character, Integer> charMap = new HashMap<>();

        for (int i = len - 1; i >= 0; i--) {
            char c = str.charAt(i);
            Integer num = charMap.get(c);

            if (num == null) {
                num = 0;
            }

            times[i] = num;
            num++;

            charMap.put(c, num);
        }

        return times;
    }

    private String removeLetters(Integer[] times, Set<Character> charSet, String str, int start) {
        if (start >= str.length()) {
            return "";
        }

        while (charSet.contains(str.charAt(start))) {
            start++;

            if (start >= str.length()) {
                return "";
            }
        }

        char c = str.charAt(start);

        if (times[start] == 0) {
            charSet.add(c);

            return c + removeLetters(times, charSet, str, start + 1);
        } else {
            int next = start + 1;
            int pos = start;

            while (times[next] > 0
                    || charSet.contains(str.charAt(next))) {
                if (str.charAt(next) < c
                        && !charSet.contains(str.charAt(next))) {
                    c = str.charAt(next);
                    pos = next;
                }

                next++;
            }

            if (str.charAt(next) < c) {
                charSet.add(str.charAt(next));

                return str.charAt(next) + removeLetters(times, charSet, str, next + 1);
            } else {
                charSet.add(c);

                return c + removeLetters(times, charSet, str, pos + 1);
            }
        }
    }

    public static void main(String[] args) {
        Solution solution = new Solution();

        //System.out.println(solution.mergeFilter("aabbaa"));
        //System.out.println(Arrays.asList(solution.conTimes("abcabcd")));
        System.out.println(solution.removeDuplicateLetters("abacb"));
    }
}
