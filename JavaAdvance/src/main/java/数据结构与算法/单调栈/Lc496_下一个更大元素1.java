package 数据结构与算法.单调栈;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Lc496_下一个更大元素1 {
    public static void main(String[] args) {
        int[] nums1 = new int[]{4, 1, 2};
        int[] nums2 = new int[]{1, 3, 4, 2};

        System.out.println(Arrays.toString(nextGreaterElements(nums1, nums2)));
    }

    public static int[] nextGreaterElements(int[] nums1, int[] nums2) {
        Stack<Integer> stack = new Stack<>();
        Map<Integer, Integer> map = new HashMap<>();
        int i = 0;
        while (i < nums2.length) {
            if (stack.isEmpty() || nums2[stack.peek()] >= nums2[i]) {
                stack.push(i);
                i++;
                continue;
            }
            while (!stack.isEmpty() && nums2[stack.peek()] < nums2[i]) {
                int index = stack.pop();
                map.put(nums2[index], nums2[i]);
            }
        }
        int[] res = new int[nums1.length];
        for (int j = 0; j < nums1.length; j++) {
            res[j] = map.getOrDefault(nums1[j], -1);
        }
        return res;
    }
}
