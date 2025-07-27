package 数据结构与算法.单调栈;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class Lc503_下一个更大元素2 {
    public static void main(String[] args) {
        int[] nums = new int[]{2,6,3,5,4,3,2,1};
        int[] ints = nextGreaterElements(nums);
        System.out.println(Arrays.toString(ints));  // [-1,5,5,5,5]
    }

    /**
     * 求左边最远大于的和右边最近大于的
     *
     * @param nums
     * @return
     */
    public static int[] nextGreaterElements(int[] nums) {
        int[] res = new int[nums.length];
        Stack<List<Integer>> stack = new Stack<>(); // 单调栈，存下标index,栈底最大
        for (int i = 0; i < nums.length; i++) {
            if (stack.isEmpty() || nums[stack.peek().get(0)] > nums[i]) {
                List<Integer> list = new ArrayList<>();
                list.add(i);
                stack.push(list);
                continue;
            }
            if (!stack.isEmpty() && nums[stack.peek().get(0)] == nums[i]) {
                stack.peek().add(i);
                continue;
            }
            // 当栈顶的元素小于当前元素，重新构造栈
            Stack<List<Integer>> stack2 = new Stack<>();
            while (!stack.isEmpty() && nums[stack.peek().get(0)] < nums[i]) {
                stack2.push(stack.pop());
            }
            if (stack.isEmpty() || nums[stack.peek().get(0)] > nums[i]) {
                ArrayList<Integer> list = new ArrayList<>();
                list.add(i);
                stack.add(list);
            } else {
                stack.peek().add(i);
            }
            while (!stack2.isEmpty()) {
                stack.push(stack2.pop());
            }
        }
        while (!stack.isEmpty()) {
            List<Integer> pop = stack.pop();
            for (Integer i : pop) {
                res[i] = stack.isEmpty() ? -1 : nums[stack.peek().get(0)];
            }
        }
        return res;
    }
}
