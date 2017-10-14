# 快速排序

稍微修改可以改为：第K大/Top K及其简单实现

时间复杂度可以几乎约为O(n)

LeetCode 提交地址 https://leetcode.com/problems/kth-largest-element-in-an-array/description/


 

<pre><code>
#include<cstdio>
#include<algorithm>

using namespace std;

int Qsort(int* a, int left, int right) {

    int temp = a[left];
    while (left < right) {
        while (a[right] >= temp && left < right) {
            right--;
        }
        a[left] = a[right];
        while (a[left] <= temp && left < right) {
            left++;
        }
        a[right] = a[left];
    }
    a[left] = temp;
    return left;
}

void QuickSort(int* a, int left, int right) {

    if (left < right) {
        int pos = Qsort(a, left, right);
        QuickSort(a, left, pos - 1);
        QuickSort(a, pos + 1, right);
    }
}

int main() {

    int a[10] = {12, 13, 26, 17, 29, 11, 15, 19, 21, 25};
    int len = 10;
    QuickSort(a, 0, len - 1);
    for (int i = 0; i < len; i++) {
        printf("%d  ", a[i]);
    }
    printf("\n");

    return 0;
}

</code></pre>




















