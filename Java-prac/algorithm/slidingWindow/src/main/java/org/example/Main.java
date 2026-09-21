package org.example;

import java.util.LinkedList;
import java.util.Queue;

public class Main {
    public static void main(String[] args) {
        testQuickSort();
//        testBinarySearch();
//        testIslandsNums();
//        testMaxVowels();
    }
    public static void testQuickSort(){
        int a[] = {7, 1, 3, 5, 13, 9, 3, 6, 1};
        Solution.quickSort(a);

        System.out.println(a);
    }

    public static void testBinarySearch(){
//        int[] a = {1};
//        int[] a = {1,2};
//        int[] a = {2,1};
        int[] a = {1,2,3};
//        int[] a = {3,1,2};
//        int[] a = {3,4,1,2};
//        int[] a = {5,6,7,8,9,0,1,2,3,4};
        int min = Solution.findMin(a);
        System.out.println("min:" + min);
    }
    public static void testIslandsNums(){
        char[][] islands = {
            {'1','1','0','0','0'},
            {'1','1','0','0','0'},
            {'0','0','1','0','0'},
            {'0','0','0','1','1'}
        };

        int n = Solution.numIslands(islands);
        System.out.println("islands:" + n);
    }
    public static void testMaxVowels(){
        String s = "leetcode";
        int k = 3;

        int res = Solution.maxVowels(s,k);
        System.out.println(res);
    }
    public static void testMaxSlidingWnd(){
        System.out.println("Hello world!");

//        int[] nums = {1,3,-1,-3,5,3,6,7};
        int[] nums = {3,2,1,1,1,1,6,7};
        int k = 3;

        int[] res = Solution.maxSlidingWindow(nums,k);
        System.out.println(res);
    }
}



class Solution {

    /**
     * 快速排序
     * @param array
     */
    public static void quickSort(int[] array) {
        int len;
        if(array == null
                || (len = array.length) == 0
                || len == 1) {
            return ;
        }
        sort(array, 0, len - 1);
    }

    /**
     * 快排核心算法，递归实现
     * @param array
     * @param left
     * @param right
     */
    public static void sort(int[] array, int left, int right) {
        if(left > right) {
            return;
        }
        // base中存放基准数
        int base = array[left];
        int i = left, j = right;
        while(i != j) {
            // 顺序很重要，先从右边开始往左找，直到找到比base值小的数
            while(array[j] >= base && i < j) {
                j--;
            }

            // 再从左往右边找，直到找到比base值大的数
            while(array[i] <= base && i < j) {
                i++;
            }

            // 上面的循环结束表示找到了位置或者(i>=j)了，交换两个数在数组中的位置
            if(i < j) {
                int tmp = array[i];
                array[i] = array[j];
                array[j] = tmp;
            }
        }

        // 将基准数放到中间的位置（基准数归位）,其实这里本质也是以此交换
        array[left] = array[i];
        array[i] = base;

        // 递归，继续向基准的左右两边执行和上面同样的操作
        // i的索引处为上面已确定好的基准值的位置，无需再处理
        sort(array, left, i - 1);
        sort(array, i + 1, right);
    }


    public static int findMin(int[] nums) {
        int low = 0;
        int high = nums.length - 1;
        while (low < high) {
            int pivot = low + (high - low) / 2;
            // 最小值一定是在和 high 在一个区间内的，所以这里要判断 pivot 和 high 的大小关系，不能去判断和 low 的关系
            if (nums[pivot] < nums[high]) {
                high = pivot;
            } else {
                low = pivot + 1;
            }
        }
        return nums[low];
    }

    public static int numIslands(char[][] grid) {
        if (grid == null || grid.length < 1 || grid[0].length<1) {
            return 0;
        }
        int num = 0;
        int nr = grid.length;
        int nc = grid[0].length;
        // 每个点都可能是起点
        for (int x =0;x<nr;x++) {
            for (int y =0;y<nc;y++) {
                if (grid[x][y]=='1') {
//                    bfs(grid,x,y);
                    dfs(grid,x,y);
                    num++;
                }
            }
        }
        return num;
    }

    // 递归条件
    private static void dfs(char[][] grid, int x, int y) {
        int rx = grid.length;
        int cy = grid[0].length;
        // 终结条件
        if (x<0 || x>=rx || y<0 || y>= cy || grid[x][y] == '0') {
            return;
        }
        // 访问方向实质是由访问路径来决定的，就是你得想清楚怎么才算一条路径
        grid[x][y]='0';
        dfs(grid,x-1,y);
        dfs(grid,x,y-1);
        dfs(grid,x+1,y);
        dfs(grid,x,y+1);
        return ;
    }


// 对于 bfs 来说，只要队列不为空，就可以一直走到头,
    private static void bfs(char[][] grid, int r, int c) {
        int nr = grid.length;
        int nc = grid[0].length;
        // 队列，用于保存邻接点
        Queue<Integer> neighbors = new LinkedList<>();
        // 对于二维可以将坐标转化为一个数字
        neighbors.add(r * nc + c);
        while (!neighbors.isEmpty()) {
            // 每次循环开始的时候，需要移出一个点
            int id = neighbors.remove();
            int row = id / nc;
            int col = id % nc;
            // 四个邻接点都是在一个while循环里的
            if (row - 1 >= 0 && grid[row-1][col] == '1') {
                neighbors.add((row-1) * nc + col);
                grid[row-1][col] = '0';
            }
            if (row + 1 < nr && grid[row+1][col] == '1') {
                neighbors.add((row+1) * nc + col);
                grid[row+1][col] = '0';
            }
            if (col - 1 >= 0 && grid[row][col-1] == '1') {
                neighbors.add(row * nc + col-1);
                grid[row][col-1] = '0';
            }
            if (col + 1 < nc && grid[row][col+1] == '1') {
                neighbors.add(row * nc + col+1);
                grid[row][col+1] = '0';
            }
        }
    }

    public static int maxVowels(String s, int k) {
        int right =0;
        int sum = 0;
        int max = 0;
        while (right < s.length()) {
            sum += isYuan(s.charAt(right)) ;
            right++;
            // right >=k 把滑动窗口的大小控制在了 K
            if (right >=k) {
                max = Math.max(max, sum);
                sum -= isYuan(s.charAt(right-k));
            }
        }
        return max;
    }

    public static int isYuan(char s) {
        return s=='a' || s=='e' ||s=='i' ||s=='o' ||s=='u' ? 1:0;
    }

    public static int[] maxSlidingWindow(int[] nums, int k) {
        int right = 0;
        int[] res = new int[nums.length -k +1];
        int index = 0;
        LinkedList<Integer> list = new LinkedList<>();// 开始构造窗口
        while (right < nums.length) {
            // 这里的list的首位必须是窗口中最大的那位，注意这里用的>不是等于，因此相同的数据可以有多份
            while (!list.isEmpty() && nums[right] > list.peekLast()) {
                list.removeLast();
            }
            // 不断添加
            list.addLast(nums[right]);
            right++;
            // 构造窗口完成，这时候需要根据条件做一些操作,right>k说明已经添加过k个数据了，只有最大的被保留下来了；
            if (right >= k){
                res[index++] = list.peekFirst();
                // 如果发现第一个已经在窗口外面了，就移除
                if(list.peekFirst() == nums[right-k]) {
                    list.removeFirst();
                }
            }
        }
        return res;
    }
}