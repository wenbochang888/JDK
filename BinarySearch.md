# 两种搜索算法

## 二分查找

<pre><code>
#include<cstdio>
#include<algorithm>

using namespace std;

bool BinarySearchRec(int* a, int left, int right, int val) {

    if (left <= right) {
        int mid = (left + right) >> 1;
        if (val == a[mid]) {
            return true;
        } else if (val < a[mid]) {
            BinarySearchRec(a, left, mid - 1, val);
        } else if (val > a[mid]){
            BinarySearchRec(a, mid + 1, right, val);
        }
    } else {
        return false;
    }
}

bool BinarySearch(int* a, int left, int right, int val) {

    while (left <= right) {
        int mid = (left + right) >> 1;
        if (a[mid] == val) {
            return true;
        } else if (val > a[mid]) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    return false;
}



int main() {

    //len == 10 为数组的长度
    int a[10] = {1, 13, 25, 17, 29, 11, 15, 19, 21, 25};
    int len = 10;
    sort(a, a + len);

    int val = 30;
    while (val > 0) {
        bool flag = BinarySearch(a, 0, len - 1, val);
        if (flag) {
            printf("%d  找到啦啦啦啦啦\n", val);
        } else {
            printf("%d  没找到\n", val);
        }
        val--;
    }

    return 0;
}

</code></pre>

## 二叉搜索树

<pre><code>
#include <cstdio>
#include <algorithm>
#include <iostream>

using namespace std;

typedef struct chang {

    struct chang* leftChild;
    struct chang* rightChild;
    int val;
}Node, *Pnode;

void InsertVal(Pnode& p, int val) {

    if (p == NULL) {
        p = new Node();
        p->val = val;
        p->leftChild = NULL;
        p->rightChild = NULL;
    } else if (val > p->val) {
        InsertVal(p->rightChild, val);
    } else {
        InsertVal(p->leftChild, val);
    }
}

void Traverse(Pnode& p) {

    if (p) {
        Traverse(p->leftChild);
        printf("%d ",p->val);
        Traverse(p->rightChild);
    }
}

bool TreeSearch(Pnode& p, int val) {

    if (p) {
        if (val == p->val) {
            return true;
        } else if (val > p->val) {
            TreeSearch(p->rightChild, val);
        } else {
            TreeSearch(p->leftChild, val);
        }
    } else {
        return false;
    }
}

int main() {

    int a[10] = {17, 13, 26, 12, 29, 11, 15, 19, 21, 25};
    int len = 10;

    Pnode p = NULL;
    for (int i = 0; i < len; i++) {
        InsertVal(p, a[i]);
    }

    Traverse(p);
    printf("\n");

    if (TreeSearch(p, 21)) {
        printf("YES\n");
    } else {
        printf("NO\n");
    }

    return 0;
}
</code></pre>