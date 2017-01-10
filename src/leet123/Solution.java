package leet123;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by littlersmall on 17/1/6.
 */
/*解题思路:
题目本意:在最多两次交易中,盈利最大
先把题目简化为只有一次交易的话,如何盈利最大?
数学描述为:一个数组a[n],求a[n]中的一对数据,a[x],a[y],满足y>x,且a[y]-a[x]最大
典型的动态规划问题.
假设a[n]的最大一对数据为a[x],a[y]
那么求a[n+1]的最大一对数据的方式为:
if a[n] > a[y] -> a[x], a[n]
还需要考虑这种情况,假如数据为[3,7,1,6]
那么最大的一对数据应该为[1,6]而不是[3,7]
所以我们需要在动态规划中保存一个当前的最小值a[min]
综上,最终的状态转移方程为:
[left, right, min] left,right为所求的一对数据,min为当前的最小值
f(1) -> [a[0], a[1], min(a[0], a[1])]
f(n) -> [a[left], a[right], min(a[0...n-1)]
f(n+1) -> if a[n] - a[min] > a[right] - a[left] -> [a[min], a[n]]
          else -> [a[left], a[right]]

再回到题目的问题,当交易变化为最多两次,那么我们可以把数组切分成两部分
a[0 - mid], a[mid+1 - n], 最终的最大值其实就是 f(0,mid) + f(mid+1, n)
而f(mid+1, n)其实就是从右往左做一次动态规划,和上面的思路完全一样
*/
public class Solution {
    static class LeftValue {
        int left;
        int right;
        int min;

        LeftValue(int left, int right, int min) {
            this.left = left;
            this.right = right;
            this.min = min;
        }

        @Override
        public String toString() {
            return left + " " + right;
        }
    }

    static class RightValue {
        int left;
        int right;
        int max;

        RightValue(int left, int right, int max) {
            this.left = left;
            this.right = right;
            this.max = max;
        }

        @Override
        public String toString() {
            return left + " " + right;
        }
    }

    public int maxProfit(int[] prices) {
        if (prices.length < 2) {
            return 0;
        }

        int maxProfit = 0;
        List<LeftValue> leftList = conLeftMax(prices);
        List<RightValue> rightList = conRightMax(prices);

        for (int start = 0; start < prices.length - 1; start++) {
            int profit = 0;
            LeftValue left = leftList.get(start);
            int index = prices.length - 1 - start - 2;
            RightValue right = index >= 0 ? rightList.get(index) : null;

            if (left.right > left.left) {
                profit += left.right - left.left;
            }

            if (right != null) {
                if (right.right > right.left) {
                    profit += right.right - right.left;
                }
            }

            if (maxProfit < profit) {
                maxProfit = profit;
            }
        }

        return maxProfit;
    }

    private List<LeftValue> conLeftMax(int[] prices) {
        List<LeftValue> valueList = new ArrayList<>();
        LeftValue value = new LeftValue(prices[0], prices[1], Math.min(prices[0], prices[1]));
        valueList.add(value);

        for (int i = 2; i < prices.length; i++) {
            LeftValue curValue = valueList.get(i - 2);
            LeftValue newValue = new LeftValue(curValue.left, curValue.right, curValue.min);

            if (prices[i] - curValue.min > curValue.right - curValue.left) {
                newValue.left = newValue.min;
                newValue.right = prices[i];
            }

            newValue.min = Math.min(prices[i], newValue.min);
            valueList.add(newValue);
        }

        return valueList;
    }

    private List<RightValue> conRightMax(int[] prices) {
        int len = prices.length;
        List<RightValue> valueList = new ArrayList<>();
        RightValue value = new RightValue(prices[len - 2], prices[len - 1], Math.max(prices[len - 2], prices[len - 1]));
        valueList.add(value);

        for (int i = len - 3; i >= 0; i--) {
            RightValue curValue = valueList.get(len - i - 3);
            RightValue newValue = new RightValue(curValue.left, curValue.right, curValue.max);

            if (curValue.max - prices[i] > curValue.right - curValue.left) {
                newValue.left = prices[i];
                newValue.right = newValue.max;
                newValue.max = Math.max(prices[i], newValue.max);
            }

            newValue.max = Math.max(prices[i], newValue.max);
            valueList.add(newValue);
        }

        return valueList;
    }

    public static void main(String[] args) {
        Solution solution = new Solution();

        System.out.println(solution.conLeftMax(new int[] {1,4,1,4,3,1}));
        System.out.println(solution.conRightMax(new int[] {1,4,1,4,3,1}));
        System.out.println(solution.maxProfit(new int[] {1,4,1,4,3,1}));
    }
}

